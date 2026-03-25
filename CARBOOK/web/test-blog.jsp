<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="dal.BlogDAO, model.Blog, java.util.List" %>
<!DOCTYPE html>
<html>
<head>
    <title>Test Blog Connection</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; }
        .success { color: green; }
        .error { color: red; }
        table { border-collapse: collapse; width: 100%; margin-top: 20px; }
        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        th { background-color: #4CAF50; color: white; }
    </style>
</head>
<body>
    <h1>Test Blog Database Connection</h1>
    
    <%
        try {
            BlogDAO blogDAO = new BlogDAO();
            
            // Test 1: Get total blog count
            int totalBlogs = blogDAO.getTotalBlogCount();
            out.println("<p class='success'>✓ Total blogs in database: " + totalBlogs + "</p>");
            
            // Test 2: Get all published blogs
            List<Blog> blogs = blogDAO.getAllPublishedBlogs();
            out.println("<p class='success'>✓ Published blogs retrieved: " + blogs.size() + "</p>");
            
            // Test 3: Display blogs in table
            if (blogs.isEmpty()) {
                out.println("<p class='error'>✗ No blogs found! Please run Blog.sql to insert sample data.</p>");
            } else {
                out.println("<h2>Blog List:</h2>");
                out.println("<table>");
                out.println("<tr><th>ID</th><th>Title</th><th>Category</th><th>Views</th><th>Published</th><th>Created</th></tr>");
                
                for (Blog blog : blogs) {
                    out.println("<tr>");
                    out.println("<td>" + blog.getBlogId() + "</td>");
                    out.println("<td>" + blog.getTitle() + "</td>");
                    out.println("<td>" + (blog.getCategoryName() != null ? blog.getCategoryName() : "N/A") + "</td>");
                    out.println("<td>" + blog.getViewCount() + "</td>");
                    out.println("<td>" + (blog.isPublished() ? "Yes" : "No") + "</td>");
                    out.println("<td>" + blog.getCreatedAt() + "</td>");
                    out.println("</tr>");
                }
                
                out.println("</table>");
            }
            
            // Test 4: Test pagination
            out.println("<h2>Pagination Test:</h2>");
            List<Blog> paginatedBlogs = blogDAO.getBlogsWithPagination(1, 6);
            out.println("<p class='success'>✓ Paginated blogs (page 1, 6 per page): " + paginatedBlogs.size() + "</p>");
            
        } catch (Exception e) {
            out.println("<p class='error'>✗ Error: " + e.getMessage() + "</p>");
            out.println("<pre>");
            e.printStackTrace(new java.io.PrintWriter(out));
            out.println("</pre>");
        }
    %>
    
    <hr>
    <h2>Next Steps:</h2>
    <ol>
        <li><a href="blog">Go to Blog Page</a></li>
        <li><a href="index.jsp">Go to Home Page</a></li>
    </ol>
    
    <h3>Troubleshooting Checklist:</h3>
    <ul>
        <li>✓ Check if SQL Server is running</li>
        <li>✓ Verify database name is CRMS_DB1</li>
        <li>✓ Check username (sa) and password (12345678)</li>
        <li>✓ Run Blog.sql to create table and insert sample data</li>
        <li>✓ Make sure JDBC driver is in lib folder</li>
    </ul>
</body>
</html>
