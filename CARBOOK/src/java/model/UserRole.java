package model;

import java.sql.Timestamp;

/**
 * UserRole Model
 * Represents the mapping between users and roles
 */
public class UserRole {
    private int userRoleId;
    private int userId;
    private int roleId;
    private Timestamp assignedAt;
    private Integer assignedBy;
    
    // Constructors
    public UserRole() {
    }
    
    public UserRole(int userRoleId, int userId, int roleId, Timestamp assignedAt,
                    Integer assignedBy) {
        this.userRoleId = userRoleId;
        this.userId = userId;
        this.roleId = roleId;
        this.assignedAt = assignedAt;
        this.assignedBy = assignedBy;
    }
    
    // Getters and Setters
    public int getUserRoleId() {
        return userRoleId;
    }
    
    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getRoleId() {
        return roleId;
    }
    
    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
    
    public Timestamp getAssignedAt() {
        return assignedAt;
    }
    
    public void setAssignedAt(Timestamp assignedAt) {
        this.assignedAt = assignedAt;
    }
    
    public Integer getAssignedBy() {
        return assignedBy;
    }
    
    public void setAssignedBy(Integer assignedBy) {
        this.assignedBy = assignedBy;
    }
    
    @Override
    public String toString() {
        return "UserRole{" +
                "userRoleId=" + userRoleId +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}
