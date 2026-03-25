package dal;

import model.User;
import utils.EmailService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Data Access Object for User authentication and management
 * @author
 */
public class UserDAO extends DBContext {
    
    /**
     * Authenticate user by username and password
     * @param username
     * @param password
     * @return User object if authentication successful, null otherwise
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND PasswordHash = ? AND IsActive = 1";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, hashPassword(password));
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                User user = extractUserFromResultSet(rs);
                // Update last login time
                updateLastLogin(user.getUserId());
                return user;
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Register a new user
     * @param user
     * @return true if registration successful, false otherwise
     */
    public boolean register(User user) {
        String sql = "INSERT INTO Users (Username, Email, PasswordHash, FullName, PhoneNumber, " +
                     "Address, DateOfBirth, DriverLicenseNumber, DriverLicenseExpiry, " +
                     "ProfileImageURL, IsActive, IsEmailVerified, CreatedAt, UpdatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, user.getUsername());
            stm.setString(2, user.getEmail());
            stm.setString(3, hashPassword(user.getPasswordHash())); // Hash the password
            stm.setString(4, user.getFullName());
            stm.setString(5, user.getPhoneNumber());
            stm.setString(6, user.getAddress());
            stm.setDate(7, user.getDateOfBirth());
            stm.setString(8, user.getDriverLicenseNumber());
            stm.setDate(9, user.getDriverLicenseExpiry());
            stm.setString(10, user.getProfileImageURL());
            stm.setBoolean(11, true); // Default active
            stm.setBoolean(12, false); // Email not verified yet
            
            int rowsAffected = stm.executeUpdate();
            
            if (rowsAffected > 0) {
                // Send welcome email
                EmailService.sendWelcomeEmail(user.getEmail(), user.getUsername());
                
                // Optionally send verification email
                // String verificationCode = EmailService.generateVerificationCode();
                // EmailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationCode);
                // Store verificationCode in session or database for later verification
                
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Register error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Check if username already exists
     * @param username
     * @return true if exists, false otherwise
     */
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Username = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Check username error: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Check if email already exists
     * @param email
     * @return true if exists, false otherwise
     */
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM Users WHERE Email = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Check email error: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get user by ID
     * @param userId
     * @return User object or null
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Get user error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get user by username
     * @param username
     * @return User object or null
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Get user by username error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get user by email
     * @param email
     * @return User object or null
     */
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, email);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Get user by email error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update user profile
     * @param user
     * @return true if update successful, false otherwise
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET FullName = ?, PhoneNumber = ?, Address = ?, " +
                     "DateOfBirth = ?, DriverLicenseNumber = ?, DriverLicenseExpiry = ?, " +
                     "ProfileImageURL = ?, UpdatedAt = GETDATE() WHERE UserID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, user.getFullName());
            stm.setString(2, user.getPhoneNumber());
            stm.setString(3, user.getAddress());
            stm.setDate(4, user.getDateOfBirth());
            stm.setString(5, user.getDriverLicenseNumber());
            stm.setDate(6, user.getDriverLicenseExpiry());
            stm.setString(7, user.getProfileImageURL());
            stm.setInt(8, user.getUserId());
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Update user error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Change user password
     * @param userId
     * @param oldPassword
     * @param newPassword
     * @return true if password changed successfully, false otherwise
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        // First verify old password
        String checkSql = "SELECT * FROM Users WHERE UserID = ? AND PasswordHash = ?";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkSql);
            checkStm.setInt(1, userId);
            checkStm.setString(2, hashPassword(oldPassword));
            ResultSet rs = checkStm.executeQuery();
            
