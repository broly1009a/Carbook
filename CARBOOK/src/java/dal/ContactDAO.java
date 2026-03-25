package dal;

import model.Contact;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ContactDAO - Data Access Object for Contact management
 */
public class ContactDAO extends DBContext {
    
    /**
     * Get all contacts
     * @return List of all contacts
     */
    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM Contacts ORDER BY CreatedAt DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error in getAllContacts: " + e.getMessage());
        }
        
        return contacts;
    }
    
    /**
     * Get contact by ID
     * @param contactId
     * @return Contact object or null
     */
    public Contact getContactById(int contactId) {
        String sql = "SELECT * FROM Contacts WHERE ContactID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, contactId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Contact contact = mapResultSetToContact(rs);
                    loadContactRelatedData(contact);
                    return contact;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getContactById: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get contacts by status
     * @param status
     * @return List of contacts
     */
    public List<Contact> getContactsByStatus(String status) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM Contacts WHERE Status = ? ORDER BY CreatedAt DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getContactsByStatus: " + e.getMessage());
        }
        
        return contacts;
    }
    
    /**
     * Get unread contacts
     * @return List of unread contacts
     */
    public List<Contact> getUnreadContacts() {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM Contacts WHERE IsRead = 0 ORDER BY CreatedAt DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                contacts.add(mapResultSetToContact(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error in getUnreadContacts: " + e.getMessage());
        }
        
        return contacts;
    }
    
    /**
     * Get contacts by email
     * @param email
     * @return List of contacts
     */
    public List<Contact> getContactsByEmail(String email) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM Contacts WHERE Email = ? ORDER BY CreatedAt DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    contacts.add(mapResultSetToContact(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getContactsByEmail: " + e.getMessage());
        }
        
        return contacts;
    }
    
    /**
     * Create new contact using stored procedure
     * @param fullName
     * @param email
     * @param phoneNumber
     * @param subject
     * @param message
     * @param contactType
     * @return Contact ID if successful, -1 otherwise
     */
    public int createContact(String fullName, String email, String phoneNumber,
                            String subject, String message, String contactType) {
        String sql = "{CALL sp_CreateContact(?, ?, ?, ?, ?, ?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setString(1, fullName);
            cs.setString(2, email);
            cs.setString(3, phoneNumber);
            cs.setString(4, subject);
            cs.setString(5, message);
            cs.setString(6, contactType);
            
            cs.execute();
            
            // Get the generated contact ID
            try (ResultSet rs = cs.getResultSet()) {
                if (rs.next()) {
                    return rs.getInt("ContactID");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in createContact: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
    
    /**
     * Create contact from Contact object
     * @param contact
     * @return Contact ID if successful, -1 otherwise
     */
    public int createContact(Contact contact) {
        return createContact(
            contact.getFullName(),
            contact.getEmail(),
            contact.getPhoneNumber(),
            contact.getSubject(),
            contact.getMessage(),
            contact.getContactType() != null ? contact.getContactType() : "General"
        );
    }
    
    /**
     * Update contact status
     * @param contactId
     * @param status
     * @param priority
     * @return true if successful, false otherwise
     */
    public boolean updateContactStatus(int contactId, String status, String priority) {
        String sql = "{CALL sp_UpdateContactStatus(?, ?, ?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, contactId);
            cs.setString(2, status);
            cs.setString(3, priority);
            
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error in updateContactStatus: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Mark contact as read
     * @param contactId
     * @return true if successful, false otherwise
     */
    public boolean markAsRead(int contactId) {
        String sql = "{CALL sp_MarkContactAsRead(?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, contactId);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error in markAsRead: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Respond to contact
     * @param contactId
     * @param responseMessage
     * @param respondedBy
     * @return true if successful, false otherwise
     */
    public boolean respondToContact(int contactId, String responseMessage, int respondedBy) {
        String sql = "{CALL sp_RespondToContact(?, ?, ?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, contactId);
            cs.setString(2, responseMessage);
            cs.setInt(3, respondedBy);
            
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error in respondToContact: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Delete contact
     * @param contactId
     * @return true if successful, false otherwise
     */
    public boolean deleteContact(int contactId) {
        String sql = "{CALL sp_DeleteContact(?)}";
        
        try (CallableStatement cs = connection.prepareCall(sql)) {
            cs.setInt(1, contactId);
            cs.execute();
            return true;
        } catch (SQLException e) {
            System.out.println("Error in deleteContact: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Get contact count by status
     * @param status
     * @return Count
     */
    public int getContactCountByStatus(String status) {
        String sql = "SELECT COUNT(*) AS total FROM Contacts WHERE Status = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error in getContactCountByStatus: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Get unread contact count
     * @return Count
     */
    public int getUnreadContactCount() {
        String sql = "SELECT COUNT(*) AS total FROM Contacts WHERE IsRead = 0";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.out.println("Error in getUnreadContactCount: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Contact object
     * @param rs
     * @return Contact object
     * @throws SQLException
     */
    private Contact mapResultSetToContact(ResultSet rs) throws SQLException {
        Contact contact = new Contact();
        contact.setContactId(rs.getInt("ContactID"));
        contact.setFullName(rs.getString("FullName"));
        contact.setEmail(rs.getString("Email"));
        contact.setPhoneNumber(rs.getString("PhoneNumber"));
        contact.setSubject(rs.getString("Subject"));
        contact.setMessage(rs.getString("Message"));
        contact.setStatus(rs.getString("Status"));
        contact.setPriority(rs.getString("Priority"));
        contact.setContactType(rs.getString("ContactType"));
        contact.setRead(rs.getBoolean("IsRead"));
        contact.setResponseMessage(rs.getString("ResponseMessage"));
        contact.setRespondedBy(rs.getInt("RespondedBy"));
        contact.setRespondedAt(rs.getTimestamp("RespondedAt"));
        contact.setCreatedAt(rs.getTimestamp("CreatedAt"));
        contact.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        
        return contact;
    }
    
    /**
     * Load related data for contact (responder)
     * @param contact
     */
    private void loadContactRelatedData(Contact contact) {
        try {
            // Load responder
            if (contact.getRespondedBy() > 0) {
                UserDAO userDAO = new UserDAO();
                User responder = userDAO.getUserById(contact.getRespondedBy());
                contact.setResponder(responder);
            }
        } catch (Exception e) {
            System.out.println("Error loading contact related data: " + e.getMessage());
        }
    }
}
