package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Payment Model
 * Represents a payment transaction
 */
public class Payment {
    private int paymentId;
    private int bookingId;
    private int paymentMethodId;
    private String paymentMethod; // For display: "VNPay", "Cash", etc.
    private String paymentReference;
    private BigDecimal amount;
    private Timestamp paymentDate;
    private String status;
    private String transactionId;
    private String transactionReference; // VNPay transaction reference
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public Payment() {
    }
    
    public Payment(int paymentId, int bookingId, int paymentMethodId, String paymentReference,
                   BigDecimal amount, Timestamp paymentDate, String status, String transactionId,
                   String notes, Timestamp createdAt, Timestamp updatedAt) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentMethodId = paymentMethodId;
        this.paymentReference = paymentReference;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.status = status;
        this.transactionId = transactionId;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public int getPaymentMethodId() {
        return paymentMethodId;
    }
    
    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
    
    public String getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public String getPaymentReference() {
        return paymentReference;
    }
    
    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Timestamp getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getTransactionReference() {
        return transactionReference;
    }
    
    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
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
    
    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", bookingId=" + bookingId +
                ", paymentReference='" + paymentReference + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
