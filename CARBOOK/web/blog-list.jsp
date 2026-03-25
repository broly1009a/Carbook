<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Blog - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .blog-thumbnail {
            width: 80px;
            height: 60px;
            object-fit: cover;
            border-radius: 4px;
        }
        .status-badge {
            font-size: 12px;
            padding: 4px 8px;
        }
        .actions-cell {
            white-space: nowrap;
        }
        .actions-cell .btn {
            padding: 4px 8px;
            font-size: 12px;
            margin-right: 4px;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 px-0">
                    <%@ include file="includes/sidebar.jsp" %>
                </div>
                
                <!-- Main Content -->
                <div class="col-md-9 col-lg-10">
                    <div class="container-fluid">
                        <!-- Header -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h2><i class="icon-book"></i> Quản lý Blog</h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="blog-management?action=create" class="btn btn-primary">
                                    <i class="icon-plus"></i> Thêm bài viết mới
                                </a>
                            </div>
                        </div>
                        
                        <!-- Success/Error Messages -->
                        <c:if test="${not empty sessionScope.success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="icon-check"></i> ${sessionScope.success}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:remove var="success" scope="session" />
                        </c:if>
                        
                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="icon-close"></i> ${sessionScope.error}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:remove var="error" scope="session" />
                        </c:if>
                        
                        <!-- Blog List -->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0">Danh sách bài viết</h5>
                                    </div>
                                    <div class="card-body p-0">
                                        <c:choose>
                                            <c:when test="${not empty blogs}">
                                                <div class="table-responsive">
                                                    <table class="table table-hover mb-0">
                                                        <thead class="thead-light">
                                                            <tr>
                                                                <th>ID</th>
                                                                <th>Hình ảnh</th>
                                                                <th>Tiêu đề</th>
                                                                <th>Danh mục</th>
                                                                <th>Tác giả</th>
                                                                <th>Lượt xem</th>
                                                                <th>Trạng thái</th>
                                                                <th>Ngày tạo</th>
                                                                <th>Thao tác</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="blog" items="${blogs}">
                                                                <tr>
                                                                    <td>${blog.blogId}</td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty blog.imageURL}">
                                                                                <img src="${blog.imageURL}" alt="${blog.title}" class="blog-thumbnail">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <img src="images/placeholder.jpg" alt="No image" class="blog-thumbnail">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <strong>${blog.title}</strong>
                                                                        <br>
                                                                        <small class="text-muted">${blog.shortSummary}</small>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty blog.categoryName}">
                                                                                <span class="badge badge-info">${blog.categoryName}</span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted">-</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty blog.author}">
                                                                                ${blog.author.fullName}
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                Admin
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <i class="icon-eye"></i> ${blog.viewCount}
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${blog.published}">
                                                                                <span class="badge badge-success status-badge">
                                                                                    <i class="icon-check"></i> Đã xuất bản
                                                                                </span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge badge-secondary status-badge">
                                                                                    <i class="icon-eye-off"></i> Nháp
                                                                                </span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy" />
                                                                        <br>
                                                                        <small class="text-muted">
                                                                            <fmt:formatDate value="${blog.createdAt}" pattern="HH:mm" />
                                                                        </small>
                                                                    </td>
                                                                    <td class="actions-cell">
                                                                        <a href="blog?action=view&id=${blog.blogId}" 
                                                                           class="btn btn-sm btn-info" 
                                                                           target="_blank"
                                                                           title="Xem">
                                                                            <i class="icon-eye"></i>
                                                                        </a>
                                                                        <a href="blog-management?action=edit&id=${blog.blogId}" 
                                                                           class="btn btn-sm btn-warning"
                                                                           title="Sửa">
                                                                            <i class="icon-pencil"></i>
                                                                        </a>
                                                                        <a href="blog-management?action=toggle-publish&id=${blog.blogId}" 
                                                                           class="btn btn-sm ${blog.published ? 'btn-secondary' : 'btn-success'}"
                                                                           onclick="return confirm('${blog.published ? 'Ẩn' : 'Xuất bản'} bài viết này?')"
                                                                           title="${blog.published ? 'Ẩn' : 'Xuất bản'}">
                                                                            <i class="icon-${blog.published ? 'eye-off' : 'check'}"></i>
                                                                        </a>
                                                                        <a href="blog-management?action=delete&id=${blog.blogId}" 
                                                                           class="btn btn-sm btn-danger"
                                                                           onclick="return confirm('Bạn có chắc chắn muốn xóa bài viết này?')"
                                                                           title="Xóa">
                                                                            <i class="icon-trash"></i>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="p-5 text-center">
                                                    <i class="icon-book" style="font-size: 48px; color: #ccc;"></i>
                                                    <h4 class="mt-3">Chưa có bài viết nào</h4>
                                                    <p class="text-muted">Bắt đầu bằng cách tạo bài viết đầu tiên của bạn</p>
                                                    <a href="blog-management?action=create" class="btn btn-primary mt-3">
                                                        <i class="icon-plus"></i> Tạo bài viết mới
                                                    </a>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Statistics -->
                        <c:if test="${not empty blogs}">
                            <div class="row mt-4">
                                <div class="col-md-3">
                                    <div class="card text-white bg-primary">
                                        <div class="card-body">
                                            <h5 class="card-title">Tổng số bài viết</h5>
                                            <h2>${blogs.size()}</h2>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card text-white bg-success">
                                        <div class="card-body">
                                            <h5 class="card-title">Đã xuất bản</h5>
                                            <h2>
                                                <c:set var="publishedCount" value="0" />
                                                <c:forEach var="blog" items="${blogs}">
                                                    <c:if test="${blog.published}">
                                                        <c:set var="publishedCount" value="${publishedCount + 1}" />
                                                    </c:if>
                                                </c:forEach>
                                                ${publishedCount}
                                            </h2>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card text-white bg-secondary">
                                        <div class="card-body">
                                            <h5 class="card-title">Nháp</h5>
                                            <h2>${blogs.size() - publishedCount}</h2>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-3">
                                    <div class="card text-white bg-info">
                                        <div class="card-body">
                                            <h5 class="card-title">Tổng lượt xem</h5>
                                            <h2>
                                                <c:set var="totalViews" value="0" />
                                                <c:forEach var="blog" items="${blogs}">
                                                    <c:set var="totalViews" value="${totalViews + blog.viewCount}" />
                                                </c:forEach>
                                                <fmt:formatNumber value="${totalViews}" />
                                            </h2>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        
                    </div>
                </div>
            </div>
        </div>
    </section>
    
     <script src="js/jquery.min.js"></script>
  <script src="js/jquery-migrate-3.0.1.min.js"></script>
  <script src="js/popper.min.js"></script>
  <script src="js/bootstrap.min.js"></script>
  <script src="js/jquery.easing.1.3.js"></script>
  <script src="js/jquery.waypoints.min.js"></script>
  <script src="js/jquery.stellar.min.js"></script>
  <script src="js/owl.carousel.min.js"></script>
  <script src="js/jquery.magnific-popup.min.js"></script>
  <script src="js/aos.js"></script>
  <script src="js/jquery.animateNumber.min.js"></script>
  <script src="js/bootstrap-datepicker.js"></script>
  <script src="js/jquery.timepicker.min.js"></script>
  <script src="js/scrollax.min.js"></script>
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBVWaKrjvy3MaE7SQ74_uJiULgl1JY0H2s&sensor=false"></script>
  <script src="js/google-map.js"></script>
  <script src="js/main.js"></script>
</body>
</html>