            if (rs.next()) {
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                
                // Old password is correct, update to new password
                String updateSql = "UPDATE Users SET PasswordHash = ?, UpdatedAt = GETDATE() WHERE UserID = ?";
                PreparedStatement updateStm = connection.prepareStatement(updateSql);
                updateStm.setString(1, hashPassword(newPassword));
                updateStm.setInt(2, userId);
                
                int rowsAffected = updateStm.executeUpdate();
                
                if (rowsAffected > 0) {
                    // Send password changed confirmation email
                    EmailService.sendPasswordChangedEmail(email, username);
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Change password error: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Reset password (for forgot password functionality)
     * @param email
     * @param newPassword
     * @return true if password reset successful, false otherwise
     */
    public boolean resetPassword(String email, String newPassword) {
        String sql = "UPDATE Users SET PasswordHash = ?, UpdatedAt = GETDATE() WHERE Email = ?";
        try {
            // Get user info first
            User user = getUserByEmail(email);
            if (user == null) {
                return false;
            }
            
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, hashPassword(newPassword));
            stm.setString(2, email);
            
            int rowsAffected = stm.executeUpdate();
            
            if (rowsAffected > 0) {
                // Send password changed confirmation email
                EmailService.sendPasswordChangedEmail(email, user.getUsername());
                return true;
            }
            return false;
        } catch (SQLException e) {
            System.out.println("Reset password error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Send password reset email with token
     * @param email
     * @return reset token if email sent successfully, null otherwise
     */
    public String sendPasswordResetEmail(String email) {
        User user = getUserByEmail(email);
        if (user == null || !user.isActive()) {
            return null;
        }
        
        // Generate reset token
        String resetToken = EmailService.generateResetToken();
        
        // Store reset token in database with expiry time
        String sql = "INSERT INTO PasswordResetTokens (UserID, Token, ExpiryDate, IsUsed, CreatedAt) " +
                     "VALUES (?, ?, DATEADD(HOUR, 1, GETDATE()), 0, GETDATE())";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, user.getUserId());
            stm.setString(2, resetToken);
            
            int rowsAffected = stm.executeUpdate();
            
            if (rowsAffected > 0) {
                // Send password reset email
                boolean emailSent = EmailService.sendPasswordResetEmail(email, user.getUsername(), resetToken);
                if (emailSent) {
                    return resetToken;
                }
            }
        } catch (SQLException e) {
            System.out.println("Send password reset email error: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Verify reset token and reset password
     * @param token
     * @param newPassword
     * @return true if password reset successful, false otherwise
     */
    public boolean resetPasswordWithToken(String token, String newPassword) {
        String checkSql = "SELECT u.Email, u.Username FROM PasswordResetTokens prt " +
                         "INNER JOIN Users u ON prt.UserID = u.UserID " +
                         "WHERE prt.Token = ? AND prt.IsUsed = 0 AND prt.ExpiryDate > GETDATE()";
        try {
            PreparedStatement checkStm = connection.prepareStatement(checkSql);
            checkStm.setString(1, token);
            ResultSet rs = checkStm.executeQuery();
            
            if (rs.next()) {
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                
                // Reset password
                boolean success = resetPassword(email, newPassword);
                
                if (success) {
                    // Mark token as used
                    String updateTokenSql = "UPDATE PasswordResetTokens SET IsUsed = 1 WHERE Token = ?";
                    PreparedStatement updateStm = connection.prepareStatement(updateTokenSql);
                    updateStm.setString(1, token);
                    updateStm.executeUpdate();
                    
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("Reset password with token error: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Activate or deactivate user account
     * @param userId
     * @param isActive
     * @return true if status changed successfully, false otherwise
     */
    public boolean setUserActiveStatus(int userId, boolean isActive) {
        String sql = "UPDATE Users SET IsActive = ?, UpdatedAt = GETDATE() WHERE UserID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setBoolean(1, isActive);
            stm.setInt(2, userId);
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Set user status error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify user email
     * @param userId
     * @return true if verification successful, false otherwise
     */
    public boolean verifyEmail(int userId) {
        String sql = "UPDATE Users SET IsEmailVerified = 1, UpdatedAt = GETDATE() WHERE UserID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Verify email error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Update last login timestamp
     * @param userId
     */
    private void updateLastLogin(int userId) {
        String sql = "UPDATE Users SET LastLoginAt = GETDATE() WHERE UserID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            stm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update last login error: " + e.getMessage());
        }
    }
    
    /**
     * Get all users (for admin)
     * @return List of all users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users ORDER BY CreatedAt DESC";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Get all users error: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Get active users only
     * @return List of active users
     */
    public List<User> getActiveUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE IsActive = 1 ORDER BY CreatedAt DESC";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Get active users error: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Search users by keyword (username, email, or full name)
     * @param keyword
     * @return List of matching users
     */
    public List<User> searchUsers(String keyword) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users WHERE Username LIKE ? OR Email LIKE ? OR FullName LIKE ? " +
                     "ORDER BY CreatedAt DESC";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            stm.setString(1, searchPattern);
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Search users error: " + e.getMessage());
        }
        return users;
    }
    
    /**
     * Delete user (soft delete by setting IsActive to false)
     * @param userId
     * @return true if deletion successful, false otherwise
     */
    public boolean deleteUser(int userId) {
        return setUserActiveStatus(userId, false);
    }
    
    /**
     * Permanently delete user from database
     * @param userId
     * @return true if deletion successful, false otherwise
     */
    public boolean permanentDeleteUser(int userId) {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Permanent delete user error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract User object from ResultSet
     * @param rs
     * @return User object
     * @throws SQLException
     */
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setEmail(rs.getString("Email"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setFullName(rs.getString("FullName"));
        user.setPhoneNumber(rs.getString("PhoneNumber"));
        user.setAddress(rs.getString("Address"));
        user.setDateOfBirth(rs.getDate("DateOfBirth"));
        user.setDriverLicenseNumber(rs.getString("DriverLicenseNumber"));
        user.setDriverLicenseExpiry(rs.getDate("DriverLicenseExpiry"));
        user.setProfileImageURL(rs.getString("ProfileImageURL"));
        user.setActive(rs.getBoolean("IsActive"));
        user.setEmailVerified(rs.getBoolean("IsEmailVerified"));
        user.setCreatedAt(rs.getTimestamp("CreatedAt"));
        user.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        user.setLastLoginAt(rs.getTimestamp("LastLoginAt"));
        user.setRoleId(rs.getInt("RoleID")); 
        return user;
    }
    
    /**
     * Hash password using SHA-256
     * @param password
     * @return hashed password
     */
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Hash password error: " + e.getMessage());
            return password; // Fallback to plain text (not recommended for production)
        }
    }
    
    

// tìm user theo GoogleID
public User getUserByGoogleId(String googleId) {
    String sql = "SELECT * FROM Users WHERE GoogleID = ?";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, googleId);
        ResultSet rs = stm.executeQuery();

        if (rs.next()) {
            return extractUserFromResultSet(rs);
        }
    } catch (SQLException e) {
        System.out.println("Get user by GoogleID error: " + e.getMessage());
    }
    return null;
}

public void updateGoogleId(int userId, String googleId) {
    String sql = "UPDATE Users SET GoogleID = ? WHERE UserID = ?";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, googleId);
        stm.setInt(2, userId);
        stm.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Update GoogleID error: " + e.getMessage());
    }
}

public void insertGoogleUser(User u) {
    String sql = "INSERT INTO Users "
            + "(Username, Email, PasswordHash, FullName, RoleID, IsActive, IsEmailVerified, GoogleID) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, u.getUsername());
        stm.setString(2, u.getEmail());
        stm.setString(3, null);
        stm.setString(4, u.getFullName());
        stm.setInt(5, 3);
        stm.setBoolean(6, true);
        stm.setBoolean(7, true);
        stm.setString(8, u.getGoogleId());
        stm.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public boolean updateUserRole(int userId, int roleId) {
    String sql = "UPDATE Users SET RoleID = ?, UpdatedAt = GETDATE() WHERE UserID = ?";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setInt(1, roleId);
        stm.setInt(2, userId);
        return stm.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Update user role error: " + e.getMessage());
        return false;
    }
}



//check phone exist
public boolean isPhoneNumberExists(String phoneNumber) {
    String sql = "SELECT COUNT(*) FROM Users WHERE PhoneNumber = ?";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, phoneNumber);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

// check blx 
public boolean isDriverLicenseExists(String licenseNumber) {
    if (licenseNumber == null || licenseNumber.trim().isEmpty()) return false;
    String sql = "SELECT COUNT(*) FROM Users WHERE DriverLicenseNumber = ?";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setString(1, licenseNumber);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) return rs.getInt(1) > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}



}
