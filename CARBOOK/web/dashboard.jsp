<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dashboard - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <!--  nav -->
    
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
                        <div class="row mb-4">
                            <div class="col-md-12">
                                <h2>Dashboard
                                    <c:if test="${dashboardType == 'admin'}"> - Quản trị viên</c:if>
                                    <c:if test="${dashboardType == 'owner'}"> - Chủ xe</c:if>
                                    <c:if test="${dashboardType == 'customer'}"> - Khách hàng</c:if>
                                </h2>
                            </div>
                        </div>
            
            <!-- Admin Dashboard -->
            <c:if test="${dashboardType == 'admin'}">
                <div class="row">
                    <div class="col-md-3">
                        <div class="card text-white bg-primary mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Tổng số xe</h5>
                                <h2>${totalCars}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-success mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Xe khả dụng</h5>
                                <h2>${availableCars}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-warning mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Đặt xe chờ duyệt</h5>
                                <h2>${pendingBookings}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-3">
                        <div class="card text-white bg-info mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Tổng doanh thu</h5>
                                <h2><fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/></h2>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row mt-4">
                    <div class="col-md-12">
                        <h4>Đặt xe gần đây</h4>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Mã đặt xe</th>
                                    <th>Khách hàng</th>
                                    <th>Ngày nhận</th>
                                    <th>Trạng thái</th>
                                    <th>Tổng tiền</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${recentBookings}" var="booking">
                                    <tr>
                                        <td>${booking.bookingReference}</td>
                                        <td>Customer #${booking.customerId}</td>
                                        <td><fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                        <td>
                                            <span class="badge badge-${booking.status == 'Pending' ? 'warning' : booking.status == 'Approved' ? 'success' : 'secondary'}">
                                                ${booking.status}
                                            </span>
                                        </td>
                                        <td><fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="₫"/></td>
                                        <td>
                                            <a href="booking?action=view&id=${booking.bookingId}" class="btn btn-sm btn-primary">Xem</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>
            
            <!-- Car Owner Dashboard -->
            <c:if test="${dashboardType == 'owner'}">
                <div class="row">
                    <div class="col-md-4">
                        <div class="card text-white bg-primary mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Xe của tôi</h5>
                                <h2>${totalCars}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card text-white bg-success mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Xe khả dụng</h5>
                                <h2>${availableCars}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card text-white bg-warning mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Đặt xe chờ duyệt</h5>
                                <h2>${pendingBookings}</h2>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row mt-4">
                    <div class="col-md-12">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4>Danh sách xe của tôi</h4>
                            <a href="car-management?action=add" class="btn btn-primary">Thêm xe mới</a>
                        </div>
                        <div class="row">
                            <c:forEach items="${ownerCars}" var="car">
                                <div class="col-md-4">
                                    <div class="card mb-3">
                                        <div class="card-body">
                                            <h5 class="card-title">${car.licensePlate}</h5>
                                            <p class="card-text">
                                                Giá: <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencySymbol="₫"/>/ngày<br>
                                                Trạng thái: <span class="badge badge-${car.status == 'Available' ? 'success' : 'warning'}">${car.status}</span>
                                            </p>
                                            <a href="car-management?action=edit&id=${car.carId}" class="btn btn-sm btn-primary">Chỉnh sửa</a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
            
            <!-- Customer Dashboard -->
            <c:if test="${dashboardType == 'customer'}">
                <div class="row">
                    <div class="col-md-4">
                        <div class="card text-white bg-primary mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Tổng số lượt đặt</h5>
                                <h2>${totalBookings}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card text-white bg-warning mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Đang hoạt động</h5>
                                <h2>${activeBookings}</h2>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card text-white bg-success mb-3">
                            <div class="card-body">
                                <h5 class="card-title">Đã hoàn thành</h5>
                                <h2>${completedBookings}</h2>
                            </div>
                        </div>
                    </div>
                </div>
                
                <div class="row mt-4">
                    <div class="col-md-12">
                        <h4>Lịch sử đặt xe</h4>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Mã đặt xe</th>
                                    <th>Ngày nhận</th>
                                    <th>Ngày trả</th>
                                    <th>Trạng thái</th>
                                    <th>Tổng tiền</th>
                                    <th>Thao tác</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${customerBookings}" var="booking">
                                    <tr>
                                        <td>${booking.bookingReference}</td>
                                        <td><fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy"/></td>
                                        <td><fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <span class="badge badge-${booking.status == 'Pending' ? 'warning' : booking.status == 'Approved' ? 'info' : booking.status == 'Completed' ? 'success' : 'secondary'}">
                                                ${booking.status}
                                            </span>
                                        </td>
                                        <td><fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="₫"/></td>
                                        <td>
                                            <a href="booking?action=view&id=${booking.bookingId}" class="btn btn-sm btn-primary">Xem</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <div class="row mt-4">
                    <div class="col-md-12">
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <h4>Xe khả dụng</h4>
                            <a href="cars" class="btn btn-primary">Xem tất cả</a>
                        </div>
                        <div class="row">
                            <c:forEach items="${availableCars}" var="car">
                                <div class="col-md-4">
                                    <div class="card mb-3">
                                        <div class="card-body">
                                            <h5 class="card-title">${car.licensePlate}</h5>
                                            <p class="card-text">
                                                Giá: <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencySymbol="₫"/>/ngày
                                            </p>
                                            <a href="booking?action=create&carId=${car.carId}" class="btn btn-primary btn-sm">Đặt xe</a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <%@ include file="includes/footer.jsp" %>
    
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
    <script src="js/main.js"></script>
</body>
</html>
