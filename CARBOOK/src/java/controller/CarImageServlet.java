package controller;

import dal.CarImageDAO;
import dal.CarDAO;
import model.CarImage;
import model.Car;
import model.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

/**
 * CarImageServlet - Handle car image upload and management
 */
@WebServlet(name = "CarImageServlet", urlPatterns = {"/car-images"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class CarImageServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads/cars";
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Only Admin (1) and Car Owner (2) can manage images
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            session.setAttribute("error", "Bạn không có quyền truy cập trang này");
            response.sendRedirect("dashboard.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        String carIdStr = request.getParameter("carId");
        
        if (carIdStr == null || carIdStr.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy thông tin xe");
            response.sendRedirect("car-management.jsp");
            return;
        }
        
        int carId = Integer.parseInt(carIdStr);
        CarDAO carDAO = new CarDAO();
        Car car = carDAO.getCarById(carId);
        
        if (car == null) {
            session.setAttribute("error", "Không tìm thấy xe");
            response.sendRedirect("car-management.jsp");
            return;
        }
        
        // Car Owner can only manage their own cars
        if (user.getRoleId() == 2 && car.getOwnerId() != user.getUserId()) {
            session.setAttribute("error", "Bạn không có quyền quản lý ảnh của xe này");
            response.sendRedirect("car-management.jsp");
            return;
        }
        
        // Load images
        CarImageDAO imageDAO = new CarImageDAO();
        List<CarImage> images = imageDAO.getImagesByCarId(carId);
        
        request.setAttribute("car", car);
        request.setAttribute("images", images);
        request.getRequestDispatcher("car-images.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        // Only Admin (1) and Car Owner (2) can manage images
        if (user.getRoleId() != 1 && user.getRoleId() != 2) {
            session.setAttribute("error", "Bạn không có quyền truy cập trang này");
            response.sendRedirect("dashboard.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        String carIdStr = request.getParameter("carId");
        
        if (carIdStr == null || carIdStr.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy thông tin xe");
            response.sendRedirect("car-management.jsp");
            return;
        }
        
        int carId = Integer.parseInt(carIdStr);
        CarDAO carDAO = new CarDAO();
        Car car = carDAO.getCarById(carId);
        
        if (car == null) {
            session.setAttribute("error", "Không tìm thấy xe");
            response.sendRedirect("car-management.jsp");
            return;
        }
        
        // Car Owner can only manage their own cars
        if (user.getRoleId() == 2 && car.getOwnerId() != user.getUserId()) {
            session.setAttribute("error", "Bạn không có quyền quản lý ảnh của xe này");
            response.sendRedirect("car-management.jsp");
            return;
        }
        
        CarImageDAO imageDAO = new CarImageDAO();
        
        switch (action != null ? action : "") {
            case "upload":
                handleUpload(request, response, carId, imageDAO, session);
                break;
            case "delete":
                handleDelete(request, response, carId, imageDAO, session);
                break;
            case "setPrimary":
                handleSetPrimary(request, response, carId, imageDAO, session);
                break;
            default:
                session.setAttribute("error", "Hành động không hợp lệ");
                response.sendRedirect("car-images?carId=" + carId);
                break;
        }
    }
    
    /**
     * Handle image upload
     */
    private void handleUpload(HttpServletRequest request, HttpServletResponse response,
                              int carId, CarImageDAO imageDAO, HttpSession session)
            throws ServletException, IOException {
        
        try {
            Part filePart = request.getPart("imageFile");
            
            if (filePart == null || filePart.getSize() == 0) {
                session.setAttribute("error", "Vui lòng chọn file ảnh");
                response.sendRedirect("car-images?carId=" + carId);
                return;
            }
            
            // Get filename
            String fileName = getFileName(filePart);
            if (fileName == null || fileName.isEmpty()) {
                session.setAttribute("error", "Tên file không hợp lệ");
                response.sendRedirect("car-images?carId=" + carId);
                return;
            }
            
            // Validate file type
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                session.setAttribute("error", "Chỉ chấp nhận file ảnh (jpg, png, gif)");
                response.sendRedirect("car-images?carId=" + carId);
                return;
            }
            
            // Create upload directory if not exists
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Generate unique filename
            String fileExtension = fileName.substring(fileName.lastIndexOf("."));
            String uniqueFileName = "car_" + carId + "_" + System.currentTimeMillis() + fileExtension;
            String filePath = uploadPath + File.separator + uniqueFileName;
            
            // Save file
            filePart.write(filePath);
            
            // Save to database
            CarImage image = new CarImage();
            image.setCarId(carId);
            image.setImageURL(UPLOAD_DIR + "/" + uniqueFileName);
            
            // Get isPrimary parameter
            String isPrimaryStr = request.getParameter("isPrimary");
            boolean isPrimary = "1".equals(isPrimaryStr) || "true".equals(isPrimaryStr);
            
            // If this is the first image, make it primary automatically
            if (imageDAO.countImagesByCarId(carId) == 0) {
                isPrimary = true;
            }
            
            image.setPrimary(isPrimary);
            image.setDisplayOrder(imageDAO.getNextDisplayOrder(carId));
            
            int imageId = imageDAO.addImage(image);
            
            if (imageId > 0) {
                // If marked as primary, update other images
                if (isPrimary) {
                    imageDAO.setPrimaryImage(imageId, carId);
                }
                session.setAttribute("success", "Tải ảnh lên thành công");
            } else {
                session.setAttribute("error", "Không thể lưu thông tin ảnh");
            }
            
        } catch (Exception e) {
            session.setAttribute("error", "Lỗi khi tải ảnh lên: " + e.getMessage());
            e.printStackTrace();
        }
        
        response.sendRedirect("car-images?carId=" + carId);
    }
    
    /**
     * Handle image deletion
     */
    private void handleDelete(HttpServletRequest request, HttpServletResponse response,
                             int carId, CarImageDAO imageDAO, HttpSession session)
            throws ServletException, IOException {
        
        String imageIdStr = request.getParameter("imageId");
        
        if (imageIdStr == null || imageIdStr.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy ảnh");
            response.sendRedirect("car-images?carId=" + carId);
            return;
        }
        
        int imageId = Integer.parseInt(imageIdStr);
        CarImage image = imageDAO.getImageById(imageId);
        
        if (image == null || image.getCarId() != carId) {
            session.setAttribute("error", "Ảnh không tồn tại");
            response.sendRedirect("car-images?carId=" + carId);
            return;
        }
        
        // Delete physical file
        try {
            String filePath = getServletContext().getRealPath("") + File.separator + image.getImageURL();
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            System.out.println("Error deleting physical file: " + e.getMessage());
        }
        
        // Delete from database
        if (imageDAO.deleteImage(imageId)) {
            session.setAttribute("success", "Xóa ảnh thành công");
            
            // If deleted image was primary, set first remaining image as primary
            if (image.isPrimary()) {
                List<CarImage> remainingImages = imageDAO.getImagesByCarId(carId);
                if (!remainingImages.isEmpty()) {
                    imageDAO.setPrimaryImage(remainingImages.get(0).getImageId(), carId);
                }
            }
        } else {
            session.setAttribute("error", "Không thể xóa ảnh");
        }
        
        response.sendRedirect("car-images?carId=" + carId);
    }
    
    /**
     * Handle setting primary image
     */
    private void handleSetPrimary(HttpServletRequest request, HttpServletResponse response,
                                  int carId, CarImageDAO imageDAO, HttpSession session)
            throws ServletException, IOException {
        
        String imageIdStr = request.getParameter("imageId");
        
        if (imageIdStr == null || imageIdStr.isEmpty()) {
            session.setAttribute("error", "Không tìm thấy ảnh");
            response.sendRedirect("car-images?carId=" + carId);
            return;
        }
        
        int imageId = Integer.parseInt(imageIdStr);
        CarImage image = imageDAO.getImageById(imageId);
        
        if (image == null || image.getCarId() != carId) {
            session.setAttribute("error", "Ảnh không tồn tại");
            response.sendRedirect("car-images?carId=" + carId);
            return;
        }
        
        if (imageDAO.setPrimaryImage(imageId, carId)) {
            session.setAttribute("success", "Đã đặt ảnh chính thành công");
        } else {
            session.setAttribute("error", "Không thể đặt ảnh chính");
        }
        
        response.sendRedirect("car-images?carId=" + carId);
    }
    
    /**
     * Extract filename from Part header
     */
    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] tokens = contentDisposition.split(";");
        
        for (String token : tokens) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return null;
    }
}
