<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách hóa đơn - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2>Danh sách hóa đơn</h2>
                <c:if test="${sessionScope.user.roleId == 1}">
                    <a href="invoice?action=create" class="btn btn-primary">
                        <i class="fas fa-plus"></i> Tạo hóa đơn mới
                    </a>
                </c:if>
            </div>
            
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">
                    ${sessionScope.error}
                    <c:remove var="error" scope="session"/>
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">
                    ${sessionScope.success}
                    <c:remove var="success" scope="session"/>
                </div>
            </c:if>
            
            <!-- Statistics Cards -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card bg-primary text-white">
                        <div class="card-body">
                            <h5>Tất cả</h5>
                            <h2>${invoices.size()}</h2>
                            <small>Tổng số hóa đơn</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-success text-white">
                        <div class="card-body">
                            <h5>Đã thanh toán</h5>
                            <h2>
                                <c:set var="paidCount" value="0"/>
                                <c:forEach var="inv" items="${invoices}">
                                    <c:if test="${inv.status == 'Paid'}">
                                        <c:set var="paidCount" value="${paidCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${paidCount}
                            </h2>
                            <small>Hóa đơn</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-warning text-white">
                        <div class="card-body">
                            <h5>Chưa thanh toán</h5>
                            <h2>
                                <c:set var="unpaidCount" value="0"/>
                                <c:forEach var="inv" items="${invoices}">
                                    <c:if test="${inv.status == 'Unpaid'}">
                                        <c:set var="unpaidCount" value="${unpaidCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${unpaidCount}
                            </h2>
                            <small>Hóa đơn</small>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card bg-danger text-white">
                        <div class="card-body">
                            <h5>Quá hạn</h5>
                            <h2>
                                <c:set var="overdueCount" value="0"/>
                                <c:forEach var="inv" items="${invoices}">
                                    <c:if test="${inv.status == 'Overdue'}">
                                        <c:set var="overdueCount" value="${overdueCount + 1}"/>
                                    </c:if>
                                </c:forEach>
                                ${overdueCount}
                            </h2>
                            <small>Hóa đơn</small>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Invoices Table -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty invoices}">
                            <div class="text-center py-5">
                                <i class="fas fa-file-invoice fa-4x text-muted mb-3"></i>
                                <p class="text-muted">Không có hóa đơn nào.</p>
                                <c:if test="${sessionScope.user.roleId == 1}">
                                    <a href="invoice?action=create" class="btn btn-primary mt-3">
                                        <i class="fas fa-plus"></i> Tạo hóa đơn đầu tiên
                                    </a>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Số hóa đơn</th>
                                            <th>Mã đặt xe</th>
                                            <c:if test="${sessionScope.user.roleId == 1}">
                                                <th>Khách hàng</th>
                                            </c:if>
                                            <th>Ngày tạo</th>
                                            <th>Hạn thanh toán</th>
                                            <th>Tổng tiền</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="invoice" items="${invoices}">
                                            <tr>
                                                <td>
                                                    <strong>${invoice.invoiceNumber}</strong>
                                                </td>
                                                <td>#${invoice.bookingId}</td>
                                                <c:if test="${sessionScope.user.roleId == 1}">
                                                    <td>
                                                        <!-- Customer name would need to be loaded via join or separate query -->
                                                        Khách hàng #${invoice.bookingId}
                                                    </td>
                                                </c:if>
                                                <td>
                                                    <fmt:formatDate value="${invoice.invoiceDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${invoice.dueDate}" pattern="dd/MM/yyyy"/>
                                                    <c:if test="${invoice.isOverdue}">
                                                        <br><small class="text-danger">Quá hạn</small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <strong>
                                                        <fmt:formatNumber value="${invoice.totalAmount}" type="number" groupingUsed="true"/> VNĐ
                                                    </strong>
                                                </td>
                                                <td>
                                                    <span class="badge badge-${invoice.statusBadgeClass}">
                                                        ${invoice.status}
                                                    </span>
                                                </td>
                                                <td>
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <a href="invoice?action=view&id=${invoice.invoiceId}" 
                                                           class="btn btn-info" 
                                                           title="Xem chi tiết">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                        <a href="invoice?action=print&id=${invoice.invoiceId}" 
                                                           class="btn btn-secondary" 
                                                           target="_blank"
                                                           title="In hóa đơn">
                                                            <i class="fas fa-print"></i>
                                                        </a>
                                                        <c:if test="${sessionScope.user.roleId == 1}">
                                                            <a href="invoice?action=delete&id=${invoice.invoiceId}" 
                                                               class="btn btn-danger" 
                                                               onclick="return confirm('Bạn có chắc muốn xóa hóa đơn này?')"
                                                               title="Xóa">
                                                                <i class="fas fa-trash"></i>
                                                            </a>
                                                        </c:if>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
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
