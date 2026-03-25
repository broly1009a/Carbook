package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Car Model
 * Represents a car in the rental system
 */
public class Car {
    private int carId;
    private int ownerId;
    private int modelId;
    private int categoryId;
    private String licensePlate;
    private String vinNumber;
    private String color;
    private int seats;
    private String fuelType;
    private String transmission;
    private BigDecimal mileage;
    private BigDecimal pricePerDay;
    private BigDecimal pricePerHour;
    private String status;
    private String location;
    private String description;
    private String features;
    private Date insuranceExpiryDate;
    private Date registrationExpiryDate;
    private boolean isVerified;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Related objects for display
    private CarModel model;
    private CarCategory category;
    private User owner;
    private String imageUrl;
    
    // Constructors
    public Car() {
    }
    
    public Car(int carId, int ownerId, int modelId, int categoryId, String licensePlate,
               String vinNumber, String color, int seats, String fuelType, String transmission,
               BigDecimal mileage, BigDecimal pricePerDay, BigDecimal pricePerHour, String status,
               String location, String description, String features, Date insuranceExpiryDate,
               Date registrationExpiryDate, boolean isVerified, Timestamp createdAt, Timestamp updatedAt) {
        this.carId = carId;
        this.ownerId = ownerId;
        this.modelId = modelId;
        this.categoryId = categoryId;
        this.licensePlate = licensePlate;
        this.vinNumber = vinNumber;
        this.color = color;
        this.seats = seats;
        this.fuelType = fuelType;
        this.transmission = transmission;
        this.mileage = mileage;
        this.pricePerDay = pricePerDay;
        this.pricePerHour = pricePerHour;
        this.status = status;
        this.location = location;
        this.description = description;
        this.features = features;
        this.insuranceExpiryDate = insuranceExpiryDate;
        this.registrationExpiryDate = registrationExpiryDate;
        this.isVerified = isVerified;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public int getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
    
    public int getModelId() {
        return modelId;
    }
    
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
    
    public int getCategoryId() {
        return categoryId;
    }
    
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public String getVinNumber() {
        return vinNumber;
    }
    
    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public int getSeats() {
        return seats;
    }
    
    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    public String getFuelType() {
        return fuelType;
    }
    
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
    
    public String getTransmission() {
        return transmission;
    }
    
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }
    
    public BigDecimal getMileage() {
        return mileage;
    }
    
    public void setMileage(BigDecimal mileage) {
        this.mileage = mileage;
    }
    
    public BigDecimal getPricePerDay() {
        return pricePerDay;
    }
    
    public void setPricePerDay(BigDecimal pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
    
    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }
    
    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getFeatures() {
        return features;
    }
    
    public void setFeatures(String features) {
        this.features = features;
    }
    
    public Date getInsuranceExpiryDate() {
        return insuranceExpiryDate;
    }
    
    public void setInsuranceExpiryDate(Date insuranceExpiryDate) {
        this.insuranceExpiryDate = insuranceExpiryDate;
    }
    
    public Date getRegistrationExpiryDate() {
        return registrationExpiryDate;
    }
    
    public void setRegistrationExpiryDate(Date registrationExpiryDate) {
        this.registrationExpiryDate = registrationExpiryDate;
    }
    
    public boolean isVerified() {
        return isVerified;
    }
    
    public void setVerified(boolean isVerified) {
        this.isVerified = isVerified;
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
    
    public CarModel getModel() {
        return model;
    }
    
    public void setModel(CarModel model) {
        this.model = model;
    }
    
    public CarCategory getCategory() {
        return category;
    }
    
    public void setCategory(CarCategory category) {
        this.category = category;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    /**
     * Get features as a list (parsed from comma-separated string)
     * @return List of features
     */
    public java.util.List<String> getFeaturesList() {
        java.util.List<String> featuresList = new java.util.ArrayList<>();
        if (features != null && !features.trim().isEmpty()) {
            String[] featuresArray = features.split("[,\n]");
            for (String feature : featuresArray) {
                String trimmedFeature = feature.trim();
                if (!trimmedFeature.isEmpty()) {
                    featuresList.add(trimmedFeature);
                }
            }
        }
        return featuresList;
    }
    
    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", licensePlate='" + licensePlate + '\'' +
                ", modelId=" + modelId +
                ", categoryId=" + categoryId +
                ", pricePerDay=" + pricePerDay +
                ", status='" + status + '\'' +
                '}';
    }
}
