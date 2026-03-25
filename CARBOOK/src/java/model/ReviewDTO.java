package model;

import java.sql.Timestamp;

/**
 * ReviewDTO - Data Transfer Object for Review with Customer Information
 */
public class ReviewDTO {
    private int reviewId;
    private int bookingId;
    private int carId;
    private int customerId;
    private String customerName;
    private String customerProfileImage;
    private int rating;
    private String comment;
    private boolean isApproved;
    private Timestamp createdAt;
    
    // Constructors
    public ReviewDTO() {
    }
    
    public ReviewDTO(int reviewId, int bookingId, int carId, int customerId,
                     String customerName, String customerProfileImage, int rating,
                     String comment, boolean isApproved, Timestamp createdAt) {
        this.reviewId = reviewId;
        this.bookingId = bookingId;
        this.carId = carId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerProfileImage = customerProfileImage;
        this.rating = rating;
        this.comment = comment;
        this.isApproved = isApproved;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public int getCustomerId() {
        return customerId;
    }
    
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerProfileImage() {
        return customerProfileImage;
    }
    
    public void setCustomerProfileImage(String customerProfileImage) {
        this.customerProfileImage = customerProfileImage;
    }
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public boolean isApproved() {
        return isApproved;
    }
    
    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
