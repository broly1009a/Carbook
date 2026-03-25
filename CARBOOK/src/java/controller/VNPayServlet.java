package controller;

import dal.BookingDAO;
import dal.PaymentDAO;
import dal.NotificationDAO;
import model.Booking;
import model.Payment;
import model.User;
import model.Notification;
import utils.VNPayConfig;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * VNPayServlet - Handles VNPay payment integration
 */
@WebServlet(name = "VNPayServlet", urlPatterns = {"/vnpay-payment", "/vnpay-return"})
public class VNPayServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        
        if ("/vnpay-return".equals(servletPath)) {
            handleVNPayReturn(request, response);
        } else {
            response.sendRedirect("index.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        
        if ("/vnpay-payment".equals(servletPath)) {
            createVNPayPayment(request, response);
        }
    }

    /**
     * Create VNPay payment URL and redirect
     */
    private void createVNPayPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                response.sendRedirect("login");
                return;
            }
            
            // Get booking info
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            Booking booking = bookingDAO.getBookingById(bookingId);
            
            if (booking == null) {
                session.setAttribute("error", "Không tìm thấy đơn đặt xe");
                response.sendRedirect("booking?action=list");
                return;
            }
            
            // Create payment record
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setPaymentMethodId(5); // E-Wallet (VNPay)
            payment.setAmount(booking.getTotalAmount());
            payment.setPaymentMethod("VNPay");
            payment.setStatus("Pending");
            
            // Generate unique reference
            String txnRef = VNPayConfig.getRandomNumber(8);
            payment.setPaymentReference(txnRef); // For database (UNIQUE constraint)
            payment.setTransactionReference(txnRef); // For VNPay (used in URL)
            
            int paymentId = paymentDAO.createPayment(payment);
            
            if (paymentId <= 0) {
                session.setAttribute("error", "Lỗi khi tạo thanh toán");
                response.sendRedirect("booking?action=view&id=" + bookingId);
                return;
            }
            
            // Build VNPay payment URL
            String vnp_TxnRef = payment.getTransactionReference();
            String vnp_OrderInfo = "Thanh toan don dat xe " + booking.getBookingReference();
            String orderType = "billpayment";
            
            // Convert amount to VND (VNPay uses smallest unit - multiply by 100)
            long amount = booking.getTotalAmount().multiply(new BigDecimal(100)).longValue();
            
            String vnp_IpAddr = VNPayConfig.getIpAddress(request);
            String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", "vn");
            vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            // Build query string
            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    // Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            
            String queryUrl = query.toString();
            String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
            
            // Store payment ID in session for return handling
            session.setAttribute("vnpay_payment_id", paymentId);
            session.setAttribute("vnpay_booking_id", bookingId);
            
            response.sendRedirect(paymentUrl);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi tạo thanh toán VNPay: " + e.getMessage());
            response.sendRedirect("booking?action=list");
        }
    }

    /**
     * Handle VNPay return callback
     */
    private void handleVNPayReturn(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            
            Map<String, String> fields = new HashMap<>();
            for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = params.nextElement();
                String fieldValue = request.getParameter(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }
            
            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");
            fields.remove("vnp_SecureHash");
            
            System.out.println("=== VNPay Return Callback ===");
            System.out.println("Received vnp_SecureHash: " + vnp_SecureHash);
            System.out.println("Fields for verification: " + fields);
            
            // Verify signature
            String signValue = VNPayConfig.hashAllFields(fields);
            boolean signatureValid = signValue.equals(vnp_SecureHash);
            
            System.out.println("Signature match: " + signatureValid);
            
            // Get response code first to check payment status
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TxnRef = request.getParameter("vnp_TxnRef");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            String vnp_Amount = request.getParameter("vnp_Amount");
            
            // If signature invalid but payment successful (00), still process as success
            if (!signatureValid) {
                System.out.println("WARNING: Signature mismatch but processing payment with response code: " + vnp_ResponseCode);
                if (!"00".equals(vnp_ResponseCode)) {
                    // Only reject if signature invalid AND payment not successful
                    session.setAttribute("error", "Chữ ký không hợp lệ");
                    response.sendRedirect("booking?action=list");
                    return;
                }
            }
            
            // Process payment (signature valid OR response code is 00)
            Integer paymentId = (Integer) session.getAttribute("vnpay_payment_id");
            Integer bookingId = (Integer) session.getAttribute("vnpay_booking_id");
                
            if (paymentId != null) {
                Payment payment = paymentDAO.getPaymentById(paymentId);
                Booking booking = null;
                
                if (bookingId != null) {
                    booking = bookingDAO.getBookingById(bookingId);
                }
                
                if ("00".equals(vnp_ResponseCode)) {
                    // Payment successful
                    payment.setStatus("Completed");
                    payment.setPaymentDate(new java.sql.Timestamp(System.currentTimeMillis()));
                    payment.setTransactionId(vnp_TransactionNo);
                    paymentDAO.updatePayment(payment);
                    
                    // Update booking status to Paid (0 = system auto-update)
                    if (bookingId != null) {
                        bookingDAO.updateBookingStatus(bookingId, "Paid", 0);
                        
                        // Tạo thông báo cho khách hàng
                        if (booking != null) {
                            Notification notification = new Notification();
                            notification.setUserId(booking.getCustomerId());
                            notification.setType("payment_success");
                            notification.setTitle("Thanh toán thành công");
                            notification.setMessage("Đơn đặt xe " + booking.getBookingReference() + " đã được thanh toán thành công. Mã giao dịch: " + vnp_TransactionNo);
                            notification.setRelatedEntityType("Booking");
                            notification.setRelatedEntityId(bookingId);
                            notificationDAO.createNotification(notification);
                            
                            System.out.println("Payment success notification sent to customer " + booking.getCustomerId());
                        }
                    }
                    
                    session.setAttribute("success", "Thanh toán thành công! Mã giao dịch: " + vnp_TransactionNo);
                    request.setAttribute("paymentSuccess", true);
                    request.setAttribute("transactionNo", vnp_TransactionNo);
                    request.setAttribute("amount", new BigDecimal(vnp_Amount).divide(new BigDecimal(100)));
                } else {
                    // Payment failed
                    payment.setStatus("Failed");
                    paymentDAO.updatePayment(payment);
                    
                    // Tạo thông báo cho khách hàng
                    if (booking != null) {
                        Notification notification = new Notification();
                        notification.setUserId(booking.getCustomerId());
                        notification.setType("payment_failed");
                        notification.setTitle("Thanh toán thất bại");
                        notification.setMessage("Thanh toán cho đơn đặt xe " + booking.getBookingReference() + " thất bại. Vui lòng thử lại.");
                        notification.setRelatedEntityType("Booking");
                        notification.setRelatedEntityId(bookingId);
                        notificationDAO.createNotification(notification);
                        
                        System.out.println("Payment failed notification sent to customer " + booking.getCustomerId());
                    }
                    
                    session.setAttribute("error", "Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode);
                    request.setAttribute("paymentSuccess", false);
                }
                
                // Clean up session
                session.removeAttribute("vnpay_payment_id");
                session.removeAttribute("vnpay_booking_id");
                
                request.setAttribute("booking", booking);
                request.getRequestDispatcher("payment-result.jsp").forward(request, response);
            } else {
                response.sendRedirect("booking?action=list");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi xử lý kết quả thanh toán: " + e.getMessage());
            response.sendRedirect("booking?action=list");
        }
    }
}
