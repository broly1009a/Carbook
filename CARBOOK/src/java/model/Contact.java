package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Contact Model
 * Represents a customer contact inquiry
 */
public class Contact {
    private int contactId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String subject;
    private String message;
    private String status;
    private String priority;
    private String contactType;
    private boolean isRead;
    private String responseMessage;
    private int respondedBy;
    private Timestamp respondedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Related objects
    private User responder;
    
    // Constructors
    public Contact() {
    }
    
    public Contact(int contactId, String fullName, String email, String phoneNumber,
                   String subject, String message, String status, String priority,
                   String contactType, boolean isRead, String responseMessage,
                   int respondedBy, Timestamp respondedAt, Timestamp createdAt,
                   Timestamp updatedAt) {
        this.contactId = contactId;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.subject = subject;
        this.message = message;
        this.status = status;
        this.priority = priority;
        this.contactType = contactType;
        this.isRead = isRead;
        this.responseMessage = responseMessage;
        this.respondedBy = respondedBy;
        this.respondedAt = respondedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getContactId() {
        return contactId;
    }
    
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getSubject() {
        return subject;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public String getContactType() {
        return contactType;
    }
    
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    
    public boolean isRead() {
        return isRead;
    }
    
    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }
    
    public String getResponseMessage() {
        return responseMessage;
    }
    
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
    
    public int getRespondedBy() {
        return respondedBy;
    }
    
    public void setRespondedBy(int respondedBy) {
        this.respondedBy = respondedBy;
    }
    
    public Timestamp getRespondedAt() {
        return respondedAt;
    }
    
    public void setRespondedAt(Timestamp respondedAt) {
        this.respondedAt = respondedAt;
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
    
    public User getResponder() {
        return responder;
    }
    
    public void setResponder(User responder) {
        this.responder = responder;
    }
    
    // Helper methods
    
    /**
     * Get formatted created date
     * @return Formatted date string
     */
    public String getFormattedCreatedDate() {
        if (createdAt == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(createdAt);
    }
    
    /**
     * Get formatted responded date
     * @return Formatted date string
     */
    public String getFormattedRespondedDate() {
        if (respondedAt == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(respondedAt);
    }
    
    /**
     * Get status badge class for Bootstrap
     * @return Bootstrap badge class
     */
    public String getStatusBadgeClass() {
        switch (status) {
            case "New":
                return "badge-info";
            case "In Progress":
                return "badge-warning";
            case "Resolved":
                return "badge-success";
            case "Closed":
                return "badge-secondary";
            case "Spam":
                return "badge-danger";
            default:
                return "badge-secondary";
        }
    }
    
    /**
     * Get priority badge class for Bootstrap
     * @return Bootstrap badge class
     */
    public String getPriorityBadgeClass() {
        switch (priority) {
            case "Low":
                return "badge-secondary";
            case "Normal":
                return "badge-info";
            case "High":
                return "badge-warning";
            case "Urgent":
                return "badge-danger";
            default:
                return "badge-secondary";
        }
    }
    
    /**
     * Check if contact has been responded to
     * @return true if responded, false otherwise
     */
    public boolean isResponded() {
        return responseMessage != null && !responseMessage.isEmpty();
    }
    
    /**
     * Get truncated message
     * @param maxLength Maximum length
     * @return Truncated message
     */
    public String getTruncatedMessage(int maxLength) {
        if (message == null) return "";
        if (message.length() <= maxLength) return message;
        return message.substring(0, maxLength) + "...";
    }
    
    /**
     * Get contact type display name
     * @return Display name
     */
    public String getContactTypeDisplay() {
        switch (contactType) {
            case "General":
                return "Chung";
            case "Inquiry":
                return "Hỏi đáp";
            case "Technical":
                return "Kỹ thuật";
            case "Feedback":
                return "Phản hồi";
            case "Complaint":
                return "Khiếu nại";
            default:
                return contactType;
        }
    }
    
    @Override
    public String toString() {
        return "Contact{" +
                "contactId=" + contactId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", subject='" + subject + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", isRead=" + isRead +
                ", createdAt=" + createdAt +
                '}';
    }
}
