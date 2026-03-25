package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * CarReturn Model
 * Represents a car return record
 */
public class CarReturn {
    private int returnId;
    private int bookingId;
    private Timestamp actualReturnDate;
    private String returnCondition;
    private BigDecimal fuelLevel;
    private BigDecimal mileage;
    private BigDecimal distanceTraveled;
    private boolean isLateReturn;
    private int lateReturnHours;
    private BigDecimal lateReturnPenalty;
    private boolean hasDamage;
    private String damageDescription;
    private BigDecimal damagePenalty;
    private BigDecimal totalPenalty;
    private String returnNotes;
    private Integer inspectedBy;
    private Timestamp createdAt;
    
    // Constructors
    public CarReturn() {
    }
    
    public CarReturn(int returnId, int bookingId, Timestamp actualReturnDate,
                     String returnCondition, BigDecimal fuelLevel, BigDecimal mileage,
                     BigDecimal distanceTraveled, boolean isLateReturn, int lateReturnHours,
                     BigDecimal lateReturnPenalty, boolean hasDamage, String damageDescription,
                     BigDecimal damagePenalty, BigDecimal totalPenalty, String returnNotes,
                     Integer inspectedBy, Timestamp createdAt) {
        this.returnId = returnId;
        this.bookingId = bookingId;
        this.actualReturnDate = actualReturnDate;
        this.returnCondition = returnCondition;
        this.fuelLevel = fuelLevel;
        this.mileage = mileage;
        this.distanceTraveled = distanceTraveled;
        this.isLateReturn = isLateReturn;
        this.lateReturnHours = lateReturnHours;
        this.lateReturnPenalty = lateReturnPenalty;
        this.hasDamage = hasDamage;
        this.damageDescription = damageDescription;
        this.damagePenalty = damagePenalty;
        this.totalPenalty = totalPenalty;
        this.returnNotes = returnNotes;
        this.inspectedBy = inspectedBy;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getReturnId() {
        return returnId;
    }
    
    public void setReturnId(int returnId) {
        this.returnId = returnId;
    }
    
    public int getBookingId() {
        return bookingId;
    }
    
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }
    
    public Timestamp getActualReturnDate() {
        return actualReturnDate;
    }
    
    public void setActualReturnDate(Timestamp actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
    
    public String getReturnCondition() {
        return returnCondition;
    }
    
    public void setReturnCondition(String returnCondition) {
        this.returnCondition = returnCondition;
    }
    
    public BigDecimal getFuelLevel() {
        return fuelLevel;
    }
    
    public void setFuelLevel(BigDecimal fuelLevel) {
        this.fuelLevel = fuelLevel;
    }
    
    public BigDecimal getMileage() {
        return mileage;
    }
    
    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }
    
    public BigDecimal getDistanceTraveled() {
        return distanceTraveled;
    }
    
    public void setDistanceTraveled(BigDecimal distanceTraveled) {
        this.distanceTraveled = distanceTraveled;
    }
    
    public boolean isLateReturn() {
        return isLateReturn;
    }
    
    public void setLateReturn(boolean isLateReturn) {
        this.isLateReturn = isLateReturn;
    }
    
    public int getLateReturnHours() {
        return lateReturnHours;
    }
    
    public void setLateReturnHours(int lateReturnHours) {
        this.lateReturnHours = lateReturnHours;
    }
    
    public BigDecimal getLateReturnPenalty() {
        return lateReturnPenalty;
    }
    
    public void setLateReturnPenalty(BigDecimal lateReturnPenalty) {
        this.lateReturnPenalty = lateReturnPenalty;
    }
    
    public boolean isHasDamage() {
        return hasDamage;
    }
    
    public void setHasDamage(boolean hasDamage) {
        this.hasDamage = hasDamage;
    }
    
    public String getDamageDescription() {
        return damageDescription;
    }
    
    public void setDamageDescription(String damageDescription) {
        this.damageDescription = damageDescription;
    }
    
    public BigDecimal getDamagePenalty() {
        return damagePenalty;
    }
    
    public void setDamagePenalty(BigDecimal damagePenalty) {
        this.damagePenalty = damagePenalty;
    }
    
    public BigDecimal getTotalPenalty() {
        return totalPenalty;
    }
    
    public void setTotalPenalty(BigDecimal totalPenalty) {
        this.totalPenalty = totalPenalty;
    }
    
    public String getReturnNotes() {
        return returnNotes;
    }
    
    public void setReturnNotes(String returnNotes) {
        this.returnNotes = returnNotes;
    }
    
    public Integer getInspectedBy() {
        return inspectedBy;
    }
    
    public void setInspectedBy(Integer inspectedBy) {
        this.inspectedBy = inspectedBy;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "CarReturn{" +
                "returnId=" + returnId +
                ", bookingId=" + bookingId +
                ", actualReturnDate=" + actualReturnDate +
                ", returnCondition='" + returnCondition + '\'' +
                ", totalPenalty=" + totalPenalty +
                '}';
    }
}
