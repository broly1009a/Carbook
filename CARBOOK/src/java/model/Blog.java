package model;

import java.sql.Timestamp;

/**
 * Blog Model
 * Represents a blog post in the car rental system
 */
public class Blog {
    private int blogId;
    private String title;
    private String content;
    private String summary;
    private String imageURL;
    private int authorId;
    private String categoryName;
    private int viewCount;
    private boolean isPublished;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Related object for display
    private User author;
    
    // Constructors
    public Blog() {
    }
    
    public Blog(int blogId, String title, String content, String summary, 
                String imageURL, int authorId, String categoryName, int viewCount,
                boolean isPublished, Timestamp createdAt, Timestamp updatedAt) {
        this.blogId = blogId;
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.imageURL = imageURL;
        this.authorId = authorId;
        this.categoryName = categoryName;
        this.viewCount = viewCount;
        this.isPublished = isPublished;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getBlogId() {
        return blogId;
    }
    
    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
    
    public int getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }
    
    public String getCategoryName() {
        return categoryName;
    }
    
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public boolean isPublished() {
        return isPublished;
    }
    
    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public User getAuthor() {
        return author;
    }
    
    public void setAuthor(User author) {
        this.author = author;
    }
    
    /**
     * Get short summary for display (max 150 characters)
     * @return Short summary
     */
    public String getShortSummary() {
        if (summary == null || summary.isEmpty()) {
            return "";
        }
        if (summary.length() <= 150) {
            return summary;
        }
        return summary.substring(0, 147) + "...";
    }
    
    /**
     * Get excerpt from content if summary is not available
     * @return Content excerpt
     */
    public String getExcerpt() {
        if (summary != null && !summary.isEmpty()) {
            return summary;
        }
        
        if (content == null || content.isEmpty()) {
            return "";
        }
        
        // Remove HTML tags
        String plainText = content.replaceAll("<[^>]*>", "");
        
        if (plainText.length() <= 200) {
            return plainText;
        }
        return plainText.substring(0, 197) + "...";
    }
    
    @Override
    public String toString() {
        return "Blog{" +
                "blogId=" + blogId +
                ", title='" + title + '\'' +
                ", authorId=" + authorId +
                ", categoryName='" + categoryName + '\'' +
                ", viewCount=" + viewCount +
                ", isPublished=" + isPublished +
                ", createdAt=" + createdAt +
                '}';
    }
}
