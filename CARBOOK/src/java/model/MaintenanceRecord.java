package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * MaintenanceRecord Model
 * Represents a car maintenance record
 */
public class MaintenanceRecord {
    private int maintenanceId;
    private int carId;
    private String maintenanceType;
    private String description;
    private String serviceProvider;
    private Timestamp serviceDate;
    private BigDecimal serviceCost;
    private Date nextServiceDate;
    private String status;
    private Integer performedBy;
    private String notes;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructors
    public MaintenanceRecord() {
    }
    
    public MaintenanceRecord(int maintenanceId, int carId, String maintenanceType,
                             String description, String serviceProvider, Timestamp serviceDate,
                             BigDecimal serviceCost, Date nextServiceDate, String status,
                             Integer performedBy, String notes, Timestamp createdAt,
                             Timestamp updatedAt) {
        this.maintenanceId = maintenanceId;
        this.carId = carId;
        this.maintenanceType = maintenanceType;
        this.description = description;
        this.serviceProvider = serviceProvider;
        this.serviceDate = serviceDate;
        this.serviceCost = serviceCost;
        this.nextServiceDate = nextServiceDate;
        this.status = status;
        this.performedBy = performedBy;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getMaintenanceId() {
        return maintenanceId;
    }
    
    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public String getMaintenanceType() {
        return maintenanceType;
    }
    
    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getServiceProvider() {
        return serviceProvider;
    }
    
    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
    
    public Timestamp getServiceDate() {
        return serviceDate;
    }
    
    public void setServiceDate(Timestamp serviceDate) {
        this.serviceDate = serviceDate;
    }
    
    public BigDecimal getServiceCost() {
        return serviceCost;
    }
    
    public void setServiceCost(BigDecimal serviceCost) {
        this.serviceCost = serviceCost;
    }
    
    public Date getNextServiceDate() {
        return nextServiceDate;
    }
    
    public void setNextServiceDate(Date nextServiceDate) {
        this.nextServiceDate = nextServiceDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Integer getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(Integer performedBy) {
        this.performedBy = performedBy;
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
        return "MaintenanceRecord{" +
                "maintenanceId=" + maintenanceId +
                ", carId=" + carId +
                ", maintenanceType='" + maintenanceType + '\'' +
                ", serviceDate=" + serviceDate +
                ", status='" + status + '\'' +
                '}';
    }
}
