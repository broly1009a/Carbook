package controller;

import dal.UserDAO;
import model.User;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate; 
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * RegisterServlet - Handles user registration
 * @author hson5
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
       
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String dateOfBirthStr = request.getParameter("dateOfBirth");
        String driverLicenseNumber = request.getParameter("driverLicenseNumber");
        String driverLicenseExpiryStr = request.getParameter("driverLicenseExpiry");
        
        UserDAO userDAO = new UserDAO();

       
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng điền đầy đủ các thông tin bắt buộc");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        // 2. Validate cf pass
        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorConfirm", "Mật khẩu xác nhận không khớp");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        
        if (password.length() < 6) {
            request.setAttribute("errorPass", "Mật khẩu phải có ít nhất 6 ký tự");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        
        
        if (phoneNumber != null && !phoneNumber.matches("\\d{10}")) {
            request.setAttribute("errorPhone", "Số điện thoại phải có đúng 10 chữ số");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        if (userDAO.isPhoneNumberExists(phoneNumber)) {
            request.setAttribute("errorPhone", "Vui lòng nhập chính xác sđt của bạn");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        
        if (driverLicenseNumber != null && !driverLicenseNumber.trim().isEmpty()) {
            if (userDAO.isDriverLicenseExists(driverLicenseNumber.trim())) {
                request.setAttribute("errorLicense", "Số GPLX này đã tồn tại,Bạn vui lòng nhập chính xác số GPLX");
                setFormData(request, username, email, fullName, phoneNumber, address, 
                           dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
        }

        
        if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
            LocalDate dob = LocalDate.parse(dateOfBirthStr);
            int age = LocalDate.now().getYear() - dob.getYear();
            if (age < 18) {
                request.setAttribute("errorDob", "Tạo tài khoản thất bại,yêu cầu phải trên 18 tuổi");
                setFormData(request, username, email, fullName, phoneNumber, address, 
                           dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
        }

        
        if (driverLicenseExpiryStr != null && !driverLicenseExpiryStr.isEmpty()) {
            LocalDate expiry = LocalDate.parse(driverLicenseExpiryStr);
            if (expiry.isBefore(LocalDate.now())) {
                request.setAttribute("errorExpiry", "Giấy phép lái xe đã hết hạn,bạn không thể tạo tài khoản");
                setFormData(request, username, email, fullName, phoneNumber, address, 
                           dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
                request.getRequestDispatcher("register.jsp").forward(request, response);
                return;
            }
        }

        

        
        if (userDAO.isUsernameExists(username.trim())) {
            request.setAttribute("errorUsername", "Tên đăng nhập đã tồn tại");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        if (userDAO.isEmailExists(email.trim())) {
            request.setAttribute("errorEmail", "Email đã được sử dụng");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
       
        User user = new User();
        user.setUsername(username.trim());
        user.setEmail(email.trim());
        user.setPasswordHash(password); 
        user.setFullName(fullName.trim());
        user.setPhoneNumber(phoneNumber != null ? phoneNumber.trim() : null);
        user.setAddress(address != null ? address.trim() : null);
        
        try {
            if (dateOfBirthStr != null && !dateOfBirthStr.isEmpty()) {
                user.setDateOfBirth(Date.valueOf(dateOfBirthStr));
            }
            if (driverLicenseExpiryStr != null && !driverLicenseExpiryStr.isEmpty()) {
                user.setDriverLicenseExpiry(Date.valueOf(driverLicenseExpiryStr));
            }
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Định dạng ngày không hợp lệ");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }
        
        user.setDriverLicenseNumber(driverLicenseNumber != null ? driverLicenseNumber.trim() : null);
        
        boolean success = userDAO.register(user);
        
        if (success) {
            request.setAttribute("success", "Đăng ký thành công!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại");
            setFormData(request, username, email, fullName, phoneNumber, address, 
                       dateOfBirthStr, driverLicenseNumber, driverLicenseExpiryStr);
            request.getRequestDispatcher("register.jsp").forward(request, response);
        }
    }
    
    private void setFormData(HttpServletRequest request, String username, String email, 
                            String fullName, String phoneNumber, String address,
                            String dateOfBirth, String driverLicenseNumber, 
                            String driverLicenseExpiry) {
        request.setAttribute("username", username);
        request.setAttribute("email", email);
        request.setAttribute("fullName", fullName);
        request.setAttribute("phoneNumber", phoneNumber);
        request.setAttribute("address", address);
        request.setAttribute("dateOfBirth", dateOfBirth);
        request.setAttribute("driverLicenseNumber", driverLicenseNumber);
        request.setAttribute("driverLicenseExpiry", driverLicenseExpiry);
    }
}