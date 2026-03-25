package controller;

import dal.ContactDAO;
import model.Contact;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * ContactServlet - Handles contact form submissions and management
 */
@WebServlet(name = "ContactServlet", urlPatterns = {"/contact"})
public class ContactServlet extends HttpServlet {
    
    private ContactDAO contactDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contactDAO = new ContactDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "form";
        }
        
        switch (action) {
            case "list":
                listContacts(request, response);
                break;
            case "view":
                viewContact(request, response);
                break;
            case "respond":
                showRespondForm(request, response);
                break;
            case "delete":
                deleteContact(request, response);
                break;
            case "mark-read":
                markAsRead(request, response);
                break;
            case "update-status":
                updateStatus(request, response);
                break;
            default:
                showContactForm(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "submit";
        }
        
        switch (action) {
            case "submit":
                submitContact(request, response);
                break;
            case "respond":
                respondToContact(request, response);
                break;
            default:
                showContactForm(request, response);
                break;
        }
    }
    
    /**
     * Show contact form page
     */
    private void showContactForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("contact.jsp").forward(request, response);
    }
    
    /**
     * Submit contact form
     */
    private void submitContact(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get form parameters
            String fullName = request.getParameter("fullName");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phoneNumber");
            String subject = request.getParameter("subject");
            String message = request.getParameter("message");
            String contactType = request.getParameter("contactType");
            
            // Validate required fields
            if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                subject == null || subject.trim().isEmpty() ||
                message == null || message.trim().isEmpty()) {
                
                request.getSession().setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
                response.sendRedirect("contact.jsp");
                return;
            }
            
            // Create contact
            int contactId = contactDAO.createContact(
                fullName.trim(),
                email.trim(),
                phoneNumber != null ? phoneNumber.trim() : null,
                subject.trim(),
                message.trim(),
                contactType != null ? contactType : "General"
            );
            
            if (contactId > 0) {
                request.getSession().setAttribute("success", 
                    "Cảm ơn bạn đã liên hệ! Chúng tôi sẽ phản hồi trong thời gian sớm nhất.");
                response.sendRedirect("contact.jsp");
            } else {
                request.getSession().setAttribute("error", 
                    "Có lỗi xảy ra khi gửi liên hệ. Vui lòng thử lại sau.");
                response.sendRedirect("contact.jsp");
            }
            
        } catch (Exception e) {
            System.out.println("Error in submitContact: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", 
                "Có lỗi xảy ra. Vui lòng thử lại sau.");
            response.sendRedirect("contact.jsp");
        }
    }
    
    /**
     * List all contacts (admin only)
     */
    private void listContacts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            String statusFilter = request.getParameter("status");
            List<Contact> contacts;
            
            if (statusFilter != null && !statusFilter.isEmpty()) {
                contacts = contactDAO.getContactsByStatus(statusFilter);
            } else {
                contacts = contactDAO.getAllContacts();
            }
            
            // Get statistics
            int newCount = contactDAO.getContactCountByStatus("New");
            int inProgressCount = contactDAO.getContactCountByStatus("In Progress");
            int resolvedCount = contactDAO.getContactCountByStatus("Resolved");
            int unreadCount = contactDAO.getUnreadContactCount();
            
            request.setAttribute("contacts", contacts);
            request.setAttribute("newCount", newCount);
            request.setAttribute("inProgressCount", inProgressCount);
            request.setAttribute("resolvedCount", resolvedCount);
            request.setAttribute("unreadCount", unreadCount);
            request.setAttribute("statusFilter", statusFilter);
            
            request.getRequestDispatcher("contact-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("Error in listContacts: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi tải danh sách liên hệ");
            response.sendRedirect("dashboard.jsp");
        }
    }
    
    /**
     * View contact details (admin only)
     */
    private void viewContact(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int contactId = Integer.parseInt(request.getParameter("id"));
            Contact contact = contactDAO.getContactById(contactId);
            
            if (contact == null) {
                request.getSession().setAttribute("error", "Không tìm thấy liên hệ");
                response.sendRedirect("contact?action=list");
                return;
            }
            
            // Mark as read
            if (!contact.isRead()) {
                contactDAO.markAsRead(contactId);
                contact.setRead(true);
            }
            
            request.setAttribute("contact", contact);
            request.getRequestDispatcher("contact-detail.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("Error in viewContact: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Có lỗi xảy ra khi xem liên hệ");
            response.sendRedirect("contact?action=list");
        }
    }
    
    /**
     * Show respond form (admin only)
     */
    private void showRespondForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int contactId = Integer.parseInt(request.getParameter("id"));
            Contact contact = contactDAO.getContactById(contactId);
            
            if (contact == null) {
                request.getSession().setAttribute("error", "Không tìm thấy liên hệ");
                response.sendRedirect("contact?action=list");
                return;
            }
            
            request.setAttribute("contact", contact);
            request.getRequestDispatcher("contact-respond.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("Error in showRespondForm: " + e.getMessage());
            request.getSession().setAttribute("error", "Có lỗi xảy ra");
            response.sendRedirect("contact?action=list");
        }
    }
    
    /**
     * Respond to contact (admin only)
     */
    private void respondToContact(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int contactId = Integer.parseInt(request.getParameter("contactId"));
            String responseMessage = request.getParameter("responseMessage");
            
            if (responseMessage == null || responseMessage.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Vui lòng nhập nội dung phản hồi");
                response.sendRedirect("contact?action=respond&id=" + contactId);
                return;
            }
            
            boolean success = contactDAO.respondToContact(contactId, responseMessage.trim(), user.getUserId());
            
            if (success) {
                request.getSession().setAttribute("success", "Đã gửi phản hồi thành công");
                response.sendRedirect("contact?action=view&id=" + contactId);
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra khi gửi phản hồi");
                response.sendRedirect("contact?action=respond&id=" + contactId);
            }
            
        } catch (Exception e) {
            System.out.println("Error in respondToContact: " + e.getMessage());
            request.getSession().setAttribute("error", "Có lỗi xảy ra");
            response.sendRedirect("contact?action=list");
        }
    }
    
    /**
     * Mark contact as read (admin only)
     */
    private void markAsRead(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int contactId = Integer.parseInt(request.getParameter("id"));
            contactDAO.markAsRead(contactId);
            
            response.sendRedirect("contact?action=list");
            
        } catch (Exception e) {
            System.out.println("Error in markAsRead: " + e.getMessage());
            response.sendRedirect("contact?action=list");
        }
    }
    
    /**
     * Update contact status (admin only)
     */
    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int contactId = Integer.parseInt(request.getParameter("id"));
            String status = request.getParameter("status");
            String priority = request.getParameter("priority");
            
            boolean success = contactDAO.updateContactStatus(contactId, status, priority);
            
            if (success) {
                request.getSession().setAttribute("success", "Đã cập nhật trạng thái");
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra");
            }
            
            response.sendRedirect("contact?action=view&id=" + contactId);
            
        } catch (Exception e) {
            System.out.println("Error in updateStatus: " + e.getMessage());
            request.getSession().setAttribute("error", "Có lỗi xảy ra");
            response.sendRedirect("contact?action=list");
        }
    }
    
    /**
     * Delete contact (admin only)
     */
    private void deleteContact(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is admin
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        try {
            int contactId = Integer.parseInt(request.getParameter("id"));
            boolean success = contactDAO.deleteContact(contactId);
            
            if (success) {
                request.getSession().setAttribute("success", "Đã xóa liên hệ thành công");
            } else {
                request.getSession().setAttribute("error", "Có lỗi xảy ra khi xóa");
            }
            
            response.sendRedirect("contact?action=list");
            
        } catch (Exception e) {
            System.out.println("Error in deleteContact: " + e.getMessage());
            request.getSession().setAttribute("error", "Có lỗi xảy ra");
            response.sendRedirect("contact?action=list");
        }
    }
}
