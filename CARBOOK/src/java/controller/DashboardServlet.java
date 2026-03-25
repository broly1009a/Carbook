package controller;

import dal.CarDAO;
import dal.BookingDAO;
import dal.PaymentDAO;
import dal.UserDAO;
import model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * DashboardServlet - Handles dashboard and statistics
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {

    private CarDAO carDAO = new CarDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        // Load statistics based on role
        if (user.getRoleId() == 1) {
            // Admin Dashboard
            loadAdminDashboard(request, response, user);
        } else if (user.getRoleId() == 2) {
            // Car Owner Dashboard
            loadCarOwnerDashboard(request, response, user);
        } else {
            // Customer Dashboard
            loadCustomerDashboard(request, response, user);
        }
    }

    private void loadAdminDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Total statistics
        int totalCars = carDAO.getAllCars().size();
        int totalBookings = bookingDAO.getAllBookings().size();
        int pendingBookings = bookingDAO.getPendingBookings().size();
        BigDecimal totalRevenue = paymentDAO.getTotalRevenue();
        
        // Recent bookings
        List<model.Booking> recentBookings = bookingDAO.getAllBookings();
        if (recentBookings.size() > 10) {
            recentBookings = recentBookings.subList(0, 10);
        }
        
        // Available cars
        int availableCars = carDAO.getAvailableCars().size();
        
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("recentBookings", recentBookings);
        request.setAttribute("availableCars", availableCars);
        request.setAttribute("dashboardType", "admin");
        
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void loadCarOwnerDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Owner's cars
        List<model.Car> ownerCars = carDAO.getCarsByOwnerId(user.getUserId());
        int totalCars = ownerCars.size();
        
        // Count available cars
        long availableCars = ownerCars.stream()
                .filter(car -> "Available".equals(car.getStatus()))
                .count();
        
        // Get bookings for owner's cars
        List<model.Booking> allBookings = bookingDAO.getAllBookings();
        long totalBookings = allBookings.stream()
                .filter(booking -> ownerCars.stream()
                        .anyMatch(car -> car.getCarId() == booking.getCarId()))
                .count();
        
        long pendingBookings = allBookings.stream()
                .filter(booking -> "Pending".equals(booking.getStatus()))
                .filter(booking -> ownerCars.stream()
                        .anyMatch(car -> car.getCarId() == booking.getCarId()))
                .count();
        
        request.setAttribute("totalCars", totalCars);
        request.setAttribute("availableCars", availableCars);
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("pendingBookings", pendingBookings);
        request.setAttribute("ownerCars", ownerCars);
        request.setAttribute("dashboardType", "owner");
        
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void loadCustomerDashboard(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Customer's bookings
        List<model.Booking> customerBookings = bookingDAO.getBookingsByCustomerId(user.getUserId());
        int totalBookings = customerBookings.size();
        
        // Count active bookings
        long activeBookings = customerBookings.stream()
                .filter(booking -> "Pending".equals(booking.getStatus()) || 
                                 "Approved".equals(booking.getStatus()))
                .count();
        
        // Count completed bookings
        long completedBookings = customerBookings.stream()
                .filter(booking -> "Completed".equals(booking.getStatus()))
                .count();
        
        // Available cars to browse
        List<model.Car> availableCars = carDAO.getAvailableCars();
        if (availableCars.size() > 6) {
            availableCars = availableCars.subList(0, 6);
        }
        
        request.setAttribute("totalBookings", totalBookings);
        request.setAttribute("activeBookings", activeBookings);
        request.setAttribute("completedBookings", completedBookings);
        request.setAttribute("customerBookings", customerBookings);
        request.setAttribute("availableCars", availableCars);
        request.setAttribute("dashboardType", "customer");
        
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }
}
