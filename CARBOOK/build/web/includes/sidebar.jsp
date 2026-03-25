<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<style>
    .sidebar {
        min-height: calc(100vh - 200px);
        background: #f8f9fa;
        border-right: 1px solid #dee2e6;
        padding: 0;
    }
    
    .sidebar-header {
        padding: 20px;
        background: #343a40;
        color: white;
    }
    
    .sidebar-header h4 {
        margin: 0;
        font-size: 18px;
    }
    
    .sidebar-menu {
        list-style: none;
        padding: 0;
        margin: 0;
    }
    
    .sidebar-menu .menu-section {
        padding: 15px 20px 5px 20px;
        font-size: 12px;
        font-weight: 600;
        text-transform: uppercase;
        color: #6c757d;
    }
    
    .sidebar-menu li a {
        display: block;
        padding: 12px 20px;
        color: #495057;
        text-decoration: none;
        transition: all 0.3s;
        border-left: 3px solid transparent;
    }
    
    .sidebar-menu li a:hover {
        background: #e9ecef;
        border-left-color: #01d28e;
        color: #01d28e;
    }
    
    .sidebar-menu li a.active {
        background: #e9ecef;
        border-left-color: #01d28e;
        color: #01d28e;
        font-weight: 600;
    }
    
    .sidebar-menu li a i {
        margin-right: 10px;
        width: 20px;
        text-align: center;
    }
    
    .sidebar-menu .badge {
        float: right;
        margin-top: 2px;
    }
    
    @media (max-width: 768px) {
        .sidebar {
            margin-bottom: 20px;
        }
    }
</style>

