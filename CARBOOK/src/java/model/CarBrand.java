package model;

import java.sql.Timestamp;

/**
 * CarBrand Model
 * Represents a car brand
 */
public class CarBrand {
    private int brandId;
    private String brandName;
    private String country;
    private String logoURL;
    private Timestamp createdAt;
    
    // Constructors
    public CarBrand() {
    }
    
    public CarBrand(int brandId, String brandName, String country, 
                    String logoURL, Timestamp createdAt) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.country = country;
        this.logoURL = logoURL;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public int getBrandId() {
        return brandId;
    }
    
    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
    
    public String getBrandName() {
        return brandName;
    }
    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
    
    public String getCountry() {
        return country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    public String getLogoURL() {
        return logoURL;
    }
    
    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "CarBrand{" +
                "brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
