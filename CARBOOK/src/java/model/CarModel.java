package model;

/**
 * CarModel Model
 * Represents a car model
 */
public class CarModel {
    private int modelId;
    private int brandId;
    private String modelName;
    private int year;
    
    // Related object
    private CarBrand brand;
    
    // Constructors
    public CarModel() {
    }
    
    public CarModel(int modelId, int brandId, String modelName, int year) {
        this.modelId = modelId;
        this.brandId = brandId;
        this.modelName = modelName;
        this.year = year;
    }
    
    // Getters and Setters
    public int getModelId() {
        return modelId;
    }
    
    public void setModelId(int modelId) {
        this.modelId = modelId;
    }
    
    public int getBrandId() {
        return brandId;
    }
    
    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }
    
    public String getModelName() {
        return modelName;
    }
    
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public CarBrand getBrand() {
        return brand;
    }
    
    public void setBrand(CarBrand brand) {
        this.brand = brand;
    }
    
    @Override
    public String toString() {
        return "CarModel{" +
                "modelId=" + modelId +
                ", brandId=" + brandId +
                ", modelName='" + modelName + '\'' +
                ", year=" + year +
                '}';
    }
}
