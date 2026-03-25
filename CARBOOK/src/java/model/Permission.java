package model;

import java.sql.Timestamp;

/**
 * Permission Model
 * Represents a system permission
 */
public class Permission {
    private int permissionId;
    private String permissionName;
    private String description;
    private String module;
    private Timestamp createdAt;
    
    // Constructors
    public Permission() {
    }
    
    public Permission(int permissionId, String permissionName, String description,
                      String module, Timestamp createdAt) {
        this.permissionId = permissionId;
        this.permissionName = permissionName;
        this.description = description;
        this.module = module;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getPermissionId() {
        return permissionId;
    }
    
    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getModule() {
        return module;
    }
    
    public void setModule(String module) {
        this.module = module;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Permission{" +
                "permissionId=" + permissionId +
                ", permissionName='" + permissionName + '\'' +
                ", module='" + module + '\'' +
                '}';
    }
}
