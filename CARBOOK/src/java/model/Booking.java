package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Booking Model
 * Represents a car booking/rental
 */
public class Booking {
    private int bookingId;
    private int carId;
    private int customerId;
    private String bookingReference;
    private Timestamp pickupDate;
    private Timestamp returnDate;
    private String pickupLocation;
    private String returnLocation;
    private int totalDays;
    private int totalHours;
    private BigDecimal basePrice;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status;
    private Integer approvedBy;
    private Timestamp approvedAt;
    private String rejectionReason;
    private String cancellationReason;
    private Integer cancelledBy;
    private Timestamp cancelledAt;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Related objects
    private Car car;
    private User customer;
    
    // Constructors
    public Booking() {
    }
    
    public Booking(int bookingId, int carId, int customerId, String bookingReference,
                   Timestamp pickupDate, Timestamp returnDate, String pickupLocation,
                   String returnLocation, int totalDays, int totalHours, BigDecimal basePrice,
                   BigDecimal taxAmount, BigDecimal discountAmount, BigDecimal totalAmount,
                   String status, Integer approvedBy, Timestamp approvedAt, String rejectionReason,
                   String cancellationReason, Integer cancelledBy, Timestamp cancelledAt,
                   String notes, Timestamp createdAt, Timestamp updatedAt) {
        this.bookingId = bookingId;
        this.carId = carId;
        this.customerId = customerId;
        this.bookingReference = bookingReference;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.totalDays = totalDays;
        this.totalHours = totalHours;
        this.basePrice = basePrice;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.rejectionReason = rejectionReason;
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
        this.cancelledAt = cancelledAt;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
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
    
    public String getBookingReference() {
        return bookingReference;
    }
    
    public void setBookingReference(String bookingReference) {
        this.bookingReference = bookingReference;
    }
    
    public Timestamp getPickupDate() {
        return pickupDate;
    }
    
    public void setPickupDate(Timestamp pickupDate) {
        this.pickupDate = pickupDate;
    }
    
    public Timestamp getReturnDate() {
        return returnDate;
    }
    
    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }
    
    public String getPickupLocation() {
        return pickupLocation;
    }
    
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    
    public String getReturnLocation() {
        return returnLocation;
    }
    
    public void setReturnLocation(String returnLocation) {
        this.returnLocation = returnLocation;
    }
    
    public int getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
    
    public int getTotalHours() {
        return totalHours;
    }
    
    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }
    
    public BigDecimal getBasePrice() {
        return basePrice;
    }
    
    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }
    
    public BigDecimal getTaxAmount() {
        return taxAmount;
    }
    
    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
    
    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }
    
    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Timestamp getApprovedAt() {
        return approvedAt;
    }
    
    public void setApprovedAt(Timestamp approvedAt) {
        this.approvedAt = approvedAt;
    }
    
    public String getRejectionReason() {
        return rejectionReason;
    }
    
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    
    public String getCancellationReason() {
        return cancellationReason;
    }
    
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    
    public Integer getCancelledBy() {
        return cancelledBy;
    }
    
    public void setCancelledBy(Integer cancelledBy) {
        this.cancelledBy = cancelledBy;
    }
    
    public Timestamp getCancelledAt() {
        return cancelledAt;
    }
    
    public void setCancelledAt(Timestamp cancelledAt) {
        this.cancelledAt = cancelledAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public Car getCar() {
        return car;
    }
    
    public void setCar(Car car) {
        this.car = car;
    }
    
    public User getCustomer() {
        return customer;
    }
    
    public void setCustomer(User customer) {
        this.customer = customer;
    }
    
    @Override
    public String toString() {
        return "Booking{" +
                "bookingId=" + bookingId +
                ", bookingReference='" + bookingReference + '\'' +
                ", carId=" + carId +
                ", customerId=" + customerId +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
