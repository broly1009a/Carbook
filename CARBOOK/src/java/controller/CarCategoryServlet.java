package controller;

import dal.CarCategoryDAO;
import model.CarCategory;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

@WebServlet(name="CarCategoryServlet", urlPatterns={"/car-category"})
public class CarCategoryServlet extends HttpServlet {

  @Override
protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    CarCategoryDAO dao = new CarCategoryDAO();
    HttpSession session = request.getSession();

    try {
        if (action == null || "list".equals(action)) {
            String nameSearch = request.getParameter("txtSearch");
            List<CarCategory> list;
            if (nameSearch != null && !nameSearch.trim().isEmpty()) {
                list = dao.searchCategoriesByName(nameSearch);
            } else {
                list = dao.getAllCategories();
            }

            request.setAttribute("categories", list);
            request.setAttribute("txtSearch", nameSearch); // Quan trọng để giữ chữ ở ô Search
            request.getRequestDispatcher("/category-list.jsp").forward(request, response);
        } 
        else if ("add".equals(action)) {
            request.getRequestDispatcher("/category-add.jsp").forward(request, response);
        } 
        else if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("category", dao.getCategoryById(id));
            request.getRequestDispatcher("/category-edit.jsp").forward(request, response);
        } 
        else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            if (dao.isCategoryInUse(id)) {
                session.setAttribute("error", "Không thể xóa danh mục đang có xe sử dụng!");
            } else {
                dao.deleteCategory(id);
                session.setAttribute("success", "Xóa thành công!");
            }
            response.sendRedirect("car-category?action=list");
        }
    } catch (Exception e) {
        response.sendRedirect("car-category?action=list");
    }
}
    @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String action = request.getParameter("action");
    CarCategoryDAO dao = new CarCategoryDAO();
    HttpSession session = request.getSession();

    String name = request.getParameter("categoryName") != null ? request.getParameter("categoryName").trim() : "";
    String description = request.getParameter("description");

    CarCategory c = new CarCategory();
    c.setCategoryName(name);
    c.setDescription(description);

    if ("add".equals(action)) {

        if (dao.isCategoryNameExists(name)) {
            request.setAttribute("error", "Tên danh mục '" + name + "' đã tồn tại!");
            request.getRequestDispatcher("/category-add.jsp").forward(request, response);
            return;
        }
        
        if(dao.createCategory(c)) {
            session.setAttribute("success", "Thêm danh mục mới thành công!");
        } else {
            session.setAttribute("error", "Lỗi khi thêm danh mục.");
        }
        
    } else if ("edit".equals(action)) {
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));
        c.setCategoryId(categoryId);

        if (dao.isCategoryNameExists(name, categoryId)) {
            request.setAttribute("error", "Tên danh mục mới bị trùng với danh mục khác!");
            request.setAttribute("category", c); // Gửi lại object để giữ data trên form
            request.getRequestDispatcher("/category-edit.jsp").forward(request, response);
            return;
        }

        if(dao.updateCategory(c)) {
            session.setAttribute("success", "Cập nhật danh mục thành công!");
        } else {
            session.setAttribute("error", "Lỗi khi cập nhật danh mục.");
        }
    }
    response.sendRedirect("car-category?action=list");
}
}