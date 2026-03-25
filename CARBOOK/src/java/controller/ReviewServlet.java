package controller;

import dal.ReviewDAO;
import dal.CarDAO;
import dal.BookingDAO;
import dal.UserDAO;
import model.Review;
import model.Car;
import model.Booking;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ReviewServlet - Handles review operations
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/review"})
public class ReviewServlet extends HttpServlet {

    private ReviewDAO reviewDAO = new ReviewDAO();
    private CarDAO carDAO = new CarDAO();
    private BookingDAO bookingDAO = new BookingDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listReviews(request, response);
                break;
            case "create":
                showReviewForm(request, response, user);
                break;
            case "approve":
                approveReview(request, response, user);
                break;
            case "delete":
                deleteReview(request, response, user);
                break;
            case "pending":
                listPendingReviews(request, response, user);
                break;
            default:
                listReviews(request, response);
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
            createReview(request, response, user);
        } else if ("update".equals(action)) {
            updateReview(request, response, user);
        }
    }

    private void listReviews(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String carIdStr = request.getParameter("carId");
        
        List<Review> reviews;
        
        if (carIdStr != null && !carIdStr.isEmpty()) {
            int carId = Integer.parseInt(carIdStr);
            reviews = reviewDAO.getReviewsByCarId(carId);
            Car car = carDAO.getCarById(carId);
            request.setAttribute("car", car);
        } else {
            reviews = reviewDAO.getAllReviews();
        }
        
        // Load customer and booking for each review
        UserDAO userDAO = new UserDAO();
        for (Review review : reviews) {
            User customer = userDAO.getUserById(review.getCustomerId());
            review.setCustomer(customer);
            
            Booking booking = bookingDAO.getBookingById(review.getBookingId());
            if (booking != null) {
                Car car = carDAO.getCarById(booking.getCarId());
                booking.setCar(car);
                review.setBooking(booking);
            }
        }
        
        request.setAttribute("reviews", reviews);
        request.getRequestDispatcher("reviews.jsp").forward(request, response);
    }

    private void listPendingReviews(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can view pending reviews
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login");
            return;
        }
        
        List<Review> pendingReviews = reviewDAO.getPendingReviews();
        
        // Load customer and booking for each review
        UserDAO userDAO = new UserDAO();
        for (Review review : pendingReviews) {
            User customer = userDAO.getUserById(review.getCustomerId());
            review.setCustomer(customer);
            
            Booking booking = bookingDAO.getBookingById(review.getBookingId());
            if (booking != null) {
                Car car = carDAO.getCarById(booking.getCarId());
                booking.setCar(car);
                review.setBooking(booking);
            }
        }
        
        request.setAttribute("pendingReviews", pendingReviews);
        request.getRequestDispatcher("pending-reviews.jsp").forward(request, response);
    }

    private void showReviewForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        if (user == null) {
            response.sendRedirect("login");
            return;
        }
        
        String bookingIdStr = request.getParameter("bookingId");
        
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect("booking");
            return;
        }
        
        int bookingId = Integer.parseInt(bookingIdStr);
        Booking booking = bookingDAO.getBookingById(bookingId);
        
        if (booking == null) {
            request.setAttribute("error", "Không tìm thấy đặt xe");
            response.sendRedirect("booking");
            return;
        }
        
        // Check if booking is completed
        if (!"Completed".equals(booking.getStatus())) {
            request.setAttribute("error", "Chỉ có thể đánh giá sau khi hoàn thành chuyến đi");
            response.sendRedirect("booking?action=view&id=" + bookingId);
            return;
        }
        
        // Check if user owns this booking
        if (booking.getCustomerId() != user.getUserId()) {
            request.setAttribute("error", "Bạn không có quyền đánh giá đặt xe này");
            response.sendRedirect("booking");
            return;
        }
        
        Car car = carDAO.getCarById(booking.getCarId());
        
        request.setAttribute("booking", booking);
        request.setAttribute("car", car);
        request.getRequestDispatcher("review-form.jsp").forward(request, response);
    }

    private void createReview(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int bookingId = Integer.parseInt(request.getParameter("bookingId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            
            // Validate rating
            if (rating < 1 || rating > 5) {
                request.setAttribute("error", "Đánh giá phải từ 1 đến 5 sao");
                showReviewForm(request, response, user);
                return;
            }
            
            Booking booking = bookingDAO.getBookingById(bookingId);
            
            if (booking == null || booking.getCustomerId() != user.getUserId()) {
                request.setAttribute("error", "Không có quyền đánh giá");
                response.sendRedirect("booking");
                return;
            }
            
            // Get carId from booking
            int carId = booking.getCarId();
            
            Review review = new Review();
            review.setBookingId(bookingId);
            review.setCarId(carId);
            review.setCustomerId(user.getUserId());
            review.setRating(rating);
            review.setComment(comment);
            
            if (reviewDAO.createReview(review)) {
                request.getSession().setAttribute("success", "Đánh giá của bạn đã được gửi và đang chờ duyệt");
                response.sendRedirect("booking?action=view&id=" + bookingId);
            } else {
                request.setAttribute("error", "Lỗi khi tạo đánh giá");
                showReviewForm(request, response, user);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("booking");
        }
    }

    private void updateReview(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            
            Review review = reviewDAO.getReviewById(reviewId);
            
            if (review == null) {
                request.setAttribute("error", "Không tìm thấy đánh giá");
                response.sendRedirect("review");
                return;
            }
            
            // Check if user owns this review
            if (review.getCustomerId() != user.getUserId() && user.getRoleId() != 1) {
                request.setAttribute("error", "Bạn không có quyền chỉnh sửa đánh giá này");
                response.sendRedirect("review");
                return;
            }
            
            review.setRating(rating);
            review.setComment(comment);
            
            if (reviewDAO.updateReview(review)) {
                request.setAttribute("success", "Cập nhật đánh giá thành công");
            } else {
                request.setAttribute("error", "Lỗi khi cập nhật đánh giá");
            }
            
            response.sendRedirect("review");
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("review");
        }
    }

    private void approveReview(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can approve
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login");
            return;
        }
        
        int reviewId = Integer.parseInt(request.getParameter("id"));
        
        if (reviewDAO.approveReview(reviewId, user.getUserId())) {
            request.setAttribute("success", "Đã duyệt đánh giá");
        } else {
            request.setAttribute("error", "Lỗi khi duyệt đánh giá");
        }
        
        response.sendRedirect("review?action=pending");
    }

    private void deleteReview(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        // Only admin can delete
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login");
            return;
        }
        
        int reviewId = Integer.parseInt(request.getParameter("id"));
        
        if (reviewDAO.deleteReview(reviewId)) {
            request.setAttribute("success", "Đã xóa đánh giá");
        } else {
            request.setAttribute("error", "Lỗi khi xóa đánh giá");
        }
        
        response.sendRedirect("review?action=pending");
    }
}
