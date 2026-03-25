package model;

import java.sql.Timestamp;

/**
 * SystemSetting Model
 * Represents a system configuration setting
 */
public class SystemSetting {
    private int settingId;
    private String settingKey;
    private String settingValue;
    private String description;
    private String category;
    private Integer updatedBy;
    private Timestamp updatedAt;
    
    // Constructors
    public SystemSetting() {
    }
    
    public SystemSetting(int settingId, String settingKey, String settingValue,
                         String description, String category, Integer updatedBy,
                         Timestamp updatedAt) {
        this.settingId = settingId;
        this.settingKey = settingKey;
        this.settingValue = settingValue;
        this.description = description;
        this.category = category;
        this.updatedBy = updatedBy;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getSettingId() {
        return settingId;
    }
    
    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }
    
    public String getSettingKey() {
        return settingKey;
    }
    
    public void setSettingKey(String settingKey) {
        this.settingKey = settingKey;
    }
    
    public String getSettingValue() {
        return settingValue;
    }
    
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Integer getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "SystemSetting{" +
                "settingId=" + settingId +
                ", settingKey='" + settingKey + '\'' +
                ", settingValue='" + settingValue + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
