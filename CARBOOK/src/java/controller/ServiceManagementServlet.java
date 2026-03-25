package controller;

import dal.ServiceDAO;
import model.Service;
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
 * ServiceManagementServlet - Handles service management for admin
 */
@WebServlet(name = "ServiceManagementServlet", urlPatterns = {"/service-management"})
public class ServiceManagementServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listServices(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteService(request, response);
                break;
            case "toggle-status":
                toggleServiceStatus(request, response);
                break;
            default:
                listServices(request, response);
                break;
        }
    }

    /**
     * List all services
     */
    private void listServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Service> services = serviceDAO.getAllServices();
            
            request.setAttribute("services", services);
            request.getRequestDispatcher("service-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
            request.getRequestDispatcher("service-list.jsp").forward(request, response);
        }
    }

    /**
     * Show create service form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("service-form.jsp").forward(request, response);
    }

    /**
     * Show edit service form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String serviceIdStr = request.getParameter("id");
            
            if (serviceIdStr == null || serviceIdStr.isEmpty()) {
                request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
                response.sendRedirect("service-management");
                return;
            }
            
            int serviceId = Integer.parseInt(serviceIdStr);
            Service service = serviceDAO.getServiceById(serviceId);
            
            if (service == null) {
                request.getSession().setAttribute("error", "Không tìm thấy dịch vụ");
                response.sendRedirect("service-management");
                return;
            }
            
            request.setAttribute("service", service);
            request.getRequestDispatcher("service-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
            response.sendRedirect("service-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi tải thông tin dịch vụ: " + e.getMessage());
            response.sendRedirect("service-management");
        }
    }

    /**
     * Delete service
     */
    private void deleteService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String serviceIdStr = request.getParameter("id");
            
            if (serviceIdStr == null || serviceIdStr.isEmpty()) {
                request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
                response.sendRedirect("service-management");
                return;
            }
            
            int serviceId = Integer.parseInt(serviceIdStr);
            boolean deleted = serviceDAO.deleteService(serviceId);
            
            if (deleted) {
                request.getSession().setAttribute("success", "Xóa dịch vụ thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể xóa dịch vụ");
            }
            
            response.sendRedirect("service-management");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
            response.sendRedirect("service-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi xóa dịch vụ: " + e.getMessage());
            response.sendRedirect("service-management");
        }
    }

    /**
     * Toggle service active status
     */
    private void toggleServiceStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String serviceIdStr = request.getParameter("id");
            
            if (serviceIdStr == null || serviceIdStr.isEmpty()) {
                request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
                response.sendRedirect("service-management");
                return;
            }
            
            int serviceId = Integer.parseInt(serviceIdStr);
            boolean toggled = serviceDAO.toggleServiceStatus(serviceId);
            
            if (toggled) {
                request.getSession().setAttribute("success", "Cập nhật trạng thái dịch vụ thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể cập nhật trạng thái");
            }
            
            response.sendRedirect("service-management");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
            response.sendRedirect("service-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi cập nhật trạng thái: " + e.getMessage());
            response.sendRedirect("service-management");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Check if user is admin
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createService(request, response);
        } else if ("update".equals(action)) {
            updateService(request, response);
        } else {
            doGet(request, response);
        }
    }

    /**
     * Create new service
     */
    private void createService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get form data
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            String shortDescription = request.getParameter("shortDescription");
            String iconClass = request.getParameter("iconClass");
            String priceStr = request.getParameter("price");
            String imageURL = request.getParameter("imageURL");
            String isActiveStr = request.getParameter("isActive");
            String displayOrderStr = request.getParameter("displayOrder");
            
            // Validate required fields
            if (serviceName == null || serviceName.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Tên dịch vụ không được để trống");
                response.sendRedirect("service-management?action=create");
                return;
            }
            
            // Create service object
            Service service = new Service();
            service.setServiceName(serviceName.trim());
            service.setDescription(description != null ? description.trim() : "");
            service.setShortDescription(shortDescription != null ? shortDescription.trim() : "");
            service.setIconClass(iconClass != null ? iconClass.trim() : "flaticon-route");
            
            // Parse price
            try {
                double price = (priceStr != null && !priceStr.isEmpty()) ? Double.parseDouble(priceStr) : 0;
                service.setPrice(price);
            } catch (NumberFormatException e) {
                service.setPrice(0);
            }
            
            service.setImageURL(imageURL != null ? imageURL.trim() : "");
            service.setActive("true".equals(isActiveStr));
            
            // Parse display order
            try {
                int displayOrder = (displayOrderStr != null && !displayOrderStr.isEmpty()) ? Integer.parseInt(displayOrderStr) : 0;
                service.setDisplayOrder(displayOrder);
            } catch (NumberFormatException e) {
                service.setDisplayOrder(0);
            }
            
            // Save service
            Service createdService = serviceDAO.createService(service);
            
            if (createdService != null) {
                request.getSession().setAttribute("success", "Tạo dịch vụ mới thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể tạo dịch vụ");
            }
            
            response.sendRedirect("service-management");
            
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi tạo dịch vụ: " + e.getMessage());
            response.sendRedirect("service-management?action=create");
        }
    }

    /**
     * Update existing service
     */
    private void updateService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get service ID
            String serviceIdStr = request.getParameter("serviceId");
            
            if (serviceIdStr == null || serviceIdStr.isEmpty()) {
                request.getSession().setAttribute("error", "ID dịch vụ không hợp lệ");
                response.sendRedirect("service-management");
                return;
            }
            
            int serviceId = Integer.parseInt(serviceIdStr);
            
            // Get form data
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            String shortDescription = request.getParameter("shortDescription");
            String iconClass = request.getParameter("iconClass");
            String priceStr = request.getParameter("price");
            String imageURL = request.getParameter("imageURL");
            String isActiveStr = request.getParameter("isActive");
            String displayOrderStr = request.getParameter("displayOrder");
            
            // Validate required fields
            if (serviceName == null || serviceName.trim().isEmpty()) {
                request.getSession().setAttribute("error", "Tên dịch vụ không được để trống");
                response.sendRedirect("service-management?action=edit&id=" + serviceId);
                return;
            }
            
            // Create service object
            Service service = new Service();
            service.setServiceId(serviceId);
            service.setServiceName(serviceName.trim());
            service.setDescription(description != null ? description.trim() : "");
            service.setShortDescription(shortDescription != null ? shortDescription.trim() : "");
            service.setIconClass(iconClass != null ? iconClass.trim() : "flaticon-route");
            
            // Parse price
            try {
                double price = (priceStr != null && !priceStr.isEmpty()) ? Double.parseDouble(priceStr) : 0;
                service.setPrice(price);
            } catch (NumberFormatException e) {
                service.setPrice(0);
            }
            
            service.setImageURL(imageURL != null ? imageURL.trim() : "");
            service.setActive("true".equals(isActiveStr));
            
            // Parse display order
            try {
                int displayOrder = (displayOrderStr != null && !displayOrderStr.isEmpty()) ? Integer.parseInt(displayOrderStr) : 0;
                service.setDisplayOrder(displayOrder);
            } catch (NumberFormatException e) {
                service.setDisplayOrder(0);
            }
            
            // Update service
            boolean updated = serviceDAO.updateService(service);
            
            if (updated) {
                request.getSession().setAttribute("success", "Cập nhật dịch vụ thành công");
            } else {
                request.getSession().setAttribute("error", "Không thể cập nhật dịch vụ");
            }
            
            response.sendRedirect("service-management");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
            response.sendRedirect("service-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi cập nhật dịch vụ: " + e.getMessage());
            response.sendRedirect("service-management");
        }
    }
}
