package controller;

import dal.BlogDAO;
import model.Blog;
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
 * BlogManagementServlet - Handles blog management for admin
 */
@WebServlet(name = "BlogManagementServlet", urlPatterns = {"/blog-management"})
public class BlogManagementServlet extends HttpServlet {

    private BlogDAO blogDAO = new BlogDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
  
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // if (user == null || user.getRoleId() != 1) {
        //     response.sendRedirect("login.jsp");
        //     return;
        // }
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listAllBlogs(request, response);
                break;
            case "create":
                showCreateForm(request, response);
                break;
            case "edit":
                showEditForm(request, response, user);
                break;
            case "delete":
                deleteBlog(request, response, user);
                break;
            case "toggle-publish":
                togglePublishStatus(request, response, user);
                break;
            default:
                listAllBlogs(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
//        if (user == null || user.getRoleId() != 1) {
//            response.sendRedirect("login.jsp");
//            return;
//        }
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createBlog(request, response, user);
        } else if ("update".equals(action)) {
            updateBlog(request, response, user);
        }
    }

    /**
     * List all blogs (for admin)
     */
    private void listAllBlogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Blog> blogs = blogDAO.getAllBlogs();
            
            request.setAttribute("blogs", blogs);
            request.getRequestDispatcher("blog-list.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách blog: " + e.getMessage());
            request.getRequestDispatcher("blog-list.jsp").forward(request, response);
        }
    }

    /**
     * Show create blog form
     */
    private void showCreateForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("blog-form.jsp").forward(request, response);
    }

    /**
     * Show edit blog form
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            String blogIdStr = request.getParameter("id");
            
            if (blogIdStr == null || blogIdStr.isEmpty()) {
                response.sendRedirect("blog-management");
                return;
            }
            
            int blogId = Integer.parseInt(blogIdStr);
            Blog blog = blogDAO.getBlogById(blogId);
            
            if (blog == null) {
                request.getSession().setAttribute("error", "Không tìm thấy bài viết");
                response.sendRedirect("blog-management");
                return;
            }
            
            request.setAttribute("blog", blog);
            request.getRequestDispatcher("blog-form.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID bài viết không hợp lệ");
            response.sendRedirect("blog-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi tải bài viết: " + e.getMessage());
            response.sendRedirect("blog-management");
        }
    }

    /**
     * Create new blog
     */
    private void createBlog(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String summary = request.getParameter("summary");
            String imageURL = request.getParameter("imageURL");
            String categoryName = request.getParameter("categoryName");
            String isPublishedStr = request.getParameter("isPublished");
            
            // Validate required fields
            if (title == null || title.trim().isEmpty() || 
                content == null || content.trim().isEmpty()) {
                request.setAttribute("error", "Tiêu đề và nội dung không được để trống");
                request.getRequestDispatcher("blog-form.jsp").forward(request, response);
                return;
            }
            
            // Create blog object
            Blog blog = new Blog();
            blog.setTitle(title);
            blog.setContent(content);
            blog.setSummary(summary);
            blog.setImageURL(imageURL);
            blog.setAuthorId(user.getUserId());
            blog.setCategoryName(categoryName);
            blog.setPublished(isPublishedStr != null && isPublishedStr.equals("1"));
            
            // Save to database
            if (blogDAO.createBlog(blog)) {
                request.getSession().setAttribute("success", "Tạo bài viết thành công");
                response.sendRedirect("blog-management");
            } else {
                request.setAttribute("error", "Lỗi khi tạo bài viết");
                request.getRequestDispatcher("blog-form.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("blog-form.jsp").forward(request, response);
        }
    }

    /**
     * Update blog
     */
    private void updateBlog(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            
            int blogId = Integer.parseInt(request.getParameter("blogId"));
            String title = request.getParameter("title");
            String content = request.getParameter("content");
            String summary = request.getParameter("summary");
            String imageURL = request.getParameter("imageURL");
            String categoryName = request.getParameter("categoryName");
            String isPublishedStr = request.getParameter("isPublished");
            
            // Validate required fields
            if (title == null || title.trim().isEmpty() || 
                content == null || content.trim().isEmpty()) {
                request.setAttribute("error", "Tiêu đề và nội dung không được để trống");
                
                // Reload blog for form
                Blog blog = blogDAO.getBlogById(blogId);
                request.setAttribute("blog", blog);
                request.getRequestDispatcher("blog-form.jsp").forward(request, response);
                return;
            }
            
            // Get existing blog
            Blog blog = blogDAO.getBlogById(blogId);
            if (blog == null) {
                request.getSession().setAttribute("error", "Không tìm thấy bài viết");
                response.sendRedirect("blog-management");
                return;
            }
            
            // Update blog object
            blog.setTitle(title);
            blog.setContent(content);
            blog.setSummary(summary);
            blog.setImageURL(imageURL);
            blog.setCategoryName(categoryName);
            blog.setPublished(isPublishedStr != null && isPublishedStr.equals("1"));
            
            // Update in database
            if (blogDAO.updateBlog(blog)) {
                request.getSession().setAttribute("success", "Cập nhật bài viết thành công");
                response.sendRedirect("blog-management");
            } else {
                request.setAttribute("error", "Lỗi khi cập nhật bài viết");
                request.setAttribute("blog", blog);
                request.getRequestDispatcher("blog-form.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID bài viết không hợp lệ");
            response.sendRedirect("blog-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("blog-management");
        }
    }

    /**
     * Delete blog
     */
    private void deleteBlog(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            String blogIdStr = request.getParameter("id");
            
            if (blogIdStr == null || blogIdStr.isEmpty()) {
                response.sendRedirect("blog-management");
                return;
            }
            
            int blogId = Integer.parseInt(blogIdStr);
            
            if (blogDAO.deleteBlog(blogId)) {
                request.getSession().setAttribute("success", "Đã xóa bài viết thành công");
            } else {
                request.getSession().setAttribute("error", "Lỗi khi xóa bài viết");
            }
            
            response.sendRedirect("blog-management");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID bài viết không hợp lệ");
            response.sendRedirect("blog-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("blog-management");
        }
    }

    /**
     * Toggle publish status
     */
    private void togglePublishStatus(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        try {
            String blogIdStr = request.getParameter("id");
            
            if (blogIdStr == null || blogIdStr.isEmpty()) {
                response.sendRedirect("blog-management");
                return;
            }
            
            int blogId = Integer.parseInt(blogIdStr);
            Blog blog = blogDAO.getBlogById(blogId);
            
            if (blog == null) {
                request.getSession().setAttribute("error", "Không tìm thấy bài viết");
                response.sendRedirect("blog-management");
                return;
            }
            
            // Toggle publish status
            blog.setPublished(!blog.isPublished());
            
            if (blogDAO.updateBlog(blog)) {
                String status = blog.isPublished() ? "đã xuất bản" : "đã ẩn";
                request.getSession().setAttribute("success", "Bài viết " + status);
            } else {
                request.getSession().setAttribute("error", "Lỗi khi thay đổi trạng thái");
            }
            
            response.sendRedirect("blog-management");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID bài viết không hợp lệ");
            response.sendRedirect("blog-management");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi: " + e.getMessage());
            response.sendRedirect("blog-management");
        }
    }
}