<div class="sidebar">
    <div class="sidebar-header">
        <h4>
            <c:choose>
                <c:when test="${sessionScope.user.roleId == 1}">
                    <i class="fa fa-cog"></i> Quản trị
                </c:when>
                <c:when test="${sessionScope.user.roleId == 2}">
                    <i class="fa fa-car"></i> Chủ xe
                </c:when>
                <c:otherwise>
                    <i class="fa fa-user"></i> Khách hàng
                </c:otherwise>
            </c:choose>
        </h4>
    </div>
    
    <ul class="sidebar-menu">
        <!-- Dashboard Link (All Roles) -->
        <li>
            <a href="dashboard" class="${pageContext.request.requestURI.contains('dashboard.jsp') ? 'active' : ''}">
                <i class="fa fa-tachometer"></i> Dashboard
            </a>
        </li>
        
        <!-- Admin Menu -->
        <c:if test="${sessionScope.user.roleId == 1}">
            <li class="menu-section">Quản lý hệ thống</li>
            <li>
                <a href="role-management" class="${pageContext.request.requestURI.contains('role-management') ? 'active' : ''}">
                    <i class="fa fa-users"></i> Vai trò người dùng
                </a>
            </li>
            <li>
                <a href="car-management" class="${pageContext.request.requestURI.contains('car-management') || pageContext.request.requestURI.contains('car-form') ? 'active' : ''}">
                    <i class="fa fa-car"></i> Quản lý xe
                </a>
            </li>
            <li>
                <a href="car-availability" class="${pageContext.request.requestURI.contains('car-availability') ? 'active' : ''}">
                    <i class="fa fa-calendar-times-o"></i> Lịch không khả dụng
                </a>
            </li>
            <li>
                <a href="maintenance" class="${pageContext.request.requestURI.contains('maintenance') ? 'active' : ''}">
                    <i class="fa fa-wrench"></i> Quản lý bảo trì
                </a>
            </li>
            
            <li class="menu-section">Đặt xe & Thanh toán</li>
            <li>
                <a href="booking?action=list" class="${pageContext.request.requestURI.contains('booking') ? 'active' : ''}">
                    <i class="fa fa-book"></i> Tất cả booking
                    <c:if test="${not empty pendingBookings && pendingBookings > 0}">
                        <span class="badge badge-warning">${pendingBookings}</span>
                    </c:if>
                </a>
            </li>
        
            <li>
                <a href="payment?action=list" class="${pageContext.request.requestURI.contains('payment') ? 'active' : ''}">
                    <i class="fa fa-money"></i> Tất cả thanh toán
                </a>
            </li>
            
            <li class="menu-section">Nội dung</li>
            <li>
                <a href="blog-management" class="${pageContext.request.requestURI.contains('blog-management') || pageContext.request.requestURI.contains('blog-list') || pageContext.request.requestURI.contains('blog-form') ? 'active' : ''}">
                    <i class="fa fa-book"></i> Quản lý Blog
                </a>
            </li>
            <li>
                <a href="service-management" class="${pageContext.request.requestURI.contains('service-management') || pageContext.request.requestURI.contains('service-list') || pageContext.request.requestURI.contains('service-form') ? 'active' : ''}">
                    <i class="fa fa-briefcase"></i> Quản lý Dịch vụ
                </a>
            </li>
            <li>
                <a href="review?action=list" class="${pageContext.request.requestURI.contains('review') ? 'active' : ''}">
                    <i class="fa fa-star"></i> Quản lý đánh giá
                </a>
            </li>
              <li class="menu-section">Liên Hệ</li>
                <li>
                <a href="contact?action=list" class="${pageContext.request.requestURI.contains('contact') ? 'active' : ''}">
                    <i class="fa fa-star"></i> Quản lý liên hệ
                </a>
            </li>
        </c:if>
        
        <!-- Car Owner Menu -->
        <c:if test="${sessionScope.user.roleId == 2}">
            <li class="menu-section">Quản lý xe của tôi</li>
            <li>
                <a href="car-management" class="${pageContext.request.requestURI.contains('car-management') || pageContext.request.requestURI.contains('car-form') ? 'active' : ''}">
                    <i class="fa fa-car"></i> Xe của tôi
                </a>
            </li>
            <li>
                <a href="car-availability" class="${pageContext.request.requestURI.contains('car-availability') ? 'active' : ''}">
                    <i class="fa fa-calendar-times-o"></i> Quản lý lịch xe
                </a>
            </li>
            <li>
                <a href="maintenance" class="${pageContext.request.requestURI.contains('maintenance') ? 'active' : ''}">
                    <i class="fa fa-wrench"></i> Lịch bảo trì
                </a>
            </li>
            
            <li class="menu-section">Đơn đặt xe</li>
            <li>
                <a href="booking?action=list" class="${pageContext.request.requestURI.contains('booking') ? 'active' : ''}">
                    <i class="fa fa-book"></i> Đơn đặt xe
                    <c:if test="${not empty pendingBookings && pendingBookings > 0}">
                        <span class="badge badge-warning">${pendingBookings}</span>
                    </c:if>
                </a>
            </li>
            <li>
                <a href="review?action=pending" class="${pageContext.request.requestURI.contains('review') ? 'active' : ''}">
                    <i class="fa fa-star"></i> Đánh giá chờ duyệt
                </a>
            </li>
        </c:if>
        
        <!-- Customer Menu -->
        <c:if test="${sessionScope.user.roleId == 3}">
            <li class="menu-section">Hoạt động của tôi</li>
            <li>
                <a href="booking?action=list" class="${pageContext.request.requestURI.contains('booking') ? 'active' : ''}">
                    <i class="fa fa-book"></i> Lịch sử đặt xe
                </a>
            </li>
            <li>
                <a href="payment?action=list" class="${pageContext.request.requestURI.contains('payment') ? 'active' : ''}">
                    <i class="fa fa-money"></i> Thanh toán
                </a>
            </li>
            <li>
                <a href="review?action=myReviews" class="${pageContext.request.requestURI.contains('review') ? 'active' : ''}">
                    <i class="fa fa-star"></i> Đánh giá của tôi
                </a>
            </li>
        </c:if>
        
        <!-- Common Menu -->
        <li class="menu-section">Cá nhân</li>
        <li>
            <a href="notification" class="${pageContext.request.requestURI.contains('notification') ? 'active' : ''}">
                <i class="fa fa-bell"></i> Thông báo
                <c:if test="${not empty unreadCount && unreadCount > 0}">
                    <span class="badge badge-danger">${unreadCount}</span>
                </c:if>
            </a>
        </li>
        <li>
            <a href="profile" class="${pageContext.request.requestURI.contains('profile') ? 'active' : ''}">
                <i class="fa fa-user"></i> Hồ sơ
            </a>
        </li>
        <li>
            <a href="change-password.jsp" class="${pageContext.request.requestURI.contains('change-password') ? 'active' : ''}">
                <i class="fa fa-lock"></i> Đổi mật khẩu
            </a>
        </li>
        <li>
            <a href="logout">
                <i class="fa fa-sign-out"></i> Đăng xuất
            </a>
        </li>
    </ul>
</div>
