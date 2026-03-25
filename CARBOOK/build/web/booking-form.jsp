<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt xe - CarBook</title>
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
            <div class="row">
                <div class="col-md-12">
                    <h2>Đặt xe</h2>
                    
                    <c:if test="${empty car}">
                        <div class="alert alert-danger">
                            <h4>Lỗi!</h4>
                            <p>Không thể tải thông tin xe. Vui lòng thử lại.</p>
                            <a href="cars" class="btn btn-primary">Quay lại danh sách xe</a>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty car}">
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">${error}</div>
                    </c:if>
                    <c:if test="${not empty sessionScope.success}">
                        <div class="alert alert-success">${sessionScope.success}</div>
                        <c:remove var="success" scope="session"/>
                    </c:if>
                    
                    <div class="row">
                        <div class="col-md-8">
                            <form action="booking" method="post">
                                <input type="hidden" name="action" value="create">
                                <input type="hidden" name="carId" value="${car.carId}">
                                
                                <div class="card mb-3">
                                    <div class="card-header">
                                        <h5>Thông tin xe</h5>
                                    </div>
                                    <div class="card-body">
                                        <p><strong>Biển số:</strong> ${car.licensePlate}</p>
                                        <p><strong>Màu sắc:</strong> ${car.color}</p>
                                        <p><strong>Số chỗ:</strong> ${car.seats}</p>
                                        <p><strong>Nhiên liệu:</strong> ${car.fuelType}</p>
                                        <p><strong>Hộp số:</strong> ${car.transmission}</p>
                                        <c:if test="${not empty car.model && not empty car.model.brand}">
                                            <p><strong>Xe:</strong> ${car.model.brand.brandName} ${car.model.modelName}</p>
                                        </c:if>
                                    </div>
                                </div>
                                
                                <div class="card mb-3">
                                    <div class="card-header">
                                        <h5>Thời gian thuê</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-md-8">
                                                <div class="form-group">
                                                    <label>Ngày nhận xe <span class="text-danger">*</span></label>
                                                    <input type="text" class="form-control" name="pickupDateOnly" id="pickup_date_only" placeholder="dd/mm/yyyy" required autocomplete="off">
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label>Giờ nhận <span class="text-danger">*</span></label>
                                                    <input type="text" class="form-control" name="pickupTimeOnly" id="pickup_time_only" placeholder="HH:MM" required autocomplete="off">
                                                </div>
                                            </div>
                                        </div>
                                        <input type="hidden" name="pickupDate" id="pickup_date_full">
                                        
                                        <div class="row">
                                            <div class="col-md-8">
                                                <div class="form-group">
                                                    <label>Ngày trả xe <span class="text-danger">*</span></label>
                                                    <input type="text" class="form-control" name="returnDateOnly" id="return_date_only" placeholder="dd/mm/yyyy" required autocomplete="off">
                                                </div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-group">
                                                    <label>Giờ trả <span class="text-danger">*</span></label>
                                                    <input type="text" class="form-control" name="returnTimeOnly" id="return_time_only" placeholder="HH:MM" required autocomplete="off">
                                                </div>
                                            </div>
                                        </div>
                                        <input type="hidden" name="returnDate" id="return_date_full">
                                        
                                        <div class="form-group">
                                            <label>Địa điểm nhận xe</label>
                                            <input type="text" class="form-control" name="pickupLocation" id="pickup_location" placeholder="Nhập địa điểm nhận xe" value="${pickupLocation}">
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Địa điểm trả xe</label>
                                            <input type="text" class="form-control" name="returnLocation" id="return_location" placeholder="Nhập địa điểm trả xe" value="${dropoffLocation}">
                                        </div>
                                        
                                        <div class="form-group">
                                            <label>Ghi chú</label>
                                            <textarea class="form-control" name="notes" rows="3" placeholder="Ghi chú thêm (nếu có)"></textarea>
                                        </div>
                                    </div>
                                </div>
                                
                                <button type="submit" class="btn btn-primary btn-lg btn-block">Xác nhận đặt xe</button>
                                <a href="cars" class="btn btn-secondary btn-lg btn-block">Hủy</a>
                            </form>
                        </div>
                        
                        <div class="col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5>Thông tin giá</h5>
                                </div>
                                <div class="card-body">
                                    <p><strong>Giá thuê:</strong> 
                                        <c:choose>
                                            <c:when test="${not empty car.pricePerDay}">
                                                <fmt:formatNumber value="${car.pricePerDay}" type="currency" currencySymbol="₫"/>/ngày
                                            </c:when>
                                            <c:otherwise>
                                                Liên hệ
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                    <p><small class="text-muted">* Thuế 10% sẽ được tính vào tổng tiền</small></p>
                                    <p><small class="text-muted">* Tổng tiền sẽ được tính sau khi chọn ngày</small></p>
                                </div>
                            </div>
                            
                            <div class="card mt-3">
                                <div class="card-header">
                                    <h5>Chính sách</h5>
                                </div>
                                <div class="card-body">
                                    <ul class="list-unstyled">
                                        <li>✓ Hủy miễn phí trước 24h</li>
                                        <li>✓ Bảo hiểm toàn diện</li>
                                        <li>✓ Hỗ trợ 24/7</li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    </c:if>
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
    
    <script>
      $(document).ready(function(){
        // Khởi tạo datepicker cho ngày
        $('#pickup_date_only, #return_date_only').datepicker({
          format: 'dd/mm/yyyy',
          autoclose: true,
          todayHighlight: true,
          startDate: new Date()
        });
        
        // Khởi tạo timepicker cho giờ
        $('#pickup_time_only, #return_time_only').timepicker({
          timeFormat: 'H:i',
          interval: 30,
          minTime: '06:00',
          maxTime: '22:00',
          defaultTime: '09:00',
          startTime: '06:00',
          dynamic: false,
          dropdown: true,
          scrollbar: true,
          show2400: false
        });
        
        // Auto-fill từ params
        var pickupDate = '${pickupDate}';
        var dropoffDate = '${dropoffDate}';
        var pickupTime = '${pickupTime}';
        var pickupLocation = '${pickupLocation}';
        var dropoffLocation = '${dropoffLocation}';
        
        if (pickupDate) {
          $('#pickup_date_only').val(pickupDate);
        }
        if (dropoffDate) {
          $('#return_date_only').val(dropoffDate);
        }
        if (pickupTime) {
          $('#pickup_time_only').val(pickupTime);
          $('#return_time_only').val(pickupTime); // Mặc định giờ trả = giờ nhận
        }
        if (pickupLocation) {
          $('#pickup_location').val(pickupLocation);
        }
        if (dropoffLocation) {
          $('#return_location').val(dropoffLocation);
        }
        
        // Validation và combine trước khi submit
        $('form').on('submit', function(e) {
          var pickupDateVal = $('#pickup_date_only').val();
          var pickupTimeVal = $('#pickup_time_only').val();
          var returnDateVal = $('#return_date_only').val();
          var returnTimeVal = $('#return_time_only').val();
          
          if (!pickupDateVal || !pickupTimeVal || !returnDateVal || !returnTimeVal) {
            alert('Vui lòng chọn đầy đủ ngày giờ nhận và trả xe');
            e.preventDefault();
            return false;
          }
          
          // Chuyển đổi mm/dd/yyyy + HH:mm sang yyyy-MM-dd HH:mm:ss
          // Bootstrap datepicker trả về mm/dd/yyyy theo US locale
          var pickupParts = pickupDateVal.split('/');
          var returnParts = returnDateVal.split('/');
          
          // pickupParts[0] = month, pickupParts[1] = day, pickupParts[2] = year
          var pickupMonth = pickupParts[0].padStart(2, '0');
          var pickupDay = pickupParts[1].padStart(2, '0');
          var pickupYear = pickupParts[2];
          
          var returnMonth = returnParts[0].padStart(2, '0');
          var returnDay = returnParts[1].padStart(2, '0');
          var returnYear = returnParts[2];
          
          // Format: yyyy-MM-dd HH:mm:ss
          var pickupFormatted = pickupYear + '-' + pickupMonth + '-' + pickupDay + ' ' + pickupTimeVal + ':00';
          var returnFormatted = returnYear + '-' + returnMonth + '-' + returnDay + ' ' + returnTimeVal + ':00';
          
          console.log('Pickup formatted: ' + pickupFormatted);
          console.log('Return formatted: ' + returnFormatted);
          
          $('#pickup_date_full').val(pickupFormatted);
          $('#return_date_full').val(returnFormatted);
          
          // Xóa name của các field tạm
          $('#pickup_date_only, #pickup_time_only, #return_date_only, #return_time_only').removeAttr('name');
        });
      });
    </script>
</body>
</html>
