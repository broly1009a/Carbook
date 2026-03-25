package controller;

import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ForgotPasswordServlet - Handles forgot password requests
 * @author
 */
@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgot-password"})
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to forgot password page
        request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String email = request.getParameter("email");
        
        // Validate input
        if (email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập địa chỉ email");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
            return;
        }
        
        // Send password reset email
        UserDAO userDAO = new UserDAO();
        String resetToken = userDAO.sendPasswordResetEmail(email.trim());
        
        if (resetToken != null) {
            // Email sent successfully
            request.setAttribute("success", 
                "Email khôi phục mật khẩu đã được gửi. Vui lòng kiểm tra hộp thư của bạn.");
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        } else {
            // Email not found or inactive
            request.setAttribute("error", 
                "Không tìm thấy tài khoản với email này hoặc tài khoản đã bị khóa");
            request.setAttribute("email", email);
            request.getRequestDispatcher("forgot-password.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Forgot Password Servlet for CARBOOK";
    }
}
