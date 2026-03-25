package controller;

import dal.UserDAO;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ChangePasswordServlet - Handles password change for logged-in users
 * @author
 */
@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login?redirect=change-password");
            return;
        }
        
        request.getRequestDispatcher("change-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login?redirect=change-password");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validate input
        if (oldPassword == null || oldPassword.isEmpty() ||
            newPassword == null || newPassword.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }
        
        // Validate new password match
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }
        
        // Validate password strength
        if (newPassword.length() < 6) {
            request.setAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }
        
        // Validate old password is different from new password
        if (oldPassword.equals(newPassword)) {
            request.setAttribute("error", "Mật khẩu mới phải khác mật khẩu cũ");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
            return;
        }
        
        // Change password
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.changePassword(currentUser.getUserId(), oldPassword, newPassword);
        
        if (success) {
            request.setAttribute("success", "Đổi mật khẩu thành công!");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Mật khẩu cũ không chính xác");
            request.getRequestDispatcher("change-password.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Change Password Servlet for CARBOOK";
    }
}
