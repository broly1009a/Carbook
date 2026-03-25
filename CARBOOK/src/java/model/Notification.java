package model;

import java.sql.Timestamp;

/**
 * Notification Model
 * Represents a notification for a user
 */
public class Notification {
    private int notificationId;
    private int userId;
    private String type;
    private String title;
    private String message;
    private String relatedEntityType;
    private Integer relatedEntityId;
    private boolean isRead;
    private boolean isSent;
    private Timestamp sentAt;
    private Timestamp createdAt;
    
    // Constructors
    public Notification() {
    }
    
    public Notification(int notificationId, int userId, String type, String title,
                        String message, String relatedEntityType, Integer relatedEntityId,
                        boolean isRead, boolean isSent, Timestamp sentAt, Timestamp createdAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.type = type;
        this.title = title;
        this.message = message;
        this.relatedEntityType = relatedEntityType;
        this.relatedEntityId = relatedEntityId;
        this.isRead = isRead;
        this.isSent = isSent;
        this.sentAt = sentAt;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getNotificationId() {
        return notificationId;
    }
    
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getRelatedEntityType() {
        return relatedEntityType;
    }
    
    public void setRelatedEntityType(String relatedEntityType) {
        this.relatedEntityType = relatedEntityType;
    }
    
    public Integer getRelatedEntityId() {
        return relatedEntityId;
    }
    
    public void setRelatedEntityId(Integer relatedEntityId) {
        this.relatedEntityId = relatedEntityId;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    public boolean isSent() {
        return isSent;
    }
    
    public void setSent(boolean isSent) {
        this.isSent = isSent;
    }
    
    public Timestamp getSentAt() {
        return sentAt;
    }
    
    public void setSentAt(Timestamp sentAt) {
        this.sentAt = sentAt;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Notification{" +
                "notificationId=" + notificationId +
                ", userId=" + userId +
                ", type='" + type + '\'' +
                ", title='" + title + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
