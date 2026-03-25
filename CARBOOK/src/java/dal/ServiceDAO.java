package dal;

import model.Service;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceDAO - Data Access Object for Service management
 */
public class ServiceDAO extends DBContext {
    
    /**
     * Get all active services for public display
     * @return List of active services
     */
    public List<Service> getAllActiveServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Services WHERE IsActive = 1 ORDER BY DisplayOrder, ServiceID";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Service service = extractServiceFromResultSet(rs);
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Error getting active services: " + e.getMessage());
        }
        return services;
    }
    
    /**
     * Get all services (for admin)
     * @return List of all services
     */
    public List<Service> getAllServices() {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Services ORDER BY DisplayOrder, ServiceID";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Service service = extractServiceFromResultSet(rs);
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all services: " + e.getMessage());
        }
        return services;
    }
    
    /**
     * Get a service by ID
     * @param serviceId The service ID
     * @return Service object or null
     */
    public Service getServiceById(int serviceId) {
        String sql = "SELECT * FROM Services WHERE ServiceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, serviceId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractServiceFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting service by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create a new service
     * @param service The service to create
     * @return The created service with ID, or null if failed
     */
    public Service createService(Service service) {
        String sql = "INSERT INTO Services (ServiceName, Description, ShortDescription, IconClass, " +
                     "Price, ImageURL, IsActive, DisplayOrder, CreatedAt, UpdatedAt) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, GETDATE(), GETDATE())";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setString(1, service.getServiceName());
            stm.setString(2, service.getDescription());
            stm.setString(3, service.getShortDescription());
            stm.setString(4, service.getIconClass());
            stm.setDouble(5, service.getPrice());
            stm.setString(6, service.getImageURL());
            stm.setBoolean(7, service.isActive());
            stm.setInt(8, service.getDisplayOrder());
            
            int affectedRows = stm.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    service.setServiceId(rs.getInt(1));
                    return service;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating service: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update an existing service
     * @param service The service to update
     * @return true if successful
     */
    public boolean updateService(Service service) {
        String sql = "UPDATE Services SET ServiceName = ?, Description = ?, ShortDescription = ?, " +
                     "IconClass = ?, Price = ?, ImageURL = ?, IsActive = ?, DisplayOrder = ?, " +
                     "UpdatedAt = GETDATE() WHERE ServiceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, service.getServiceName());
            stm.setString(2, service.getDescription());
            stm.setString(3, service.getShortDescription());
            stm.setString(4, service.getIconClass());
            stm.setDouble(5, service.getPrice());
            stm.setString(6, service.getImageURL());
            stm.setBoolean(7, service.isActive());
            stm.setInt(8, service.getDisplayOrder());
            stm.setInt(9, service.getServiceId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating service: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a service
     * @param serviceId The service ID to delete
     * @return true if successful
     */
    public boolean deleteService(int serviceId) {
        String sql = "DELETE FROM Services WHERE ServiceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, serviceId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting service: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Toggle service active status
     * @param serviceId The service ID
     * @return true if successful
     */
    public boolean toggleServiceStatus(int serviceId) {
        String sql = "UPDATE Services SET IsActive = CASE WHEN IsActive = 1 THEN 0 ELSE 1 END, " +
                     "UpdatedAt = GETDATE() WHERE ServiceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, serviceId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error toggling service status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get count of active services
     * @return Number of active services
     */
    public int getActiveServiceCount() {
        String sql = "SELECT COUNT(*) FROM Services WHERE IsActive = 1";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting active service count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Search services by name
     * @param keyword Search keyword
     * @return List of matching services
     */
    public List<Service> searchServices(String keyword) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Services WHERE ServiceName LIKE ? OR Description LIKE ? " +
                     "ORDER BY DisplayOrder, ServiceID";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            stm.setString(1, searchPattern);
            stm.setString(2, searchPattern);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Service service = extractServiceFromResultSet(rs);
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Error searching services: " + e.getMessage());
        }
        return services;
    }
    
    /**
     * Get services in a price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of services in price range
     */
    public List<Service> getServicesByPriceRange(double minPrice, double maxPrice) {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM Services WHERE IsActive = 1 AND Price BETWEEN ? AND ? " +
                     "ORDER BY Price, DisplayOrder";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setDouble(1, minPrice);
            stm.setDouble(2, maxPrice);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Service service = extractServiceFromResultSet(rs);
                services.add(service);
            }
        } catch (SQLException e) {
            System.out.println("Error getting services by price range: " + e.getMessage());
        }
        return services;
    }
    
    /**
     * Extract Service object from ResultSet
     * @param rs ResultSet from query
     * @return Service object
     * @throws SQLException
     */
    private Service extractServiceFromResultSet(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("ServiceID"));
        service.setServiceName(rs.getString("ServiceName"));
        service.setDescription(rs.getString("Description"));
        service.setShortDescription(rs.getString("ShortDescription"));
        service.setIconClass(rs.getString("IconClass"));
        service.setPrice(rs.getDouble("Price"));
        service.setImageURL(rs.getString("ImageURL"));
        service.setActive(rs.getBoolean("IsActive"));
        service.setDisplayOrder(rs.getInt("DisplayOrder"));
        service.setCreatedAt(rs.getTimestamp("CreatedAt"));
        service.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        return service;
    }
}
