package controller;

import dal.CarDAO;
import dal.CarBrandDAO;
import dal.CarModelDAO;
import dal.CarCategoryDAO;
import model.Car;
import model.CarBrand;
import model.CarModel;
import model.CarCategory;
import model.User;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * CarManagementServlet - Handles car CRUD operations
 */
@WebServlet(name = "CarManagementServlet", urlPatterns = {"/car-management"})
public class CarManagementServlet extends HttpServlet {

    private static final int ROLE_ADMIN = 1;
    private static final int ROLE_CAR_OWNER = 2;
    private static final int MIN_SEATS = 2;
    private static final int MAX_SEATS = 50;
    private static final Set<String> ALLOWED_STATUS = new HashSet<>(Arrays.asList(
        "Available", "Rented", "Maintenance", "Inactive"
    ));
    private static final Set<String> ALLOWED_TRANSMISSION = new HashSet<>(Arrays.asList(
        "Automatic", "Manual"
    ));

    private CarDAO carDAO = new CarDAO();
    private CarBrandDAO brandDAO = new CarBrandDAO();
    private CarModelDAO modelDAO = new CarModelDAO();
    private CarCategoryDAO categoryDAO = new CarCategoryDAO();

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
                listCars(request, response, user);
                break;
            case "add":
            case "create":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteCar(request, response, user);
                break;
            case "view":
                viewCar(request, response);
                break;
            case "verify":
                verifyCar(request, response, user);
                break;
            default:
                listCars(request, response, user);
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
            createCar(request, response, user);
        } else if ("update".equals(action)) {
            updateCar(request, response, user);
        } else if ("updateStatus".equals(action)) {
            updateCarStatus(request, response, user);
        }
    }

    private void listCars(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Car> cars;
        
        // Get filter parameters
        String search = request.getParameter("search");
        String categoryIdStr = request.getParameter("categoryId");
        String statusFilter = request.getParameter("status");
        
        System.out.println("=== Car Filter Debug ===");
        System.out.println("Search: " + search);
        System.out.println("Category ID: " + categoryIdStr);
        System.out.println("Status: " + statusFilter);
        System.out.println("User Role: " + user.getRoleId());
        
        // Admin sees all cars, CarOwner sees only their cars
        if (user.getRoleId() == ROLE_ADMIN) { // Admin
            cars = carDAO.getAllCars();
        } else if (user.getRoleId() == ROLE_CAR_OWNER) { // CarOwner
            cars = carDAO.getCarsByOwnerId(user.getUserId());
        } else {
            cars = carDAO.getAvailableCars();
        }
        
        System.out.println("Total cars before filter: " + (cars != null ? cars.size() : 0));
        
        // Apply filters
        if (cars != null && !cars.isEmpty()) {
            List<Car> filteredCars = new ArrayList<>();
            
            for (Car car : cars) {
                boolean matchesSearch = true;
                boolean matchesCategory = true;
                boolean matchesStatus = true;
                
                // Search filter (license plate or description)
                if (search != null && !search.trim().isEmpty()) {
                    String searchLower = search.toLowerCase();
                    matchesSearch = (car.getLicensePlate() != null && car.getLicensePlate().toLowerCase().contains(searchLower)) ||
                                  (car.getDescription() != null && car.getDescription().toLowerCase().contains(searchLower));
                }
                
                // Category filter
                if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                    try {
                        int categoryId = Integer.parseInt(categoryIdStr);
                        matchesCategory = (car.getCategoryId() == categoryId);
                        System.out.println("Car " + car.getCarId() + " categoryId: " + car.getCategoryId() + 
                                         ", filter: " + categoryId + ", matches: " + matchesCategory);
                    } catch (NumberFormatException e) {
                        // Invalid category ID, ignore filter
                    }
                }
                
                // Status filter
                if (statusFilter != null && !statusFilter.trim().isEmpty()) {
                    matchesStatus = statusFilter.equals(car.getStatus());
                    System.out.println("Car " + car.getCarId() + " status: " + car.getStatus() + 
                                     ", filter: " + statusFilter + ", matches: " + matchesStatus);
                }
                
                // Add car if it matches all filters
                if (matchesSearch && matchesCategory && matchesStatus) {
                    filteredCars.add(car);
                }
            }
            
            cars = filteredCars;
            System.out.println("Total cars after filter: " + cars.size());
        }
        System.out.println("=== End Car Filter Debug ===");
        
        // Load brands and categories for filters and form
        List<CarBrand> brands = brandDAO.getAllBrands();
        List<CarCategory> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("cars", cars);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("car-management.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<CarBrand> brands = brandDAO.getAllBrands();
        List<CarCategory> categories = categoryDAO.getAllCategories();
        
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("car-form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int carId = Integer.parseInt(request.getParameter("id"));
        Car car = carDAO.getCarById(carId);
        
        if (car == null) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Không tìm thấy xe");
            response.sendRedirect("car-management");
            return;
        }
        
        List<CarBrand> brands = brandDAO.getAllBrands();
        List<CarCategory> categories = categoryDAO.getAllCategories();
        
        List<CarModel> models = null;
        if (car.getModel() != null && car.getModel().getBrandId() > 0) {
            models = modelDAO.getModelsByBrandId(car.getModel().getBrandId());
        }
        
        request.setAttribute("car", car);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("models", models);
        request.getRequestDispatcher("car-form.jsp").forward(request, response);
    }

    private void viewCar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer carId = parsePositiveInt(request.getParameter("id"));
        if (carId == null) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "ID xe không hợp lệ");
            response.sendRedirect("car-management");
            return;
        }

        Car car = carDAO.getCarById(carId);
        
        if (car == null) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Không tìm thấy xe với ID: " + carId);
            response.sendRedirect("car-management");
            return;
        }
        
        request.setAttribute("car", car);
        request.getRequestDispatcher("car-detail.jsp").forward(request, response);
    }

  private void createCar(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            Car car = new Car();
            car.setOwnerId(user.getUserId());

            String validationError = validateAndPopulateCar(request, car);
            if (validationError != null) {
                request.setAttribute("error", validationError);
                forwardCarForm(request, response, car);
                return;
            }

            if (carDAO.createCar(car)) {
                HttpSession session = request.getSession();
                session.setAttribute("success", "Thêm xe thành công");
                response.sendRedirect("car-management");
            } else {
                request.setAttribute("error", buildSaveErrorMessage(car));
                forwardCarForm(request, response, car);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            forwardCarForm(request, response, null);
        }
    }
    private void updateCar(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            // System.out.println("=== UPDATE CAR DEBUG ===");
            // System.out.println("User ID: " + user.getUserId());
            // System.out.println("User Role ID: " + user.getRoleId());
            // System.out.println("User Name: " + user.getFullName());
            
            int carId = Integer.parseInt(request.getParameter("carId"));
            Car car = carDAO.getCarById(carId);
            
            if (car == null) {
                HttpSession session = request.getSession();
                session.setAttribute("error", "Không tìm thấy xe");
                response.sendRedirect("car-management");
                return;
            }
            
            // Check ownership
            if (user.getRoleId() != ROLE_ADMIN && car.getOwnerId() != user.getUserId()) {
                // System.out.println("OWNERSHIP CHECK FAILED!");
                // System.out.println("Car Owner ID: " + car.getOwnerId());
                // System.out.println("Current User ID: " + user.getUserId());
                // System.out.println("User Role ID: " + user.getRoleId());
                // System.out.println("=======================");

                request.setAttribute("error", "Bạn không có quyền chỉnh sửa xe này");
                forwardCarForm(request, response, car);
                return;
            }

            String validationError = validateAndPopulateCar(request, car);
            if (validationError != null) {
                request.setAttribute("error", validationError);
                forwardCarForm(request, response, car);
                return;
            }

            if (carDAO.updateCar(car)) {
                HttpSession session = request.getSession();
                session.setAttribute("success", "Cập nhật xe thành công");
                response.sendRedirect("car-management");
            } else {
                request.setAttribute("error", buildSaveErrorMessage(car));
                forwardCarForm(request, response, car);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            // Reload form data for editing
            Car car = carDAO.getCarById(Integer.parseInt(request.getParameter("carId")));
            forwardCarForm(request, response, car);
        }
    }

    private void updateCarStatus(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Integer carId = parsePositiveInt(request.getParameter("carId"));
        String status = normalize(request.getParameter("status"));

        if (carId == null || isBlank(status) || !ALLOWED_STATUS.contains(status)) {
            response.getWriter().write("{\"success\": false, \"message\": \"Dữ liệu không hợp lệ\"}");
            return;
        }

        Car car = carDAO.getCarById(carId);
        if (car == null) {
            response.getWriter().write("{\"success\": false, \"message\": \"Không tìm thấy xe\"}");
            return;
        }

        if (user.getRoleId() != ROLE_ADMIN && car.getOwnerId() != user.getUserId()) {
            response.getWriter().write("{\"success\": false, \"message\": \"Không có quyền cập nhật\"}");
            return;
        }

        if (carDAO.updateCarStatus(carId, status)) {
            response.getWriter().write("{\"success\": true}");
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"Cập nhật thất bại\"}");
        }
    }

    private void deleteCar(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int carId = Integer.parseInt(request.getParameter("id"));
        Car car = carDAO.getCarById(carId);
        
        if (car == null) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Không tìm thấy xe");
            response.sendRedirect("car-management");
            return;
        }
        
        // Check ownership
        if (user.getRoleId() != ROLE_ADMIN && car.getOwnerId() != user.getUserId()) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Bạn không có quyền xóa xe này");
            response.sendRedirect("car-management");
            return;
        }
        
        HttpSession session = request.getSession();
        if (carDAO.deleteCar(carId)) {
            session.setAttribute("success", "Xóa xe thành công");
        } else {
            session.setAttribute("error", "Lỗi khi xóa xe");
        }
        
        response.sendRedirect("car-management");
    }

    private void verifyCar(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can verify cars
        if (user.getRoleId() != ROLE_ADMIN) {
            HttpSession session = request.getSession();
            session.setAttribute("error", "Bạn không có quyền xác minh xe");
            response.sendRedirect("car-management");
            return;
        }
        
        int carId = Integer.parseInt(request.getParameter("id"));
        boolean isVerified = Boolean.parseBoolean(request.getParameter("verified"));
        
        HttpSession session = request.getSession();
        if (carDAO.verifyCar(carId, isVerified)) {
            session.setAttribute("success", "Cập nhật trạng thái xác minh thành công");
        } else {
            session.setAttribute("error", "Lỗi khi cập nhật trạng thái xác minh");
        }
        
        response.sendRedirect("car-management");
    }

    private String validateAndPopulateCar(HttpServletRequest request, Car car) {
        String licensePlate = normalize(request.getParameter("licensePlate"));
        if (isBlank(licensePlate)) {
            return "Biển số không được để trống";
        }
        if (licensePlate.length() > 20) {
            return "Biển số không được vượt quá 20 ký tự";
        }
        car.setLicensePlate(licensePlate.toUpperCase());

        if (car.getCarId() > 0) {
            if (carDAO.isLicensePlateExistsForOtherCar(car.getLicensePlate(), car.getCarId())) {
                return "Biển số đã tồn tại trong hệ thống";
            }
        } else if (carDAO.isLicensePlateExists(car.getLicensePlate())) {
            return "Biển số đã tồn tại trong hệ thống";
        }

        Integer categoryId = parsePositiveInt(request.getParameter("categoryId"));
        if (categoryId == null) {
            return "Vui lòng chọn danh mục hợp lệ";
        }
        if (categoryDAO.getCategoryById(categoryId) == null) {
            return "Danh mục xe không tồn tại";
        }
        car.setCategoryId(categoryId);

        Integer brandId = parsePositiveInt(request.getParameter("brandId"));
        if (brandId == null) {
            return "Vui lòng chọn hãng xe hợp lệ";
        }
        if (brandDAO.getBrandById(brandId) == null) {
            return "Hãng xe không tồn tại";
        }

        Integer modelId = parsePositiveInt(request.getParameter("modelId"));
        if (modelId == null) {
            return "Vui lòng chọn model hợp lệ";
        }

        CarModel selectedModel = modelDAO.getModelById(modelId);
        if (selectedModel == null) {
            return "Model không tồn tại";
        }
        if (selectedModel.getBrandId() != brandId) {
            return "Model không thuộc hãng xe đã chọn";
        }

        int currentYear = java.time.LocalDate.now().getYear();
        if (selectedModel.getYear() > currentYear) {
            return "Không thể lưu xe! Dòng xe " + selectedModel.getModelName()
                    + " có năm sản xuất (" + selectedModel.getYear() + ") lớn hơn năm hiện tại.";
        }
        car.setModelId(modelId);
        car.setModel(selectedModel);

        Integer seats = parsePositiveInt(request.getParameter("seats"));
        if (seats == null || seats < MIN_SEATS || seats > MAX_SEATS) {
            return "Số chỗ phải trong khoảng từ " + MIN_SEATS + " đến " + MAX_SEATS;
        }
        car.setSeats(seats);

        BigDecimal pricePerDay = parseDecimal(request.getParameter("pricePerDay"));
        if (pricePerDay == null || pricePerDay.compareTo(BigDecimal.ZERO) <= 0) {
            return "Giá thuê/ngày phải lớn hơn 0";
        }
        car.setPricePerDay(pricePerDay);

        String pricePerHourStr = normalize(request.getParameter("pricePerHour"));
        if (!isBlank(pricePerHourStr)) {
            BigDecimal pricePerHour = parseDecimal(pricePerHourStr);
            if (pricePerHour == null || pricePerHour.compareTo(BigDecimal.ZERO) < 0) {
                return "Giá thuê/giờ không hợp lệ";
            }
            car.setPricePerHour(pricePerHour);
        } else {
            car.setPricePerHour(null);
        }

        String mileageStr = normalize(request.getParameter("mileage"));
        if (!isBlank(mileageStr)) {
            BigDecimal mileage = parseDecimal(mileageStr);
            if (mileage == null || mileage.compareTo(BigDecimal.ZERO) < 0) {
                return "Số km đã đi không hợp lệ";
            }
            car.setMileage(mileage);
        } else {
            car.setMileage(null);
        }

        String transmission = normalize(request.getParameter("transmission"));
        if (!isBlank(transmission) && !ALLOWED_TRANSMISSION.contains(transmission)) {
            return "Hộp số không hợp lệ";
        }
        car.setTransmission(transmission);

        String status = normalize(request.getParameter("status"));
        if (isBlank(status)) {
            status = "Available";
        }
        if (!ALLOWED_STATUS.contains(status)) {
            return "Trạng thái không hợp lệ";
        }
        car.setStatus(status);

        String insuranceDate = normalize(request.getParameter("insuranceExpiryDate"));
        Date insuranceExpiryDate = parseDate(insuranceDate);
        if (!isBlank(insuranceDate) && insuranceExpiryDate == null) {
            return "Ngày hết hạn bảo hiểm không hợp lệ";
        }
        car.setInsuranceExpiryDate(insuranceExpiryDate);

        String registrationDate = normalize(request.getParameter("registrationExpiryDate"));
        Date registrationExpiryDate = parseDate(registrationDate);
        if (!isBlank(registrationDate) && registrationExpiryDate == null) {
            return "Ngày hết hạn đăng kiểm không hợp lệ";
        }
        car.setRegistrationExpiryDate(registrationExpiryDate);

        String vinNumber = normalize(request.getParameter("vinNumber"));
        if (!isBlank(vinNumber)) {
            if (car.getCarId() > 0) {
                if (carDAO.isVinNumberExistsForOtherCar(vinNumber, car.getCarId())) {
                    return "Số VIN đã tồn tại trong hệ thống";
                }
            } else if (carDAO.isVinNumberExists(vinNumber)) {
                return "Số VIN đã tồn tại trong hệ thống";
            }
        }

        car.setVinNumber(vinNumber);
        car.setColor(normalize(request.getParameter("color")));
        car.setFuelType(normalize(request.getParameter("fuelType")));
        car.setLocation(normalize(request.getParameter("location")));
        car.setDescription(normalize(request.getParameter("description")));
        car.setFeatures(normalize(request.getParameter("features")));

        return null;
    }

    private void forwardCarForm(HttpServletRequest request, HttpServletResponse response, Car car)
            throws ServletException, IOException {
        List<CarBrand> brands = brandDAO.getAllBrands();
        List<CarCategory> categories = categoryDAO.getAllCategories();

        List<CarModel> models = null;
        Integer brandId = parsePositiveInt(request.getParameter("brandId"));
        if (brandId != null) {
            models = modelDAO.getModelsByBrandId(brandId);
        } else if (car != null && car.getModel() != null && car.getModel().getBrandId() > 0) {
            models = modelDAO.getModelsByBrandId(car.getModel().getBrandId());
        }

        request.setAttribute("car", car);
        request.setAttribute("brands", brands);
        request.setAttribute("categories", categories);
        request.setAttribute("models", models);
        request.getRequestDispatcher("car-form.jsp").forward(request, response);
    }

    private Integer parsePositiveInt(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            int parsed = Integer.parseInt(value.trim());
            if (parsed <= 0) {
                return null;
            }
            return parsed;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal parseDecimal(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Date parseDate(String value) {
        if (isBlank(value)) {
            return null;
        }
        try {
            return Date.valueOf(value.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String buildSaveErrorMessage(Car car) {
        if (!isBlank(car.getVinNumber())) {
            if (car.getCarId() > 0) {
                if (carDAO.isVinNumberExistsForOtherCar(car.getVinNumber(), car.getCarId())) {
                    return "Số VIN đã tồn tại trong hệ thống";
                }
            } else if (carDAO.isVinNumberExists(car.getVinNumber())) {
                return "Số VIN đã tồn tại trong hệ thống";
            }
        }

        if (!isBlank(car.getLicensePlate())) {
            if (car.getCarId() > 0) {
                if (carDAO.isLicensePlateExistsForOtherCar(car.getLicensePlate(), car.getCarId())) {
                    return "Biển số đã tồn tại trong hệ thống";
                }
            } else if (carDAO.isLicensePlateExists(car.getLicensePlate())) {
                return "Biển số đã tồn tại trong hệ thống";
            }
        }

        return car.getCarId() > 0
                ? "Lỗi khi cập nhật xe vào cơ sở dữ liệu"
                : "Lỗi khi thêm xe vào cơ sở dữ liệu";
    }
}
