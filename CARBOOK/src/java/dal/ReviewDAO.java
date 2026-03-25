package dal;

import model.Review;
import model.ReviewDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ReviewDAO - Data Access Object for Review management
 */
public class ReviewDAO extends DBContext {
    
    /**
     * Get all reviews
     * @return List of all reviews
     */
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all reviews: " + e.getMessage());
        }
        return reviews;
    }
    
    /**
     * Get review by ID
     * @param reviewId
     * @return Review object or null
     */
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM Reviews WHERE ReviewID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, reviewId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractReviewFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting review by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get reviews by car ID
     * @param carId
     * @return List of reviews for the car
     */
    public List<Review> getReviewsByCarId(int carId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE CarID = ? AND IsApproved = 1 ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting reviews by car ID: " + e.getMessage());
        }
        return reviews;
    }
    
    /**
     * Get reviews with customer information by car ID
     * @param carId
     * @return List of ReviewDTO with customer info
     */
    public List<ReviewDTO> getReviewsWithCustomerByCarId(int carId) {
        List<ReviewDTO> reviews = new ArrayList<>();
        String sql = "SELECT r.ReviewID, r.BookingID, r.CarID, r.CustomerID, " +
                     "u.FullName, u.ProfileImageURL, r.Rating, r.Comment, " +
                     "r.IsApproved, r.CreatedAt " +
                     "FROM Reviews r " +
                     "LEFT JOIN Users u ON r.CustomerID = u.UserID " +
                     "WHERE r.CarID = ? AND r.IsApproved = 1 " +
                     "ORDER BY r.CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                ReviewDTO review = new ReviewDTO();
                review.setReviewId(rs.getInt("ReviewID"));
                review.setBookingId(rs.getInt("BookingID"));
                review.setCarId(rs.getInt("CarID"));
                review.setCustomerId(rs.getInt("CustomerID"));
                review.setCustomerName(rs.getString("FullName"));
                review.setCustomerProfileImage(rs.getString("ProfileImageURL"));
                review.setRating(rs.getInt("Rating"));
                review.setComment(rs.getString("Comment"));
                review.setApproved(rs.getBoolean("IsApproved"));
                review.setCreatedAt(rs.getTimestamp("CreatedAt"));
                reviews.add(review);
            }
        } catch (SQLException e) {
            System.out.println("Error getting reviews with customer by car ID: " + e.getMessage());
        }
        return reviews;
    }
    
    /**
     * Get reviews by customer ID
     * @param customerId
     * @return List of reviews by the customer
     */
    public List<Review> getReviewsByCustomerId(int customerId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE CustomerID = ? ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, customerId);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting reviews by customer ID: " + e.getMessage());
        }
        return reviews;
    }
    
    /**
     * Get review by booking ID
     * @param bookingId
     * @return Review object or null if not found
     */
    public Review getReviewByBookingId(int bookingId) {
        String sql = "SELECT * FROM Reviews WHERE BookingID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, bookingId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractReviewFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting review by booking ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get pending reviews (not approved)
     * @return List of pending reviews
     */
    public List<Review> getPendingReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE IsApproved = 0 ORDER BY CreatedAt DESC";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                reviews.add(extractReviewFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting pending reviews: " + e.getMessage());
        }
        return reviews;
    }
    
    /**
     * Create a new review
     * @param review
     * @return true if successful, false otherwise
     */
    public boolean createReview(Review review) {
        String sql = "INSERT INTO Reviews (BookingID, CarID, CustomerID, Rating, Comment) VALUES (?, ?, ?, ?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, review.getBookingId());
            stm.setInt(2, review.getCarId());
            stm.setInt(3, review.getCustomerId());
            stm.setInt(4, review.getRating());
            stm.setString(5, review.getComment());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating review: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update review
     * @param review
     * @return true if successful, false otherwise
     */
    public boolean updateReview(Review review) {
        String sql = "UPDATE Reviews SET Rating = ?, Comment = ? WHERE ReviewID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, review.getRating());
            stm.setString(2, review.getComment());
            stm.setInt(3, review.getReviewId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating review: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Approve review
     * @param reviewId
     * @param approvedBy
     * @return true if successful, false otherwise
     */
    public boolean approveReview(int reviewId, int approvedBy) {
        String sql = "UPDATE Reviews SET IsApproved = 1, ApprovedBy = ? WHERE ReviewID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, approvedBy);
            stm.setInt(2, reviewId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error approving review: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete review
     * @param reviewId
     * @return true if successful, false otherwise
     */
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM Reviews WHERE ReviewID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, reviewId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting review: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Get average rating for a car
     * @param carId
     * @return Average rating
     */
    public double getAverageRating(int carId) {
        String sql = "SELECT AVG(CAST(Rating AS FLOAT)) AS AvgRating FROM Reviews WHERE CarID = ? AND IsApproved = 1";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("AvgRating");
            }
        } catch (SQLException e) {
            System.out.println("Error getting average rating: " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Get review count for a car
     * @param carId
     * @return Number of approved reviews
     */
    public int getReviewCount(int carId) {
        String sql = "SELECT COUNT(*) AS ReviewCount FROM Reviews WHERE CarID = ? AND IsApproved = 1";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("ReviewCount");
            }
        } catch (SQLException e) {
            System.out.println("Error getting review count: " + e.getMessage());
        }
        return 0;
    }
    
    /**
     * Extract Review object from ResultSet
     * @param rs
     * @return Review object
     * @throws SQLException
     */
    private Review extractReviewFromResultSet(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("ReviewID"));
        review.setBookingId(rs.getInt("BookingID"));
        review.setCarId(rs.getInt("CarID"));
        review.setCustomerId(rs.getInt("CustomerID"));
        review.setRating(rs.getInt("Rating"));
        review.setComment(rs.getString("Comment"));
        review.setApproved(rs.getBoolean("IsApproved"));
        review.setApprovedBy(rs.getInt("ApprovedBy"));
        review.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return review;
    }
}
