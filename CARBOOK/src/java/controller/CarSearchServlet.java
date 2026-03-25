package controller;

import dal.CarDAO;
import dal.CarCategoryDAO;
import dal.CarImageDAO;
import model.Car;
import model.CarCategory;
import model.CarImage;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CarSearchServlet - Handles car search and listing
 */
@WebServlet(name = "CarSearchServlet", urlPatterns = {"/cars", "/search-cars"})
public class CarSearchServlet extends HttpServlet {

    private CarDAO carDAO = new CarDAO();
    private CarCategoryDAO categoryDAO = new CarCategoryDAO();
    private CarImageDAO imageDAO = new CarImageDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get search parameters
            String keyword = request.getParameter("keyword");
            String categoryIdStr = request.getParameter("categoryId");
            String minPriceStr = request.getParameter("minPrice");
            String maxPriceStr = request.getParameter("maxPrice");
            String pickupLocation = request.getParameter("pickupLocation");
            String dropoffLocation = request.getParameter("dropoffLocation");
            String pickupDate = request.getParameter("pickupDate");
            String dropoffDate = request.getParameter("dropoffDate");
            String pickupTime = request.getParameter("pickupTime");
            
            // Parse numeric parameters
            int categoryId = 0;
            double minPrice = 0;
            double maxPrice = 0;
            
            if (categoryIdStr != null && !categoryIdStr.isEmpty()) {
                try {
                    categoryId = Integer.parseInt(categoryIdStr);
                } catch (NumberFormatException e) {
                    categoryId = 0;
                }
            }
            
            if (minPriceStr != null && !minPriceStr.isEmpty()) {
                try {
                    minPrice = Double.parseDouble(minPriceStr);
                } catch (NumberFormatException e) {
                    minPrice = 0;
                }
            }
            
            if (maxPriceStr != null && !maxPriceStr.isEmpty()) {
                try {
                    maxPrice = Double.parseDouble(maxPriceStr);
                } catch (NumberFormatException e) {
                    maxPrice = 0;
                }
            }
            
            List<Car> cars = null;
            
            // Check if date range search is requested
            boolean hasDateSearch = pickupDate != null && !pickupDate.isEmpty() && 
                                   dropoffDate != null && !dropoffDate.isEmpty();
            
            if (hasDateSearch) {
                // Search by date availability and location
                cars = carDAO.getAvailableCarsForBooking(pickupDate, dropoffDate, 
                                                         pickupLocation, dropoffLocation);
                System.out.println("Available cars for dates: " + (cars != null ? cars.size() : "null") + " cars found");
            } else if ((keyword != null && !keyword.trim().isEmpty()) || categoryId > 0 || 
                      minPrice > 0 || maxPrice > 0) {
                // Search by keyword, category, or price
                cars = carDAO.searchCars(keyword, categoryId, minPrice, maxPrice);
                System.out.println("Search results: " + (cars != null ? cars.size() : "null") + " cars found");
            } else {
                // Default: list all available cars
                cars = carDAO.getAvailableCars();
                System.out.println("Available cars: " + (cars != null ? cars.size() : "null") + " cars found");
            }
            
            // Get all categories for filter
            List<CarCategory> categories = categoryDAO.getAllCategories();
            System.out.println("Categories loaded: " + (categories != null ? categories.size() : "null"));
            
            // Load primary image for each car
            if (cars != null && !cars.isEmpty()) {
                System.out.println("Loading images for " + cars.size() + " cars");
                for (Car car : cars) {
                    CarImage primaryImage = imageDAO.getPrimaryImage(car.getCarId());
                    if (primaryImage != null) {
                        car.setImageUrl(primaryImage.getImageURL());
                        System.out.println("Car " + car.getCarId() + " - Primary image: " + primaryImage.getImageURL());
                    } else {
                        // If no primary image, get the first image
                        List<CarImage> images = imageDAO.getImagesByCarId(car.getCarId());
                        if (images != null && !images.isEmpty()) {
                            car.setImageUrl(images.get(0).getImageURL());
                            System.out.println("Car " + car.getCarId() + " - First image: " + images.get(0).getImageURL());
                        } else {
                            System.out.println("Car " + car.getCarId() + " - No images found, using default");
                            car.setImageUrl("images/car-1.jpg"); // Set default image
                        }
                    }
                }
            }
            
            // Set attributes with null safety
            request.setAttribute("cars", cars != null ? cars : new java.util.ArrayList<>());
            request.setAttribute("categories", categories != null ? categories : new java.util.ArrayList<>());
            request.setAttribute("keyword", keyword);
            request.setAttribute("selectedCategoryId", categoryId);
            request.setAttribute("minPrice", minPrice);
            request.setAttribute("maxPrice", maxPrice);
            request.setAttribute("pickupLocation", pickupLocation);
            request.setAttribute("dropoffLocation", dropoffLocation);
            request.setAttribute("pickupDate", pickupDate);
            request.setAttribute("dropoffDate", dropoffDate);
            request.setAttribute("pickupTime", pickupTime);
            
            request.getRequestDispatcher("car.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách xe: " + e.getMessage());
            request.setAttribute("cars", new java.util.ArrayList<>());
            request.setAttribute("categories", new java.util.ArrayList<>());
            request.getRequestDispatcher("car.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
