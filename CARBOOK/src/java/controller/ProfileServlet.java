package controller;

import dal.UserDAO;
import model.User;
import java.io.IOException;
import java.sql.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ProfileServlet - Handles user profile view and update
 * @author
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login?redirect=profile");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Refresh user data from database
        UserDAO userDAO = new UserDAO();
        User freshUser = userDAO.getUserById(user.getUserId());
        
        if (freshUser != null) {
            session.setAttribute("user", freshUser);
            request.setAttribute("user", freshUser);
        }
        
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login?redirect=profile");
            return;
        }
        
        User currentUser = (User) session.getAttribute("user");
        
        // Get form parameters
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String driverLicenseNumber = request.getParameter("driverLicenseNumber");
        String driverLicenseExpiryStr = request.getParameter("driverLicenseExpiry");
        String profileImageURL = request.getParameter("profileImageURL");
        
        // Validate input
        if (fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Họ tên không được để trống");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }
        //  Validate input cho PhoneNumber
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            request.setAttribute("error", "Số điện thoại không được để trống");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        } else if (!phoneNumber.trim().matches("^0\\d{9}$")) {
            request.setAttribute("error", "Số điện thoại không hợp lệ. Vui lòng nhập đúng 10 chữ số và bắt đầu bằng số 0");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }
        
        // Validate input cho Giấy phép lái xe (Driver License Number)
        if (driverLicenseNumber == null || driverLicenseNumber.trim().isEmpty()) {
            request.setAttribute("error", "Số giấy phép lái xe không được để trống");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        } else if (!driverLicenseNumber.trim().matches("^\\d{12}$")) {
            request.setAttribute("error", "Số giấy phép lái xe không hợp lệ. Vui lòng nhập đúng 12 chữ số");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }
        
        // Update user object
        currentUser.setFullName(fullName.trim());
        currentUser.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
        currentUser.setAddress(address != null ? address.trim() : null);
        currentUser.setDriverLicenseNumber(driverLicenseNumber != null ? driverLicenseNumber.trim() : null);
        currentUser.setProfileImageURL(profileImageURL != null ? profileImageURL.trim() : null);
        
       // Parse dates and Validate
        try {
            // 1. Validate Ngày sinh
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                Date dob = Date.valueOf(dateOfBirthStr);
                
                // Lấy ra năm sinh và kiểm tra phải >= 1926
                int birthYear = dob.toLocalDate().getYear();
                if (birthYear < 1926) {
                    request.setAttribute("error", "Năm sinh không hợp lệ)");
                    request.setAttribute("user", currentUser);
                    request.getRequestDispatcher("profile.jsp").forward(request, response);
                    return;
                }
                currentUser.setDateOfBirth(dob);
            }
            
            // 2. Validate Ngày hết hạn GPLX
            if (driverLicenseExpiryStr != null && !driverLicenseExpiryStr.isEmpty()) {
                Date expiryDate = Date.valueOf(driverLicenseExpiryStr);
                
                // Lấy năm hiện tại của hệ thống và so sánh
                int currentYear = java.time.LocalDate.now().getYear();
                int expiryYear = expiryDate.toLocalDate().getYear();
                
                if (expiryYear < currentYear) {
                    request.setAttribute("error", "Năm hết hạn giấy phép lái xe không hợp lệ");
                    request.setAttribute("user", currentUser);
                    request.getRequestDispatcher("profile.jsp").forward(request, response);
                    return;
                }
                currentUser.setDriverLicenseExpiry(expiryDate);
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Định dạng ngày không hợp lệ");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
            return;
        }
        
        // Update user in database
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.updateUser(currentUser);
        
        if (success) {
            // Refresh user data
            User updatedUser = userDAO.getUserById(currentUser.getUserId());
            session.setAttribute("user", updatedUser);
            session.setAttribute("fullName", updatedUser.getFullName());
            
            request.setAttribute("success", "Cập nhật thông tin thành công!");
            request.setAttribute("user", updatedUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Cập nhật thông tin thất bại. Vui lòng thử lại");
            request.setAttribute("user", currentUser);
            request.getRequestDispatcher("profile.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Profile Servlet for CARBOOK";
    }
}
