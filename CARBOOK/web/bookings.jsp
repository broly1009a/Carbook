<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách đặt xe - CarBook</title>
 <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/open-iconic-bootstrap.min.css">
    <link rel="stylesheet" href="css/animate.css">
    <link rel="stylesheet" href="css/owl.carousel.min.css">
    <link rel="stylesheet" href="css/owl.theme.default.min.css">
    <link rel="stylesheet" href="css/magnific-popup.css">
    <link rel="stylesheet" href="css/aos.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/bootstrap-datepicker.css">
    <link rel="stylesheet" href="css/jquery.timepicker.css">
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/icomoon.css">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <h2 class="mb-4">
                <c:choose>
                    <c:when test="${sessionScope.user.roleId == 1}">Tất cả đơn đặt xe</c:when>
                    <c:when test="${sessionScope.user.roleId == 2}">Đơn đặt xe của tôi</c:when>
                    <c:otherwise>Lịch sử đặt xe</c:otherwise>
                </c:choose>
            </h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            
            <!-- Filters -->
            <div class="card mb-3">
                <div class="card-body">
                    <form action="booking" method="get" class="form-inline">
                        <input type="hidden" name="action" value="list">
                        
                        <div class="form-group mr-2">
                            <label class="mr-2">Trạng thái:</label>
                            <select name="status" class="form-control">
                                <option value="">Tất cả</option>
                                <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Chờ duyệt</option>
                                <option value="Approved" ${param.status == 'Approved' ? 'selected' : ''}>Đã duyệt</option>
                                <option value="Rejected" ${param.status == 'Rejected' ? 'selected' : ''}>Đã từ chối</option>
                                <option value="Cancelled" ${param.status == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                                <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                            </select>
                        </div>
                        
                        <div class="form-group mr-2">
                            <label class="mr-2">Từ ngày:</label>
                            <input type="date" name="fromDate" class="form-control" value="${param.fromDate}">
                        </div>
                        
                        <div class="form-group mr-2">
                            <label class="mr-2">Đến ngày:</label>
                            <input type="date" name="toDate" class="form-control" value="${param.toDate}">
                        </div>
                        
                        <button type="submit" class="btn btn-primary">Lọc</button>
                        <a href="booking?action=list" class="btn btn-secondary ml-2">Xóa lọc</a>
                    </form>
                </div>
            </div>
            
            <!-- Bookings table -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty bookings}">
                            <p class="text-center">Không có đơn đặt xe nào.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn</th>
                                            <th>Xe</th>
                                            <c:if test="${sessionScope.user.roleId == 1}">
                                                <th>Khách hàng</th>
                                                <th>Chủ xe</th>
                                            </c:if>
                                            <c:if test="${sessionScope.user.roleId == 2}">
                                                <th>Khách hàng</th>
                                            </c:if>
                                            <th>Ngày nhận</th>
                                            <th>Ngày trả</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="booking" items="${bookings}">
                                            <tr>
                                                <td>${booking.bookingReference}</td>
                                                <td>${booking.car.licensePlate}</td>
                                                <c:if test="${sessionScope.user.roleId == 1}">
                                                    <td>${booking.customer.fullName}</td>
                                                    <td>${booking.car.owner.fullName}</td>
                                                </c:if>
                                                <c:if test="${sessionScope.user.roleId == 2}">
                                                    <td>${booking.customer.fullName}</td>
                                                </c:if>
                                                <td><fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy"/></td>
                                                <td><fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy"/></td>
                                                <td><fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="₫"/></td>
                                                <td>
                                                    <span class="badge badge-${booking.status == 'Pending' ? 'warning' : booking.status == 'Approved' ? 'info' : booking.status == 'Completed' ? 'success' : 'secondary'}">
                                                        ${booking.status}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="booking?action=view&id=${booking.bookingId}" class="btn btn-sm btn-info">Chi tiết</a>
                                                    <c:if test="${booking.status == 'Pending' && (sessionScope.user.roleId == 1 || sessionScope.user.roleId == 2)}">
                                                        <a href="booking?action=approve&id=${booking.bookingId}" class="btn btn-sm btn-success" onclick="return confirm('Duyệt đơn này?')">Duyệt</a>
                                                    </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                            
                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <nav>
                                    <ul class="pagination justify-content-center">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="booking?action=list&page=${i}&status=${param.status}&fromDate=${param.fromDate}&toDate=${param.toDate}">${i}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </nav>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
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
