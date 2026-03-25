package controller;

import dal.CarBrandDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import model.CarBrand;
import model.User;

@WebServlet(name="CarBrandServlet", urlPatterns={"/brand"})
public class CarBrandServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        CarBrandDAO dao = new CarBrandDAO();
        List<CarBrand> list = null;

       

        if (action == null || "list".equals(action)) {
            request.setAttribute("brands", dao.getAllBrands());
            request.getRequestDispatcher("/brand-list.jsp").forward(request, response);
            return;
        } 
        else if ("add".equals(action)) {
            request.getRequestDispatcher("/brand-add.jsp").forward(request, response);
            return;
        }else if ("search".equals(action)) {

    String name = request.getParameter("nameSearch");
    String country = request.getParameter("countrySearch");

 
    if (name != null && !name.trim().isEmpty()) {
        list = dao.searchByName(name);

        if (country != null && !country.trim().isEmpty()) {
            List<CarBrand> filtered = new ArrayList<>();
            for (CarBrand b : list) {
                if (b.getCountry().toLowerCase()
                        .contains(country.toLowerCase())) {
                    filtered.add(b);
                }
            }
            list = filtered;
        }
    }

    else if (country != null && !country.trim().isEmpty()) {
        list = dao.searchByCountry(country);
    }
    else {
        list = dao.getAllBrands();
    }

    request.setAttribute("nameSearch", name);
    request.setAttribute("countrySearch", country);
}
        else if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                CarBrand b = dao.getBrandById(id);
                if (b != null) {
                    request.setAttribute("brand", b);
                    request.getRequestDispatcher("/brand-edit.jsp").forward(request, response);
                } else {
                    session.setAttribute("error", "Hãng xe không tồn tại!");
                    response.sendRedirect("brand?action=list");
                    return;
                }
            } catch (Exception e) {
                response.sendRedirect("brand?action=list");
                return;
            }
        } 
       
        else if ("delete".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                
              //check trung ten
                if (dao.hasModels(id)) {
                    session.setAttribute("error", "Không thể xóa! Hãng xe này hiện đang có các Model liên kết. Hãy xóa các Model trước!");
                } else {
                    
                    if (dao.deleteBrand(id)) {
                        session.setAttribute("success", "Xóa hãng xe thành công!");
                    } else {
                        session.setAttribute("error", "Xóa thất bại!");
                    }
                }
            } catch (Exception e) {
                session.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
                
            }
            
            response.sendRedirect("brand?action=list");
            return;
        }
        request.setAttribute("brands", list); 
    request.getRequestDispatcher("/brand-list.jsp").forward(request, response);
    }

   @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    HttpSession session = request.getSession();
    String action = request.getParameter("action");
    CarBrandDAO dao = new CarBrandDAO();
    String name = request.getParameter("brandName") != null ? request.getParameter("brandName").trim() : "";
    String country = request.getParameter("country");
    String logo = request.getParameter("logoURL");

    CarBrand b = new CarBrand();
    b.setBrandName(name);
    b.setCountry(country);
    b.setLogoURL(logo);

    if ("add".equals(action)) {
        
        if (dao.isBrandNameExists(name)) {
            session.setAttribute("error", "Tên hãng xe '" + name + "' đã tồn tại!");
            response.sendRedirect("brand?action=add"); 
            return; 
        }

        if (dao.createBrand(b)) {
            session.setAttribute("success", "Thêm hãng xe mới thành công!");
        } else {
            session.setAttribute("error", "Thêm hãng xe thất bại!");
        }
    } 
    else if ("edit".equals(action)) {
        try {
            int brandId = Integer.parseInt(request.getParameter("brandId"));
            b.setBrandId(brandId);

            
            if (dao.isBrandNameExists(name, brandId)) {
                session.setAttribute("error", "Tên hãng xe mới bị trùng với hãng khác!");
                response.sendRedirect("brand?action=edit&id=" + brandId);
                return;
            }

            if (dao.updateBrand(b)) {
                session.setAttribute("success", "Cập nhật thông tin thành công!");
            } else {
                session.setAttribute("error", "Cập nhật thất bại!");
            }
        } catch (Exception e) {
            session.setAttribute("error", "Dữ liệu không hợp lệ!");
        }
    }

    
    response.sendRedirect("brand?action=list");
}
}