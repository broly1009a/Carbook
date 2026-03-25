<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết đặt xe - CarBook</title>
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
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            
            <div class="row">
                <div class="col-md-8">
                    <h2>Chi tiết đặt xe</h2>
                    
                    <div class="card mb-3">
                        <div class="card-header d-flex justify-content-between">
                            <h5>Mã đặt xe: ${booking.bookingReference}</h5>
                            <span class="badge badge-${booking.status == 'Pending' ? 'warning' : booking.status == 'Approved' ? 'info' : booking.status == 'Completed' ? 'success' : 'secondary'} badge-lg">
                                ${booking.status}
                            </span>
                        </div>
                        <div class="card-body">
                            <h6>Thông tin xe</h6>
                            <p>
                                <strong>Biển số:</strong> ${car.licensePlate}<br>
                                <strong>Màu sắc:</strong> ${car.color}<br>
                                <strong>Số chỗ:</strong> ${car.seats}<br>
                                <strong>Hộp số:</strong> ${car.transmission}
                            </p>
                            
                            <hr>
                            
                            <h6>Thông tin thuê</h6>
                            <p>
                                <strong>Ngày nhận:</strong> <fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy HH:mm"/><br>
                                <strong>Ngày trả:</strong> <fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy HH:mm"/><br>
                                <strong>Số ngày:</strong> ${booking.totalDays} ngày<br>
                                <c:if test="${not empty booking.pickupLocation}">
                                    <strong>Địa điểm nhận:</strong> ${booking.pickupLocation}<br>
                                </c:if>
                                <c:if test="${not empty booking.returnLocation}">
                                    <strong>Địa điểm trả:</strong> ${booking.returnLocation}<br>
                                </c:if>
                            </p>
                            
                            <c:if test="${not empty booking.notes}">
                                <hr>
                                <h6>Ghi chú</h6>
                                <p>${booking.notes}</p>
                            </c:if>
                        </div>
                    </div>
                    
                    <!-- Action buttons based on status and role -->
                    <div class="card">
                        <div class="card-body">
                            <c:if test="${booking.status == 'Pending'}">
                                <c:if test="${sessionScope.user.roleId == 1 || sessionScope.user.roleId == 2}">
                                    <a href="booking?action=approve&id=${booking.bookingId}" class="btn btn-success" onclick="return confirm('Xác nhận duyệt đặt xe này?')">Duyệt</a>
                                    <button class="btn btn-danger" data-toggle="modal" data-target="#rejectModal">Từ chối</button>
                                </c:if>
                                <c:if test="${booking.customerId == sessionScope.user.userId || sessionScope.user.roleId == 1}">
                                    <button class="btn btn-warning" data-toggle="modal" data-target="#cancelModal">Hủy đặt xe</button>
                                </c:if>
                            </c:if>
                            
                            <c:if test="${booking.status == 'Approved'}">
                                <!-- VNPay Payment Button -->
                                <form action="vnpay-payment" method="post" style="display: inline;">
                                    <input type="hidden" name="bookingId" value="${booking.bookingId}">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="ion-ios-card"></i> Thanh toán VNPay
                                    </button>
                                </form>
                                <a href="payment?action=create&bookingId=${booking.bookingId}" class="btn btn-info">
                                    <i class="ion-ios-cash"></i> Thanh toán khác
                                </a>
                                <c:if test="${sessionScope.user.roleId == 1 || sessionScope.user.roleId == 2}">
                                    <a href="booking?action=complete&id=${booking.bookingId}" class="btn btn-success" onclick="return confirm('Xác nhận hoàn thành?')">Hoàn thành</a>
                                </c:if>
                            </c:if>
                            
                            <c:if test="${booking.status == 'Completed' && booking.customerId == sessionScope.user.userId}">
                                <a href="review?action=create&bookingId=${booking.bookingId}" class="btn btn-info">Đánh giá</a>
                            </c:if>
                            
                            <a href="booking" class="btn btn-secondary">Quay lại</a>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Tóm tắt thanh toán</h5>
                        </div>
                        <div class="card-body">
                            <p>
                                <strong>Giá cơ bản:</strong> <fmt:formatNumber value="${booking.basePrice}" type="currency" currencySymbol="₫"/><br>
                                <strong>Thuế (10%):</strong> <fmt:formatNumber value="${booking.taxAmount}" type="currency" currencySymbol="₫"/><br>
                                <c:if test="${booking.discountAmount > 0}">
                                    <strong>Giảm giá:</strong> -<fmt:formatNumber value="${booking.discountAmount}" type="currency" currencySymbol="₫"/><br>
                                </c:if>
                            </p>
                            <hr>
                            <h4><strong>Tổng cộng:</strong> <fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="₫"/></h4>
                        </div>
                    </div>
                    
                    <div class="card mt-3">
                        <div class="card-header">
                            <h5>Thông tin thêm</h5>
                        </div>
                        <div class="card-body">
                            <p><small>
                                <strong>Ngày tạo:</strong> <fmt:formatDate value="${booking.createdAt}" pattern="dd/MM/yyyy HH:mm"/><br>
                                <c:if test="${not empty booking.approvedAt}">
                                    <strong>Ngày duyệt:</strong> <fmt:formatDate value="${booking.approvedAt}" pattern="dd/MM/yyyy HH:mm"/><br>
                                </c:if>
                                <c:if test="${not empty booking.cancelledAt}">
                                    <strong>Ngày hủy:</strong> <fmt:formatDate value="${booking.cancelledAt}" pattern="dd/MM/yyyy HH:mm"/><br>
                                    <strong>Lý do hủy:</strong> ${booking.cancellationReason}<br>
                                </c:if>
                                <c:if test="${not empty booking.rejectionReason}">
                                    <strong>Lý do từ chối:</strong> ${booking.rejectionReason}<br>
                                </c:if>
                            </small></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Reject Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form action="booking" method="get">
                    <input type="hidden" name="action" value="reject">
                    <input type="hidden" name="id" value="${booking.bookingId}">
                    <div class="modal-header">
                        <h5 class="modal-title">Từ chối đặt xe</h5>
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Lý do từ chối:</label>
                            <textarea class="form-control" name="reason" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                        <button type="submit" class="btn btn-danger">Từ chối</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    
    <!-- Cancel Modal -->
    <div class="modal fade" id="cancelModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <form action="booking" method="get">
                    <input type="hidden" name="action" value="cancel">
                    <input type="hidden" name="id" value="${booking.bookingId}">
                    <div class="modal-header">
                        <h5 class="modal-title">Hủy đặt xe</h5>
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="form-group">
                            <label>Lý do hủy:</label>
                            <textarea class="form-control" name="reason" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-warning">Hủy đặt xe</button>
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
