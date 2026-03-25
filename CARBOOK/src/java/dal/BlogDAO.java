package dal;

import model.Blog;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * BlogDAO - Data Access Object for Blog management
 */
public class BlogDAO extends DBContext {
    
    private UserDAO userDAO = new UserDAO();
    
    /**
     * Get all published blogs
     * @return List of published blogs
     */
    public List<Blog> getAllPublishedBlogs() {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM Blogs WHERE IsPublished = 1 ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all published blogs: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Get all blogs (for admin)
     * @return List of all blogs
     */
    public List<Blog> getAllBlogs() {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM Blogs ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error getting all blogs: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Get blogs with pagination
     * @param page Page number (starting from 1)
     * @param pageSize Number of blogs per page
     * @return List of blogs
     */
    public List<Blog> getBlogsWithPagination(int page, int pageSize) {
        List<Blog> blogs = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        String sql = "SELECT * FROM Blogs " +
                     "WHERE IsPublished = 1 " +
                     "ORDER BY CreatedAt DESC " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, offset);
            stm.setInt(2, pageSize);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error getting blogs with pagination: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Get total count of published blogs
     * @return Total blog count
     */
    public int getTotalBlogCount() {
        String sql = "SELECT COUNT(*) AS Total FROM Blogs WHERE IsPublished = 1";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("Total");
            }
        } catch (SQLException e) {
            System.out.println("Error getting total blog count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Get blog by ID
     * @param blogId Blog ID
     * @return Blog object or null
     */
    public Blog getBlogById(int blogId) {
        String sql = "SELECT * FROM Blogs WHERE BlogID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, blogId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                return blog;
            }
        } catch (SQLException e) {
            System.out.println("Error getting blog by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get recent blogs
     * @param limit Number of blogs to retrieve
     * @return List of recent blogs
     */
    public List<Blog> getRecentBlogs(int limit) {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Blogs WHERE IsPublished = 1 ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, limit);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error getting recent blogs: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Get blogs by category
     * @param categoryName Category name
     * @return List of blogs in category
     */
    public List<Blog> getBlogsByCategory(String categoryName) {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM Blogs WHERE CategoryName = ? AND IsPublished = 1 ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, categoryName);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error getting blogs by category: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Search blogs by title or content
     * @param keyword Search keyword
     * @return List of matching blogs
     */
    public List<Blog> searchBlogs(String keyword) {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT * FROM Blogs " +
                     "WHERE IsPublished = 1 AND (Title LIKE ? OR Content LIKE ? OR Summary LIKE ?) " +
                     "ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            stm.setString(1, searchPattern);
            stm.setString(2, searchPattern);
            stm.setString(3, searchPattern);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error searching blogs: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Increment view count for a blog
     * @param blogId Blog ID
     * @return true if successful
     */
    public boolean incrementViewCount(int blogId) {
        String sql = "UPDATE Blogs SET ViewCount = ViewCount + 1 WHERE BlogID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, blogId);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error incrementing view count: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Create a new blog
     * @param blog Blog object
     * @return true if successful
     */
    public boolean createBlog(Blog blog) {
        String sql = "INSERT INTO Blogs (Title, Content, Summary, ImageURL, AuthorID, CategoryName, IsPublished) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, blog.getTitle());
            stm.setString(2, blog.getContent());
            stm.setString(3, blog.getSummary());
            stm.setString(4, blog.getImageURL());
            stm.setInt(5, blog.getAuthorId());
            stm.setString(6, blog.getCategoryName());
            stm.setBoolean(7, blog.isPublished());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating blog: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update a blog
     * @param blog Blog object
     * @return true if successful
     */
    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE Blogs SET Title = ?, Content = ?, Summary = ?, ImageURL = ?, " +
                     "CategoryName = ?, IsPublished = ?, UpdatedAt = GETDATE() " +
                     "WHERE BlogID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, blog.getTitle());
            stm.setString(2, blog.getContent());
            stm.setString(3, blog.getSummary());
            stm.setString(4, blog.getImageURL());
            stm.setString(5, blog.getCategoryName());
            stm.setBoolean(6, blog.isPublished());
            stm.setInt(7, blog.getBlogId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating blog: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete a blog
     * @param blogId Blog ID
     * @return true if successful
     */
    public boolean deleteBlog(int blogId) {
        String sql = "DELETE FROM Blogs WHERE BlogID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, blogId);
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting blog: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get popular blogs (by view count)
     * @param limit Number of blogs to retrieve
     * @return List of popular blogs
     */
    public List<Blog> getPopularBlogs(int limit) {
        List<Blog> blogs = new ArrayList<>();
        String sql = "SELECT TOP (?) * FROM Blogs WHERE IsPublished = 1 ORDER BY ViewCount DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, limit);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                Blog blog = extractBlogFromResultSet(rs);
                loadBlogAuthor(blog);
                blogs.add(blog);
            }
        } catch (SQLException e) {
            System.out.println("Error getting popular blogs: " + e.getMessage());
        }
        return blogs;
    }
    
    /**
     * Extract Blog object from ResultSet
     * @param rs ResultSet
     * @return Blog object
     * @throws SQLException
     */
    private Blog extractBlogFromResultSet(ResultSet rs) throws SQLException {
        Blog blog = new Blog();
        blog.setBlogId(rs.getInt("BlogID"));
        blog.setTitle(rs.getString("Title"));
        blog.setContent(rs.getString("Content"));
        blog.setSummary(rs.getString("Summary"));
        blog.setImageURL(rs.getString("ImageURL"));
        blog.setAuthorId(rs.getInt("AuthorID"));
        blog.setCategoryName(rs.getString("CategoryName"));
        blog.setViewCount(rs.getInt("ViewCount"));
        blog.setPublished(rs.getBoolean("IsPublished"));
        blog.setCreatedAt(rs.getTimestamp("CreatedAt"));
        blog.setUpdatedAt(rs.getTimestamp("UpdatedAt"));
        return blog;
    }
    
    /**
     * Load author information for blog
     * @param blog Blog object
     */
    private void loadBlogAuthor(Blog blog) {
        if (blog.getAuthorId() > 0) {
            User author = userDAO.getUserById(blog.getAuthorId());
            if (author != null) {
                blog.setAuthor(author);
            }
        }
    }
}
