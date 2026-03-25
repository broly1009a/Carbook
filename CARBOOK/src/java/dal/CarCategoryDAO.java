package dal;

import model.CarCategory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * CarCategoryDAO - Data Access Object for Car Category management
 */
public class CarCategoryDAO extends DBContext {
    
    /**
     * Get all car categories
     * @return List of all categories
     */
    public List<CarCategory> getAllCategories() {
        List<CarCategory> categories = new ArrayList<>();
        String sql = "SELECT * FROM CarCategories ORDER BY CategoryName";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            
            while (rs.next()) {
                categories.add(extractCategoryFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all categories: " + e.getMessage());
        }
        return categories;
    }
    
    /**
     * Get category by ID
     * @param categoryId
     * @return CarCategory object or null
     */
    public CarCategory getCategoryById(int categoryId) {
        String sql = "SELECT * FROM CarCategories WHERE CategoryID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, categoryId);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting category by ID: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Get category by name
     * @param categoryName
     * @return CarCategory object or null
     */
    public CarCategory getCategoryByName(String categoryName) {
        String sql = "SELECT * FROM CarCategories WHERE CategoryName = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, categoryName);
            ResultSet rs = stm.executeQuery();
            
            if (rs.next()) {
                return extractCategoryFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting category by name: " + e.getMessage());
        }
        return null;
    }
    
    /**
     * Create a new category
     * @param category
     * @return true if successful, false otherwise
     */
    public boolean createCategory(CarCategory category) {
        String sql = "INSERT INTO CarCategories (CategoryName, Description) VALUES (?, ?)";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, category.getCategoryName());
            stm.setString(2, category.getDescription());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating category: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Update category
     * @param category
     * @return true if successful, false otherwise
     */
    public boolean updateCategory(CarCategory category) {
        String sql = "UPDATE CarCategories SET CategoryName = ?, Description = ? WHERE CategoryID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, category.getCategoryName());
            stm.setString(2, category.getDescription());
            stm.setInt(3, category.getCategoryId());
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating category: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Delete category
     * @param categoryId
     * @return true if successful, false otherwise
     */
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM CarCategories WHERE CategoryID = ?";
        
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, categoryId);
            
            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting category: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Extract CarCategory object from ResultSet
     * @param rs
     * @return CarCategory object
     * @throws SQLException
     */
    private CarCategory extractCategoryFromResultSet(ResultSet rs) throws SQLException {
        CarCategory category = new CarCategory();
        category.setCategoryId(rs.getInt("CategoryID"));
        category.setCategoryName(rs.getString("CategoryName"));
        category.setDescription(rs.getString("Description"));
        category.setCreatedAt(rs.getTimestamp("CreatedAt"));
        return category;
    }
    // co xe dang dung 
    public boolean isCategoryInUse(int categoryId) {
    String sql = "SELECT COUNT(*) FROM Cars WHERE CategoryID = ?";
    try {
        PreparedStatement stm = connection.prepareStatement(sql);
        stm.setInt(1, categoryId);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (SQLException e) {
        System.out.println("Error checking category usage: " + e.getMessage());
    }
    return false;
}
    
    /**
 * Kiểm tra xem tên phân loại đã tồn tại trong Database chưa (Dùng cho ADD)
 * @param categoryName Tên phân loại cần kiểm tra
 * @return true nếu đã tồn tại, false nếu chưa
 */
public boolean isCategoryNameExists(String categoryName) {
    String sql = "SELECT COUNT(*) FROM CarCategories WHERE CategoryName = ?";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setString(1, categoryName.trim());
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error isCategoryNameExists: " + e.getMessage());
    }
    return false;
}

/**
 * Kiểm tra xem tên phân loại đã tồn tại chưa, nhưng bỏ qua bản ghi hiện tại (Dùng cho EDIT)
 * @param categoryName Tên mới muốn cập nhật
 * @param excludeId ID của phân loại đang sửa
 * @return true nếu trùng với phân loại KHÁC, false nếu không trùng
 */
public boolean isCategoryNameExists(String categoryName, int excludeId) {
    String sql = "SELECT COUNT(*) FROM CarCategories WHERE CategoryName = ? AND CategoryID != ?";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setString(1, categoryName.trim());
        stm.setInt(2, excludeId);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.out.println("Error isCategoryNameExists (exclude): " + e.getMessage());
    }
    return false;
}


public List<CarCategory> searchCategoriesByName(String name) {
    List<CarCategory> list = new ArrayList<>();
    if (name == null) name = ""; 
    
    String sql = "SELECT * FROM CarCategories WHERE CategoryName LIKE ? ORDER BY CategoryName";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {

        stm.setString(1, "%" + name.trim() + "%");
        try (ResultSet rs = stm.executeQuery()) {
            while (rs.next()) {
                list.add(extractCategoryFromResultSet(rs));
            }
        }
    } catch (SQLException e) {
        System.err.println("Error searchCategoriesByName: " + e.getMessage());
    }
    return list;
}
}
