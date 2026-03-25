package model;

import java.util.Date;

/**
 * Service model represents a service offered by CarBook
 */
public class Service {
    private int serviceId;
    private String serviceName;
    private String description;
    private String shortDescription;
    private String iconClass;
    private double price;
    private String imageURL;
    private boolean isActive;
    private int displayOrder;
    private Date createdAt;
    private Date updatedAt;
    
    // Constructors
    public Service() {
    }
    
    public Service(int serviceId, String serviceName, String description, String shortDescription,
                   String iconClass, double price, String imageURL, boolean isActive, 
                   int displayOrder, Date createdAt, Date updatedAt) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.shortDescription = shortDescription;
        this.iconClass = iconClass;
        this.price = price;
        this.imageURL = imageURL;
        this.isActive = isActive;
        this.displayOrder = displayOrder;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }
    
    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public int getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    
    /**
     * Get formatted price in VND
     * @return Formatted price string
     */
    public String getFormattedPrice() {
        return String.format("%,.0f VNĐ", price);
    }
    
    /**
     * Get short description with max length
     * @param maxLength Maximum length of description
     * @return Truncated description
     */
    public String getTruncatedDescription(int maxLength) {
        if (shortDescription == null) {
            return "";
        }
        if (shortDescription.length() <= maxLength) {
            return shortDescription;
        }
        return shortDescription.substring(0, maxLength) + "...";
    }
    
    /**
     * Get default icon class if none is set
     * @return Icon class
     */
    public String getIconClassOrDefault() {
        return (iconClass != null && !iconClass.isEmpty()) ? iconClass : "flaticon-route";
    }
    
    @Override
    public String toString() {
        return "Service{" +
                "serviceId=" + serviceId +
                ", serviceName='" + serviceName + '\'' +
                ", price=" + price +
                ", isActive=" + isActive +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
