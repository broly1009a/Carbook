package dal;

import model.CarAvailability;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * CarAvailabilityDAO - Data Access Object for Car Availability management
 */
public class CarAvailabilityDAO extends DBContext {
    
    /**
     * Get all availability records
     * @return List of all availability records
     */
    public List<CarAvailability> getAllAvailability() {
        List<CarAvailability> list = new ArrayList<>();
        String sql = "SELECT ca.*, c.LicensePlate, " +
                     "CONCAT(cb.BrandName, ' ', cm.ModelName) AS CarName " +
                     "FROM CarAvailability ca " +
                     "LEFT JOIN Cars c ON ca.CarID = c.CarID " +
                     "LEFT JOIN CarModels cm ON c.ModelID = cm.ModelID " +
                     "LEFT JOIN CarBrands cb ON cm.BrandID = cb.BrandID " +
                     "ORDER BY ca.StartDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all availability: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Get availability by ID
     * @param availabilityId
     * @return CarAvailability object or null
     */
    public CarAvailability getAvailabilityById(int availabilityId) {
        String sql = "SELECT ca.*, c.LicensePlate, " +
                     "CONCAT(cb.BrandName, ' ', cm.ModelName) AS CarName " +
                     "FROM CarAvailability ca " +
                     "LEFT JOIN Cars c ON ca.CarID = c.CarID " +
                     "LEFT JOIN CarModels cm ON c.ModelID = cm.ModelID " +
                     "LEFT JOIN CarBrands cb ON cm.BrandID = cb.BrandID " +
                     "WHERE ca.AvailabilityID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, availabilityId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting availability by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get availability records by car ID
     * @param carId
     * @return List of availability records for the car
     */
    public List<CarAvailability> getAvailabilityByCarId(int carId) {
        List<CarAvailability> list = new ArrayList<>();
        String sql = "SELECT ca.*, c.LicensePlate, " +
                     "CONCAT(cb.BrandName, ' ', cm.ModelName) AS CarName " +
                     "FROM CarAvailability ca " +
                     "LEFT JOIN Cars c ON ca.CarID = c.CarID " +
                     "LEFT JOIN CarModels cm ON c.ModelID = cm.ModelID " +
                     "LEFT JOIN CarBrands cb ON cm.BrandID = cb.BrandID " +
                     "WHERE ca.CarID = ? " +
                     "ORDER BY ca.StartDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting availability by car ID: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Get blocked dates for a car (IsAvailable = 0)
     * @param carId
     * @return List of blocked availability records
     */
    public List<CarAvailability> getBlockedDatesByCarId(int carId) {
        List<CarAvailability> list = new ArrayList<>();
        String sql = "SELECT ca.*, c.LicensePlate, " +
                     "CONCAT(cb.BrandName, ' ', cm.ModelName) AS CarName " +
                     "FROM CarAvailability ca " +
                     "LEFT JOIN Cars c ON ca.CarID = c.CarID " +
                     "LEFT JOIN CarModels cm ON c.ModelID = cm.ModelID " +
                     "LEFT JOIN CarBrands cb ON cm.BrandID = cb.BrandID " +
                     "WHERE ca.CarID = ? AND ca.IsAvailable = 0 " +
                     "ORDER BY ca.StartDate";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting blocked dates: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Check if car is available for a date range
     * @param carId
     * @param startDate
     * @param endDate
     * @return true if available, false if blocked
     */
    public boolean isCarAvailableForDateRange(int carId, Date startDate, Date endDate) {
        String sql = "SELECT COUNT(*) AS BlockCount FROM CarAvailability " +
                     "WHERE CarID = ? AND IsAvailable = 0 " +
                     "AND (" +
                     "   (? BETWEEN StartDate AND EndDate) " +
                     "   OR (? BETWEEN StartDate AND EndDate) " +
                     "   OR (StartDate BETWEEN ? AND ?) " +
                     ")";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            stm.setDate(2, startDate);
            stm.setDate(3, endDate);
            stm.setDate(4, startDate);
            stm.setDate(5, endDate);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("BlockCount") == 0;
            }
        } catch (SQLException e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Create new availability record
     * @param availability
     * @return availability ID if successful, -1 otherwise
     */
    public int createAvailability(CarAvailability availability) {
        String sql = "INSERT INTO CarAvailability (CarID, StartDate, EndDate, IsAvailable, Reason) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setInt(1, availability.getCarId());
            stm.setDate(2, availability.getStartDate());
            stm.setDate(3, availability.getEndDate());
            stm.setBoolean(4, availability.isAvailable());
            stm.setString(5, availability.getReason());
            
            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating availability: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Update availability record
     * @param availability
     * @return true if successful, false otherwise
     */
    public boolean updateAvailability(CarAvailability availability) {
        String sql = "UPDATE CarAvailability SET " +
                     "StartDate = ?, EndDate = ?, IsAvailable = ?, Reason = ? " +
                     "WHERE AvailabilityID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setDate(1, availability.getStartDate());
            stm.setDate(2, availability.getEndDate());
            stm.setBoolean(3, availability.isAvailable());
            stm.setString(4, availability.getReason());
            stm.setInt(5, availability.getAvailabilityId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating availability: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete availability record
     * @param availabilityId
     * @return true if successful, false otherwise
     */
    public boolean deleteAvailability(int availabilityId) {
        String sql = "DELETE FROM CarAvailability WHERE AvailabilityID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, availabilityId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting availability: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get upcoming blocked dates (from today onwards)
     * @param carId
     * @return List of upcoming blocked dates
     */
    public List<CarAvailability> getUpcomingBlockedDates(int carId) {
        List<CarAvailability> list = new ArrayList<>();
        String sql = "SELECT ca.*, c.LicensePlate, " +
                     "CONCAT(cb.BrandName, ' ', cm.ModelName) AS CarName " +
                     "FROM CarAvailability ca " +
                     "LEFT JOIN Cars c ON ca.CarID = c.CarID " +
                     "LEFT JOIN CarModels cm ON c.ModelID = cm.ModelID " +
                     "LEFT JOIN CarBrands cb ON cm.BrandID = cb.BrandID " +
                     "WHERE ca.CarID = ? AND ca.IsAvailable = 0 " +
                     "AND ca.EndDate >= CAST(GETDATE() AS DATE) " +
                     "ORDER BY ca.StartDate";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting upcoming blocked dates: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Extract CarAvailability from ResultSet
     * @param rs
     * @return CarAvailability object
     * @throws SQLException
     */
    private CarAvailability extractFromResultSet(ResultSet rs) throws SQLException {
        CarAvailability availability = new CarAvailability();
        availability.setAvailabilityId(rs.getInt("AvailabilityID"));
        availability.setCarId(rs.getInt("CarID"));
        availability.setStartDate(rs.getDate("StartDate"));
        availability.setEndDate(rs.getDate("EndDate"));
        availability.setAvailable(rs.getBoolean("IsAvailable"));
        availability.setReason(rs.getString("Reason"));
        availability.setCreatedAt(rs.getTimestamp("CreatedAt"));
        
        // Optional car info
        try {
            availability.setLicensePlate(rs.getString("LicensePlate"));
            availability.setCarName(rs.getString("CarName"));
        } catch (SQLException e) {
            // Ignore if columns not present
        }
        
        return availability;
    }
}
