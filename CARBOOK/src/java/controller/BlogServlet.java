package controller;

import dal.BlogDAO;
import dal.CommentDAO;
import model.Blog;
import model.Comment;
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
 * BlogServlet - Handles blog operations
 */
@WebServlet(name = "BlogServlet", urlPatterns = {"/blog", "/blogs"})
public class BlogServlet extends HttpServlet {

    private BlogDAO blogDAO = new BlogDAO();
    private CommentDAO commentDAO = new CommentDAO();
    private static final int BLOGS_PER_PAGE = 6;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "list":
                listBlogs(request, response);
                break;
            case "view":
                viewBlog(request, response);
                break;
            case "category":
                listBlogsByCategory(request, response);
                break;
            case "search":
                searchBlogs(request, response);
                break;
            case "popular":
                listPopularBlogs(request, response);
                break;
            default:
                listBlogs(request, response);
                break;
        }
    }

    /**
     * List all blogs with pagination
     */
    private void listBlogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get page number from request, default to 1
            String pageParam = request.getParameter("page");
            int currentPage = 1;
            
            if (pageParam != null && !pageParam.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageParam);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            
            // Get total blog count and calculate total pages
            int totalBlogs = blogDAO.getTotalBlogCount();
            int totalPages = (int) Math.ceil((double) totalBlogs / BLOGS_PER_PAGE);
            
            // Get blogs for current page
            List<Blog> blogs = blogDAO.getBlogsWithPagination(currentPage, BLOGS_PER_PAGE);
            
            // Set attributes for JSP
            request.setAttribute("blogs", blogs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBlogs", totalBlogs);
            
            // Forward to blog.jsp
            request.getRequestDispatcher("blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải danh sách blog: " + e.getMessage());
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    /**
     * View a single blog post
     */
    private void viewBlog(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String blogIdStr = request.getParameter("id");
            
            if (blogIdStr == null || blogIdStr.isEmpty()) {
                response.sendRedirect("blog");
                return;
            }
            
            int blogId = Integer.parseInt(blogIdStr);
            Blog blog = blogDAO.getBlogById(blogId);
            
            if (blog == null) {
                request.setAttribute("error", "Không tìm thấy bài viết");
                response.sendRedirect("blog");
                return;
            }
            
            // Increment view count
            blogDAO.incrementViewCount(blogId);
            
            // Get comments for this blog
            List<Comment> comments = commentDAO.getCommentsByBlogId(blogId);
            int commentCount = commentDAO.getCommentCount(blogId);
            
            // Get recent blogs for sidebar
            List<Blog> recentBlogs = blogDAO.getRecentBlogs(5);
            
            // Get popular blogs for sidebar
            List<Blog> popularBlogs = blogDAO.getPopularBlogs(5);
            
            request.setAttribute("blog", blog);
            request.setAttribute("comments", comments);
            request.setAttribute("commentCount", commentCount);
            request.setAttribute("recentBlogs", recentBlogs);
            request.setAttribute("popularBlogs", popularBlogs);
            
            request.getRequestDispatcher("blog-single.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID bài viết không hợp lệ");
            response.sendRedirect("blog");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải bài viết: " + e.getMessage());
            response.sendRedirect("blog");
        }
    }

    /**
     * List blogs by category
     */
    private void listBlogsByCategory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String category = request.getParameter("name");
            
            if (category == null || category.isEmpty()) {
                response.sendRedirect("blog");
                return;
            }
            
            List<Blog> blogs = blogDAO.getBlogsByCategory(category);
            
            request.setAttribute("blogs", blogs);
            request.setAttribute("categoryName", category);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            
            request.getRequestDispatcher("blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải blog theo danh mục: " + e.getMessage());
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    /**
     * Search blogs
     */
    private void searchBlogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String keyword = request.getParameter("keyword");
            
            if (keyword == null || keyword.trim().isEmpty()) {
                response.sendRedirect("blog");
                return;
            }
            
            List<Blog> blogs = blogDAO.searchBlogs(keyword);
            
            request.setAttribute("blogs", blogs);
            request.setAttribute("searchKeyword", keyword);
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            
            request.getRequestDispatcher("blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tìm kiếm blog: " + e.getMessage());
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    /**
     * List popular blogs
     */
    private void listPopularBlogs(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Blog> blogs = blogDAO.getPopularBlogs(10);
            
            request.setAttribute("blogs", blogs);
            request.setAttribute("viewMode", "popular");
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            
            request.getRequestDispatcher("blog.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải blog phổ biến: " + e.getMessage());
            request.getRequestDispatcher("blog.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("addComment".equals(action)) {
            addComment(request, response);
        } else {
            doGet(request, response);
        }
    }
    
    /**
     * Add a comment or reply to a blog post
     */
    private void addComment(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            // Check if user is logged in
            if (user == null) {
                session.setAttribute("error", "Vui lòng đăng nhập để bình luận");
                response.sendRedirect("login.jsp");
                return;
            }
            
            String blogIdStr = request.getParameter("blogId");
            String commentText = request.getParameter("commentText");
            String parentCommentIdStr = request.getParameter("parentCommentId");
            
            // Validate input
            if (blogIdStr == null || blogIdStr.isEmpty() || 
                commentText == null || commentText.trim().isEmpty()) {
                session.setAttribute("error", "Nội dung bình luận không được để trống");
                response.sendRedirect("blog?action=view&id=" + blogIdStr);
                return;
            }
            
            int blogId = Integer.parseInt(blogIdStr);
            
            // Check if blog exists
            Blog blog = blogDAO.getBlogById(blogId);
            if (blog == null) {
                session.setAttribute("error", "Bài viết không tồn tại");
                response.sendRedirect("blog");
                return;
            }
            
            // Create comment object
            Comment comment = new Comment();
            comment.setBlogId(blogId);
            comment.setUserId(user.getUserId());
            comment.setCommentText(commentText.trim());
            
            // Set parent comment ID if this is a reply
            if (parentCommentIdStr != null && !parentCommentIdStr.isEmpty()) {
                try {
                    int parentCommentId = Integer.parseInt(parentCommentIdStr);
                    comment.setParentCommentId(parentCommentId);
                } catch (NumberFormatException e) {
                    // Ignore if invalid parent ID
                }
            }
            
            // Save comment
            Comment createdComment = commentDAO.createComment(comment);
            
            if (createdComment != null) {
                session.setAttribute("success", "Bình luận của bạn đã được đăng thành công");
            } else {
                session.setAttribute("error", "Không thể đăng bình luận. Vui lòng thử lại");
            }
            
            // Redirect back to blog post
            response.sendRedirect("blog?action=view&id=" + blogId + "#comments");
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Dữ liệu không hợp lệ");
            response.sendRedirect("blog");
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi đăng bình luận: " + e.getMessage());
            response.sendRedirect("blog");
        }
    }
}
