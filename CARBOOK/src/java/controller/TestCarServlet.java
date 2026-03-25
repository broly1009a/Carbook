package controller;

import dal.CarDAO;
import dal.CarBrandDAO;
import dal.CarCategoryDAO;
import dal.CarModelDAO;
import model.Car;
import model.CarBrand;
import model.CarCategory;
import model.CarModel;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * TestCarServlet - Test servlet to check database connectivity and data retrieval
 */
@WebServlet(name = "TestCarServlet", urlPatterns = {"/test-cars"})
public class TestCarServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Test Car Database</title>");
            out.println("<meta charset='UTF-8'>");
            out.println("<style>");
            out.println("body { font-family: Arial, sans-serif; margin: 20px; }");
            out.println("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
            out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
            out.println("th { background-color: #4CAF50; color: white; }");
            out.println("tr:nth-child(even) { background-color: #f2f2f2; }");
            out.println(".success { color: green; font-weight: bold; }");
            out.println(".error { color: red; font-weight: bold; }");
            out.println(".info { background-color: #e3f2fd; padding: 10px; margin: 10px 0; border-left: 4px solid #2196F3; }");
            out.println("</style>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<h1>Test Database - getAllCars()</h1>");
            
            // Test CarDAO.getAllCars()
            CarDAO carDAO = new CarDAO();
            List<Car> cars = carDAO.getAllCars();
            
            if (cars != null) {
                out.println("<p class='success'>✓ Kết nối database thành công!</p>");
                out.println("<p class='info'>Tổng số xe trong database: <strong>" + cars.size() + "</strong></p>");
                
                if (cars.isEmpty()) {
                    out.println("<p class='error'>⚠ Database chưa có dữ liệu xe. Vui lòng chạy file CRMS_Sample_Data.sql</p>");
                } else {
                    // Load additional data for display
                    CarBrandDAO brandDAO = new CarBrandDAO();
                    CarModelDAO modelDAO = new CarModelDAO();
                    CarCategoryDAO categoryDAO = new CarCategoryDAO();
                    
                    out.println("<h2>Danh sách xe:</h2>");
                    out.println("<table>");
                    out.println("<tr>");
                    out.println("<th>ID</th>");
                    out.println("<th>Biển số</th>");
                    out.println("<th>Hãng xe</th>");
                    out.println("<th>Model</th>");
                    out.println("<th>Danh mục</th>");
                    out.println("<th>Màu</th>");
                    out.println("<th>Số ghế</th>");
                    out.println("<th>Giá/ngày</th>");
                    out.println("<th>Trạng thái</th>");
                    out.println("<th>Verified</th>");
                    out.println("</tr>");
                    
                    for (Car car : cars) {
                        out.println("<tr>");
                        out.println("<td>" + car.getCarId() + "</td>");
                        out.println("<td>" + (car.getLicensePlate() != null ? car.getLicensePlate() : "N/A") + "</td>");
                        
                        // Get brand name
                        String brandName = "N/A";
                        CarModel model = modelDAO.getModelById(car.getModelId());
                        if (model != null) {
                            CarBrand brand = brandDAO.getBrandById(model.getBrandId());
                            if (brand != null) {
                                brandName = brand.getBrandName();
                            }
                        }
                        out.println("<td>" + brandName + "</td>");
                        
                        // Get model name
                        String modelName = model != null ? model.getModelName() : "N/A";
                        out.println("<td>" + modelName + "</td>");
                        
                        // Get category name
                        String categoryName = "N/A";
                        CarCategory category = categoryDAO.getCategoryById(car.getCategoryId());
                        if (category != null) {
                            categoryName = category.getCategoryName();
                        }
                        out.println("<td>" + categoryName + "</td>");
                        
                        out.println("<td>" + (car.getColor() != null ? car.getColor() : "N/A") + "</td>");
                        out.println("<td>" + car.getSeats() + "</td>");
                        out.println("<td>" + (car.getPricePerDay() != null ? car.getPricePerDay() + " ₫" : "N/A") + "</td>");
                        out.println("<td>" + (car.getStatus() != null ? car.getStatus() : "N/A") + "</td>");
                        out.println("<td>" + (car.isVerified() ? "✓" : "✗") + "</td>");
                        out.println("</tr>");
                    }
                    
                    out.println("</table>");
                }
            } else {
                out.println("<p class='error'>✗ Lỗi: Không thể kết nối database hoặc không lấy được dữ liệu</p>");
            }
            
            // Test available cars
            out.println("<hr>");
            out.println("<h2>Test getAvailableCars()</h2>");
            List<Car> availableCars = carDAO.getAvailableCars();
            if (availableCars != null) {
                out.println("<p class='info'>Số xe Available và Verified: <strong>" + availableCars.size() + "</strong></p>");
            }
            
            // Test categories
            out.println("<hr>");
            out.println("<h2>Test CarCategories</h2>");
            CarCategoryDAO categoryDAO = new CarCategoryDAO();
            List<CarCategory> categories = categoryDAO.getAllCategories();
            if (categories != null) {
                out.println("<p class='info'>Số danh mục: <strong>" + categories.size() + "</strong></p>");
                if (!categories.isEmpty()) {
                    out.println("<ul>");
                    for (CarCategory cat : categories) {
                        out.println("<li>" + cat.getCategoryName() + " (ID: " + cat.getCategoryId() + ")</li>");
                    }
                    out.println("</ul>");
                }
            }
            
            out.println("<hr>");
            out.println("<p><a href='cars'>← Quay lại trang danh sách xe</a></p>");
            
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            out.println("<p class='error'>Lỗi: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        } finally {
            out.close();
        }
    }
}
