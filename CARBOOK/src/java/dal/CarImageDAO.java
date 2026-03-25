package dal;

import model.CarImage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * CarImageDAO - Data Access Object for Car Image management
 */
public class CarImageDAO extends DBContext {
    
    /**
     * Get all images for a specific car
     * @param carId
     * @return List of car images ordered by DisplayOrder
     */
    public List<CarImage> getImagesByCarId(int carId) {
        List<CarImage> images = new ArrayList<>();
        String sql = "SELECT * FROM CarImages WHERE CarID = ? ORDER BY DisplayOrder, ImageID";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                images.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting images by car ID: " + e.getMessage());
        }
        return images;
    }
    
    /**
     * Get primary image for a car
     * @param carId
     * @return Primary CarImage or null if none
     */
    public CarImage getPrimaryImage(int carId) {
        String sql = "SELECT TOP 1 * FROM CarImages WHERE CarID = ? AND IsPrimary = 1";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting primary image: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get image by ID
     * @param imageId
     * @return CarImage or null
     */
    public CarImage getImageById(int imageId) {
        String sql = "SELECT * FROM CarImages WHERE ImageID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, imageId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting image by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Add new image for a car
     * @param image
     * @return image ID if successful, -1 otherwise
     */
    public int addImage(CarImage image) {
        String sql = "INSERT INTO CarImages (CarID, ImageURL, IsPrimary, DisplayOrder) " +
                     "VALUES (?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setInt(1, image.getCarId());
            stm.setString(2, image.getImageURL());
            stm.setBoolean(3, image.isPrimary());
            stm.setInt(4, image.getDisplayOrder());
            
            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding image: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Update image information
     * @param image
     * @return true if successful, false otherwise
     */
    public boolean updateImage(CarImage image) {
        String sql = "UPDATE CarImages SET ImageURL = ?, IsPrimary = ?, DisplayOrder = ? " +
                     "WHERE ImageID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, image.getImageURL());
            stm.setBoolean(2, image.isPrimary());
            stm.setInt(3, image.getDisplayOrder());
            stm.setInt(4, image.getImageId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating image: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete image
     * @param imageId
     * @return true if successful, false otherwise
     */
    public boolean deleteImage(int imageId) {
        String sql = "DELETE FROM CarImages WHERE ImageID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, imageId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting image: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Set an image as primary (and unset others for the same car)
     * @param imageId
     * @param carId
     * @return true if successful, false otherwise
     */
    public boolean setPrimaryImage(int imageId, int carId) {
        try {
            // First, unset all primary images for this car
            String sql1 = "UPDATE CarImages SET IsPrimary = 0 WHERE CarID = ?";
            PreparedStatement stm1 = connection.prepareStatement(sql1);
            stm1.setInt(1, carId);
            stm1.executeUpdate();
            
            // Then set the selected image as primary
            String sql2 = "UPDATE CarImages SET IsPrimary = 1 WHERE ImageID = ?";
            PreparedStatement stm2 = connection.prepareStatement(sql2);
            stm2.setInt(1, imageId);
            
            return stm2.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error setting primary image: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get next display order for a car
     * @param carId
     * @return next display order number
     */
    public int getNextDisplayOrder(int carId) {
        String sql = "SELECT ISNULL(MAX(DisplayOrder), 0) + 1 AS NextOrder FROM CarImages WHERE CarID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("NextOrder");
            }
        } catch (SQLException e) {
            System.out.println("Error getting next display order: " + e.getMessage());
        }
        return 1;
    }
    
    /**
     * Count images for a car
     * @param carId
     * @return number of images
     */
    public int countImagesByCarId(int carId) {
        String sql = "SELECT COUNT(*) AS ImageCount FROM CarImages WHERE CarID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("ImageCount");
            }
        } catch (SQLException e) {
            System.out.println("Error counting images: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Extract CarImage from ResultSet
     * @param rs
     * @return CarImage object
     * @throws SQLException
     */
    private CarImage extractFromResultSet(ResultSet rs) throws SQLException {
        CarImage image = new CarImage();
        image.setImageId(rs.getInt("ImageID"));
        image.setCarId(rs.getInt("CarID"));
        image.setImageURL(rs.getString("ImageURL"));
        image.setPrimary(rs.getBoolean("IsPrimary"));
        image.setDisplayOrder(rs.getInt("DisplayOrder"));
        image.setUploadedAt(rs.getTimestamp("UploadedAt"));
        return image;
    }
}
