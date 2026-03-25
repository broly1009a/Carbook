package controller;

import dal.RoleDAO;
import model.Role;
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
 * RoleManagementServlet - Handles role management (Admin only)
 */
@WebServlet(name = "RoleManagementServlet", urlPatterns = {"/role-management"})
public class RoleManagementServlet extends HttpServlet {

    private RoleDAO roleDAO = new RoleDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
     
        if (user == null || user.getRoleId() != 1) {
            response.sendRedirect("login");
            return;
        }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listRoles(request, response);
                break;
           
            case "delete":
                deleteRole(request, response);
                break;
                case "search":
        searchRoles(request, response);
        break;
            default:
                listRoles(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Only admin can access
//        if (user == null || user.getRoleId() != 1) {
//            response.sendRedirect("login");
//            return;
//        }
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createRole(request, response);
        } else if ("update".equals(action)) {
            updateRole(request, response);
        }
    }

    private void listRoles(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Role> roles = roleDAO.getAllRoles();
        
        request.setAttribute("roles", roles);
        request.getRequestDispatcher("role-management.jsp").forward(request, response);
    }

  

  

 private void createRole(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        String roleName = request.getParameter("roleName");
        String description = request.getParameter("description");

        if (roleDAO.checkRoleNameExists(roleName)) {

            request.getSession().setAttribute("error",
                    "Vai trò này đã có trong hệ thống, không thể thêm!");

        } else {

            Role role = new Role();
            role.setRoleName(roleName);
            role.setDescription(description);

            if (roleDAO.createRole(role)) {
                request.getSession().setAttribute("success",
                        "Thêm vai trò thành công!");
            } else {
                request.getSession().setAttribute("error",
                        "Lỗi khi thêm vai trò!");
            }
        }

        response.sendRedirect("role-management");

    } catch (Exception e) {

        request.getSession().setAttribute("error",
                "Lỗi: " + e.getMessage());
        response.sendRedirect("role-management");
    }
}
 

 private void updateRole(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        int roleId = Integer.parseInt(request.getParameter("roleId"));
        String roleName = request.getParameter("roleName").trim(); 
        String description = request.getParameter("description");

        if (roleDAO.checkRoleNameExistsUpdate(roleName, roleId)) {
            request.getSession().setAttribute("error", "Tên vai trò '" + roleName + "' đã tồn tại trong hệ thống!");
        } else {
            
            Role role = new Role();
            role.setRoleId(roleId);
            role.setRoleName(roleName);
            role.setDescription(description);

            if (roleDAO.updateRole(role)) {
                request.getSession().setAttribute("success", "Cập nhật vai trò thành công!");
            } else {
                request.getSession().setAttribute("error", "Lỗi khi cập nhật vai trò!");
            }
        }

        response.sendRedirect("role-management");

    } catch (Exception e) {
        request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
        response.sendRedirect("role-management");
    }
}

private void deleteRole(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    try {
        int roleId = Integer.parseInt(request.getParameter("id"));

   
        if (roleId <= 4) {
            request.getSession().setAttribute("error", "Không thể xóa vai trò hệ thống mặc định!");
        } 
       
        else if (roleDAO.isRoleInUse(roleId)) {
            request.getSession().setAttribute("error", "Không thể xóa! Hiện đang có người dùng thuộc vai trò này.");
        } 
      
        else {
            if (roleDAO.deleteRole(roleId)) {
                request.getSession().setAttribute("success", "Xóa vai trò thành công");
            } else {
                request.getSession().setAttribute("error", "Lỗi hệ thống khi xóa vai trò");
            }
        }
    } catch (Exception e) {
        request.getSession().setAttribute("error", "ID không hợp lệ!");
    }

    response.sendRedirect("role-management");
}
    private void searchRoles(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String keyword = request.getParameter("keyword");

    if (keyword == null) {
        keyword = "";
    }

    List<Role> roles = roleDAO.searchByName(keyword);

    request.setAttribute("roles", roles);
    request.setAttribute("keyword", keyword);

    request.getRequestDispatcher("role-management.jsp").forward(request, response);
}
}
