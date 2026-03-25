package controller;

import dal.NotificationDAO;
import model.Notification;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * NotificationServlet - Handles notification operations
 */
@WebServlet(name = "NotificationServlet", urlPatterns = {"/notification", "/notifications"})
public class NotificationServlet extends HttpServlet {

    private NotificationDAO notificationDAO = new NotificationDAO();

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
                listNotifications(request, response, user);
                break;
            case "unread":
                listUnreadNotifications(request, response, user);
                break;
            case "markRead":
                markAsRead(request, response, user);
                break;
            case "markAllRead":
                markAllAsRead(request, response, user);
                break;
            case "delete":
                deleteNotification(request, response, user);
                break;
            case "count":
                getUnreadCount(request, response, user);
                break;
            default:
                listNotifications(request, response, user);
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
            createNotification(request, response, user);
        }
    }

    private void listNotifications(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String filter = request.getParameter("filter");
        List<Notification> notifications;
        
        // Check if filter is "unread"
        if ("unread".equals(filter)) {
            notifications = notificationDAO.getUnreadNotifications(user.getUserId());
        } else {
            // Default: show all notifications
            notifications = notificationDAO.getNotificationsByUserId(user.getUserId());
        }
        
        int unreadCount = notificationDAO.getUnreadCount(user.getUserId());
        
        request.setAttribute("notifications", notifications);
        request.setAttribute("unreadCount", unreadCount);
        request.getRequestDispatcher("notifications.jsp").forward(request, response);
    }

    private void listUnreadNotifications(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Notification> notifications = notificationDAO.getUnreadNotifications(user.getUserId());
        
        request.setAttribute("notifications", notifications);
        request.setAttribute("unreadOnly", true);
        request.getRequestDispatcher("notifications.jsp").forward(request, response);
    }

    private void markAsRead(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int notificationId = Integer.parseInt(request.getParameter("id"));
        
        Notification notification = notificationDAO.getNotificationById(notificationId);
        
        if (notification == null || notification.getUserId() != user.getUserId()) {
            response.sendRedirect("notifications");
            return;
        }
        
        if (notificationDAO.markAsRead(notificationId)) {
            // Update unread count in session
            updateUnreadCountInSession(request, user);
            
            String redirectUrl = request.getParameter("redirect");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("notifications");
            }
        } else {
            response.sendRedirect("notifications");
        }
    }

    private void markAllAsRead(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        if (notificationDAO.markAllAsRead(user.getUserId())) {
            // Update unread count in session (should be 0 now)
            updateUnreadCountInSession(request, user);
            request.setAttribute("success", "Đã đánh dấu tất cả thông báo là đã đọc");
        } else {
            request.setAttribute("error", "Lỗi khi đánh dấu thông báo");
        }
        
        response.sendRedirect("notifications");
    }

    private void deleteNotification(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int notificationId = Integer.parseInt(request.getParameter("id"));
        
        Notification notification = notificationDAO.getNotificationById(notificationId);
        
        if (notification == null || notification.getUserId() != user.getUserId()) {
            response.sendRedirect("notifications");
            return;
        }
        
        if (notificationDAO.deleteNotification(notificationId)) {
            request.setAttribute("success", "Đã xóa thông báo");
        } else {
            request.setAttribute("error", "Lỗi khi xóa thông báo");
        }
        
        response.sendRedirect("notifications");
    }

    private void getUnreadCount(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int count = notificationDAO.getUnreadCount(user.getUserId());
        
        response.setContentType("application/json");
        response.getWriter().write("{\"count\": " + count + "}");
    }

    private void createNotification(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            String type = request.getParameter("type");
            String title = request.getParameter("title");
            String message = request.getParameter("message");
            String relatedEntityType = request.getParameter("relatedEntityType");
            String relatedEntityIdStr = request.getParameter("relatedEntityId");
            
            Notification notification = new Notification();
            notification.setUserId(userId);
            notification.setType(type);
            notification.setTitle(title);
            notification.setMessage(message);
            notification.setRelatedEntityType(relatedEntityType);
            
            if (relatedEntityIdStr != null && !relatedEntityIdStr.isEmpty()) {
                notification.setRelatedEntityId(Integer.parseInt(relatedEntityIdStr));
            }
            
            if (notificationDAO.createNotification(notification)) {
                response.getWriter().write("{\"success\": true}");
            } else {
                response.getWriter().write("{\"success\": false}");
            }
        } catch (Exception e) {
            response.getWriter().write("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
    
    /**
     * Helper method to update unread count in session
     */
    private void updateUnreadCountInSession(HttpServletRequest request, User user) {
        try {
            int unreadCount = notificationDAO.getUnreadCount(user.getUserId());
            HttpSession session = request.getSession();
            session.setAttribute("unreadCount", unreadCount);
            request.setAttribute("unreadCount", unreadCount);
        } catch (Exception e) {
            System.err.println("Error updating unread count: " + e.getMessage());
        }
    }
}
