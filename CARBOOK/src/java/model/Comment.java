package model;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Comment model represents a blog comment or reply
 */
public class Comment {
    private int commentId;
    private int blogId;
    private int userId;
    private Integer parentCommentId; // Nullable for top-level comments
    private String commentText;
    private Date createdAt;
    private Date updatedAt;
    private boolean isDeleted;
    
    // User information (loaded via JOIN)
    private User user;
    
    // Nested replies
    private List<Comment> replies;
    
    // Constructors
    public Comment() {
        this.replies = new ArrayList<>();
    }
    
    public Comment(int commentId, int blogId, int userId, Integer parentCommentId, 
                   String commentText, Date createdAt, Date updatedAt, boolean isDeleted) {
        this.commentId = commentId;
        this.blogId = blogId;
        this.userId = userId;
        this.parentCommentId = parentCommentId;
        this.commentText = commentText;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.replies = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getCommentId() {
        return commentId;
    }
    
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }
    
    public int getBlogId() {
        return blogId;
    }
    
    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public Integer getParentCommentId() {
        return parentCommentId;
    }
    
    public void setParentCommentId(Integer parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
    
    public String getCommentText() {
        return commentText;
    }
    
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public boolean isDeleted() {
        return isDeleted;
    }
    
    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<Comment> getReplies() {
        return replies;
    }
    
    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }
    
    public void addReply(Comment reply) {
        this.replies.add(reply);
    }
    
    // Helper methods
    public boolean isTopLevel() {
        return parentCommentId == null;
    }
    
    public boolean hasReplies() {
        return replies != null && !replies.isEmpty();
    }
    
    public String getUserName() {
        return user != null ? user.getFullName() : "Anonymous";
    }
    
    public String getUserProfileImage() {
//        if (user != null && user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
//            return user.getProfileImage();
//        }
        return "images/person_1.jpg"; // Default avatar
    }
    
    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", blogId=" + blogId +
                ", userId=" + userId +
                ", parentCommentId=" + parentCommentId +
                ", commentText='" + commentText + '\'' +
                ", createdAt=" + createdAt +
                ", isDeleted=" + isDeleted +
                '}';
    }
}
