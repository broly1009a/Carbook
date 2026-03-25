package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Invoice Model
 * Represents an invoice for a booking
 */
public class Invoice {
    private int invoiceId;
    private int bookingId;
    private int paymentId;
    private String invoiceNumber;
    private Timestamp invoiceDate;
    private Timestamp dueDate;
    private BigDecimal subTotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String status;
    private String notes;
    private Timestamp createdAt;
    
    // Constructors
    public Invoice() {
    }
    
    public Invoice(int invoiceId, int bookingId, int paymentId, String invoiceNumber,
                   Timestamp invoiceDate, Timestamp dueDate, BigDecimal subTotal,
                   BigDecimal taxAmount, BigDecimal discountAmount, BigDecimal totalAmount,
                   String status, String notes, Timestamp createdAt) {
        this.invoiceId = invoiceId;
        this.bookingId = bookingId;
        this.paymentId = paymentId;
        this.invoiceNumber = invoiceNumber;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.subTotal = subTotal;
        this.taxAmount = taxAmount;
        this.discountAmount = discountAmount;
        this.totalAmount = totalAmount;
        this.status = status;
        this.notes = notes;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getInvoiceId() {
        return invoiceId;
    }
    
    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public String getInvoiceNumber() {
        return invoiceNumber;
    }
    
    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
    
    public Timestamp getInvoiceDate() {
        return invoiceDate;
    }
    
    public void setInvoiceDate(Timestamp invoiceDate) {
        this.invoiceDate = invoiceDate;
    }
    
    public Timestamp getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }
    
    public BigDecimal getSubTotal() {
        return subTotal;
    }
    
    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
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
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    // Helper methods
    public String getFormattedTotalAmount() {
        if (totalAmount == null) return "0 VNĐ";
        return String.format("%,.0f VNĐ", totalAmount);
    }
    
    public String getFormattedSubTotal() {
        if (subTotal == null) return "0 VNĐ";
        return String.format("%,.0f VNĐ", subTotal);
    }
    
    public String getFormattedTaxAmount() {
        if (taxAmount == null) return "0 VNĐ";
        return String.format("%,.0f VNĐ", taxAmount);
    }
    
    public String getFormattedDiscountAmount() {
        if (discountAmount == null) return "0 VNĐ";
        return String.format("%,.0f VNĐ", discountAmount);
    }
    
    public String getStatusBadgeClass() {
        if (status == null) return "secondary";
        switch (status) {
            case "Paid":
                return "success";
            case "Unpaid":
                return "warning";
            case "Overdue":
                return "danger";
            case "Cancelled":
                return "secondary";
            default:
                return "info";
        }
    }
    
    public boolean isPaid() {
        return "Paid".equals(status);
    }
    
    public boolean isOverdue() {
        return "Overdue".equals(status);
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", invoiceNumber='" + invoiceNumber + '\'' +
                ", bookingId=" + bookingId +
                ", paymentId=" + paymentId +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                '}';
    }
}
