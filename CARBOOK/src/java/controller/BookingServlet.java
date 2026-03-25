package controller;

import dal.BookingDAO;
import dal.CarAvailabilityDAO;
import dal.CarDAO;
import dal.PaymentDAO;
import dal.NotificationDAO;
import dal.ReviewDAO;
import model.Booking;
import model.Car;
import model.User;
import model.Notification;
import model.Review;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * BookingServlet - Handles booking operations
 */
@WebServlet(name = "BookingServlet", urlPatterns = {"/booking"})
public class BookingServlet extends HttpServlet {

    private BookingDAO bookingDAO = new BookingDAO();
    private CarAvailabilityDAO availabilityDAO = new CarAvailabilityDAO();
    private CarDAO carDAO = new CarDAO();
    private PaymentDAO paymentDAO = new PaymentDAO();
    private NotificationDAO notificationDAO = new NotificationDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();

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
                listBookings(request, response, user);
                break;
            case "create":
                showBookingForm(request, response);
                break;
            case "view":
                viewBooking(request, response);
                break;
            case "cancel":
                cancelBooking(request, response, user);
                break;
            case "approve":
                approveBooking(request, response, user);
                break;
            case "reject":
                rejectBooking(request, response, user);
                break;
            case "complete":
                completeBooking(request, response, user);
                break;
            default:
                listBookings(request, response, user);
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
            createBooking(request, response, user);
        }
    }

    private void listBookings(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        List<Booking> bookings;
        
        // Get filter parameter
        String statusFilter = request.getParameter("status");
        
        System.out.println("=== Booking Filter Debug ===");
        System.out.println("Status filter: " + statusFilter);
        System.out.println("User Role: " + user.getRoleId());
        
        // Load bookings based on role
        if (user.getRoleId() == 1) { // Admin
            bookings = bookingDAO.getAllBookings();
        } else if (user.getRoleId() == 2) { // CarOwner
            // Get bookings for cars owned by this user
            List<Car> ownerCars = carDAO.getCarsByOwnerId(user.getUserId());
            bookings = new ArrayList<>();
            for (Car car : ownerCars) {
                List<Booking> carBookings = bookingDAO.getBookingsByCarId(car.getCarId());
                if (carBookings != null) {
                    bookings.addAll(carBookings);
                }
            }
        } else { // Customer
            bookings = bookingDAO.getBookingsByCustomerId(user.getUserId());
        }
        
        System.out.println("Total bookings before filter: " + (bookings != null ? bookings.size() : 0));
        
        // Apply status filter if provided
        if (statusFilter != null && !statusFilter.trim().isEmpty() && bookings != null && !bookings.isEmpty()) {
            List<Booking> filteredBookings = new ArrayList<>();
            
            for (Booking booking : bookings) {
                if (statusFilter.equals(booking.getStatus())) {
                    filteredBookings.add(booking);
                    System.out.println("Booking " + booking.getBookingId() + 
                                     " status: " + booking.getStatus() + " - MATCHES");
                } else {
                    System.out.println("Booking " + booking.getBookingId() + 
                                     " status: " + booking.getStatus() + " - NO MATCH");
                }
            }
            
            bookings = filteredBookings;
            System.out.println("Total bookings after filter: " + bookings.size());
        }
        System.out.println("=== End Booking Filter Debug ===");
        
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("bookings.jsp").forward(request, response);
    }

    private void showBookingForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String carIdStr = request.getParameter("carId");
        
        System.out.println("showBookingForm called with carId: " + carIdStr);
        
        if (carIdStr == null || carIdStr.isEmpty()) {
            response.sendRedirect("cars");
            return;
        }
        
        try {
            int carId = Integer.parseInt(carIdStr);
            System.out.println("Fetching car with ID: " + carId);
            
            Car car = carDAO.getCarById(carId);
            System.out.println("Car fetched: " + (car != null ? car.getCarId() : "null"));
            
            if (car == null) {
                request.getSession().setAttribute("error", "Không tìm thấy xe");
                response.sendRedirect("cars");
                return;
            }
            
            // Log detailed car status for debugging
            System.out.println("Car Status: [" + car.getStatus() + "]");
            System.out.println("Car Status Length: " + (car.getStatus() != null ? car.getStatus().length() : "null"));
            System.out.println("Car Status equals 'Available': " + "Available".equals(car.getStatus()));
            System.out.println("Car Status trimmed equals 'Available': " + (car.getStatus() != null && car.getStatus().trim().equals("Available")));
            
            if (!"Available".equals(car.getStatus())) {
                System.out.println("Car is NOT available - Status: [" + car.getStatus() + "]");
                request.getSession().setAttribute("error", "Xe không khả dụng (Status: " + car.getStatus() + ")");
                response.sendRedirect("cars");
                return;
            }
            
            System.out.println("Car is available - proceeding with booking form");
            
            // Forward search parameters to booking form
            String pickupLocation = request.getParameter("pickupLocation");
            String dropoffLocation = request.getParameter("dropoffLocation");
            String pickupDate = request.getParameter("pickupDate");
            String dropoffDate = request.getParameter("dropoffDate");
            String pickupTime = request.getParameter("pickupTime");
            
            if (pickupLocation != null) request.setAttribute("pickupLocation", pickupLocation);
            if (dropoffLocation != null) request.setAttribute("dropoffLocation", dropoffLocation);
            if (pickupDate != null) request.setAttribute("pickupDate", pickupDate);
            if (dropoffDate != null) request.setAttribute("dropoffDate", dropoffDate);
            if (pickupTime != null) request.setAttribute("pickupTime", pickupTime);
            
            System.out.println("Forwarding to booking-form.jsp");
            request.setAttribute("car", car);
            request.getRequestDispatcher("booking-form.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println("Error in showBookingForm: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("cars");
        }
    }

    private void createBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            System.out.println("=== CREATE BOOKING START ===");
            
            int carId = Integer.parseInt(request.getParameter("carId"));
            System.out.println("Car ID: " + carId);
            
            Car car = carDAO.getCarById(carId);
            
            if (car == null) {
                System.out.println("Car not found!");
                request.getSession().setAttribute("error", "Không tìm thấy xe");
                response.sendRedirect("cars");
                return;
            }

            if (!"Available".equals(car.getStatus())) {
                request.getSession().setAttribute("error", "Xe hiện không khả dụng để đặt");
                response.sendRedirect("cars");
                return;
            }
            
            String pickupDateStr = request.getParameter("pickupDate");
            String returnDateStr = request.getParameter("returnDate");
            
            System.out.println("Pickup Date String: " + pickupDateStr);
            System.out.println("Return Date String: " + returnDateStr);
            
            if (pickupDateStr == null || pickupDateStr.isEmpty() || 
                returnDateStr == null || returnDateStr.isEmpty()) {
                System.out.println("Date strings are empty!");
                request.getSession().setAttribute("error", "Vui lòng chọn ngày giờ nhận và trả xe");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
                return;
            }
            
            // Parse format: yyyy-MM-dd HH:mm:ss (already formatted from form)
            Timestamp pickupDate = Timestamp.valueOf(pickupDateStr);
            Timestamp returnDate = Timestamp.valueOf(returnDateStr);
            java.sql.Date pickupSqlDate = new java.sql.Date(pickupDate.getTime());
            java.sql.Date returnSqlDate = new java.sql.Date(returnDate.getTime());
            
            System.out.println("Pickup Date: " + pickupDate);
            System.out.println("Return Date: " + returnDate);
            
            // Validate dates
            LocalDateTime pickup = pickupDate.toLocalDateTime();
            LocalDateTime returnTime = returnDate.toLocalDateTime();
            LocalDateTime now = LocalDateTime.now();

            if (pickup.isBefore(now)) {
                request.getSession().setAttribute("error", "Thời gian nhận xe không được trước thời điểm hiện tại");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
                return;
            }
            
            if (returnTime.isBefore(pickup)) {
                request.getSession().setAttribute("error", "Ngày trả xe phải sau ngày nhận xe");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
                return;
            }

            if (!availabilityDAO.isCarAvailableForDateRange(carId, pickupSqlDate, returnSqlDate)) {
                request.getSession().setAttribute("error", "Xe không khả dụng trong khoảng thời gian bạn chọn");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
                return;
            }

            if (bookingDAO.hasActiveBookingInPeriod(carId, pickupSqlDate, returnSqlDate)) {
                request.getSession().setAttribute("error", "Xe đã có booking trong khoảng thời gian này. Vui lòng chọn thời gian khác.");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
                return;
            }

            if (bookingDAO.hasActiveMaintenanceInPeriod(carId, pickupSqlDate, returnSqlDate)) {
                request.getSession().setAttribute("error", "Xe đang có lịch bảo trì trong khoảng thời gian này. Vui lòng chọn thời gian khác.");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
                return;
            }
            
            // Calculate total days and hours
            long totalHours = ChronoUnit.HOURS.between(pickup, returnTime);
            int totalDays = (int) ChronoUnit.DAYS.between(pickup, returnTime);
            
            if (totalDays == 0) {
                totalDays = 1; // Minimum 1 day
            }
            
            System.out.println("Total Days: " + totalDays + ", Total Hours: " + totalHours);
            
            // Calculate pricing
            BigDecimal basePrice = car.getPricePerDay().multiply(new BigDecimal(totalDays));
            BigDecimal taxRate = new BigDecimal("0.10"); // 10% tax
            BigDecimal taxAmount = basePrice.multiply(taxRate);
            BigDecimal totalAmount = basePrice.add(taxAmount);
            
            System.out.println("Base Price: " + basePrice + ", Total: " + totalAmount);
            
            // Create booking
            Booking booking = new Booking();
            booking.setCarId(carId);
            booking.setCustomerId(user.getUserId());
            booking.setBookingReference(bookingDAO.generateBookingReference());
            booking.setPickupDate(pickupDate);
            booking.setReturnDate(returnDate);
            booking.setPickupLocation(request.getParameter("pickupLocation"));
            booking.setReturnLocation(request.getParameter("returnLocation"));
            booking.setTotalDays(totalDays);
            booking.setTotalHours((int) totalHours);
            booking.setBasePrice(basePrice);
            booking.setTaxAmount(taxAmount);
            booking.setDiscountAmount(BigDecimal.ZERO);
            booking.setTotalAmount(totalAmount);
            booking.setStatus("Pending");
            booking.setNotes(request.getParameter("notes"));
            
            System.out.println("Creating booking...");
            int bookingId = bookingDAO.createBooking(booking);
            System.out.println("Booking ID: " + bookingId);
            
            if (bookingId > 0) {
                // Update car status
                System.out.println("Updating car status to 'Booked' for car ID: " + carId);
                boolean statusUpdated = carDAO.updateCarStatus(carId, "Booked");
                System.out.println("Car status update result: " + statusUpdated);
                
                // Tạo thông báo cho car owner (nếu có)
                if (car.getOwnerId() > 0) {
                    Notification ownerNotification = new Notification();
                    ownerNotification.setUserId(car.getOwnerId());
                    ownerNotification.setType("new_booking");
                    ownerNotification.setTitle("Có đơn đặt xe mới");
                    ownerNotification.setMessage("Xe " + car.getLicensePlate() + " có đơn đặt mới (Mã: " + booking.getBookingReference() + "). Vui lòng xem xét và duyệt.");
                    ownerNotification.setRelatedEntityType("Booking");
                    ownerNotification.setRelatedEntityId(bookingId);
                    notificationDAO.createNotification(ownerNotification);
                    
                    System.out.println("Notification sent to car owner " + car.getOwnerId() + " for new booking " + bookingId);
                }
                
                System.out.println("Booking created successfully!");
                request.getSession().setAttribute("success", "Đặt xe thành công! Mã đặt xe: " + booking.getBookingReference());
                response.sendRedirect("booking?action=view&id=" + bookingId);
            } else {
                System.out.println("Failed to create booking!");
                request.getSession().setAttribute("error", "Lỗi khi tạo đặt xe");
                request.setAttribute("car", car);
                request.getRequestDispatcher("booking-form.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("=== ERROR IN CREATE BOOKING ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("cars");
        }
    }

    private void viewBooking(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            request.setAttribute("error", "Không tìm thấy đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        Car car = carDAO.getCarById(booking.getCarId());
        
        // Check if this booking has a review
        Review existingReview = reviewDAO.getReviewByBookingId(bookingId);
        
        request.setAttribute("booking", booking);
        request.setAttribute("car", car);
        request.setAttribute("existingReview", existingReview);
        request.getRequestDispatcher("booking-detail.jsp").forward(request, response);
    }

    private void approveBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin or car owner can approve
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            request.setAttribute("error", "Bạn không có quyền duyệt đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        int bookingId = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (bookingDAO.approveBooking(bookingId, user.getUserId())) {
            // Tạo thông báo cho khách hàng
            Notification notification = new Notification();
            notification.setUserId(booking.getCustomerId());
            notification.setType("booking_approved");
            notification.setTitle("Đơn đặt xe đã được duyệt");
            notification.setMessage("Đơn đặt xe " + booking.getBookingReference() + " đã được duyệt. Vui lòng thanh toán để hoàn tất đặt xe.");
            notification.setRelatedEntityType("Booking");
            notification.setRelatedEntityId(bookingId);
            notificationDAO.createNotification(notification);
            
            System.out.println("Notification sent to customer " + booking.getCustomerId() + " for approved booking " + bookingId);
            request.setAttribute("success", "Đã duyệt đặt xe");
        } else {
            request.setAttribute("error", "Lỗi khi duyệt đặt xe");
        }
        
        response.sendRedirect("booking?action=view&id=" + bookingId);
    }

    private void rejectBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin or car owner can reject
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            request.setAttribute("error", "Bạn không có quyền từ chối đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        int bookingId = Integer.parseInt(request.getParameter("id"));
        String reason = request.getParameter("reason");
        
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Không đủ điều kiện";
        }
        
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (bookingDAO.rejectBooking(bookingId, reason)) {
            // Set car back to available
            System.out.println("Rejecting booking - Setting car " + booking.getCarId() + " back to Available");
            boolean statusUpdated = carDAO.updateCarStatus(booking.getCarId(), "Available");
            System.out.println("Car status update result: " + statusUpdated);
            
            // Tạo thông báo cho khách hàng
            Notification notification = new Notification();
            notification.setUserId(booking.getCustomerId());
            notification.setType("booking_rejected");
            notification.setTitle("Đơn đặt xe đã bị từ chối");
            notification.setMessage("Đơn đặt xe " + booking.getBookingReference() + " đã bị từ chối. Lý do: " + reason);
            notification.setRelatedEntityType("Booking");
            notification.setRelatedEntityId(bookingId);
            notificationDAO.createNotification(notification);
            
            System.out.println("Notification sent to customer " + booking.getCustomerId() + " for rejected booking " + bookingId);
            request.setAttribute("success", "Đã từ chối đặt xe");
        } else {
            request.setAttribute("error", "Lỗi khi từ chối đặt xe");
        }
        
        response.sendRedirect("booking?action=view&id=" + bookingId);
    }

    private void cancelBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        int bookingId = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            request.setAttribute("error", "Không tìm thấy đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        // Check if user can cancel
        if (booking.getCustomerId() != user.getUserId() && user.getRoleId() != 1) {
            request.setAttribute("error", "Bạn không có quyền hủy đặt xe này");
            response.sendRedirect("booking");
            return;
        }
        
        String reason = request.getParameter("reason");
        if (reason == null || reason.trim().isEmpty()) {
            reason = "Khách hàng hủy";
        }
        
        if (bookingDAO.cancelBooking(bookingId, user.getUserId(), reason)) {
            // Set car back to available
            System.out.println("Cancelling booking - Setting car " + booking.getCarId() + " back to Available");
            boolean statusUpdated = carDAO.updateCarStatus(booking.getCarId(), "Available");
            System.out.println("Car status update result: " + statusUpdated);
            
            // Tạo thông báo cho khách hàng (nếu admin hủy)
            if (user.getUserId() != booking.getCustomerId()) {
                Notification notification = new Notification();
                notification.setUserId(booking.getCustomerId());
                notification.setType("booking_cancelled");
                notification.setTitle("Đơn đặt xe đã bị hủy");
                notification.setMessage("Đơn đặt xe " + booking.getBookingReference() + " đã bị hủy. Lý do: " + reason);
                notification.setRelatedEntityType("Booking");
                notification.setRelatedEntityId(bookingId);
                notificationDAO.createNotification(notification);
                
                System.out.println("Notification sent to customer " + booking.getCustomerId() + " for cancelled booking " + bookingId);
            }
            request.setAttribute("success", "Đã hủy đặt xe");
        } else {
            request.setAttribute("error", "Lỗi khi hủy đặt xe");
        }
        
        response.sendRedirect("booking?action=view&id=" + bookingId);
    }

    private void completeBooking(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin or car owner can complete
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            request.setAttribute("error", "Bạn không có quyền hoàn thành đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        int bookingId = Integer.parseInt(request.getParameter("id"));
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (bookingDAO.completeBooking(bookingId)) {
            // Set car back to available
            System.out.println("Completing booking - Setting car " + booking.getCarId() + " back to Available");
            boolean statusUpdated = carDAO.updateCarStatus(booking.getCarId(), "Available");
            System.out.println("Car status update result: " + statusUpdated);
            
            // Tạo thông báo cho khách hàng
            Notification notification = new Notification();
            notification.setUserId(booking.getCustomerId());
            notification.setType("booking_completed");
            notification.setTitle("Đơn đặt xe đã hoàn thành");
            notification.setMessage("Đơn đặt xe " + booking.getBookingReference() + " đã hoàn thành. Cảm ơn bạn đã sử dụng dịch vụ! Bạn có thể đánh giá trải nghiệm của mình.");
            notification.setRelatedEntityType("Booking");
            notification.setRelatedEntityId(bookingId);
            notificationDAO.createNotification(notification);
            
            System.out.println("Notification sent to customer " + booking.getCustomerId() + " for completed booking " + bookingId);
            request.setAttribute("success", "Đã hoàn thành đặt xe");
        } else {
            request.setAttribute("error", "Lỗi khi hoàn thành đặt xe");
        }
        
        response.sendRedirect("booking?action=view&id=" + bookingId);
    }
}
