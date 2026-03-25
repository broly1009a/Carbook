<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử thanh toán - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <h2 class="mb-4">Lịch sử thanh toán</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            
            <!-- Filters -->
            <div class="card mb-3">
                <div class="card-body">
                    <form action="payment" method="get" class="form-inline">
                        <input type="hidden" name="action" value="list">
                        
                        <div class="form-group mr-2">
                            <label class="mr-2">Trạng thái:</label>
                            <select name="status" class="form-control">
                                <option value="">Tất cả</option>
                                <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Chờ xử lý</option>
                                <option value="Paid" ${param.status == 'Paid' ? 'selected' : ''}>Đã thanh toán</option>
                                <option value="Failed" ${param.status == 'Failed' ? 'selected' : ''}>Thất bại</option>
                                <option value="Refunded" ${param.status == 'Refunded' ? 'selected' : ''}>Đã hoàn tiền</option>
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
                        <a href="payment?action=list" class="btn btn-secondary ml-2">Xóa lọc</a>
                    </form>
                </div>
            </div>
            
            <!-- Payments table -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty payments}">
                            <p class="text-center">Không có giao dịch nào.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Mã GD</th>
                                            <th>Mã đơn</th>
                                            <c:if test="${sessionScope.user.roleId == 1}">
                                                <th>Khách hàng</th>
                                            </c:if>
                                            <th>Phương thức</th>
                                            <th>Ngày thanh toán</th>
                                            <th>Số tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="payment" items="${payments}">
                                            <tr>
                                                <td>${payment.transactionId}</td>
                                                <td>${payment.paymentReference}</td>
                                                <c:if test="${sessionScope.user.roleId == 1}">
                                                    <td>Booking #${payment.bookingId}</td>
                                                </c:if>
                                                <td>${payment.paymentMethod}</td>
                                                <td><fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td><fmt:formatNumber value="${payment.amount}" type="currency" currencySymbol="₫"/></td>
                                                <td>
                                                    <span class="badge badge-${payment.status == 'Paid' ? 'success' : payment.status == 'Pending' ? 'warning' : payment.status == 'Failed' ? 'danger' : 'info'}">
                                                        <c:choose>
                                                            <c:when test="${payment.status == 'Paid'}">Đã thanh toán</c:when>
                                                            <c:when test="${payment.status == 'Pending'}">Chờ xử lý</c:when>
                                                            <c:when test="${payment.status == 'Failed'}">Thất bại</c:when>
                                                            <c:when test="${payment.status == 'Refunded'}">Đã hoàn tiền</c:when>
                                                            <c:otherwise>${payment.status}</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="payment?action=view&id=${payment.paymentId}" class="btn btn-sm btn-info">Chi tiết</a>
                                                    <c:if test="${payment.status == 'Pending' && sessionScope.user.roleId == 1}">
                                                        <form action="payment" method="post" style="display:inline;">
                                                            <input type="hidden" name="action" value="confirm">
                                                            <input type="hidden" name="id" value="${payment.paymentId}">
                                                            <button type="submit" class="btn btn-sm btn-success" onclick="return confirm('Xác nhận thanh toán?')">Xác nhận</button>
                                                        </form>
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
                                                <a class="page-link" href="payment?action=list&page=${i}&status=${param.status}&fromDate=${param.fromDate}&toDate=${param.toDate}">${i}</a>
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
