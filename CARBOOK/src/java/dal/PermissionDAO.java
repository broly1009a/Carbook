package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * PermissionDAO - Xử lý truy vấn quyền hạn dựa trên DBContext của dự án CRMS_DB
 */
public class PermissionDAO extends DBContext {

    /**
     * Lấy danh sách PermissionKey (mã quyền) của một Role cụ thể
     * @param roleId ID của vai trò cần lấy quyền
     * @return List các chuỗi quyền như "MANAGE_CARS", "VIEW_REPORT"...
     */
    public List<String> getPermissionsByRoleId(int roleId) {
        List<String> permissions = new ArrayList<>();
        
        // Câu lệnh SQL JOIN giữa bảng Permissions và Role_Permission
        String sql = "SELECT p.PermissionKey " +
                     "FROM Permissions p " +
                     "JOIN Role_Permission rp ON p.PermissionID = rp.PermissionID " +
                     "WHERE rp.RoleID = ?";

        try {
           
            if (connection != null) {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, roleId);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    permissions.add(rs.getString("PermissionKey"));
                }
            }
        } catch (SQLException ex) {
          
            java.util.logging.Logger.getLogger(PermissionDAO.class.getName())
                .log(java.util.logging.Level.SEVERE, "Lỗi truy vấn Permission", ex);
        } finally {
            
        }
        
        return permissions;
    }

    // Test 
    public static void main(String[] args) {
        PermissionDAO dao = new PermissionDAO();
        
        List<String> list = dao.getPermissionsByRoleId(1);
        System.out.println("Danh sách quyền của Role 1: " + list);
    }
}