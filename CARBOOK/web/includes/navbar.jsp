<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String currentPage = request.getRequestURI();
    String servletPath = request.getServletPath();
    boolean isMainPage = currentPage.contains("index.jsp") || currentPage.contains("home") || 
                         currentPage.contains("about.jsp") || currentPage.contains("services") || 
                         currentPage.contains("cars") || currentPage.contains("blog") || 
                         currentPage.contains("contact.jsp") ||
                         servletPath.equals("/home") || servletPath.equals("/services") || 
                         servletPath.equals("/cars") || servletPath.equals("/blog");
%>
<nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
    <div class="container">
        <a class="navbar-brand" href="index.html">Car<span>Book</span></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav" aria-controls="ftco-nav" aria-expanded="false" aria-label="Toggle navigation">
            <span class="oi oi-menu"></span> Menu
        </button>

        <div class="collapse navbar-collapse" id="ftco-nav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item"><a href="home" class="nav-link" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>Trang chủ</a></li>
                <li class="nav-item"><a href="about.jsp" class="nav-link" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>Giới thiệu</a></li>
                <li class="nav-item"><a href="services" class="nav-link" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>Dịch vụ</a></li>
                <li class="nav-item"><a href="cars" class="nav-link" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>Thuê xe</a></li>
                <li class="nav-item"><a href="blog" class="nav-link" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>Blog</a></li>
                <li class="nav-item"><a href="contact.jsp" class="nav-link" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>Liên hệ</a></li>
                
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <!-- Notification Icon -->
                        <li class="nav-item">
                            <a class="nav-link position-relative" href="notification" style="font-size: 1.0rem;">
                                🔔
                                <c:if test="${not empty unreadCount && unreadCount > 0}">
                                    <span class="badge badge-danger position-absolute" style="top: 5px; right: 5px; font-size: 0.7rem; padding: 2px 5px; border-radius: 10px;">
                                        ${unreadCount > 99 ? '99+' : unreadCount}
                                    </span>
                                </c:if>
                            </a>
                        </li>
                        
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" <%= !isMainPage ? "style='color: #000 !important;'" : "" %>>
                                <i class="ion-person"></i> ${sessionScope.user.fullName}
                            </a>
                            <div class="dropdown-menu" aria-labelledby="userDropdown">
                                <c:if test="${sessionScope.user.roleId != 3}">
                                    <a class="dropdown-item" href="dashboard">Dashboard</a>
                                </c:if>
                                
                                <c:if test="${sessionScope.user.roleId == 1}">
                                    <div class="dropdown-divider"></div>
                                    <h6 class="dropdown-header">Quản trị</h6>
                                    <a class="dropdown-item" href="role-management">Vai trò</a>
                                    <a class="dropdown-item " href="verify"> Phê duyệt xe mới</a>
                                    <a class="dropdown-item" href="car-management">Quản lý xe</a>
                                     <a class="dropdown-item" href="brand">Quản lý hãng xe</a>
                                      <a class="dropdown-item" href="car-models">Quản lý dòng xe</a>
                                       <a class="dropdown-item" href="car-images">Quản lý ảnh xe</a>                                  
                                    <a class="dropdown-item" href="assign-role">Nâng/Hạ quyền</a>
                                     <a class="dropdown-item" href="users">Quản lý người dùng</a>
                                    <a class="dropdown-item" href="car-availability">Lịch không khả dụng</a>
                                    <a class="dropdown-item" href="maintenance">Quản lý bảo trì</a>
                                    <a class="dropdown-item" href="booking?action=list">Tất cả booking</a>
                                    <a class="dropdown-item" href="payment?action=list">Tất cả thanh toán</a>
                                </c:if>
                                
                                <c:if test="${sessionScope.user.roleId == 2}">
                                    <div class="dropdown-divider"></div>
                                    <h6 class="dropdown-header">Chủ xe</h6>
                                    <a class="dropdown-item" href="car-management">Xe của tôi</a>
                                    <a class="dropdown-item" href="brand?action=list">Danh sách hãng</a>
                                    <a class="dropdown-item" href="car-availability">Quản lý lịch xe</a>
                                    <a class="dropdown-item" href="maintenance">Lịch bảo trì</a>
                                    <a class="dropdown-item" href="booking?action=list">Đơn đặt xe</a>
                                    <a class="dropdown-item" href="review?action=pending">Đánh giá chờ duyệt</a>
                                </c:if>
                                
                                <c:if test="${sessionScope.user.roleId == 3}">
                                    <div class="dropdown-divider"></div>
                                    <h6 class="dropdown-header">Khách hàng</h6>
                                    <a class="dropdown-item" href="booking?action=list">Lịch sử đặt xe</a>
                                    <a class="dropdown-item" href="payment?action=list">Thanh toán</a>
                                    <a class="dropdown-item" href="review?action=myReviews">Đánh giá của tôi</a>
                                </c:if>
                                
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="notification">
                                    Thông báo 
                                    <c:if test="${not empty unreadCount && unreadCount > 0}">
                                        <span class="badge badge-danger">${unreadCount}</span>
                                    </c:if>
                                </a>
                                <a class="dropdown-item" href="profile">Hồ sơ</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" href="logout">Đăng xuất</a>
                            </div>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a href="login.jsp" class="nav-link">Đăng nhập</a></li>
                        <li class="nav-item"><a href="register.jsp" class="nav-link">Đăng ký</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
