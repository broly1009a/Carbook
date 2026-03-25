<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <h2>Thanh toán</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <div class="row">
                <div class="col-md-8">
                    <div class="card mb-3">
                        <div class="card-header">
                            <h5>Thông tin đặt xe</h5>
                        </div>
                        <div class="card-body">
                            <p><strong>Mã đơn:</strong> ${booking.bookingReference}</p>
                            <p><strong>Xe:</strong> ${booking.car.licensePlate}</p>
                            <p><strong>Ngày nhận:</strong> <fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                            <p><strong>Ngày trả:</strong> <fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy HH:mm"/></p>
                        </div>
                    </div>
                    
                    <form action="payment" method="post">
                        <input type="hidden" name="action" value="process">
                        <input type="hidden" name="bookingId" value="${booking.bookingId}">
                        
                        <div class="card mb-3">
                            <div class="card-header">
                                <h5>Phương thức thanh toán</h5>
                            </div>
                            <div class="card-body">
                                <c:forEach var="method" items="${paymentMethods}">
                                    <div class="form-check mb-3">
                                        <input class="form-check-input" type="radio" name="paymentMethodId" id="method${method.paymentMethodId}" value="${method.paymentMethodId}" required>
                                        <label class="form-check-label" for="method${method.paymentMethodId}">
                                            <strong>${method.methodName}</strong>
                                            <c:if test="${not empty method.description}">
                                                <br><small class="text-muted">${method.description}</small>
                                            </c:if>
                                        </label>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <div class="card mb-3">
                            <div class="card-header">
                                <h5>Ghi chú (tùy chọn)</h5>
                            </div>
                            <div class="card-body">
                                <textarea class="form-control" name="notes" rows="3" placeholder="Ghi chú về thanh toán..."></textarea>
                            </div>
                        </div>
                        
                        <button type="submit" class="btn btn-primary btn-lg btn-block">Xác nhận thanh toán</button>
                        <a href="booking?action=detail&id=${booking.bookingId}" class="btn btn-secondary btn-lg btn-block">Quay lại</a>
                    </form>
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
                            <h5>Chính sách</h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-unstyled">
                                <li>✓ Thanh toán an toàn</li>
                                <li>✓ Hoàn tiền trong 24h</li>
                                <li>✓ Hỗ trợ 24/7</li>
                            </ul>
                        </div>
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
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBVWaKrjvy3MaE7SQ74_uJiULgl1JY0H2s&sensor=false"></script>
  <script src="js/google-map.js"></script>
  <script src="js/main.js"></script>
</body>
</html>
