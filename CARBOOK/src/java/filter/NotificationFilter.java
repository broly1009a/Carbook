package filter;

import dal.NotificationDAO;
import model.User;
import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * NotificationFilter - Automatically sets unread notification count for all requests
 * This allows the navbar to always display the current unread count
 */
@WebFilter(filterName = "NotificationFilter", urlPatterns = {"/*"})
public class NotificationFilter implements Filter {

    private NotificationDAO notificationDAO;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        notificationDAO = new NotificationDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false);
        
        // Only set unread count if user is logged in
        if (session != null) {
            User user = (User) session.getAttribute("user");
            
            if (user != null) {
                try {
                    // Get unread notification count from database
                    int unreadCount = notificationDAO.getUnreadCount(user.getUserId());
                    
                    // Set as request attribute for this request
                    request.setAttribute("unreadCount", unreadCount);
                    
                    // Also update session attribute for persistence
                    session.setAttribute("unreadCount", unreadCount);
                } catch (Exception e) {
                    // Log error but don't break the request
                    System.err.println("Error getting unread notification count: " + e.getMessage());
                }
            }
        }
        
        // Continue with the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        notificationDAO = null;
    }
}
