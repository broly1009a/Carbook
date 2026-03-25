package controller;

import dal.UserDAO;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "UserManagementServlet", urlPatterns = {"/users"})
public class UserManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        
       
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;
        if (currentUser == null || currentUser.getRoleId() != 1) {         
            response.sendRedirect("login"); 
            return;
        }

      
        String action = request.getParameter("action");
        String keyword = request.getParameter("keyword");
        UserDAO userDAO = new UserDAO();
        List<User> users;

        if ("search".equals(action) && keyword != null) {
            users = userDAO.searchUsers(keyword);
            request.setAttribute("keyword", keyword);
        } else {
            users = userDAO.getAllUsers();
        }
        request.setAttribute("users", users);
        request.getRequestDispatcher("user-management.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute("user") : null;

        if (currentUser == null || currentUser.getRoleId() != 1) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = request.getParameter("action");
        String userIdStr = request.getParameter("userId");

        if (userIdStr != null) {
            try {
                int userId = Integer.parseInt(userIdStr);
                UserDAO userDAO = new UserDAO();
                boolean success = false;

                switch (action) {
                    case "activate":
                        success = userDAO.setUserActiveStatus(userId, true);
                        break;
                    case "deactivate":
                        success = userDAO.setUserActiveStatus(userId, false);
                        break;
                    case "delete":

                        success = userDAO.deleteUser(userId);
                        break;
                }


                if (success) {
                    response.sendRedirect("users?success=1");
                } else {
                    response.sendRedirect("users?error=1");
                }

            } catch (Exception e) {
                response.sendRedirect("users?error=system");
            }
        }
    }
}