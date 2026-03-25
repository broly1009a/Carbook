package model;

import java.sql.Timestamp;

/**
 * CarImage Model
 * Represents an image of a car
 */
public class CarImage {
    private int imageId;
    private int carId;
    private String imageURL;
    private boolean isPrimary;
    private int displayOrder;
    private Timestamp uploadedAt;
    
    // Constructors
    public CarImage() {
    }
    
    public CarImage(int imageId, int carId, String imageURL, boolean isPrimary,
                    int displayOrder, Timestamp uploadedAt) {
        this.imageId = imageId;
        this.carId = carId;
        this.imageURL = imageURL;
        this.isPrimary = isPrimary;
        this.displayOrder = displayOrder;
        this.uploadedAt = uploadedAt;
    }
    
    // Getters and Setters
    public int getImageId() {
        return imageId;
    }
    
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
    public boolean isPrimary() {
        return isPrimary;
    }
    
    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    
    public int getDisplayOrder() {
        return displayOrder;
    }
    
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
    
    public Timestamp getUploadedAt() {
        return uploadedAt;
    }
    
    public void setUploadedAt(Timestamp uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
    
    @Override
    public String toString() {
        return "CarImage{" +
                "imageId=" + imageId +
                ", carId=" + carId +
                ", imageURL='" + imageURL + '\'' +
                ", isPrimary=" + isPrimary +
                '}';
    }
}
