package model;

import java.sql.Timestamp;
import java.sql.Date;

/**
 * User Model
 * Represents a user in the Car Rental Management System
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phoneNumber;
    private String address;
    private Date dateOfBirth;
    private String driverLicenseNumber;
    private Date driverLicenseExpiry;
    private String profileImageURL;
    private boolean isActive;
    private boolean isEmailVerified;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp lastLoginAt;
    
    // Role reference - direct relationship
    private int roleId;
    private Role role;
    private String googleId;

    // Constructors
    public User() {
    }
    
    public User(int userId, String username, String email, String passwordHash, 
                String fullName, String phoneNumber, String address, Date dateOfBirth,
                String driverLicenseNumber, Date driverLicenseExpiry, String profileImageURL,
                boolean isActive, boolean isEmailVerified, Timestamp createdAt, 
                Timestamp updatedAt, Timestamp lastLoginAt, int roleId,String googleId) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.driverLicenseNumber = driverLicenseNumber;
        this.driverLicenseExpiry = driverLicenseExpiry;
        this.profileImageURL = profileImageURL;
        this.isActive = isActive;
        this.isEmailVerified = isEmailVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastLoginAt = lastLoginAt;
        this.roleId = roleId;
        this.googleId = googleId;
    }
    
    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getDriverLicenseNumber() {
        return driverLicenseNumber;
    }
    
    public void setDriverLicenseNumber(String driverLicenseNumber) {
        this.driverLicenseNumber = driverLicenseNumber;
    }
    
    public Date getDriverLicenseExpiry() {
        return driverLicenseExpiry;
    }
    
    public void setDriverLicenseExpiry(Date driverLicenseExpiry) {
        this.driverLicenseExpiry = driverLicenseExpiry;
    }
    
    public String getProfileImageURL() {
        return profileImageURL;
    }
    
    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public boolean isEmailVerified() {
        return isEmailVerified;
    }
    
    public void setEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Timestamp getLastLoginAt() {
        return lastLoginAt;
    }
    
    public void setLastLoginAt(Timestamp lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }
    
    public int getRoleId() {
        return roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    public Role getRole() {
        return role;
    }
    
    public void setRole(Role role) {
        this.role = role;
        if (role != null) {
            this.roleId = role.getRoleId();
        }
    }
    public String getGoogleId() {
    return googleId;
}

public void setGoogleId(String googleId) {
    this.googleId = googleId;
}

    
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
