package dal;

import model.MaintenanceRecord;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * MaintenanceRecordDAO - Data Access Object for Maintenance Record management
 */
public class MaintenanceRecordDAO extends DBContext {
    
    /**
     * Get all maintenance records
     * @return List of all maintenance records
     */
    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        List<MaintenanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRecords ORDER BY ServiceDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all maintenance records: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Get maintenance record by ID
     * @param maintenanceId
     * @return MaintenanceRecord object or null
     */
    public MaintenanceRecord getMaintenanceById(int maintenanceId) {
        String sql = "SELECT * FROM MaintenanceRecords WHERE MaintenanceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, maintenanceId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting maintenance by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get maintenance records by car ID
     * @param carId
     * @return List of maintenance records for the car
     */
    public List<MaintenanceRecord> getMaintenanceByCarId(int carId) {
        List<MaintenanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRecords WHERE CarID = ? ORDER BY ServiceDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting maintenance by car ID: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Get maintenance records by status
     * @param status
     * @return List of maintenance records with the status
     */
    public List<MaintenanceRecord> getMaintenanceByStatus(String status) {
        List<MaintenanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRecords WHERE Status = ? ORDER BY ServiceDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, status);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting maintenance by status: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Get upcoming maintenance (Scheduled or In Progress)
     * @return List of upcoming maintenance records
     */
    public List<MaintenanceRecord> getUpcomingMaintenance() {
        List<MaintenanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRecords " +
                     "WHERE Status IN ('Scheduled', 'In Progress') " +
                     "AND ServiceDate >= CAST(GETDATE() AS DATE) " +
                     "ORDER BY ServiceDate ASC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting upcoming maintenance: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Get upcoming maintenance for a specific car
     * @param carId
     * @return List of upcoming maintenance records for the car
     */
    public List<MaintenanceRecord> getUpcomingMaintenanceByCarId(int carId) {
        List<MaintenanceRecord> list = new ArrayList<>();
        String sql = "SELECT * FROM MaintenanceRecords " +
                     "WHERE CarID = ? " +
                     "AND Status IN ('Scheduled', 'In Progress') " +
                     "AND ServiceDate >= CAST(GETDATE() AS DATE) " +
                     "ORDER BY ServiceDate ASC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                list.add(extractFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting upcoming maintenance by car ID: " + e.getMessage());
        }
        return list;
    }
    
    /**
     * Create new maintenance record
     * @param maintenance
     * @return maintenance ID if successful, -1 otherwise
     */
    public int createMaintenance(MaintenanceRecord maintenance) {
        String sql = "INSERT INTO MaintenanceRecords " +
                     "(CarID, MaintenanceType, Description, ServiceProvider, ServiceDate, " +
                     "ServiceCost, NextServiceDate, Status, PerformedBy, Notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setInt(1, maintenance.getCarId());
            stm.setString(2, maintenance.getMaintenanceType());
            stm.setString(3, maintenance.getDescription());
            stm.setString(4, maintenance.getServiceProvider());
            stm.setTimestamp(5, maintenance.getServiceDate());
            stm.setBigDecimal(6, maintenance.getServiceCost());
            stm.setDate(7, maintenance.getNextServiceDate());
            stm.setString(8, maintenance.getStatus());
            
            if (maintenance.getPerformedBy() != null) {
                stm.setInt(9, maintenance.getPerformedBy());
            } else {
                stm.setNull(9, java.sql.Types.INTEGER);
            }
            
            stm.setString(10, maintenance.getNotes());
            
            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating maintenance: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Update maintenance record
     * @param maintenance
     * @return true if successful, false otherwise
     */
    public boolean updateMaintenance(MaintenanceRecord maintenance) {
        String sql = "UPDATE MaintenanceRecords SET " +
                     "MaintenanceType = ?, Description = ?, ServiceProvider = ?, " +
                     "ServiceDate = ?, ServiceCost = ?, NextServiceDate = ?, " +
                     "Status = ?, PerformedBy = ?, Notes = ?, UpdatedAt = GETDATE() " +
                     "WHERE MaintenanceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, maintenance.getMaintenanceType());
            stm.setString(2, maintenance.getDescription());
            stm.setString(3, maintenance.getServiceProvider());
            stm.setTimestamp(4, maintenance.getServiceDate());
            stm.setBigDecimal(5, maintenance.getServiceCost());
            stm.setDate(6, maintenance.getNextServiceDate());
            stm.setString(7, maintenance.getStatus());
            
            if (maintenance.getPerformedBy() != null) {
                stm.setInt(8, maintenance.getPerformedBy());
            } else {
                stm.setNull(8, java.sql.Types.INTEGER);
            }
            
            stm.setString(9, maintenance.getNotes());
            stm.setInt(10, maintenance.getMaintenanceId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating maintenance: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update maintenance status
     * @param maintenanceId
     * @param status
     * @return true if successful, false otherwise
     */
    public boolean updateMaintenanceStatus(int maintenanceId, String status) {
        String sql = "UPDATE MaintenanceRecords SET Status = ?, UpdatedAt = GETDATE() " +
                     "WHERE MaintenanceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, status);
            stm.setInt(2, maintenanceId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating maintenance status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete maintenance record
     * @param maintenanceId
     * @return true if successful, false otherwise
     */
    public boolean deleteMaintenance(int maintenanceId) {
        String sql = "DELETE FROM MaintenanceRecords WHERE MaintenanceID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, maintenanceId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting maintenance: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Extract MaintenanceRecord from ResultSet
     * @param rs
     * @return MaintenanceRecord object
     * @throws SQLException
     */
    private MaintenanceRecord extractFromResultSet(ResultSet rs) throws SQLException {
        MaintenanceRecord maintenance = new MaintenanceRecord();
        maintenance.setMaintenanceId(rs.getInt("MaintenanceID"));
        maintenance.setCarId(rs.getInt("CarID"));
        maintenance.setMaintenanceType(rs.getString("MaintenanceType"));
        maintenance.setDescription(rs.getString("Description"));
        maintenance.setServiceProvider(rs.getString("ServiceProvider"));
        maintenance.setServiceDate(rs.getTimestamp("ServiceDate"));
        maintenance.setServiceCost(rs.getBigDecimal("ServiceCost"));
        maintenance.setNextServiceDate(rs.getDate("NextServiceDate"));
        maintenance.setStatus(rs.getString("Status"));
        
        int performedBy = rs.getInt("PerformedBy");
        if (!rs.wasNull()) {
            maintenance.setPerformedBy(performedBy);
        }
        
        maintenance.setNotes(rs.getString("Notes"));
        maintenance.setCreatedAt(rs.getTimestamp("CreatedAt"));
        maintenance.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        
        return maintenance;
    }
}
