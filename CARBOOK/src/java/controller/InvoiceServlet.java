package controller;

import dal.InvoiceDAO;
import dal.BookingDAO;
import dal.PaymentDAO;
import model.Invoice;
import model.Booking;
import model.Payment;
import model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * InvoiceServlet - Handles invoice operations
 */
@WebServlet(name = "InvoiceServlet", urlPatterns = {"/invoice"})
public class InvoiceServlet extends HttpServlet {

    private InvoiceDAO invoiceDAO = new InvoiceDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();

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
                listInvoices(request, response, user);
                break;
            case "view":
                viewInvoice(request, response, user);
                break;
            case "print":
                printInvoice(request, response, user);
                break;
            case "create":
                showCreateForm(request, response, user);
                break;
            case "delete":
                deleteInvoice(request, response, user);
                break;
            default:
                listInvoices(request, response, user);
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
            createInvoice(request, response, user);
        }
    }

    private void listInvoices(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Invoice> invoices;
        
        if (user.getRoleId() == 1) { // Admin
            invoices = invoiceDAO.getAllInvoices();
        } else {
            // Get invoices for user's bookings
            invoices = invoiceDAO.getInvoicesByCustomerId(user.getUserId());
        }
        
        request.setAttribute("invoices", invoices);
        request.getRequestDispatcher("invoices.jsp").forward(request, response);
    }

    private void viewInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int invoiceId = Integer.parseInt(request.getParameter("id"));
            Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
            
            if (invoice == null) {
                request.getSession().setAttribute("error", "Không tìm thấy hóa đơn");
                response.sendRedirect("invoice");
                return;
            }
            
            // Check access permission
            Booking booking = bookingDAO.getBookingById(invoice.getBookingId());
            if (user.getRoleId() != 1 && booking.getCustomerId() != user.getUserId()) {
                request.getSession().setAttribute("error", "Bạn không có quyền xem hóa đơn này");
                response.sendRedirect("invoice");
                return;
            }
            
            // Get related data
            Payment payment = null;
            if (invoice.getPaymentId() > 0) {
                payment = paymentDAO.getPaymentById(invoice.getPaymentId());
            }
            
            request.setAttribute("invoice", invoice);
            request.setAttribute("booking", booking);
            request.setAttribute("payment", payment);
            request.getRequestDispatcher("invoice-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID hóa đơn không hợp lệ");
            response.sendRedirect("invoice");
        }
    }

    private void printInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int invoiceId = Integer.parseInt(request.getParameter("id"));
            Invoice invoice = invoiceDAO.getInvoiceById(invoiceId);
            
            if (invoice == null) {
                request.getSession().setAttribute("error", "Không tìm thấy hóa đơn");
                response.sendRedirect("invoice");
                return;
            }
            
            // Check access permission
            Booking booking = bookingDAO.getBookingById(invoice.getBookingId());
            if (user.getRoleId() != 1 && booking.getCustomerId() != user.getUserId()) {
                request.getSession().setAttribute("error", "Bạn không có quyền in hóa đơn này");
                response.sendRedirect("invoice");
                return;
            }
            
            // Get related data
            Payment payment = null;
            if (invoice.getPaymentId() > 0) {
                payment = paymentDAO.getPaymentById(invoice.getPaymentId());
            }
            
            request.setAttribute("invoice", invoice);
            request.setAttribute("booking", booking);
            request.setAttribute("payment", payment);
            request.getRequestDispatcher("invoice-print.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID hóa đơn không hợp lệ");
            response.sendRedirect("invoice");
        }
    }

    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can manually create invoices
        if (user.getRoleId() != 1) {
            request.getSession().setAttribute("error", "Bạn không có quyền tạo hóa đơn");
            response.sendRedirect("invoice");
            return;
        }
        
        String bookingIdStr = request.getParameter("bookingId");
        if (bookingIdStr != null) {
            try {
                int bookingId = Integer.parseInt(bookingIdStr);
                Booking booking = bookingDAO.getBookingById(bookingId);
                
                if (booking != null) {
                    // Check if invoice already exists
                    Invoice existingInvoice = invoiceDAO.getInvoiceByBookingId(bookingId);
                    if (existingInvoice != null) {
                        request.getSession().setAttribute("error", "Hóa đơn cho đặt xe này đã tồn tại");
                        response.sendRedirect("invoice?action=view&id=" + existingInvoice.getInvoiceId());
                        return;
                    }
                    
                    request.setAttribute("booking", booking);
                }
            } catch (NumberFormatException e) {
                // Invalid booking ID
            }
        }
        
        // Get all bookings without invoices
        List<Booking> bookings = bookingDAO.getAllBookings();
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("invoice-form.jsp").forward(request, response);
    }

    private void createInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can manually create invoices
        if (user.getRoleId() != 1) {
            request.getSession().setAttribute("error", "Bạn không có quyền tạo hóa đơn");
            response.sendRedirect("invoice");
            return;
        }
        
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            
            // Check if invoice already exists
            Invoice existingInvoice = invoiceDAO.getInvoiceByBookingId(bookingId);
            if (existingInvoice != null) {
                request.getSession().setAttribute("error", "Hóa đơn cho đặt xe này đã tồn tại");
                response.sendRedirect("invoice?action=view&id=" + existingInvoice.getInvoiceId());
                return;
            }
            
            String paymentIdStr = request.getParameter("paymentId");
            Integer paymentId = null;
            if (paymentIdStr != null && !paymentIdStr.isEmpty()) {
                paymentId = Integer.parseInt(paymentIdStr);
            }
            
            String taxRateStr = request.getParameter("taxRate");
            BigDecimal taxRate = new BigDecimal("10.00"); // Default 10%
            if (taxRateStr != null && !taxRateStr.isEmpty()) {
                taxRate = new BigDecimal(taxRateStr);
            }
            
            String discountStr = request.getParameter("discountAmount");
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (discountStr != null && !discountStr.isEmpty()) {
                discountAmount = new BigDecimal(discountStr);
            }
            
            String notes = request.getParameter("notes");
            
            // Create invoice using stored procedure
            int invoiceId = invoiceDAO.createInvoiceFromBooking(bookingId, paymentId, taxRate, discountAmount, notes);
            
            if (invoiceId > 0) {
                request.getSession().setAttribute("success", "Tạo hóa đơn thành công!");
                response.sendRedirect("invoice?action=view&id=" + invoiceId);
            } else {
                request.getSession().setAttribute("error", "Lỗi khi tạo hóa đơn");
                response.sendRedirect("invoice?action=create&bookingId=" + bookingId);
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            response.sendRedirect("invoice?action=create");
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("invoice?action=create");
        }
    }

    private void deleteInvoice(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can delete invoices
        if (user.getRoleId() != 1) {
            request.getSession().setAttribute("error", "Bạn không có quyền xóa hóa đơn");
            response.sendRedirect("invoice");
            return;
        }
        
        try {
            int invoiceId = Integer.parseInt(request.getParameter("id"));
            
            if (invoiceDAO.deleteInvoice(invoiceId)) {
                request.getSession().setAttribute("success", "Xóa hóa đơn thành công!");
            } else {
                request.getSession().setAttribute("error", "Không thể xóa hóa đơn");
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID hóa đơn không hợp lệ");
        }
        
        response.sendRedirect("invoice");
    }
}
