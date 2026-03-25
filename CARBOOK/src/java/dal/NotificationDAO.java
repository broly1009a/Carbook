package dal;

import model.Notification;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * NotificationDAO - Data Access Object for Notification management
 */
public class NotificationDAO extends DBContext {
    
    /**
     * Get all notifications for a user
     * @param userId
     * @return List of user's notifications
     */
    public List<Notification> getNotificationsByUserId(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting notifications by user ID: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Get unread notifications for a user
     * @param userId
     * @return List of unread notifications
     */
    public List<Notification> getUnreadNotifications(int userId) {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM Notifications WHERE UserID = ? AND IsRead = 0 ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                notifications.add(extractNotificationFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting unread notifications: " + e.getMessage());
        }
        return notifications;
    }
    
    /**
     * Get notification by ID
     * @param notificationId
     * @return Notification object or null
     */
    public Notification getNotificationById(int notificationId) {
        String sql = "SELECT * FROM Notifications WHERE NotificationID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, notificationId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractNotificationFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting notification by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create a new notification
     * @param notification
     * @return true if successful, false otherwise
     */
    public boolean createNotification(Notification notification) {
        String sql = "INSERT INTO Notifications (UserID, Type, Title, Message, RelatedEntityType, RelatedEntityID) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, notification.getUserId());
            stm.setString(2, notification.getType());
            stm.setString(3, notification.getTitle());
            stm.setString(4, notification.getMessage());
            stm.setString(5, notification.getRelatedEntityType());
            stm.setInt(6, notification.getRelatedEntityId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating notification: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark notification as read
     * @param notificationId
     * @return true if successful, false otherwise
     */
    public boolean markAsRead(int notificationId) {
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE NotificationID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, notificationId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marking notification as read: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Mark all notifications as read for a user
     * @param userId
     * @return true if successful, false otherwise
     */
    public boolean markAllAsRead(int userId) {
        String sql = "UPDATE Notifications SET IsRead = 1 WHERE UserID = ? AND IsRead = 0";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error marking all notifications as read: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete notification
     * @param notificationId
     * @return true if successful, false otherwise
     */
    public boolean deleteNotification(int notificationId) {
        String sql = "DELETE FROM Notifications WHERE NotificationID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, notificationId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting notification: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get unread notification count for a user
     * @param userId
     * @return Number of unread notifications
     */
    public int getUnreadCount(int userId) {
        String sql = "SELECT COUNT(*) AS UnreadCount FROM Notifications WHERE UserID = ? AND IsRead = 0";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, userId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("UnreadCount");
            }
        } catch (SQLException e) {
            System.out.println("Error getting unread count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Extract Notification object from ResultSet
     * @param rs
     * @return Notification object
     * @throws SQLException
     */
    private Notification extractNotificationFromResultSet(ResultSet rs) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(rs.getInt("NotificationID"));
        notification.setUserId(rs.getInt("UserID"));
        notification.setType(rs.getString("Type"));
        notification.setTitle(rs.getString("Title"));
        notification.setMessage(rs.getString("Message"));
        notification.setRelatedEntityType(rs.getString("RelatedEntityType"));
        notification.setRelatedEntityId(rs.getInt("RelatedEntityID"));
        notification.setRead(rs.getBoolean("IsRead"));
        notification.setSent(rs.getBoolean("IsSent"));
        notification.setSentAt(rs.getTimestamp("SentAt"));
        notification.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return notification;
    }
}
