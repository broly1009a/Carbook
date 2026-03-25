package dal;

import model.Car;
import model.CarModel;
import model.CarCategory;
import model.CarBrand;
import model.User;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * CarDAO - Data Access Object for Car management
 */
public class CarDAO extends DBContext {

    private CarModelDAO modelDAO = new CarModelDAO();
    private CarCategoryDAO categoryDAO = new CarCategoryDAO();
    private CarBrandDAO brandDAO = new CarBrandDAO();

    /**
     * Get all cars
     *
     * @return List of all cars
     */
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Cars ORDER BY CreatedAt DESC";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting all cars: " + e.getMessage());
        }
        return cars;
    }

    /**
     * Get car by ID
     *
     * @param carId
     * @return Car object or null
     */
    public Car getCarById(int carId) {
        String sql = "SELECT * FROM Cars WHERE CarID = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return extractCarFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error getting car by ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Get cars by owner ID
     *
     * @param ownerId
     * @return List of cars owned by the user
     */
    public List<Car> getCarsByOwnerId(int ownerId) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Cars WHERE OwnerID = ? ORDER BY CreatedAt DESC";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, ownerId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting cars by owner ID: " + e.getMessage());
        }
        return cars;
    }

    /**
     * Get available cars
     *
     * @return List of available cars
     */
    public List<Car> getAvailableCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Cars WHERE Status = 'Available' AND IsVerified = 1 ORDER BY CreatedAt DESC";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting available cars: " + e.getMessage());
        }
        return cars;
    }

    /**
     * Get cars by category
     *
     * @param categoryId
     * @return List of cars in the category
     */
    public List<Car> getCarsByCategory(int categoryId) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Cars WHERE CategoryID = ? AND Status = 'Available' AND IsVerified = 1";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, categoryId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error getting cars by category: " + e.getMessage());
        }
        return cars;
    }

    /**
     * Search cars by criteria
     *
     * @param keyword Search keyword
     * @param categoryId Category filter (0 for all)
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of matching cars
     */
    public List<Car> searchCars(String keyword, int categoryId, double minPrice, double maxPrice) {
        List<Car> cars = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Cars WHERE Status = 'Available' AND IsVerified = 1");

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LicensePlate LIKE ? OR Description LIKE ?)");
        }
        if (categoryId > 0) {
            sql.append(" AND CategoryID = ?");
        }
        if (minPrice > 0) {
            sql.append(" AND PricePerDay >= ?");
        }
        if (maxPrice > 0) {
            sql.append(" AND PricePerDay <= ?");
        }
        sql.append(" ORDER BY CreatedAt DESC");

        try {
            PreparedStatement stm = connection.prepareStatement(sql.toString());
            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword + "%";
                stm.setString(paramIndex++, searchPattern);
                stm.setString(paramIndex++, searchPattern);
            }
            if (categoryId > 0) {
                stm.setInt(paramIndex++, categoryId);
            }
            if (minPrice > 0) {
                stm.setDouble(paramIndex++, minPrice);
            }
            if (maxPrice > 0) {
                stm.setDouble(paramIndex++, maxPrice);
            }

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error searching cars: " + e.getMessage());
        }
        return cars;
    }

    /**
     * Create a new car
     *
     * @param car
     * @return true if successful, false otherwise
     */
    public boolean createCar(Car car) {
        String sql = "INSERT INTO Cars (OwnerID, ModelID, CategoryID, LicensePlate, VINNumber, Color, Seats, "
                + "FuelType, Transmission, Mileage, PricePerDay, PricePerHour, Status, Location, Description, "
                + "Features, InsuranceExpiryDate, RegistrationExpiryDate) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, car.getOwnerId());
            stm.setInt(2, car.getModelId());
            stm.setInt(3, car.getCategoryId());
            stm.setString(4, car.getLicensePlate());
            stm.setString(5, car.getVinNumber());
            stm.setString(6, car.getColor());
            stm.setInt(7, car.getSeats());
            stm.setString(8, car.getFuelType());
            stm.setString(9, car.getTransmission());
            stm.setBigDecimal(10, car.getMileage());
            stm.setBigDecimal(11, car.getPricePerDay());
            stm.setBigDecimal(12, car.getPricePerHour());
            stm.setString(13, car.getStatus());
            stm.setString(14, car.getLocation());
            stm.setString(15, car.getDescription());
            stm.setString(16, car.getFeatures());
            stm.setDate(17, car.getInsuranceExpiryDate());
            stm.setDate(18, car.getRegistrationExpiryDate());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error creating car: " + e.getMessage());
        }
        return false;
    }

    /**
     * Update car
     *
     * @param car
     * @return true if successful, false otherwise
     */
    public boolean updateCar(Car car) {
        String sql = "UPDATE Cars SET ModelID = ?, CategoryID = ?, LicensePlate = ?, VINNumber = ?, Color = ?, "
                + "Seats = ?, FuelType = ?, Transmission = ?, Mileage = ?, PricePerDay = ?, PricePerHour = ?, "
                + "Status = ?, Location = ?, Description = ?, Features = ?, InsuranceExpiryDate = ?, "
                + "RegistrationExpiryDate = ?, UpdatedAt = GETDATE() WHERE CarID = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, car.getModelId());
            stm.setInt(2, car.getCategoryId());
            stm.setString(3, car.getLicensePlate());
            stm.setString(4, car.getVinNumber());
            stm.setString(5, car.getColor());
            stm.setInt(6, car.getSeats());
            stm.setString(7, car.getFuelType());
            stm.setString(8, car.getTransmission());
            stm.setBigDecimal(9, car.getMileage());
            stm.setBigDecimal(10, car.getPricePerDay());
            stm.setBigDecimal(11, car.getPricePerHour());
            stm.setString(12, car.getStatus());
            stm.setString(13, car.getLocation());
            stm.setString(14, car.getDescription());
            stm.setString(15, car.getFeatures());
            stm.setDate(16, car.getInsuranceExpiryDate());
            stm.setDate(17, car.getRegistrationExpiryDate());
            stm.setInt(18, car.getCarId());

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating car: " + e.getMessage());
        }
        return false;
    }

    public boolean isLicensePlateExists(String licensePlate) {
        String sql = "SELECT 1 FROM Cars WHERE LicensePlate = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, licensePlate);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking license plate: " + e.getMessage());
            return false;
        }
    }

    public boolean isLicensePlateExistsForOtherCar(String licensePlate, int carId) {
        String sql = "SELECT 1 FROM Cars WHERE LicensePlate = ? AND CarID <> ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, licensePlate);
            stm.setInt(2, carId);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking license plate for other car: " + e.getMessage());
            return false;
        }
    }

    public boolean isVinNumberExists(String vinNumber) {
        String sql = "SELECT 1 FROM Cars WHERE VINNumber = ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, vinNumber);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking VIN number: " + e.getMessage());
            return false;
        }
    }

    public boolean isVinNumberExistsForOtherCar(String vinNumber, int carId) {
        String sql = "SELECT 1 FROM Cars WHERE VINNumber = ? AND CarID <> ?";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, vinNumber);
            stm.setInt(2, carId);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error checking VIN number for other car: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update car status
     *
     * @param carId
     * @param status
     * @return true if successful, false otherwise
     */
    public boolean updateCarStatus(int carId, String status) {
        String sql = "UPDATE Cars SET Status = ?, UpdatedAt = GETDATE() WHERE CarID = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, status);
            stm.setInt(2, carId);

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating car status: " + e.getMessage());
        }
        return false;
    }

    /**
     * Verify car
     *
     * @param carId
     * @param isVerified
     * @return true if successful, false otherwise
     */
    public boolean verifyCar(int carId, boolean isVerified) {
        String sql = "UPDATE Cars SET IsVerified = ?, UpdatedAt = GETDATE() WHERE CarID = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setBoolean(1, isVerified);
            stm.setInt(2, carId);

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error verifying car: " + e.getMessage());
        }
        return false;
    }

    /**
     * Delete car
     *
     * @param carId
     * @return true if successful, false otherwise
     */
    public boolean deleteCar(int carId) {
        String sql = "DELETE FROM Cars WHERE CarID = ?";

        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setInt(1, carId);

            return stm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting car: " + e.getMessage());
        }
        return false;
    }

    /**
     * Extract Car object from ResultSet
     *
     * @param rs
     * @return Car object
     * @throws SQLException
     */
    private Car extractCarFromResultSet(ResultSet rs) throws SQLException {
        Car car = new Car();
        car.setCarId(rs.getInt("CarID"));
        car.setOwnerId(rs.getInt("OwnerID"));
        car.setModelId(rs.getInt("ModelID"));
        car.setCategoryId(rs.getInt("CategoryID"));
        car.setLicensePlate(rs.getString("LicensePlate"));
        car.setVinNumber(rs.getString("VINNumber"));
        car.setColor(rs.getString("Color"));
        car.setSeats(rs.getInt("Seats"));
        car.setFuelType(rs.getString("FuelType"));
        car.setTransmission(rs.getString("Transmission"));
        car.setMileage(rs.getBigDecimal("Mileage"));
        car.setPricePerDay(rs.getBigDecimal("PricePerDay"));
        car.setPricePerHour(rs.getBigDecimal("PricePerHour"));
        car.setStatus(rs.getString("Status"));
        car.setLocation(rs.getString("Location"));
        car.setDescription(rs.getString("Description"));
        car.setFeatures(rs.getString("Features"));
        car.setInsuranceExpiryDate(rs.getDate("InsuranceExpiryDate"));
        car.setRegistrationExpiryDate(rs.getDate("RegistrationExpiryDate"));
        car.setVerified(rs.getBoolean("IsVerified"));
        car.setCreatedAt(rs.getTimestamp("CreatedAt"));
        car.setUpdatedAt(rs.getTimestamp("UpdatedAt"));

        // Load related objects
        loadCarRelatedData(car);

        return car;
    }

    /**
     * Load related data for car (model, brand, category, image)
     *
     * @param car
     */
    private void loadCarRelatedData(Car car) {
        try {
            // Load model and brand
            if (car.getModelId() > 0) {
                CarModel model = modelDAO.getModelById(car.getModelId());
                if (model != null) {
                    if (model.getBrandId() > 0) {
                        CarBrand brand = brandDAO.getBrandById(model.getBrandId());
                        if (brand != null) {
                            model.setBrand(brand);
                        }
                    }
                    car.setModel(model);
                }
            }

            // Load category
            if (car.getCategoryId() > 0) {
                CarCategory category = categoryDAO.getCategoryById(car.getCategoryId());
                if (category != null) {
                    car.setCategory(category);
                }
            }

            // Load owner
            if (car.getOwnerId() > 0) {
                UserDAO userDAO = new UserDAO();
                User owner = userDAO.getUserById(car.getOwnerId());
                if (owner != null) {
                    car.setOwner(owner);
                }
            }

            // Load first image
            String imageSql = "SELECT TOP 1 ImageURL FROM CarImages WHERE CarID = ? ORDER BY DisplayOrder";
            PreparedStatement stm = connection.prepareStatement(imageSql);
            stm.setInt(1, car.getCarId());
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                car.setImageUrl(rs.getString("ImageURL"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading car related data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get available cars during specified date range and location
     *
     * @param pickupDate Pickup date string (yyyy-MM-dd or dd/MM/yyyy)
     * @param dropoffDate Dropoff date string (yyyy-MM-dd or dd/MM/yyyy)
     * @param pickupLocation Pickup location (optional)
     * @param dropoffLocation Dropoff location (optional)
     * @return List of available cars
     */
    public List<Car> getAvailableCarsForBooking(String pickupDate, String dropoffDate,
            String pickupLocation, String dropoffLocation) {
        List<Car> availableCars = new ArrayList<>();

        // Get all available cars first
        List<Car> allCars = getAvailableCars();

        // If no date filter, return all available cars
        if (pickupDate == null || pickupDate.isEmpty() || dropoffDate == null || dropoffDate.isEmpty()) {
            return filterByLocation(allCars, pickupLocation, dropoffLocation);
        }

        try {
            // Convert date strings to SQL dates
            java.sql.Date sqlPickupDate = parseDateString(pickupDate);
            java.sql.Date sqlDropoffDate = parseDateString(dropoffDate);

            // Filter cars by availability
            for (Car car : allCars) {
                if (isCarAvailableForPeriod(car.getCarId(), sqlPickupDate, sqlDropoffDate)) {
                    availableCars.add(car);
                }
            }

            // Filter by location if provided
            return filterByLocation(availableCars, pickupLocation, dropoffLocation);

        } catch (Exception e) {
            System.out.println("Error getting available cars for booking: " + e.getMessage());
            return allCars; // Return all cars if error
        }
    }

    /**
     * Check if a car is available during the specified date range
     *
     * @param carId Car ID
     * @param pickupDate Pickup date
     * @param dropoffDate Dropoff date
     * @return true if available, false otherwise
     */
    public boolean isCarAvailableForPeriod(int carId, java.sql.Date pickupDate, java.sql.Date dropoffDate) {
        // Check for existing bookings
        String sqlBookings = "SELECT COUNT(*) FROM Bookings "
                + "WHERE CarID = ? "
                + "AND Status NOT IN ('Cancelled', 'Rejected') "
                + "AND ((PickupDate <= ? AND ReturnDate >= ?) "
                + "OR (PickupDate <= ? AND ReturnDate >= ?) "
                + "OR (PickupDate >= ? AND ReturnDate <= ?))";

        try {
            PreparedStatement stm = connection.prepareStatement(sqlBookings);
            stm.setInt(1, carId);
            stm.setDate(2, dropoffDate);
            stm.setDate(3, dropoffDate);
            stm.setDate(4, pickupDate);
            stm.setDate(5, pickupDate);
            stm.setDate(6, pickupDate);
            stm.setDate(7, dropoffDate);

            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int conflictCount = rs.getInt(1);
                if (conflictCount > 0) {
                    return false; // Has booking conflicts
                }
            }

            // Check for blocked periods in CarAvailability
            String sqlAvailability = "SELECT COUNT(*) FROM CarAvailability "
                    + "WHERE CarID = ? AND IsAvailable = 0 "
                    + "AND ((StartDate <= ? AND EndDate >= ?) "
                    + "OR (StartDate <= ? AND EndDate >= ?) "
                    + "OR (StartDate >= ? AND EndDate <= ?))";

            stm = connection.prepareStatement(sqlAvailability);
            stm.setInt(1, carId);
            stm.setDate(2, dropoffDate);
            stm.setDate(3, dropoffDate);
            stm.setDate(4, pickupDate);
            stm.setDate(5, pickupDate);
            stm.setDate(6, pickupDate);
            stm.setDate(7, dropoffDate);

            rs = stm.executeQuery();
            if (rs.next()) {
                int blockedCount = rs.getInt(1);
                if (blockedCount > 0) {
                    return false; // Has blocked periods
                }
            }

            // Check for active maintenance (In Progress or Scheduled)
            String sqlMaintenance = "SELECT COUNT(*) FROM MaintenanceRecords "
                    + "WHERE CarID = ? "
                    + "AND Status IN ('Scheduled', 'In Progress') "
                    + "AND ((CAST(ServiceDate AS DATE) <= ? AND CAST(ISNULL(NextServiceDate, ServiceDate) AS DATE) >= ?) "
                    + "OR (CAST(ServiceDate AS DATE) <= ? AND CAST(ISNULL(NextServiceDate, ServiceDate) AS DATE) >= ?) "
                    + "OR (CAST(ServiceDate AS DATE) >= ? AND CAST(ServiceDate AS DATE) <= ?))";

            stm = connection.prepareStatement(sqlMaintenance);
            stm.setInt(1, carId);
            stm.setDate(2, dropoffDate);
            stm.setDate(3, dropoffDate);
            stm.setDate(4, pickupDate);
            stm.setDate(5, pickupDate);
            stm.setDate(6, pickupDate);
            stm.setDate(7, dropoffDate);

            rs = stm.executeQuery();
            if (rs.next()) {
                int maintenanceCount = rs.getInt(1);
                if (maintenanceCount > 0) {
                    return false; // Has maintenance scheduled
                }
            }

            return true; // Available if no conflicts and not blocked

        } catch (SQLException e) {
            System.out.println("Error checking car availability: " + e.getMessage());
        }

        return true; // Default to available if error
    }

    /**
     * Filter cars by location
     *
     * @param cars List of cars to filter
     * @param pickupLocation Pickup location (optional)
     * @param dropoffLocation Dropoff location (optional)
     * @return Filtered list of cars
     */
    private List<Car> filterByLocation(List<Car> cars, String pickupLocation, String dropoffLocation) {
        if ((pickupLocation == null || pickupLocation.trim().isEmpty())
                && (dropoffLocation == null || dropoffLocation.trim().isEmpty())) {
            return cars;
        }

        List<Car> filteredCars = new ArrayList<>();
        for (Car car : cars) {
            String carLocation = car.getLocation();
            if (carLocation != null) {
                boolean matchPickup = (pickupLocation == null || pickupLocation.trim().isEmpty()
                        || carLocation.toLowerCase().contains(pickupLocation.toLowerCase()));
                boolean matchDropoff = (dropoffLocation == null || dropoffLocation.trim().isEmpty()
                        || carLocation.toLowerCase().contains(dropoffLocation.toLowerCase()));

                if (matchPickup && matchDropoff) {
                    filteredCars.add(car);
                }
            }
        }

        return filteredCars;
    }

    /**
     * Parse date string from various formats
     *
     * @param dateStr Date string (yyyy-MM-dd or dd/MM/yyyy)
     * @return SQL Date
     */
    private java.sql.Date parseDateString(String dateStr) {
        try {
            // Try yyyy-MM-dd format first
            if (dateStr.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return java.sql.Date.valueOf(dateStr);
            }

            // Try dd/MM/yyyy format
            if (dateStr.matches("\\d{2}/\\d{2}/\\d{4}")) {
                String[] parts = dateStr.split("/");
                String isoDate = parts[2] + "-" + parts[1] + "-" + parts[0];
                return java.sql.Date.valueOf(isoDate);
            }

            // Default: try direct conversion
            return java.sql.Date.valueOf(dateStr);
        } catch (Exception e) {
            System.out.println("Error parsing date: " + dateStr + " - " + e.getMessage());
            return null;
        }
    }
    // admin duyet xe

    public List<Car> getPendingCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Cars WHERE IsVerified = 0 ORDER BY CreatedAt DESC";
        try {
            PreparedStatement stm = connection.prepareStatement(sql);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {

                cars.add(extractCarFromResultSet(rs)); //map data
            }
        } catch (SQLException e) {
            System.out.println("Lỗi getPendingCars: " + e.getMessage());
        }
        return cars;
    }
}
