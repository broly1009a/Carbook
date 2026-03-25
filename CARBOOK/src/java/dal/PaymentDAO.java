package dal;

import model.Payment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * PaymentDAO - Data Access Object for Payment management
 */
public class PaymentDAO extends DBContext {
    
    /**
     * Get all payments
     * @return List of all payments
     */
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, pm.MethodName " +
                     "FROM Payments p " +
                     "LEFT JOIN PaymentMethods pm ON p.PaymentMethodID = pm.PaymentMethodID " +
                     "ORDER BY p.PaymentDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all payments: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Get payment by ID
     * @param paymentId
     * @return Payment object or null
     */
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT p.*, pm.MethodName " +
                     "FROM Payments p " +
                     "LEFT JOIN PaymentMethods pm ON p.PaymentMethodID = pm.PaymentMethodID " +
                     "WHERE p.PaymentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, paymentId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting payment by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get payment by reference
     * @param paymentReference
     * @return Payment object or null
     */
    public Payment getPaymentByReference(String paymentReference) {
        String sql = "SELECT p.*, pm.MethodName " +
                     "FROM Payments p " +
                     "LEFT JOIN PaymentMethods pm ON p.PaymentMethodID = pm.PaymentMethodID " +
                     "WHERE p.PaymentReference = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, paymentReference);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractPaymentFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting payment by reference: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get payments by booking ID
     * @param bookingId
     * @return List of payments for the booking
     */
    public List<Payment> getPaymentsByBookingId(int bookingId) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, pm.MethodName " +
                     "FROM Payments p " +
                     "LEFT JOIN PaymentMethods pm ON p.PaymentMethodID = pm.PaymentMethodID " +
                     "WHERE p.BookingID = ? ORDER BY p.PaymentDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, bookingId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting payments by booking ID: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Get payments by status
     * @param status
     * @return List of payments with the status
     */
    public List<Payment> getPaymentsByStatus(String status) {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT p.*, pm.MethodName " +
                     "FROM Payments p " +
                     "LEFT JOIN PaymentMethods pm ON p.PaymentMethodID = pm.PaymentMethodID " +
                     "WHERE p.Status = ? ORDER BY p.PaymentDate DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, status);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                payments.add(extractPaymentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting payments by status: " + e.getMessage());
        }
        return payments;
    }
    
    /**
     * Get pending payments
     * @return List of pending payments
     */
    public List<Payment> getPendingPayments() {
        return getPaymentsByStatus("Pending");
    }
    
    /**
     * Get paid payments
     * @return List of paid payments
     */
    public List<Payment> getPaidPayments() {
        return getPaymentsByStatus("Paid");
    }
    
    /**
     * Create a new payment
     * @param payment
     * @return payment ID if successful, -1 otherwise
     */
    public int createPayment(Payment payment) {
        String sql = "INSERT INTO Payments (BookingID, PaymentMethodID, PaymentReference, Amount, Status, " +
                     "TransactionID, Notes) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            stm.setInt(1, payment.getBookingId());
            stm.setInt(2, payment.getPaymentMethodId());
            stm.setString(3, payment.getPaymentReference());
            stm.setBigDecimal(4, payment.getAmount());
            stm.setString(5, payment.getStatus());
            stm.setString(6, payment.getTransactionId());
            stm.setString(7, payment.getNotes());
            
            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = stm.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating payment: " + e.getMessage());
        }
        return -1;
    }
    
    /**
     * Update payment
     * @param payment
     * @return true if successful, false otherwise
     */
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE Payments SET PaymentMethodID = ?, Amount = ?, Status = ?, " +
                     "TransactionID = ?, Notes = ?, UpdatedAt = GETDATE() WHERE PaymentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, payment.getPaymentMethodId());
            stm.setBigDecimal(2, payment.getAmount());
            stm.setString(3, payment.getStatus());
            stm.setString(4, payment.getTransactionId());
            stm.setString(5, payment.getNotes());
            stm.setInt(6, payment.getPaymentId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating payment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update payment status
     * @param paymentId
     * @param status
     * @return true if successful, false otherwise
     */
    public boolean updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE Payments SET Status = ?, UpdatedAt = GETDATE() WHERE PaymentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, status);
            stm.setInt(2, paymentId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating payment status: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark payment as paid
     * @param paymentId
     * @param transactionId
     * @return true if successful, false otherwise
     */
    public boolean markAsPaid(int paymentId, String transactionId) {
        String sql = "UPDATE Payments SET Status = 'Paid', TransactionID = ?, PaymentDate = GETDATE(), " +
                     "UpdatedAt = GETDATE() WHERE PaymentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, transactionId);
            stm.setInt(2, paymentId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marking payment as paid: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark payment as failed
     * @param paymentId
     * @return true if successful, false otherwise
     */
    public boolean markAsFailed(int paymentId) {
        return updatePaymentStatus(paymentId, "Failed");
    }
    
    /**
     * Delete payment
     * @param paymentId
     * @return true if successful, false otherwise
     */
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM Payments WHERE PaymentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, paymentId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting payment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Generate unique payment reference
     * @return Payment reference string
     */
    public String generatePaymentReference() {
        return "PAY" + System.currentTimeMillis();
    }
    
    /**
     * Get total revenue
     * @return Total revenue from paid payments
     */
    public BigDecimal getTotalRevenue() {
        String sql = "SELECT SUM(Amount) AS TotalRevenue FROM Payments WHERE Status = 'Paid'";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getBigDecimal("TotalRevenue");
            }
        } catch (SQLException e) {
            System.out.println("Error getting total revenue: " + e.getMessage());
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Extract Payment object from ResultSet
     * @param rs
     * @return Payment object
     * @throws SQLException
     */
    private Payment extractPaymentFromResultSet(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("PaymentID"));
        payment.setBookingId(rs.getInt("BookingID"));
        payment.setPaymentMethodId(rs.getInt("PaymentMethodID"));
        payment.setPaymentReference(rs.getString("PaymentReference"));
        payment.setAmount(rs.getBigDecimal("Amount"));
        payment.setPaymentDate(rs.getTimestamp("PaymentDate"));
        payment.setStatus(rs.getString("Status"));
        payment.setTransactionId(rs.getString("TransactionID"));
        payment.setNotes(rs.getString("Notes"));
        payment.setCreatedAt(rs.getTimestamp("CreatedAt"));
        payment.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        // Set payment method name from JOIN
        payment.setPaymentMethod(rs.getString("MethodName"));
        return payment;
    }
}
