package controller;

import dal.ServiceDAO;
import model.Service;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * ServiceServlet - Handles service operations for public users
 */
@WebServlet(name = "ServiceServlet", urlPatterns = {"/services", "/service"})
public class ServiceServlet extends HttpServlet {

    private ServiceDAO serviceDAO = new ServiceDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listServices(request, response);
                break;
            case "view":
                viewService(request, response);
                break;
            case "search":
                searchServices(request, response);
                break;
            default:
                listServices(request, response);
                break;
        }
    }

    /**
     * List all active services
     */
    private void listServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Service> services = serviceDAO.getAllActiveServices();
            
            request.setAttribute("services", services);
            request.setAttribute("serviceCount", services.size());
            
            request.getRequestDispatcher("services.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách dịch vụ: " + e.getMessage());
            request.getRequestDispatcher("services.jsp").forward(request, response);
        }
    }

    /**
     * View a single service
     */
    private void viewService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String serviceIdStr = request.getParameter("id");
            
            if (serviceIdStr == null || serviceIdStr.isEmpty()) {
                response.sendRedirect("services");
                return;
            }
            
            int serviceId = Integer.parseInt(serviceIdStr);
            Service service = serviceDAO.getServiceById(serviceId);
            
            if (service == null || !service.isActive()) {
                request.setAttribute("error", "Không tìm thấy dịch vụ");
                response.sendRedirect("services");
                return;
            }
            
            // Get all services for sidebar
            List<Service> allServices = serviceDAO.getAllActiveServices();
            
            request.setAttribute("service", service);
            request.setAttribute("allServices", allServices);
            
            request.getRequestDispatcher("service-single.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID dịch vụ không hợp lệ");
            response.sendRedirect("services");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải thông tin dịch vụ: " + e.getMessage());
            response.sendRedirect("services");
        }
    }

    /**
     * Search services
     */
    private void searchServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            
            if (keyword == null || keyword.trim().isEmpty()) {
                response.sendRedirect("services");
                return;
            }
            
            List<Service> services = serviceDAO.searchServices(keyword);
            
            request.setAttribute("services", services);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("serviceCount", services.size());
            
            request.getRequestDispatcher("services.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tìm kiếm dịch vụ: " + e.getMessage());
            request.getRequestDispatcher("services.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
