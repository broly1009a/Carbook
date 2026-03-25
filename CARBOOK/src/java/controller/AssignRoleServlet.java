package controller;

import dal.RoleDAO;
import dal.UserDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Role;
import model.User;

@WebServlet(name="AssignRoleServlet", urlPatterns={"/assign-role"})
public class AssignRoleServlet extends HttpServlet {
   
    @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
throws ServletException, IOException {
    String action = request.getParameter("action");
    UserDAO u = new UserDAO();
    RoleDAO r = new RoleDAO();

    if (action != null) {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            if ("delete".equals(action)) { 
                u.setUserActiveStatus(id, false);
                request.getSession().setAttribute("success", "Đã khóa tài khoản thành công!");
            } else if ("unblock".equals(action)) { 
                u.setUserActiveStatus(id, true); 
                request.getSession().setAttribute("success", "Đã mở khóa tài khoản thành công!");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Lỗi thao tác trạng thái!");
        }
        response.sendRedirect("assign-role");
        return;
    }

    
    String keyword = request.getParameter("keyword");
    List<User> userList = (keyword != null && !keyword.trim().isEmpty()) 
                          ? u.searchUsers(keyword) : u.getAllUsers();
    List<Role> roleList = r.getAllRoles();

    request.setAttribute("userList", userList);
    request.setAttribute("roleList", roleList);
    request.setAttribute("keyword", keyword);
    request.getRequestDispatcher("assign-role.jsp").forward(request, response);
}

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        UserDAO u = new UserDAO();
        
        try {
            int userId = Integer.parseInt(request.getParameter("userId"));
            int newRoleId = Integer.parseInt(request.getParameter("roleId"));
            
            if (u.updateUserRole(userId, newRoleId)) {
                request.getSession().setAttribute("success", "Cập nhật vai trò thành công!");
            } else {
                request.getSession().setAttribute("error", "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ!");
        }
        response.sendRedirect("assign-role");
    }
}