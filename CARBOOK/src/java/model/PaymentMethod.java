package model;

import java.sql.Timestamp;

/**
 * PaymentMethod Model
 * Represents a payment method
 */
public class PaymentMethod {
    private int paymentMethodId;
    private String methodName;
    private String description;
    private boolean isActive;
    private Timestamp createdAt;
    
    // Constructors
    public PaymentMethod() {
    }
    
    public PaymentMethod(int paymentMethodId, String methodName, String description,
                         boolean isActive, Timestamp createdAt) {
        this.paymentMethodId = paymentMethodId;
        this.methodName = methodName;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getPaymentMethodId() {
        return paymentMethodId;
    }
    
    public void setPaymentMethodId(int paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }
    
    public String getMethodName() {
        return methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "PaymentMethod{" +
                "paymentMethodId=" + paymentMethodId +
                ", methodName='" + methodName + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
