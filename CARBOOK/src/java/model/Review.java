package model;

import java.sql.Timestamp;

/**
 * Review Model
 * Represents a customer review for a car
 */
public class Review {
    private int reviewId;
    private int bookingId;
    private int carId;
    private int customerId;
    private int rating;
    private String comment;
    private boolean isApproved;
    private Integer approvedBy;
    private Timestamp createdAt;
    
    // Related objects
    private User customer;
    private Booking booking;
    private Car car;
    
    // Constructors
    public Review() {
    }
    
    public Review(int reviewId, int bookingId, int carId, int customerId,
                  int rating, String comment, boolean isApproved, Integer approvedBy,
                  Timestamp createdAt) {
        this.reviewId = reviewId;
        this.bookingId = bookingId;
        this.carId = carId;
        this.customerId = customerId;
        this.rating = rating;
        this.comment = comment;
        this.isApproved = isApproved;
        this.approvedBy = approvedBy;
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
    
    public Integer getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public User getCustomer() {
        return customer;
    }
    
    public void setCustomer(User customer) {
        this.customer = customer;
    }
    
    public Booking getBooking() {
        return booking;
    }
    
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    
    public Car getCar() {
        return car;
    }
    
    public void setCar(Car car) {
        this.car = car;
    }
    
    public String getStatus() {
        return isApproved ? "Approved" : "Pending";
    }
    
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", carId=" + carId +
                ", customerId=" + customerId +
                ", rating=" + rating +
                ", isApproved=" + isApproved +
                '}';
    }
}
