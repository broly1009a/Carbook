package dal;

import model.Invoice;
import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * InvoiceDAO - Data Access Object for Invoice operations
 */
public class InvoiceDAO extends DBContext {
    
    /**
     * Get all invoices
     */
    public List<Invoice> getAllInvoices() {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoices ORDER BY InvoiceDate DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                invoices.add(mapResultSetToInvoice(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllInvoices: " + e.getMessage());
        }
        
        return invoices;
    }
    
    /**
     * Get invoice by ID
     */
    public Invoice getInvoiceById(int invoiceId) {
        String sql = "SELECT * FROM Invoices WHERE InvoiceID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoiceById: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get invoice by invoice number
     */
    public Invoice getInvoiceByNumber(String invoiceNumber) {
        String sql = "SELECT * FROM Invoices WHERE InvoiceNumber = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, invoiceNumber);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoiceByNumber: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get invoice by booking ID
     */
    public Invoice getInvoiceByBookingId(int bookingId) {
        String sql = "SELECT * FROM Invoices WHERE BookingID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoiceByBookingId: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get invoice by payment ID
     */
    public Invoice getInvoiceByPaymentId(int paymentId) {
        String sql = "SELECT * FROM Invoices WHERE PaymentID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToInvoice(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoiceByPaymentId: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get invoices by status
     */
    public List<Invoice> getInvoicesByStatus(String status) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM Invoices WHERE Status = ? ORDER BY InvoiceDate DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoicesByStatus: " + e.getMessage());
        }
        
        return invoices;
    }
    
    /**
     * Create invoice from booking
     */
    public int createInvoiceFromBooking(int bookingId, Integer paymentId, 
                                       BigDecimal taxRate, BigDecimal discountAmount, String notes) {
        String sql = "{CALL sp_CreateInvoiceFromBooking(?, ?, ?, ?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, bookingId);
            
            if (paymentId != null && paymentId > 0) {
                cs.setInt(2, paymentId);
            } else {
                cs.setNull(2, Types.INTEGER);
            }
            
            cs.setBigDecimal(3, taxRate != null ? taxRate : new BigDecimal("10.00"));
            cs.setBigDecimal(4, discountAmount != null ? discountAmount : BigDecimal.ZERO);
            
            cs.execute();
            
            // Get the InvoiceID from result set
            try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) {
                    return rs.getInt("InvoiceID");
                }
            }
            
            return -1;
            
        } catch (SQLException e) {
            System.out.println("Error in createInvoiceFromBooking: " + e.getMessage());
            return -1;
        }
    }
    
    /**
     * Create invoice manually
     */
    public int createInvoice(Invoice invoice) {
        String sql = "INSERT INTO Invoices (BookingID, PaymentID, InvoiceNumber, InvoiceDate, DueDate, " +
                    "SubTotal, TaxAmount, DiscountAmount, TotalAmount, Status, Notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, invoice.getBookingId());
            
            if (invoice.getPaymentId() > 0) {
                ps.setInt(2, invoice.getPaymentId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setString(3, invoice.getInvoiceNumber());
            ps.setTimestamp(4, invoice.getInvoiceDate());
            ps.setTimestamp(5, invoice.getDueDate());
            ps.setBigDecimal(6, invoice.getSubTotal());
            ps.setBigDecimal(7, invoice.getTaxAmount());
            ps.setBigDecimal(8, invoice.getDiscountAmount());
            ps.setBigDecimal(9, invoice.getTotalAmount());
            ps.setString(10, invoice.getStatus());
            ps.setString(11, invoice.getNotes());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in createInvoice: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Update invoice
     */
    public boolean updateInvoice(Invoice invoice) {
        String sql = "UPDATE Invoices SET BookingID = ?, PaymentID = ?, InvoiceNumber = ?, " +
                    "InvoiceDate = ?, DueDate = ?, SubTotal = ?, TaxAmount = ?, DiscountAmount = ?, " +
                    "TotalAmount = ?, Status = ?, Notes = ? WHERE InvoiceID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoice.getBookingId());
            
            if (invoice.getPaymentId() > 0) {
                ps.setInt(2, invoice.getPaymentId());
            } else {
                ps.setNull(2, Types.INTEGER);
            }
            
            ps.setString(3, invoice.getInvoiceNumber());
            ps.setTimestamp(4, invoice.getInvoiceDate());
            ps.setTimestamp(5, invoice.getDueDate());
            ps.setBigDecimal(6, invoice.getSubTotal());
            ps.setBigDecimal(7, invoice.getTaxAmount());
            ps.setBigDecimal(8, invoice.getDiscountAmount());
            ps.setBigDecimal(9, invoice.getTotalAmount());
            ps.setString(10, invoice.getStatus());
            ps.setString(11, invoice.getNotes());
            ps.setInt(12, invoice.getInvoiceId());
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error in updateInvoice: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Mark invoice as paid
     */
    public boolean markInvoiceAsPaid(int invoiceId, int paymentId) {
        String sql = "{CALL sp_MarkInvoiceAsPaid(?, ?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, invoiceId);
            cs.setInt(2, paymentId);
            
            cs.execute();
            return true;
            
        } catch (SQLException e) {
            System.out.println("Error in markInvoiceAsPaid: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete invoice
     */
    public boolean deleteInvoice(int invoiceId) {
        String sql = "DELETE FROM Invoices WHERE InvoiceID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error in deleteInvoice: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate unique invoice number
     */
    public String generateInvoiceNumber() {
        String sql = "SELECT dbo.GenerateInvoiceNumber() AS InvoiceNumber";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getString("InvoiceNumber");
            }
        } catch (SQLException e) {
            System.out.println("Error in generateInvoiceNumber: " + e.getMessage());
        }
        
        // Fallback if function doesn't exist
        return "INV-" + System.currentTimeMillis();
    }
    
    /**
     * Get invoices by customer ID
     */
    public List<Invoice> getInvoicesByCustomerId(int customerId) {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT i.* FROM Invoices i " +
                    "INNER JOIN Bookings b ON i.BookingID = b.BookingID " +
                    "WHERE b.CustomerID = ? " +
                    "ORDER BY i.InvoiceDate DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    invoices.add(mapResultSetToInvoice(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoicesByCustomerId: " + e.getMessage());
        }
        
        return invoices;
    }
    
    /**
     * Get invoice count by status
     */
    public int getInvoiceCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM Invoices WHERE Status = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getInvoiceCountByStatus: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Invoice object
     */
    private Invoice mapResultSetToInvoice(ResultSet rs) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(rs.getInt("InvoiceID"));
        invoice.setBookingId(rs.getInt("BookingID"));
        
        int paymentId = rs.getInt("PaymentID");
        if (!rs.wasNull()) {
            invoice.setPaymentId(paymentId);
        }
        
        invoice.setInvoiceNumber(rs.getString("InvoiceNumber"));
        invoice.setInvoiceDate(rs.getTimestamp("InvoiceDate"));
        invoice.setDueDate(rs.getTimestamp("DueDate"));
        invoice.setSubTotal(rs.getBigDecimal("SubTotal"));
        invoice.setTaxAmount(rs.getBigDecimal("TaxAmount"));
        invoice.setDiscountAmount(rs.getBigDecimal("DiscountAmount"));
        invoice.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        invoice.setStatus(rs.getString("Status"));
        invoice.setNotes(rs.getString("Notes"));
        invoice.setCreatedAt(rs.getTimestamp("CreatedAt"));
        
        return invoice;
    }
}
