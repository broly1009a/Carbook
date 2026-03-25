<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty availability ? 'Sửa' : 'Thêm'} lịch không khả dụng - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row">
                <div class="col-md-8 offset-md-2">
                    <h2 class="mb-4">${not empty availability ? 'Sửa' : 'Thêm'} lịch không khả dụng</h2>
                    
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            ${error}
                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                        </div>
                    </c:if>
                    
                    <div class="card">
                        <div class="card-body">
                            <!-- Debug info -->
                            <c:if test="${empty cars}">
                                <div class="alert alert-warning">
                                    Không có xe nào trong hệ thống hoặc danh sách xe chưa được load.
                                </div>
                            </c:if>
                            <c:if test="${not empty cars}">
                                <p class="text-muted">Có ${cars.size()} xe trong danh sách</p>
                            </c:if>
                            
                            <form action="car-availability" method="post">
                                <input type="hidden" name="action" value="${not empty availability ? 'update' : 'create'}">
                                <c:if test="${not empty availability}">
                                    <input type="hidden" name="availabilityId" value="${availability.availabilityId}">
                                </c:if>
                                
                                <!-- Car Selection -->
                                <div class="form-group">
                                    <label for="carId">Xe <span class="text-danger">*</span></label>
                                    <select class="form-control" id="carId" name="carId" required 
                                            ${not empty availability ? 'disabled' : ''}>
                                        <option value="">-- Chọn xe --</option>
                                        <c:forEach var="car" items="${cars}">
                                            <option value="${car.carId}" 
                                                ${(not empty availability && availability.carId == car.carId) || 
                                                  (not empty selectedCarId && selectedCarId == car.carId) ? 'selected' : ''}>
                                                <c:choose>
                                                    <c:when test="${not empty car.model && not empty car.model.brand}">
                                                        ${car.model.brand.brandName} ${car.model.modelName} (${car.licensePlate})
                                                    </c:when>
                                                    <c:otherwise>
                                                        ${car.licensePlate}
                                                    </c:otherwise>
                                                </c:choose>
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${not empty availability}">
                                        <input type="hidden" name="carId" value="${availability.carId}">
                                    </c:if>
                                </div>
                                
                                <!-- Date Range -->
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="startDate">Ngày bắt đầu <span class="text-danger">*</span></label>
                                            <input type="date" class="form-control" id="startDate" name="startDate" 
                                                   value="<fmt:formatDate value='${availability.startDate}' pattern='yyyy-MM-dd'/>" 
                                                   required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="form-group">
                                            <label for="endDate">Ngày kết thúc <span class="text-danger">*</span></label>
                                            <input type="date" class="form-control" id="endDate" name="endDate" 
                                                   value="<fmt:formatDate value='${availability.endDate}' pattern='yyyy-MM-dd'/>" 
                                                   required>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Availability Status -->
                                <div class="form-group">
                                    <label for="isAvailable">Trạng thái <span class="text-danger">*</span></label>
                                    <select class="form-control" id="isAvailable" name="isAvailable" required>
                                        <option value="0" ${not empty availability && !availability.available ? 'selected' : 'selected'}>
                                            Không khả dụng (Block xe)
                                        </option>
                                        <option value="1" ${not empty availability && availability.available ? 'selected' : ''}>
                                            Khả dụng
                                        </option>
                                    </select>
                                    <small class="form-text text-muted">
                                        Chọn "Không khả dụng" để chặn xe trong khoảng thời gian này
                                    </small>
                                </div>
                                
                                <!-- Reason -->
                                <div class="form-group">
                                    <label for="reason">Lý do</label>
                                    <textarea class="form-control" id="reason" name="reason" rows="3" 
                                              placeholder="Ví dụ: Bảo trì định kỳ, Sửa chữa, Chủ xe sử dụng...">${availability.reason}</textarea>
                                    <small class="form-text text-muted">
                                        Ghi chú lý do để dễ quản lý
                                    </small>
                                </div>
                                
                                <!-- Action Buttons -->
                                <div class="form-group">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-save"></i> ${not empty availability ? 'Cập nhật' : 'Thêm mới'}
                                    </button>
                                    <a href="car-availability<c:if test='${not empty availability}'>?carId=${availability.carId}</c:if><c:if test='${not empty selectedCarId}'>?carId=${selectedCarId}</c:if>" 
                                       class="btn btn-secondary">
                                        <i class="fa fa-times"></i> Hủy
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                    
                    <!-- Help Box -->
                    <div class="card mt-3">
                        <div class="card-body">
                            <h5 class="card-title">Lưu ý</h5>
                            <ul class="mb-0">
                                <li>Ngày kết thúc phải sau ngày bắt đầu</li>
                                <li>Khi đặt trạng thái "Không khả dụng", xe sẽ không thể được đặt trong khoảng thời gian này</li>
                                <li>Khách hàng sẽ không thấy xe này khi tìm kiếm trong khoảng thời gian bị block</li>
                                <li>Bạn có thể tạo nhiều lịch block cho cùng một xe</li>
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
    <script src="js/main.js"></script>
    
    <script>
        // Validate dates
        document.querySelector('form').addEventListener('submit', function(e) {
            var startDate = new Date(document.getElementById('startDate').value);
            var endDate = new Date(document.getElementById('endDate').value);
            
            if (endDate < startDate) {
                e.preventDefault();
                alert('Ngày kết thúc phải sau ngày bắt đầu!');
                return false;
            }
        });
        
        // Set minimum date to today for start date
        var today = new Date().toISOString().split('T')[0];
        document.getElementById('startDate').setAttribute('min', today);
        
        // Update end date minimum when start date changes
        document.getElementById('startDate').addEventListener('change', function() {
            document.getElementById('endDate').setAttribute('min', this.value);
        });
    </script>
</body>
</html>
