package controller;

import dal.CarAvailabilityDAO;
import dal.CarDAO;
import dal.BookingDAO;
import model.CarAvailability;
import model.Car;
import model.User;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * CarAvailabilityServlet - Handles car availability/blocking operations
 */
@WebServlet(name = "CarAvailabilityServlet", urlPatterns = {"/car-availability"})
public class CarAvailabilityServlet extends HttpServlet {

    private CarAvailabilityDAO availabilityDAO = new CarAvailabilityDAO();
    private CarDAO carDAO = new CarDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listAvailability(request, response, user);
                break;
            case "create":
                showCreateForm(request, response, user);
                break;
            case "edit":
                showEditForm(request, response, user);
                break;
            case "delete":
                deleteAvailability(request, response, user);
                break;
            default:
                listAvailability(request, response, user);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createAvailability(request, response, user);
        } else if ("update".equals(action)) {
            updateAvailability(request, response, user);
        }
    }

    /**
     * List availability records
     */
    private void listAvailability(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<CarAvailability> availabilityList;
        
        String carIdParam = request.getParameter("carId");
        
        if (carIdParam != null && !carIdParam.isEmpty()) {
            // List for specific car
            int carId = Integer.parseInt(carIdParam);
            Car car = carDAO.getCarById(carId);
            
            // Check permission
            if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
                request.setAttribute("error", "Bạn không có quyền xem thông tin này");
                response.sendRedirect("dashboard");
                return;
            }
            
            availabilityList = availabilityDAO.getAvailabilityByCarId(carId);
            request.setAttribute("car", car);
        } else {
            // List all (admin) or for user's cars
            if (user.getRoleId() == 1) {
                availabilityList = availabilityDAO.getAllAvailability();
            } else {
                // Get all availability for user's cars
                List<Car> userCars = carDAO.getCarsByOwnerId(user.getUserId());
                availabilityList = new java.util.ArrayList<>();
                for (Car car : userCars) {
                    availabilityList.addAll(availabilityDAO.getAvailabilityByCarId(car.getCarId()));
                }
            }
        }
        
        request.setAttribute("availabilityList", availabilityList);
        request.getRequestDispatcher("car-availability.jsp").forward(request, response);
    }

    /**
     * Show create form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        String carIdParam = request.getParameter("carId");
        
        // Get user's cars
        List<Car> userCars;
        if (user.getRoleId() == 1) {
            userCars = carDAO.getAllCars();
        } else {
            userCars = carDAO.getCarsByOwnerId(user.getUserId());
        }
        
        request.setAttribute("cars", userCars);
        
        // Pre-select car if provided
        if (carIdParam != null && !carIdParam.isEmpty()) {
            request.setAttribute("selectedCarId", carIdParam);
        }
        
        request.getRequestDispatcher("car-availability-form.jsp").forward(request, response);
    }

    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int availabilityId = Integer.parseInt(request.getParameter("id"));
        CarAvailability availability = availabilityDAO.getAvailabilityById(availabilityId);
        
        if (availability == null) {
            request.setAttribute("error", "Không tìm thấy lịch này");
            response.sendRedirect("car-availability");
            return;
        }
        
        // Check permission
        Car car = carDAO.getCarById(availability.getCarId());
        if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
            request.setAttribute("error", "Bạn không có quyền chỉnh sửa");
            response.sendRedirect("car-availability");
            return;
        }
        
        // Get user's cars for dropdown
        List<Car> userCars;
        if (user.getRoleId() == 1) {
            userCars = carDAO.getAllCars();
        } else {
            userCars = carDAO.getCarsByOwnerId(user.getUserId());
        }
        
        request.setAttribute("availability", availability);
        request.setAttribute("cars", userCars);
        request.getRequestDispatcher("car-availability-form.jsp").forward(request, response);
    }

    /**
     * Create new availability record
     */
    private void createAvailability(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isAvailable = "1".equals(request.getParameter("isAvailable"));
            String reason = request.getParameter("reason");
            
            // Validate dates
            if (endDate.before(startDate)) {
                request.setAttribute("error", "Ngày kết thúc phải sau ngày bắt đầu");
                showCreateForm(request, response, user);
                return;
            }
            
            // Check permission
            Car car = carDAO.getCarById(carId);
            if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
                request.setAttribute("error", "Bạn không có quyền thêm lịch cho xe này");
                response.sendRedirect("car-availability");
                return;
            }
            
            // Check if marking as unavailable and there are existing bookings
            if (!isAvailable && bookingDAO.hasActiveBookingInPeriod(carId, startDate, endDate)) {
                request.setAttribute("error", "Không thể đặt lịch không khả dụng vì đã có booking trong khoảng thời gian này. Vui lòng hủy booking trước.");
                showCreateForm(request, response, user);
                return;
            }
            
            // Check if marking as unavailable and there is scheduled/active maintenance
            if (!isAvailable && bookingDAO.hasActiveMaintenanceInPeriod(carId, startDate, endDate)) {
                request.setAttribute("error", "Xe đã có lịch bảo trì trong khoảng thời gian này. Vui lòng kiểm tra lại lịch bảo trì.");
                showCreateForm(request, response, user);
                return;
            }
            
            // Create availability
            CarAvailability availability = new CarAvailability();
            availability.setCarId(carId);
            availability.setStartDate(startDate);
            availability.setEndDate(endDate);
            availability.setAvailable(isAvailable);
            availability.setReason(reason);
            
            int result = availabilityDAO.createAvailability(availability);
            
            if (result > 0) {
                request.setAttribute("success", "Đã thêm lịch thành công");
                response.sendRedirect("car-availability?carId=" + carId);
            } else {
                request.setAttribute("error", "Không thể thêm lịch");
                showCreateForm(request, response, user);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            showCreateForm(request, response, user);
        }
    }

    /**
     * Update availability record
     */
    private void updateAvailability(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int availabilityId = Integer.parseInt(request.getParameter("availabilityId"));
            int carId = Integer.parseInt(request.getParameter("carId"));
            Date startDate = Date.valueOf(request.getParameter("startDate"));
            Date endDate = Date.valueOf(request.getParameter("endDate"));
            boolean isAvailable = "1".equals(request.getParameter("isAvailable"));
            String reason = request.getParameter("reason");
            
            // Validate dates
            if (endDate.before(startDate)) {
                request.setAttribute("error", "Ngày kết thúc phải sau ngày bắt đầu");
                showEditForm(request, response, user);
                return;
            }
            
            // Check if marking as unavailable and there are existing bookings
            if (!isAvailable && bookingDAO.hasActiveBookingInPeriod(carId, startDate, endDate)) {
                request.setAttribute("error", "Không thể đặt lịch không khả dụng vì đã có booking trong khoảng thời gian này. Vui lòng hủy booking trước.");
                showEditForm(request, response, user);
                return;
            }
            
            // Check if marking as unavailable and there is scheduled/active maintenance
            if (!isAvailable && bookingDAO.hasActiveMaintenanceInPeriod(carId, startDate, endDate)) {
                request.setAttribute("error", "Xe đã có lịch bảo trì trong khoảng thời gian này. Vui lòng kiểm tra lại lịch bảo trì.");
                showEditForm(request, response, user);
                return;
            }
            
            // Check permission
            CarAvailability existing = availabilityDAO.getAvailabilityById(availabilityId);
            Car car = carDAO.getCarById(existing.getCarId());
            if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
                request.setAttribute("error", "Bạn không có quyền chỉnh sửa");
                response.sendRedirect("car-availability");
                return;
            }
            
            // Update availability
            CarAvailability availability = new CarAvailability();
            availability.setAvailabilityId(availabilityId);
            availability.setCarId(carId);
            availability.setStartDate(startDate);
            availability.setEndDate(endDate);
            availability.setAvailable(isAvailable);
            availability.setReason(reason);
            
            boolean result = availabilityDAO.updateAvailability(availability);
            
            if (result) {
                request.setAttribute("success", "Đã cập nhật lịch thành công");
                response.sendRedirect("car-availability?carId=" + carId);
            } else {
                request.setAttribute("error", "Không thể cập nhật lịch");
                showEditForm(request, response, user);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            showEditForm(request, response, user);
        }
    }

    /**
     * Delete availability record
     */
    private void deleteAvailability(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int availabilityId = Integer.parseInt(request.getParameter("id"));
        CarAvailability availability = availabilityDAO.getAvailabilityById(availabilityId);
        
        if (availability == null) {
            request.setAttribute("error", "Không tìm thấy lịch này");
            response.sendRedirect("car-availability");
            return;
        }
        
        // Check permission
        Car car = carDAO.getCarById(availability.getCarId());
        if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
            request.setAttribute("error", "Bạn không có quyền xóa");
            response.sendRedirect("car-availability");
            return;
        }
        
        boolean result = availabilityDAO.deleteAvailability(availabilityId);
        
        if (result) {
            request.setAttribute("success", "Đã xóa lịch thành công");
        } else {
            request.setAttribute("error", "Không thể xóa lịch");
        }
        
        response.sendRedirect("car-availability?carId=" + availability.getCarId());
    }
}
