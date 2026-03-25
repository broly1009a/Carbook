package controller;

import dal.MaintenanceRecordDAO;
import dal.CarDAO;
import dal.BookingDAO;
import dal.CarAvailabilityDAO;
import model.MaintenanceRecord;
import model.Car;
import model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * MaintenanceServlet - Handles maintenance record operations
 */
@WebServlet(name = "MaintenanceServlet", urlPatterns = {"/maintenance"})
public class MaintenanceServlet extends HttpServlet {

    private MaintenanceRecordDAO maintenanceDAO = new MaintenanceRecordDAO();
    private CarDAO carDAO = new CarDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private CarAvailabilityDAO availabilityDAO = new CarAvailabilityDAO();

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
                listMaintenance(request, response, user);
                break;
            case "create":
                showCreateForm(request, response, user);
                break;
            case "edit":
                showEditForm(request, response, user);
                break;
            case "delete":
                deleteMaintenance(request, response, user);
                break;
            case "complete":
                completeMaintenance(request, response, user);
                break;
            default:
                listMaintenance(request, response, user);
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
            createMaintenance(request, response, user);
        } else if ("update".equals(action)) {
            updateMaintenance(request, response, user);
        }
    }

    /**
     * List maintenance records
     */
    private void listMaintenance(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<MaintenanceRecord> maintenanceList;
        
        String carIdParam = request.getParameter("carId");
        String statusFilter = request.getParameter("status");
        
        System.out.println("=== Maintenance Filter Debug ===");
        System.out.println("Car ID: " + carIdParam);
        System.out.println("Status filter: " + statusFilter);
        System.out.println("User Role: " + user.getRoleId());
        
        // First, load data based on role and carId (if provided)
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
            
            maintenanceList = maintenanceDAO.getMaintenanceByCarId(carId);
            request.setAttribute("car", car);
        } else if (user.getRoleId() == 1) {
            // Admin sees all
            maintenanceList = maintenanceDAO.getAllMaintenanceRecords();
        } else if (user.getRoleId() == 2) {
            // Car owner sees only their cars' maintenance
            List<Car> userCars = carDAO.getCarsByOwnerId(user.getUserId());
            maintenanceList = new ArrayList<>();
            for (Car car : userCars) {
                maintenanceList.addAll(maintenanceDAO.getMaintenanceByCarId(car.getCarId()));
            }
        } else {
            // Customers cannot access
            request.setAttribute("error", "Bạn không có quyền truy cập trang này");
            response.sendRedirect("dashboard");
            return;
        }
        
        System.out.println("Total maintenance records before filter: " + (maintenanceList != null ? maintenanceList.size() : 0));
        
        // Apply status filter if provided
        if (statusFilter != null && !statusFilter.trim().isEmpty() && maintenanceList != null && !maintenanceList.isEmpty()) {
            List<MaintenanceRecord> filteredList = new ArrayList<>();
            
            for (MaintenanceRecord maintenance : maintenanceList) {
                if (statusFilter.equals(maintenance.getStatus())) {
                    filteredList.add(maintenance);
                    System.out.println("Maintenance " + maintenance.getMaintenanceId() + 
                                     " status: " + maintenance.getStatus() + " - MATCHES");
                } else {
                    System.out.println("Maintenance " + maintenance.getMaintenanceId() + 
                                     " status: " + maintenance.getStatus() + " - NO MATCH");
                }
            }
            
            maintenanceList = filteredList;
            System.out.println("Total maintenance records after filter: " + maintenanceList.size());
        }
        System.out.println("=== End Maintenance Filter Debug ===");
        
        request.setAttribute("maintenanceList", maintenanceList);
        request.getRequestDispatcher("maintenance-records.jsp").forward(request, response);
    }

    /**
     * Show create form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only Admin and Car Owners can create
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            request.setAttribute("error", "Bạn không có quyền thêm lịch bảo trì");
            response.sendRedirect("dashboard");
            return;
        }
        
        // Get user's cars for dropdown
        List<Car> userCars;
        if (user.getRoleId() == 1) {
            userCars = carDAO.getAllCars();
        } else {
            userCars = carDAO.getCarsByOwnerId(user.getUserId());
        }
        
        // Pre-select car if carId provided
        String carIdParam = request.getParameter("carId");
        if (carIdParam != null && !carIdParam.isEmpty()) {
            int carId = Integer.parseInt(carIdParam);
            Car selectedCar = carDAO.getCarById(carId);
            request.setAttribute("selectedCar", selectedCar);
        }
        
        request.setAttribute("cars", userCars);
        request.getRequestDispatcher("maintenance-form.jsp").forward(request, response);
    }

    /**
     * Show edit form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int maintenanceId = Integer.parseInt(request.getParameter("id"));
        MaintenanceRecord maintenance = maintenanceDAO.getMaintenanceById(maintenanceId);
        
        if (maintenance == null) {
            request.setAttribute("error", "Không tìm thấy lịch bảo trì này");
            response.sendRedirect("maintenance");
            return;
        }
        
        // Check permission
        Car car = carDAO.getCarById(maintenance.getCarId());
        if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
            request.setAttribute("error", "Bạn không có quyền chỉnh sửa");
            response.sendRedirect("maintenance");
            return;
        }
        
        // Get user's cars for dropdown
        List<Car> userCars;
        if (user.getRoleId() == 1) {
            userCars = carDAO.getAllCars();
        } else {
            userCars = carDAO.getCarsByOwnerId(user.getUserId());
        }
        
        request.setAttribute("maintenance", maintenance);
        request.setAttribute("cars", userCars);
        request.getRequestDispatcher("maintenance-form.jsp").forward(request, response);
    }

    /**
     * Create new maintenance record
     */
    private void createMaintenance(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int carId = Integer.parseInt(request.getParameter("carId"));
            String maintenanceType = request.getParameter("maintenanceType");
            String description = request.getParameter("description");
            String serviceProvider = request.getParameter("serviceProvider");
            String serviceDateStr = request.getParameter("serviceDate");
            String serviceCostStr = request.getParameter("serviceCost");
            String nextServiceDateStr = request.getParameter("nextServiceDate");
            String status = request.getParameter("status");
            String notes = request.getParameter("notes");
            
            // Check permission
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                request.setAttribute("error", "Không tìm thấy xe");
                response.sendRedirect("maintenance");
                return;
            }

            if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
                request.setAttribute("error", "Bạn không có quyền thêm lịch bảo trì cho xe này");
                response.sendRedirect("maintenance");
                return;
            }
            
            // Parse dates for validation
            Date serviceDate = null;
            Date nextServiceDate = null;
            
            if (serviceDateStr != null && !serviceDateStr.isEmpty()) {
                serviceDate = Date.valueOf(serviceDateStr);
            }
            
            if (nextServiceDateStr != null && !nextServiceDateStr.isEmpty()) {
                nextServiceDate = Date.valueOf(nextServiceDateStr);
            }

            if (serviceDate == null) {
                request.setAttribute("error", "Vui lòng chọn ngày bảo trì");
                showCreateForm(request, response, user);
                return;
            }

            if (nextServiceDate != null && nextServiceDate.before(serviceDate)) {
                request.setAttribute("error", "Ngày bảo trì tiếp theo phải sau hoặc bằng ngày bảo trì");
                showCreateForm(request, response, user);
                return;
            }
            
            // Only active maintenance should be validated against booking/availability overlap
            if ("Scheduled".equals(status) || "In Progress".equals(status)) {
                Date endDate = nextServiceDate != null ? nextServiceDate : serviceDate;
                
                if (bookingDAO.hasActiveBookingInPeriod(carId, serviceDate, endDate)) {
                    request.setAttribute("error", "Không thể tạo lịch bảo trì vì xe đã có booking trong khoảng thời gian này. Vui lòng hủy booking trước.");
                    showCreateForm(request, response, user);
                    return;
                }
                
                // Check if car is already blocked in CarAvailability
                if (!availabilityDAO.isCarAvailableForDateRange(carId, serviceDate, endDate)) {
                    request.setAttribute("error", "Không thể tạo lịch bảo trì vì xe đã có lịch không khả dụng trong khoảng thời gian này.");
                    showCreateForm(request, response, user);
                    return;
                }
            }
            
            // Create maintenance record
            MaintenanceRecord maintenance = new MaintenanceRecord();
            maintenance.setCarId(carId);
            maintenance.setMaintenanceType(maintenanceType);
            maintenance.setDescription(description);
            maintenance.setServiceProvider(serviceProvider);
            
            // Set service date (already parsed)
            if (serviceDate != null) {
                maintenance.setServiceDate(new Timestamp(serviceDate.getTime()));
            }
            
            // Parse service cost
            if (serviceCostStr != null && !serviceCostStr.isEmpty()) {
                maintenance.setServiceCost(new BigDecimal(serviceCostStr));
            }
            
            // Set next service date (already parsed)
            if (nextServiceDate != null) {
                maintenance.setNextServiceDate(nextServiceDate);
            }
            
            maintenance.setStatus(status);
            maintenance.setPerformedBy(user.getUserId());
            maintenance.setNotes(notes);
            
            int result = maintenanceDAO.createMaintenance(maintenance);
            
            if (result > 0) {
                HttpSession session = request.getSession();
                session.setAttribute("success", "Đã thêm lịch bảo trì thành công");
                response.sendRedirect("maintenance?carId=" + carId);
            } else {
                request.setAttribute("error", "Không thể thêm lịch bảo trì");
                showCreateForm(request, response, user);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            showCreateForm(request, response, user);
        }
    }

    /**
     * Update maintenance record
     */
    private void updateMaintenance(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int maintenanceId = Integer.parseInt(request.getParameter("maintenanceId"));
            int carId = Integer.parseInt(request.getParameter("carId"));
            String maintenanceType = request.getParameter("maintenanceType");
            String description = request.getParameter("description");
            String serviceProvider = request.getParameter("serviceProvider");
            String serviceDateStr = request.getParameter("serviceDate");
            String serviceCostStr = request.getParameter("serviceCost");
            String nextServiceDateStr = request.getParameter("nextServiceDate");
            String status = request.getParameter("status");
            String notes = request.getParameter("notes");
            
            // Check permission
            Car car = carDAO.getCarById(carId);
            if (car == null) {
                request.setAttribute("error", "Không tìm thấy xe");
                response.sendRedirect("maintenance");
                return;
            }

            if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
                request.setAttribute("error", "Bạn không có quyền chỉnh sửa");
                response.sendRedirect("maintenance");
                return;
            }
            
            // Parse dates for validation
            Date serviceDate = null;
            Date nextServiceDate = null;
            
            if (serviceDateStr != null && !serviceDateStr.isEmpty()) {
                serviceDate = Date.valueOf(serviceDateStr);
            }
            
            if (nextServiceDateStr != null && !nextServiceDateStr.isEmpty()) {
                nextServiceDate = Date.valueOf(nextServiceDateStr);
            }

            if (serviceDate == null) {
                request.setAttribute("error", "Vui lòng chọn ngày bảo trì");
                showEditForm(request, response, user);
                return;
            }

            if (nextServiceDate != null && nextServiceDate.before(serviceDate)) {
                request.setAttribute("error", "Ngày bảo trì tiếp theo phải sau hoặc bằng ngày bảo trì");
                showEditForm(request, response, user);
                return;
            }
            
            // Only active maintenance should be validated against booking/availability overlap
            if ("Scheduled".equals(status) || "In Progress".equals(status)) {
                Date endDate = nextServiceDate != null ? nextServiceDate : serviceDate;
                
                if (bookingDAO.hasActiveBookingInPeriod(carId, serviceDate, endDate)) {
                    request.setAttribute("error", "Không thể cập nhật lịch bảo trì vì xe đã có booking trong khoảng thời gian này. Vui lòng hủy booking trước.");
                    showEditForm(request, response, user);
                    return;
                }
                
                // Check if car is already blocked in CarAvailability
                if (!availabilityDAO.isCarAvailableForDateRange(carId, serviceDate, endDate)) {
                    request.setAttribute("error", "Không thể cập nhật lịch bảo trì vì xe đã có lịch không khả dụng trong khoảng thời gian này.");
                    showEditForm(request, response, user);
                    return;
                }
            }
            
            // Update maintenance record
            MaintenanceRecord maintenance = new MaintenanceRecord();
            maintenance.setMaintenanceId(maintenanceId);
            maintenance.setCarId(carId);
            maintenance.setMaintenanceType(maintenanceType);
            maintenance.setDescription(description);
            maintenance.setServiceProvider(serviceProvider);
            
            // Set service date (already parsed)
            if (serviceDate != null) {
                maintenance.setServiceDate(new Timestamp(serviceDate.getTime()));
            }
            
            // Parse service cost
            if (serviceCostStr != null && !serviceCostStr.isEmpty()) {
                maintenance.setServiceCost(new BigDecimal(serviceCostStr));
            }
            
            // Set next service date (already parsed)
            if (nextServiceDate != null) {
                maintenance.setNextServiceDate(nextServiceDate);
            }
            
            maintenance.setStatus(status);
            maintenance.setPerformedBy(user.getUserId());
            maintenance.setNotes(notes);
            
            boolean result = maintenanceDAO.updateMaintenance(maintenance);
            
            if (result) {
                HttpSession session = request.getSession();
                session.setAttribute("success", "Đã cập nhật lịch bảo trì thành công");
                response.sendRedirect("maintenance?carId=" + carId);
            } else {
                request.setAttribute("error", "Không thể cập nhật lịch bảo trì");
                showEditForm(request, response, user);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            showEditForm(request, response, user);
        }
    }

    /**
     * Delete maintenance record
     */
    private void deleteMaintenance(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int maintenanceId = Integer.parseInt(request.getParameter("id"));
        MaintenanceRecord maintenance = maintenanceDAO.getMaintenanceById(maintenanceId);
        
        if (maintenance == null) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Không tìm thấy lịch bảo trì");
            response.sendRedirect("maintenance");
            return;
        }
        
        // Check permission
        Car car = carDAO.getCarById(maintenance.getCarId());
        if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Bạn không có quyền xóa");
            response.sendRedirect("maintenance");
            return;
        }
        
        boolean result = maintenanceDAO.deleteMaintenance(maintenanceId);
        
        HttpSession session = request.getSession();
        if (result) {
            session.setAttribute("success", "Đã xóa lịch bảo trì thành công");
        } else {
            session.setAttribute("error", "Không thể xóa lịch bảo trì");
        }
        response.sendRedirect("maintenance");
    }

    /**
     * Mark maintenance as completed
     */
    private void completeMaintenance(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int maintenanceId = Integer.parseInt(request.getParameter("id"));
        MaintenanceRecord maintenance = maintenanceDAO.getMaintenanceById(maintenanceId);
        
        if (maintenance == null) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Không tìm thấy lịch bảo trì");
            response.sendRedirect("maintenance");
            return;
        }
        
        // Check permission
        Car car = carDAO.getCarById(maintenance.getCarId());
        if (user.getRoleId() != 1 && car.getOwnerId() != user.getUserId()) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Bạn không có quyền cập nhật");
            response.sendRedirect("maintenance");
            return;
        }
        
        boolean result = maintenanceDAO.updateMaintenanceStatus(maintenanceId, "Completed");
        
        HttpSession session = request.getSession();
        if (result) {
            session.setAttribute("success", "Đã hoàn thành bảo trì");
            response.sendRedirect("maintenance?carId=" + maintenance.getCarId());
        } else {
            session.setAttribute("error", "Không thể cập nhật trạng thái");
            response.sendRedirect("maintenance");
        }
    }
}
