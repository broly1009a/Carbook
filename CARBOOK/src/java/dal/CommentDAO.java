package dal;

import model.Comment;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CommentDAO - Data Access Object for Comment management
 */
public class CommentDAO extends DBContext {
    
    private UserDAO userDAO = new UserDAO();
    
    public List<Comment> getCommentsByBlogId(int blogId) {
        List<Comment> allComments = new ArrayList<>();
        Map<Integer, Comment> commentMap = new HashMap<>();
        
        String sql = "SELECT c.CommentID, c.BlogID, c.UserID, c.ParentCommentID, " +
                     "c.CommentText, c.CreatedAt, c.UpdatedAt, c.IsDeleted, " +
                     "u.FullName, u.ProfileImageURL, u.Email " +
                     "FROM Comments c " +
                     "INNER JOIN Users u ON c.UserID = u.UserID " +
                     "WHERE c.BlogID = ? AND c.IsDeleted = 0 " +
                     "ORDER BY c.CreatedAt ASC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, blogId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Comment comment = extractCommentFromResultSet(rs);
                
                // Create user object
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                comment.setUser(user);
                
                commentMap.put(comment.getCommentId(), comment);
                allComments.add(comment);
            }
            
            // Second pass: Build hierarchy
            List<Comment> topLevelComments = new ArrayList<>();
            for (Comment comment : allComments) {
                if (comment.isTopLevel()) {
                    topLevelComments.add(comment);
                } else {
                    Comment parent = commentMap.get(comment.getParentCommentId());
                    if (parent != null) {
                        parent.addReply(comment);
                    }
                }
            }
            
            return topLevelComments;
            
        } catch (SQLException e) {
            System.out.println("Error getting comments for blog " + blogId + ": " + e.getMessage());
        }
        return new ArrayList<>();
    }
    
    /**
     * Get comment count for a blog
     * @param blogId The blog ID
     * @return Number of comments
     */
    public int getCommentCount(int blogId) {
        String sql = "SELECT COUNT(*) FROM Comments WHERE BlogID = ? AND IsDeleted = 0";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, blogId);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error getting comment count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get a comment by ID
     * @param commentId The comment ID
     * @return Comment object or null
     */
    public Comment getCommentById(int commentId) {
        String sql = "SELECT c.*, u.FullName, u.ProfileImageURL, u.Email " +
                     "FROM Comments c " +
                     "INNER JOIN Users u ON c.UserID = u.UserID " +
                     "WHERE c.CommentID = ? AND c.IsDeleted = 0";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, commentId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                Comment comment = extractCommentFromResultSet(rs);
                
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                comment.setUser(user);
                
                return comment;
            }
        } catch (SQLException e) {
            System.out.println("Error getting comment by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create a new comment
     * @param comment The comment to create
     * @return The created comment with ID, or null if failed
     */
    public Comment createComment(Comment comment) {
        String sql = "INSERT INTO Comments (BlogID, UserID, ParentCommentID, CommentText, CreatedAt, UpdatedAt) " +
                     "VALUES (?, ?, ?, ?, GETDATE(), GETDATE())";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stm.setInt(1, comment.getBlogId());
            stm.setInt(2, comment.getUserId());
            
            if (comment.getParentCommentId() != null) {
                stm.setInt(3, comment.getParentCommentId());
            } else {
                stm.setNull(3, java.sql.Types.INTEGER);
            }
            
            stm.setString(4, comment.getCommentText());
            
            int affectedRows = stm.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = stm.getGeneratedKeys();
                if (rs.next()) {
                    comment.setCommentId(rs.getInt(1));
                    return comment;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating comment: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Update a comment
     * @param comment The comment to update
     * @return true if successful
     */
    public boolean updateComment(Comment comment) {
        String sql = "UPDATE Comments SET CommentText = ?, UpdatedAt = GETDATE() WHERE CommentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, comment.getCommentText());
            stm.setInt(2, comment.getCommentId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating comment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Soft delete a comment (mark as deleted)
     * @param commentId The comment ID to delete
     * @return true if successful
     */
    public boolean deleteComment(int commentId) {
        String sql = "UPDATE Comments SET IsDeleted = 1, UpdatedAt = GETDATE() WHERE CommentID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, commentId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting comment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Hard delete a comment and all its replies (admin only)
     * @param commentId The comment ID to delete
     * @return true if successful
     */
    public boolean hardDeleteComment(int commentId) {
        // First delete all child comments
        String deleteChildren = "DELETE FROM Comments WHERE ParentCommentID = ?";
        String deleteParent = "DELETE FROM Comments WHERE CommentID = ?";
        
        try {
            // Delete children first
            PreparedStatement stm1 = connection.prepareStatement(deleteChildren);
            stm1.setInt(1, commentId);
            stm1.executeUpdate();
            
            // Delete parent
            PreparedStatement stm2 = connection.prepareStatement(deleteParent);
            stm2.setInt(1, commentId);
            return stm2.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error hard deleting comment: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get recent comments (for admin dashboard)
     * @param limit Number of comments to retrieve
     * @return List of recent comments
     */
    public List<Comment> getRecentComments(int limit) {
        List<Comment> comments = new ArrayList<>();
        String sql = "SELECT TOP (?) c.*, u.FullName, u.ProfileImageURL " +
                     "FROM Comments c " +
                     "INNER JOIN Users u ON c.UserID = u.UserID " +
                     "WHERE c.IsDeleted = 0 " +
                     "ORDER BY c.CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, limit);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Comment comment = extractCommentFromResultSet(rs);
                
                User user = new User();
                user.setUserId(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                comment.setUser(user);
                
                comments.add(comment);
            }
        } catch (SQLException e) {
            System.out.println("Error getting recent comments: " + e.getMessage());
        }
        return comments;
    }
    
    /**
     * Extract Comment object from ResultSet
     * @param rs ResultSet from query
     * @return Comment object
     * @throws SQLException
     */
    private Comment extractCommentFromResultSet(ResultSet rs) throws SQLException {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("CommentID"));
        comment.setBlogId(rs.getInt("BlogID"));
        comment.setUserId(rs.getInt("UserID"));
        
        int parentId = rs.getInt("ParentCommentID");
        if (!rs.wasNull()) {
            comment.setParentCommentId(parentId);
        }
        
        comment.setCommentText(rs.getString("CommentText"));
        comment.setCreatedAt(rs.getTimestamp("CreatedAt"));
        comment.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        comment.setDeleted(rs.getBoolean("IsDeleted"));
        
        return comment;
    }
}
