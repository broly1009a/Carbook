package controller;

import dal.PaymentDAO;
import dal.PaymentMethodDAO;
import dal.BookingDAO;
import dal.InvoiceDAO;
import model.Payment;
import model.PaymentMethod;
import model.Booking;
import model.Invoice;
import model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * PaymentServlet - Handles payment operations
 */
@WebServlet(name = "PaymentServlet", urlPatterns = {"/payment"})
public class PaymentServlet extends HttpServlet {

    private PaymentDAO paymentDAO = new PaymentDAO();
    private PaymentMethodDAO paymentMethodDAO = new PaymentMethodDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private InvoiceDAO invoiceDAO = new InvoiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listPayments(request, response, user);
                break;
            case "create":
                showPaymentForm(request, response);
                break;
            case "view":
                viewPayment(request, response);
                break;
            case "process":
                processPayment(request, response, user);
                break;
            default:
                listPayments(request, response, user);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createPayment(request, response, user);
        } else if ("confirm".equals(action)) {
            confirmPayment(request, response, user);
        }
    }

    private void listPayments(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Payment> payments;
        
        // Get filter parameters
        String statusFilter = request.getParameter("status");
        String fromDateStr = request.getParameter("fromDate");
        String toDateStr = request.getParameter("toDate");
        
        System.out.println("=== Payment Filter Debug ===");
        System.out.println("Status filter: " + statusFilter);
        System.out.println("From date: " + fromDateStr);
        System.out.println("To date: " + toDateStr);
        
        if (user.getRoleId() == 1) { // Admin
            payments = paymentDAO.getAllPayments();
        } else {
            // Get payments for user's bookings
            List<Booking> bookings = bookingDAO.getBookingsByCustomerId(user.getUserId());
            payments = new ArrayList<>();
            for (Booking booking : bookings) {
                payments.addAll(paymentDAO.getPaymentsByBookingId(booking.getBookingId()));
            }
        }
        
        System.out.println("Total payments before filter: " + (payments != null ? payments.size() : 0));
        
        // Apply filters
        if (payments != null && !payments.isEmpty()) {
            List<Payment> filteredPayments = new ArrayList<>();
            
            for (Payment payment : payments) {
                boolean matchesStatus = true;
                boolean matchesDateRange = true;
                
                // Status filter
                if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                    matchesStatus = statusFilter.equals(payment.getStatus());
                    System.out.println("Payment " + payment.getPaymentId() + " status: " + payment.getStatus() + 
                                     ", filter: " + statusFilter + ", matches: " + matchesStatus);
                }
                
                // Date range filter
                if (payment.getPaymentDate() != null) {
                    if (fromDateStr != null && !fromDateStr.trim().isEmpty()) {
                        try {
                            java.sql.Date fromDate = java.sql.Date.valueOf(fromDateStr);
                            java.util.Date paymentDate = new java.util.Date(payment.getPaymentDate().getTime());
                            matchesDateRange = matchesDateRange && (paymentDate.equals(fromDate) || paymentDate.after(fromDate));
                            System.out.println("Payment " + payment.getPaymentId() + " date: " + payment.getPaymentDate() + 
                                             ", from: " + fromDate + ", matches from: " + matchesDateRange);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid fromDate format: " + fromDateStr);
                        }
                    }
                    
                    if (toDateStr != null && !toDateStr.trim().isEmpty()) {
                        try {
                            java.sql.Date toDate = java.sql.Date.valueOf(toDateStr);
                            // Add one day to include the entire "to" date
                            java.util.Calendar cal = java.util.Calendar.getInstance();
                            cal.setTime(toDate);
                            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
                            java.util.Date toDatePlusOne = cal.getTime();
                            
                            java.util.Date paymentDate = new java.util.Date(payment.getPaymentDate().getTime());
                            matchesDateRange = matchesDateRange && paymentDate.before(toDatePlusOne);
                            System.out.println("Payment " + payment.getPaymentId() + " date: " + payment.getPaymentDate() + 
                                             ", to: " + toDate + ", matches to: " + matchesDateRange);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid toDate format: " + toDateStr);
                        }
                    }
                }
                
                // Add payment if it matches all filters
                if (matchesStatus && matchesDateRange) {
                    filteredPayments.add(payment);
                }
            }
            
            payments = filteredPayments;
            System.out.println("Total payments after filter: " + payments.size());
        }
        System.out.println("=== End Payment Filter Debug ===");
        
        request.setAttribute("payments", payments);
        request.getRequestDispatcher("payments.jsp").forward(request, response);
    }

    private void showPaymentForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookingIdStr = request.getParameter("bookingId");
        
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect("booking");
            return;
        }
        
        int bookingId = Integer.parseInt(bookingIdStr);
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            request.setAttribute("error", "Không tìm thấy đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        // Check if payment already exists
        List<Payment> existingPayments = paymentDAO.getPaymentsByBookingId(bookingId);
        boolean hasPaidPayment = existingPayments.stream()
                .anyMatch(p -> "Paid".equals(p.getStatus()));
        
        if (hasPaidPayment) {
            request.setAttribute("error", "Đặt xe này đã được thanh toán");
            response.sendRedirect("booking?action=view&id=" + bookingId);
            return;
        }
        
        List<PaymentMethod> paymentMethods = paymentMethodDAO.getActivePaymentMethods();
        
        request.setAttribute("booking", booking);
        request.setAttribute("paymentMethods", paymentMethods);
        request.getRequestDispatcher("payment-form.jsp").forward(request, response);
    }

    private void createPayment(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int paymentMethodId = Integer.parseInt(request.getParameter("paymentMethodId"));
            
            Booking booking = bookingDAO.getBookingById(bookingId);
            
            if (booking == null) {
                request.setAttribute("error", "Không tìm thấy đặt xe");
                response.sendRedirect("booking");
                return;
            }
            
            // Check if user owns this booking
            if (booking.getCustomerId() != user.getUserId()) {
                request.setAttribute("error", "Bạn không có quyền thanh toán cho đặt xe này");
                response.sendRedirect("booking");
                return;
            }
            
            Payment payment = new Payment();
            payment.setBookingId(bookingId);
            payment.setPaymentMethodId(paymentMethodId);
            payment.setPaymentReference(paymentDAO.generatePaymentReference());
            payment.setAmount(booking.getTotalAmount());
            payment.setStatus("Pending");
            payment.setNotes(request.getParameter("notes"));
            
            int paymentId = paymentDAO.createPayment(payment);
            
            if (paymentId > 0) {
                request.getSession().setAttribute("success", "Tạo thanh toán thành công!");
                response.sendRedirect("payment?action=process&id=" + paymentId);
            } else {
                request.setAttribute("error", "Lỗi khi tạo thanh toán");
                showPaymentForm(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("booking");
        }
    }

    private void processPayment(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int paymentId = Integer.parseInt(request.getParameter("id"));
        Payment payment = paymentDAO.getPaymentById(paymentId);
        
        if (payment == null) {
            request.setAttribute("error", "Không tìm thấy thanh toán");
            response.sendRedirect("payment");
            return;
        }
        
        Booking booking = bookingDAO.getBookingById(payment.getBookingId());
        PaymentMethod paymentMethod = paymentMethodDAO.getPaymentMethodById(payment.getPaymentMethodId());
        
        request.setAttribute("payment", payment);
        request.setAttribute("booking", booking);
        request.setAttribute("paymentMethod", paymentMethod);
        request.getRequestDispatcher("payment-process.jsp").forward(request, response);
    }

    private void confirmPayment(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            // Accept both "id" and "paymentId" parameters for flexibility
            String paymentIdParam = request.getParameter("id");
            if (paymentIdParam == null || paymentIdParam.isEmpty()) {
                paymentIdParam = request.getParameter("paymentId");
            }
            int paymentId = Integer.parseInt(paymentIdParam);
            String transactionId = request.getParameter("transactionId");
            
            Payment payment = paymentDAO.getPaymentById(paymentId);
            
            if (payment == null) {
                request.setAttribute("error", "Không tìm thấy thanh toán");
                response.sendRedirect("payment");
                return;
            }
            
            // For demo: auto-generate transaction ID if not provided
            if (transactionId == null || transactionId.trim().isEmpty()) {
                transactionId = "TXN" + System.currentTimeMillis();
            }
            
            if (paymentDAO.markAsPaid(paymentId, transactionId)) {
                // Update booking status to Approved if payment successful
                Booking booking = bookingDAO.getBookingById(payment.getBookingId());
                if ("Pending".equals(booking.getStatus())) {
                    bookingDAO.approveBooking(booking.getBookingId(), user.getUserId());
                }
                // Auto-create invoice after successful payment
                try {
                    Invoice existingInvoice = invoiceDAO.getInvoiceByBookingId(payment.getBookingId());
                    if (existingInvoice == null) {
                        // Create new invoice
                        int invoiceId = invoiceDAO.createInvoiceFromBooking(
                            payment.getBookingId(), 
                            paymentId, 
                            new BigDecimal("10.00"), // 10% tax
                            BigDecimal.ZERO, // no discount
                            "Hóa đơn tự động được tạo sau thanh toán"
                        );
                        
                        if (invoiceId > 0) {
                            request.getSession().setAttribute("success", "Thanh toán thành công! Hóa đơn đã được tạo.");
                            request.getSession().setAttribute("invoiceId", invoiceId);
                        } else {
                            request.getSession().setAttribute("success", "Thanh toán thành công!");
                        }
                    } else {
                        // Update existing invoice with payment info
                        invoiceDAO.markInvoiceAsPaid(existingInvoice.getInvoiceId(), paymentId);
                        request.getSession().setAttribute("success", "Thanh toán thành công! Hóa đơn đã được cập nhật.");
                        request.getSession().setAttribute("invoiceId", existingInvoice.getInvoiceId());
                    }
                } catch (Exception e) {
                    System.out.println("Error creating invoice: " + e.getMessage());
                    request.getSession().setAttribute("success", "Thanh toán thành công!");
                }
                
                request.getSession().setAttribute("success", "Thanh toán thành công!");
                response.sendRedirect("payment?action=view&id=" + paymentId);
            } else {
                request.setAttribute("error", "Lỗi khi xác nhận thanh toán");
                response.sendRedirect("payment?action=process&id=" + paymentId);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("payment");
        }
    }

    private void viewPayment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int paymentId = Integer.parseInt(request.getParameter("id"));
        Payment payment = paymentDAO.getPaymentById(paymentId);
        
        if (payment == null) {
            request.setAttribute("error", "Không tìm thấy thanh toán");
            response.sendRedirect("payment");
            return;
        }
        
        Booking booking = bookingDAO.getBookingById(payment.getBookingId());
        PaymentMethod paymentMethod = paymentMethodDAO.getPaymentMethodById(payment.getPaymentMethodId());
        
        // Get invoice if exists
        Invoice invoice = invoiceDAO.getInvoiceByPaymentId(paymentId);
        if (invoice == null) {
            // Try to get by booking ID
            invoice = invoiceDAO.getInvoiceByBookingId(payment.getBookingId());
        }
        
        request.setAttribute("payment", payment);
        request.setAttribute("booking", booking);
        request.setAttribute("paymentMethod", paymentMethod);
        request.setAttribute("invoice", invoice);
        request.getRequestDispatcher("payment-detail.jsp").forward(request, response);
    }
}
