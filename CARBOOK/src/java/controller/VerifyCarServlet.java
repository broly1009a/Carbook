package controller;

import dal.CarDAO;
import dal.CarCategoryDAO;
import dal.NotificationDAO;
import model.Car;
import model.CarCategory;
import model.Notification;
import model.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "VerifyCarServlet", urlPatterns = {"/verify"})
public class VerifyCarServlet extends HttpServlet {

    private final CarDAO carDAO = new CarDAO();
    private final CarCategoryDAO categoryDAO = new CarCategoryDAO();
    private final NotificationDAO notificationDAO = new NotificationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 1. Lấy tham số search và lọc từ URL
        String search = request.getParameter("search");
        String categoryIdStr = request.getParameter("categoryId");

        // 2. Lấy danh sách gốc (xe chưa duyệt IsVerified = 0)
        List<Car> allPending = carDAO.getPendingCars();
        List<Car> filteredPending = new ArrayList<>();

        // 3. Thực hiện lọc dữ liệu
        if (allPending != null) {
            for (Car car : allPending) {
                boolean matchesSearch = true;
                boolean matchesCategory = true;

                if (search != null && !search.trim().isEmpty()) {
                    String s = search.toLowerCase();
                    matchesSearch = (car.getLicensePlate() != null && car.getLicensePlate().toLowerCase().contains(s)) ||
                                    (car.getDescription() != null && car.getDescription().toLowerCase().contains(s));
                }

                if (categoryIdStr != null && !categoryIdStr.trim().isEmpty()) {
                    int catId = Integer.parseInt(categoryIdStr);
                    matchesCategory = (car.getCategoryId() == catId);
                }

                if (matchesSearch && matchesCategory) {
                    filteredPending.add(car);
                }
            }
        }

        // 4. Đẩy data ra JSP
        request.setAttribute("pendingCars", filteredPending);
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.getRequestDispatcher("verify-car.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String carIdStr = request.getParameter("carId");
        String action = request.getParameter("action");

        try {
            int carId = Integer.parseInt(carIdStr);
            Car car = carDAO.getCarById(carId);

            if (car != null) {
                Notification noti = new Notification();
                noti.setUserId(car.getOwnerId());
                noti.setRelatedEntityType("Car");
                noti.setRelatedEntityId(carId);

                if ("approve".equals(action)) {
                    carDAO.verifyCar(carId, true);
                    carDAO.updateCarStatus(carId, "Available");
                    
                    noti.setTitle("Xe đã được duyệt!");
                    noti.setMessage("Chúc mừng! Xe " + car.getLicensePlate() + " đã được duyệt thành công.");
                    noti.setType("Success");
                    session.setAttribute("success", "Đã duyệt xe " + car.getLicensePlate());
                } else if ("reject".equals(action)) {
                    carDAO.updateCarStatus(carId, "Rejected");
                    
                    noti.setTitle("Yêu cầu duyệt xe bị từ chối");
                    noti.setMessage("Rất tiếc, xe " + car.getLicensePlate() + " không đủ điều kiện duyệt.");
                    noti.setType("Danger");
                    session.setAttribute("success", "Đã từ chối xe.");
                }
                // Gửi thông báo cho chủ xe
                notificationDAO.createNotification(noti);
            }
        } catch (Exception e) {
            session.setAttribute("error", "Lỗi: " + e.getMessage());
        }
        response.sendRedirect("verify");
    }
}