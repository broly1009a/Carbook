package controller;

import dal.CarDAO;
import model.Car;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HomeServlet - Handles home page display
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private CarDAO carDAO = new CarDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Get featured cars (latest 8 available cars)
            List<Car> featuredCars = carDAO.getAvailableCars();
            
            // Limit to 8 cars for carousel
            if (featuredCars != null && featuredCars.size() > 8) {
                featuredCars = featuredCars.subList(0, 8);
            }
            
            // Get statistics
            int totalCars = carDAO.getAllCars().size();
            
            request.setAttribute("featuredCars", featuredCars);
            request.setAttribute("totalCars", totalCars);
            
            request.getRequestDispatcher("index.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải trang chủ: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
