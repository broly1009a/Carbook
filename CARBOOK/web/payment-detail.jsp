<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết thanh toán - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <h2>Chi tiết thanh toán</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    ${success}
                    <c:if test="${not empty sessionScope.invoiceId}">
                        <br>
                        <a href="invoice?action=view&id=${sessionScope.invoiceId}" class="btn btn-sm btn-success mt-2">
                            <i class="fas fa-file-invoice"></i> Xem hóa đơn
                        </a>
                        <c:remove var="invoiceId" scope="session"/>
                    </c:if>
                </div>
            </c:if>
            
            <div class="row">
                <div class="col-md-8">
                    <div class="card mb-3">
                        <div class="card-header d-flex justify-content-between">
                            <h5>Mã thanh toán: ${payment.transactionId}</h5>
                            <span class="badge badge-${payment.status == 'Paid' ? 'success' : payment.status == 'Pending' ? 'warning' : payment.status == 'Failed' ? 'danger' : 'secondary'} badge-lg">
                                <c:choose>
                                    <c:when test="${payment.status == 'Paid'}">Đã thanh toán</c:when>
                                    <c:when test="${payment.status == 'Pending'}">Chờ xử lý</c:when>
                                    <c:when test="${payment.status == 'Failed'}">Thất bại</c:when>
                                    <c:when test="${payment.status == 'Refunded'}">Đã hoàn tiền</c:when>
                                    <c:otherwise>${payment.status}</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div class="card-body">
                            <h6>Thông tin đơn đặt xe</h6>
                            <p>
                                <strong>Mã đơn:</strong> ${booking.bookingReference}<br>
                                <strong>Xe:</strong> ${booking.car.licensePlate}<br>
                                <strong>Ngày nhận:</strong> <fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy HH:mm"/><br>
                                <strong>Ngày trả:</strong> <fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </p>
                            
                            <hr>
                            
                            <h6>Thông tin thanh toán</h6>
                            <p>
                                <strong>Phương thức:</strong> ${payment.paymentMethod}<br>
                                <strong>Ngày thanh toán:</strong> <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/><br>
                                <strong>Số tiền:</strong> <fmt:formatNumber value="${payment.amount}" type="currency" currencySymbol="₫"/>
                            </p>
                            
                            <c:if test="${not empty payment.notes}">
                                <hr>
                                <h6>Ghi chú</h6>
                                <p>${payment.notes}</p>
                            </c:if>
                        </div>
                    </div>
                    
                    <c:if test="${payment.status == 'Pending' && (sessionScope.user.roleId == 1 || payment.booking.car.ownerId == sessionScope.user.userId)}">
                        <div class="card">
                            <div class="card-body">
                                <a href="payment?action=confirm&id=${payment.paymentId}" class="btn btn-success" onclick="return confirm('Xác nhận thanh toán đã hoàn thành?')">Xác nhận</a>
                                <button class="btn btn-danger" data-toggle="modal" data-target="#failModal">Đánh dấu thất bại</button>
                                <a href="payment" class="btn btn-secondary">Quay lại</a>
                            </div>
                        </div>
                    </c:if>
                </div>
                
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Tóm tắt</h5>
                        </div>
                        <div class="card-body">
                            <p>
                                <strong>Giá cơ bản:</strong> <fmt:formatNumber value="${booking.basePrice}" type="currency" currencySymbol="₫"/><br>
                                <strong>Thuế:</strong> <fmt:formatNumber value="${booking.taxAmount}" type="currency" currencySymbol="₫"/><br>
                                <c:if test="${booking.discountAmount > 0}">
                                    <strong>Giảm giá:</strong> -<fmt:formatNumber value="${booking.discountAmount}" type="currency" currencySymbol="₫"/><br>
                                </c:if>
                            </p>
                            <hr>
                            <h4><strong>Tổng cộng:</strong> <fmt:formatNumber value="${payment.amount}" type="currency" currencySymbol="₫"/></h4>
                        </div>
                    </div>
                    
                    <div class="card mt-3">
                        <div class="card-header">
                            <h5>Hóa đơn</h5>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty invoice}">
                                    <p>
                                        <strong>Số hóa đơn:</strong> ${invoice.invoiceNumber}<br>
                                        <strong>Trạng thái:</strong> 
                                        <span class="badge badge-${invoice.statusBadgeClass}">${invoice.status}</span>
                                    </p>
                                    <a href="invoice?action=view&id=${invoice.invoiceId}" class="btn btn-primary btn-sm btn-block">
                                        <i class="fas fa-file-invoice"></i> Xem hóa đơn
                                    </a>
                                    <a href="invoice?action=print&id=${invoice.invoiceId}" class="btn btn-secondary btn-sm btn-block" target="_blank">
                                        <i class="fas fa-print"></i> In hóa đơn
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted">
                                        <small>Hóa đơn chưa được tạo</small>
                                    </p>
                                    <c:if test="${sessionScope.user.roleId == 1}">
                                        <a href="invoice?action=create&bookingId=${booking.bookingId}&paymentId=${payment.paymentId}" 
                                           class="btn btn-success btn-sm btn-block">
                                            <i class="fas fa-plus"></i> Tạo hóa đơn
                                        </a>
                                    </c:if>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    
                    <div class="card mt-3">
                        <div class="card-header">
                            <h5>Lịch sử</h5>
                        </div>
                        <div class="card-body">
                            <p><small>
                                <strong>Ngày tạo:</strong> <fmt:formatDate value="${payment.createdAt}" pattern="dd/MM/yyyy HH:mm"/><br>
                                <c:if test="${not empty payment.updatedAt}">
                                    <strong>Cập nhật:</strong> <fmt:formatDate value="${payment.updatedAt}" pattern="dd/MM/yyyy HH:mm"/><br>
                                </c:if>
                                <c:if test="${not empty payment.paymentDate}">
                                    <strong>Thanh toán:</strong> <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/><br>
                                </c:if>
                            </small></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Fail Modal -->
    <div class="modal fade" id="failModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form action="payment" method="get">
                    <input type="hidden" name="action" value="fail">
                    <input type="hidden" name="id" value="${payment.paymentId}">
                    <div class="modal-header">
                        <h5 class="modal-title">Đánh dấu thất bại</h5>
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Lý do thất bại:</label>
                            <textarea class="form-control" name="reason" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger">Xác nhận</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
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
