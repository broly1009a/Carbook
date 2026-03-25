package controller;

import dal.CarModelDAO;
import dal.CarBrandDAO;
import model.CarModel;
import model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

import com.google.gson.Gson; 


@WebServlet(name = "CarModelServlet", urlPatterns = {"/car-models"})
public class CarModelServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CarModelDAO modelDAO = new CarModelDAO();
        CarBrandDAO brandDAO = new CarBrandDAO();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        try {
            String action = request.getParameter("action");

            if ("getByBrand".equals(action)) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                try {
                    int brandId = Integer.parseInt(request.getParameter("brandId"));
                    List<CarModel> list = modelDAO.getModelsByBrandId(brandId);

                    Gson gson = new Gson();
                    String json = gson.toJson(list);

                    PrintWriter out = response.getWriter();
                    out.print(json);
                    out.flush();
                } catch (Exception e) {
                    response.getWriter().print("[]");
                }
                return;
            }

            if ("add".equals(action)) {
                request.setAttribute("brands", brandDAO.getAllBrands());
                request.getRequestDispatcher("/model-add.jsp").forward(request, response);
            } else if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                request.setAttribute("model", modelDAO.getModelById(id));
                request.setAttribute("brands", brandDAO.getAllBrands());
                request.getRequestDispatcher("/model-edit.jsp").forward(request, response);
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                if (modelDAO.deleteModel(id)) {
                    session.setAttribute("success", "Xóa dòng xe thành công!");
                } else {
                    session.setAttribute("error", "Xóa thất bại!");
                }
                response.sendRedirect("car-models");
            } else {
                String brandName = request.getParameter("brandName");
                String modelName = request.getParameter("modelName");
                String yearRaw = request.getParameter("year");

                List<CarModel> list = modelDAO.getAllModels();
                if ((brandName == null || brandName.trim().isEmpty())
                        && (modelName == null || modelName.trim().isEmpty())
                        && (yearRaw == null || yearRaw.trim().isEmpty())) {

                    request.setAttribute("models", list);
                    request.getRequestDispatcher("/model-list.jsp").forward(request, response);
                    return;
                }

                List<CarModel> filtered = new java.util.ArrayList<>();

                Integer year = null;
                if (yearRaw != null && !yearRaw.trim().isEmpty()) {
                    year = Integer.parseInt(yearRaw);
                }

                for (CarModel m : list) {
                    boolean match = true;
                    if (brandName != null && !brandName.trim().isEmpty()) {
                        if (m.getBrand() == null
                                || !m.getBrand().getBrandName().toLowerCase()
                                        .contains(brandName.toLowerCase())) {
                            match = false;
                        }
                    }

                    if (modelName != null && !modelName.trim().isEmpty()) {
                        if (!m.getModelName().toLowerCase()
                                .contains(modelName.toLowerCase())) {
                            match = false;
                        }
                    }

                    if (year != null) {
                        if (m.getYear() != year) {
                            match = false;
                        }
                    }
                    if (match) {
                        filtered.add(m);
                    }
                }

                request.setAttribute("models", filtered);
                request.getRequestDispatcher("/model-list.jsp").forward(request, response);
            }
        } finally {
            modelDAO.closeConnection();
            brandDAO.closeConnection();
        }
    }

@Override
protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    CarModelDAO modelDAO = new CarModelDAO();
    CarBrandDAO brandDAO = new CarBrandDAO();
    HttpSession session = request.getSession();
    int currentYear = LocalDate.now().getYear();

    try {
        String action = request.getParameter("action");
        int brandId = Integer.parseInt(request.getParameter("brandId"));
        String name = request.getParameter("modelName");
        int year = Integer.parseInt(request.getParameter("year"));
        String errorMsg = null;
        if (year < 1950) {
            errorMsg = "Năm sản xuất không được nhỏ hơn 1950";
        } else if (year > currentYear) {
            errorMsg = "Năm sản xuất không được lớn hơn năm hiện tại (" + currentYear + ")";
        }
        if (errorMsg == null) {
            if ("create".equals(action)) {
                if (modelDAO.isModelDuplicate(brandId, name, year)) {
                    errorMsg = "Dòng xe '" + name + "' năm " + year + " đã tồn tại cho hãng này!";
                }
            } else if ("update".equals(action)) {
                int modelId = Integer.parseInt(request.getParameter("modelId"));
                if (modelDAO.isModelDuplicate(brandId, name, year, modelId)) {
                    errorMsg = "Thông tin dòng xe bị trùng với dữ liệu đã có!";
                }
            }
        }
        if (errorMsg != null) {
            request.setAttribute("error", errorMsg);
            request.setAttribute("brands", brandDAO.getAllBrands());           
            if ("create".equals(action)) {
                request.getRequestDispatcher("/model-add.jsp").forward(request, response);
            } else {
                int modelId = Integer.parseInt(request.getParameter("modelId"));         
                request.setAttribute("model", modelDAO.getModelById(modelId));
                request.getRequestDispatcher("/model-edit.jsp").forward(request, response);
            }
            return;
        }

       
        if ("create".equals(action)) {
            CarModel m = new CarModel(0, brandId, name, year);
            if (modelDAO.createModel(m)) {
                session.setAttribute("success", "Thêm dòng xe mới thành công!");
            }
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("modelId"));
            CarModel m = new CarModel(id, brandId, name, year);
            if (modelDAO.updateModel(m)) {
                session.setAttribute("success", "Cập nhật thành công!");
            }
        }
        response.sendRedirect("car-models");
    } catch (Exception e) {
        session.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
        response.sendRedirect("car-models");
    } finally {
        modelDAO.closeConnection();
        brandDAO.closeConnection();
    }
}
}
