package dal;

import model.PaymentMethod;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentMethodDAO - Data Access Object for Payment Method management
 */
public class PaymentMethodDAO extends DBContext {
    
    /**
     * Get all payment methods
     * @return List of all payment methods
     */
    public List<PaymentMethod> getAllPaymentMethods() {
        List<PaymentMethod> methods = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethods ORDER BY MethodName";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                methods.add(extractPaymentMethodFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all payment methods: " + e.getMessage());
        }
        return methods;
    }
    
    /**
     * Get active payment methods
     * @return List of active payment methods
     */
    public List<PaymentMethod> getActivePaymentMethods() {
        List<PaymentMethod> methods = new ArrayList<>();
        String sql = "SELECT * FROM PaymentMethods WHERE IsActive = 1 ORDER BY MethodName";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                methods.add(extractPaymentMethodFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting active payment methods: " + e.getMessage());
        }
        return methods;
    }
    
    /**
     * Get payment method by ID
     * @param methodId
     * @return PaymentMethod object or null
     */
    public PaymentMethod getPaymentMethodById(int methodId) {
        String sql = "SELECT * FROM PaymentMethods WHERE PaymentMethodID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, methodId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractPaymentMethodFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting payment method by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get payment method by name
     * @param methodName
     * @return PaymentMethod object or null
     */
    public PaymentMethod getPaymentMethodByName(String methodName) {
        String sql = "SELECT * FROM PaymentMethods WHERE MethodName = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, methodName);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractPaymentMethodFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting payment method by name: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create a new payment method
     * @param method
     * @return true if successful, false otherwise
     */
    public boolean createPaymentMethod(PaymentMethod method) {
        String sql = "INSERT INTO PaymentMethods (MethodName, Description, IsActive) VALUES (?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, method.getMethodName());
            stm.setString(2, method.getDescription());
            stm.setBoolean(3, method.isActive());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating payment method: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update payment method
     * @param method
     * @return true if successful, false otherwise
     */
    public boolean updatePaymentMethod(PaymentMethod method) {
        String sql = "UPDATE PaymentMethods SET MethodName = ?, Description = ?, IsActive = ? WHERE PaymentMethodID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, method.getMethodName());
            stm.setString(2, method.getDescription());
            stm.setBoolean(3, method.isActive());
            stm.setInt(4, method.getPaymentMethodId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating payment method: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete payment method
     * @param methodId
     * @return true if successful, false otherwise
     */
    public boolean deletePaymentMethod(int methodId) {
        String sql = "DELETE FROM PaymentMethods WHERE PaymentMethodID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, methodId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting payment method: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Extract PaymentMethod object from ResultSet
     * @param rs
     * @return PaymentMethod object
     * @throws SQLException
     */
    private PaymentMethod extractPaymentMethodFromResultSet(ResultSet rs) throws SQLException {
        PaymentMethod method = new PaymentMethod();
        method.setPaymentMethodId(rs.getInt("PaymentMethodID"));
        method.setMethodName(rs.getString("MethodName"));
        method.setDescription(rs.getString("Description"));
        method.setActive(rs.getBoolean("IsActive"));
        method.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return method;
    }
}
