package controller;

import dal.CarDAO;
import dal.CarImageDAO;
import dal.ReviewDAO;
import model.Car;
import model.CarImage;
import model.ReviewDTO;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * CarDetailServlet - Handles car detail view
 */
@WebServlet(name = "CarDetailServlet", urlPatterns = {"/car-detail"})
public class CarDetailServlet extends HttpServlet {

    private CarDAO carDAO = new CarDAO();
    private CarImageDAO imageDAO = new CarImageDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            String carIdStr = request.getParameter("id");
            
            if (carIdStr == null || carIdStr.isEmpty()) {
                response.sendRedirect("cars");
                return;
            }
            
            int carId = Integer.parseInt(carIdStr);
            Car car = carDAO.getCarById(carId);
            
            if (car == null) {
                request.setAttribute("error", "Không tìm thấy xe với ID: " + carId);
                response.sendRedirect("cars");
                return;
            }
            
            // Load all images for the car
            List<CarImage> carImages = imageDAO.getImagesByCarId(carId);
            
            // Set primary image to car object
            if (carImages != null && !carImages.isEmpty()) {
                CarImage primaryImage = imageDAO.getPrimaryImage(carId);
                if (primaryImage != null) {
                    car.setImageUrl(primaryImage.getImageURL());
                } else {
                    car.setImageUrl(carImages.get(0).getImageURL());
                }
            }
            
            // Load reviews with customer information
            List<ReviewDTO> reviews = reviewDAO.getReviewsWithCustomerByCarId(carId);
            double averageRating = reviewDAO.getAverageRating(carId);
            int reviewCount = reviewDAO.getReviewCount(carId);
            
            request.setAttribute("car", car);
            request.setAttribute("carImages", carImages != null ? carImages : new java.util.ArrayList<>());
            request.setAttribute("reviews", reviews);
            request.setAttribute("averageRating", averageRating);
            request.setAttribute("reviewCount", reviewCount);
            request.getRequestDispatcher("car-single.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID xe không hợp lệ");
            response.sendRedirect("cars");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải thông tin xe: " + e.getMessage());
            response.sendRedirect("cars");
        }
    }
}
