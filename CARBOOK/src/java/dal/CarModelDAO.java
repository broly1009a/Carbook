package dal;

import model.CarModel;
import model.CarBrand;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CarModelDAO extends DBContext {


    public List<CarModel> getAllModels() {
        List<CarModel> list = new ArrayList<>();
        String sql = "SELECT m.*, b.BrandName FROM CarModels m " +
                     "JOIN CarBrands b ON m.BrandID = b.BrandID " +
                     "ORDER BY b.BrandName, m.ModelName";
        try (PreparedStatement st = connection.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                CarModel m = new CarModel();
                m.setModelId(rs.getInt("ModelID"));
                m.setBrandId(rs.getInt("BrandID"));
                m.setModelName(rs.getString("ModelName"));
                m.setYear(rs.getInt("Year"));
                CarBrand b = new CarBrand();
                b.setBrandName(rs.getString("BrandName"));
                m.setBrand(b); 
                
                list.add(m);
            }
        } catch (SQLException e) {
            System.err.println("Error getAllModels: " + e.getMessage());
        }
        return list;
    }

  
    public List<CarModel> getModelsByBrandId(int brandId) {
        List<CarModel> list = new ArrayList<>();
        String sql = "SELECT * FROM CarModels WHERE BrandID = ? ORDER BY ModelName";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, brandId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    list.add(new CarModel(
                        rs.getInt("ModelID"),
                        rs.getInt("BrandID"),
                        rs.getString("ModelName"),
                        rs.getInt("Year")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getModelsByBrandId: " + e.getMessage());
        }
        return list;
    }

    public CarModel getModelById(int id) {
        String sql = "SELECT * FROM CarModels WHERE ModelID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return new CarModel(
                        rs.getInt("ModelID"),
                        rs.getInt("BrandID"),
                        rs.getString("ModelName"),
                        rs.getInt("Year")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getModelById: " + e.getMessage());
        }
        return null;
    }

    
    public boolean createModel(CarModel m) {
        String sql = "INSERT INTO CarModels (BrandID, ModelName, [Year]) VALUES (?, ?, ?)";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, m.getBrandId());
            st.setString(2, m.getModelName());
            st.setInt(3, m.getYear());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error createModel: " + e.getMessage());
        }
        return false;
    }

   
    public boolean updateModel(CarModel m) {
        String sql = "UPDATE CarModels SET BrandID = ?, ModelName = ?, [Year] = ? WHERE ModelID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, m.getBrandId());
            st.setString(2, m.getModelName());
            st.setInt(3, m.getYear());
            st.setInt(4, m.getModelId());
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updateModel: " + e.getMessage());
        }
        return false;
    }

 
    public boolean deleteModel(int id) {
        String sql = "DELETE FROM CarModels WHERE ModelID = ?";
        try (PreparedStatement st = connection.prepareStatement(sql)) {
            st.setInt(1, id);
            return st.executeUpdate() > 0;
        } catch (SQLException e) {
          
            throw new RuntimeException("Ràng buộc dữ liệu: Dòng xe này đang có xe sử dụng!");
        }
    }
    
public List<CarModel> searchByBrandName(String brandName) {
    List<CarModel> list = new ArrayList<>();

    String sql = "SELECT m.ModelID, m.BrandID, m.ModelName, m.[Year], b.BrandName " +
                 "FROM CarModels m " +
                 "JOIN CarBrands b ON m.BrandID = b.BrandID " +
                 "WHERE b.BrandName LIKE ?";

    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, "%" + brandName + "%");
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            CarModel m = new CarModel();
            m.setModelId(rs.getInt("ModelID"));
            m.setBrandId(rs.getInt("BrandID"));
            m.setModelName(rs.getString("ModelName"));
            m.setYear(rs.getInt("Year"));

            CarBrand b = new CarBrand();
            b.setBrandName(rs.getString("BrandName"));
            m.setBrand(b);

            list.add(m);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return list;
}
    
   public List<CarModel> searchByModelName(String modelName) {
    List<CarModel> list = new ArrayList<>();

    String sql = "SELECT m.ModelID, m.BrandID, m.ModelName, m.[Year], b.BrandName " +
                 "FROM CarModels m " +
                 "JOIN CarBrands b ON m.BrandID = b.BrandID " +
                 "WHERE m.ModelName LIKE ?";

    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setString(1, "%" + modelName + "%");
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            CarModel m = new CarModel();
            m.setModelId(rs.getInt("ModelID"));
            m.setBrandId(rs.getInt("BrandID"));
            m.setModelName(rs.getString("ModelName"));
            m.setYear(rs.getInt("Year"));

            CarBrand b = new CarBrand();
            b.setBrandName(rs.getString("BrandName"));
            m.setBrand(b);

            list.add(m);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return list;
}
    
   public List<CarModel> searchByYear(int year) {
    List<CarModel> list = new ArrayList<>();

    String sql = "SELECT m.ModelID, m.BrandID, m.ModelName, m.[Year], b.BrandName " +
                 "FROM CarModels m " +
                 "JOIN CarBrands b ON m.BrandID = b.BrandID " +
                 "WHERE m.[Year] = ?";

    try (PreparedStatement st = connection.prepareStatement(sql)) {
        st.setInt(1, year);
        ResultSet rs = st.executeQuery();

        while (rs.next()) {
            CarModel m = new CarModel();
            m.setModelId(rs.getInt("ModelID"));
            m.setBrandId(rs.getInt("BrandID"));
            m.setModelName(rs.getString("ModelName"));
            m.setYear(rs.getInt("Year"));

            CarBrand b = new CarBrand();
            b.setBrandName(rs.getString("BrandName"));
            m.setBrand(b);

            list.add(m);
        }
    } catch (SQLException e) {
        System.err.println(e.getMessage());
    }

    return list;
}
   
   /**
 * Kiểm tra trùng lặp cho trường hợp THÊM MỚI (Add)
 */
public boolean isModelDuplicate(int brandId, String modelName, int year) {
    String sql = "SELECT COUNT(*) FROM CarModels WHERE BrandID = ? AND ModelName = ? AND [Year] = ?";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setInt(1, brandId);
        stm.setString(2, modelName.trim());
        stm.setInt(3, year);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error isModelDuplicate (Add): " + e.getMessage());
    }
    return false;
}

/**
 * Kiểm tra trùng lặp cho trường hợp CẬP NHẬT (Edit)
 * Loại trừ chính ID hiện tại để người dùng có thể giữ nguyên dữ liệu cũ
 */
public boolean isModelDuplicate(int brandId, String modelName, int year, int excludeModelId) {
    String sql = "SELECT COUNT(*) FROM CarModels WHERE BrandID = ? AND ModelName = ? AND [Year] = ? AND ModelID != ?";
    try (PreparedStatement stm = connection.prepareStatement(sql)) {
        stm.setInt(1, brandId);
        stm.setString(2, modelName.trim());
        stm.setInt(3, year);
        stm.setInt(4, excludeModelId);
        try (ResultSet rs = stm.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
    } catch (SQLException e) {
        System.err.println("Error isModelDuplicate (Edit): " + e.getMessage());
    }
    return false;
}

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}