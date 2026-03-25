package filter;

import model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter("/*") 
public class AuthorizationFilter implements Filter {

   //key/value
    private static final Map<String, String> config = new HashMap<>();

    static {
        
        config.put("/role-management", "MANAGE_ROLES");
        config.put("/users", "MANAGE_USERS");
        config.put("/booking", "CUSTOMER_ACCESS");
        config.put("/brand", "MANAGE_BRANDS");
        config.put("/car-category", "MANAGE_CATEGORIES");
        config.put("/car-detail", "CUSTOMER_ACCESS");
        config.put("/car-images", "MANAGE_CAR_IMAGES");
        config.put("/car-management", "MANAGE_CARS");
        config.put("/car-models", "MANAGE_MODELS");
        config.put("/dashboard", "CUSTOMER_ACCESS");
        config.put("/notifications", "CUSTOMER_ACCESS");
        config.put("/profile", "CUSTOMER_ACCESS");
        config.put("/assign-role", "MANAGE_ROLES");
        config.put("/verify", "VERIFY_CARS");
    }

@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String path = req.getServletPath();
        String action = req.getParameter("action");

        if ("/car-models".equals(path) && "getByBrand".equals(action)) {
            chain.doFilter(request, response);
            return;
        }

        if (config.containsKey(path)) {
            HttpSession session = req.getSession(false);
            User user = (session != null) ? (User) session.getAttribute("user") : null;

            List<String> permissions = (session != null) ? (List<String>) session.getAttribute("userPermissions") : null;


            if (user == null) {
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }


            if (user.getRoleId() == 1) {
                chain.doFilter(request, response);
                return;
            }


            String requiredPermission = config.get(path);
            if (permissions == null || !permissions.contains(requiredPermission)) {
               
                req.setAttribute("errorMsg", "Bạn không có quyền truy cập chức năng này!");
                req.getRequestDispatcher("/403.jsp").forward(req, res);
                return;
            }
        }

       
        chain.doFilter(request, response);
    }
}