<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:choose><c:when test="${not empty maintenance}">Sửa lịch bảo trì</c:when><c:otherwise>Thêm lịch bảo trì</c:otherwise></c:choose> - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row">
                <div class="col-md-12">
                    <h2 class="mb-4">
                        <c:choose>
                            <c:when test="${not empty maintenance}">Sửa lịch bảo trì</c:when>
                            <c:otherwise>Thêm lịch bảo trì mới</c:otherwise>
                        </c:choose>
                    </h2>
                </div>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${error}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>
            
            <div class="row">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <form method="post" action="maintenance">
                                <input type="hidden" name="action" value="${not empty maintenance ? 'update' : 'create'}">
                                <c:if test="${not empty maintenance}">
                                    <input type="hidden" name="maintenanceId" value="${maintenance.maintenanceId}">
                                </c:if>
                                
                                <!-- Car Selection -->
                                <div class="form-group">
                                    <label for="carId">Xe <span class="text-danger">*</span></label>
                                    <select class="form-control" id="carId" name="carId" required>
                                        <option value="">-- Chọn xe --</option>
                                        <c:forEach var="car" items="${cars}">
                                            <option value="${car.carId}" 
                                                    ${(not empty maintenance && maintenance.carId == car.carId) || (not empty selectedCar && selectedCar.carId == car.carId) ? 'selected' : ''}>
                                                ${car.model.brand.brandName} ${car.model.modelName} (${car.licensePlate})
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <!-- Maintenance Type -->
                                <div class="form-group">
                                    <label for="maintenanceType">Loại bảo trì <span class="text-danger">*</span></label>
                                    <select class="form-control" id="maintenanceType" name="maintenanceType" required>
                                        <option value="">-- Chọn loại --</option>
                                        <option value="Routine" ${maintenance.maintenanceType == 'Routine' ? 'selected' : ''}>Bảo trì định kỳ (Routine)</option>
                                        <option value="Repair" ${maintenance.maintenanceType == 'Repair' ? 'selected' : ''}>Sửa chữa (Repair)</option>
                                        <option value="Inspection" ${maintenance.maintenanceType == 'Inspection' ? 'selected' : ''}>Kiểm tra (Inspection)</option>
                                    </select>
                                </div>
                                
                                <!-- Description -->
                                <div class="form-group">
                                    <label for="description">Mô tả <span class="text-danger">*</span></label>
                                    <textarea class="form-control" id="description" name="description" rows="3" required>${maintenance.description}</textarea>
                                    <small class="form-text text-muted">Mô tả chi tiết công việc bảo trì</small>
                                </div>
                                
                                <!-- Service Provider -->
                                <div class="form-group">
                                    <label for="serviceProvider">Nhà cung cấp dịch vụ</label>
                                    <input type="text" class="form-control" id="serviceProvider" name="serviceProvider" 
                                           value="${maintenance.serviceProvider}" placeholder="Ví dụ: Garage ABC, Toyota Service">
                                </div>
                                
                                <!-- Service Date -->
                                <div class="form-group">
                                    <label for="serviceDate">Ngày bảo trì <span class="text-danger">*</span></label>
                                    <input type="date" class="form-control" id="serviceDate" name="serviceDate" required
                                           value="<fmt:formatDate value='${maintenance.serviceDate}' pattern='yyyy-MM-dd'/>">
                                </div>
                                
                                <!-- Service Cost -->
                                <div class="form-group">
                                    <label for="serviceCost">Chi phí (VNĐ)</label>
                                    <input type="number" class="form-control" id="serviceCost" name="serviceCost" 
                                           value="${maintenance.serviceCost}" min="0" step="1000" placeholder="0">
                                </div>
                                
                                <!-- Next Service Date -->
                                <div class="form-group">
                                    <label for="nextServiceDate">Ngày bảo trì tiếp theo</label>
                                    <input type="date" class="form-control" id="nextServiceDate" name="nextServiceDate"
                                           value="<fmt:formatDate value='${maintenance.nextServiceDate}' pattern='yyyy-MM-dd'/>">
                                    <small class="form-text text-muted">Ngày dự kiến cho lần bảo trì tiếp theo</small>
                                </div>
                                
                                <!-- Status -->
                                <div class="form-group">
                                    <label for="status">Trạng thái <span class="text-danger">*</span></label>
                                    <select class="form-control" id="status" name="status" required>
                                        <option value="Scheduled" ${empty maintenance || maintenance.status == 'Scheduled' ? 'selected' : ''}>Đã lên lịch</option>
                                        <option value="In Progress" ${maintenance.status == 'In Progress' ? 'selected' : ''}>Đang bảo trì</option>
                                        <option value="Completed" ${maintenance.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                                        <option value="Cancelled" ${maintenance.status == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                                    </select>
                                    <small class="form-text text-muted">
                                        <strong>Lưu ý:</strong> Xe sẽ bị block tự động khi trạng thái là "Đã lên lịch" hoặc "Đang bảo trì"
                                    </small>
                                </div>
                                
                                <!-- Notes -->
                                <div class="form-group">
                                    <label for="notes">Ghi chú</label>
                                    <textarea class="form-control" id="notes" name="notes" rows="2" placeholder="Ghi chú thêm...">${maintenance.notes}</textarea>
                                </div>
                                
                                <div class="form-group">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fa fa-save"></i> 
                                        <c:choose>
                                            <c:when test="${not empty maintenance}">Cập nhật</c:when>
                                            <c:otherwise>Thêm mới</c:otherwise>
                                        </c:choose>
                                    </button>
                                    <a href="maintenance<c:if test='${not empty maintenance}'>?carId=${maintenance.carId}</c:if>" class="btn btn-secondary">
                                        <i class="fa fa-times"></i> Hủy
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-body">
                            <h5 class="card-title">Thông tin hữu ích</h5>
                            <h6>Loại bảo trì:</h6>
                            <ul>
                                <li><strong>Routine:</strong> Bảo trì định kỳ (thay dầu, kiểm tra lốp...)</li>
                                <li><strong>Repair:</strong> Sửa chữa các hư hỏng</li>
                                <li><strong>Inspection:</strong> Kiểm tra, đánh giá tình trạng xe</li>
                            </ul>
                            
                            <h6 class="mt-3">Trạng thái:</h6>
                            <ul>
                                <li><strong>Đã lên lịch:</strong> Lịch bảo trì đã được lên kế hoạch</li>
                                <li><strong>Đang bảo trì:</strong> Xe đang được bảo trì</li>
                                <li><strong>Hoàn thành:</strong> Đã hoàn thành</li>
                                <li><strong>Đã hủy:</strong> Lịch bảo trì bị hủy</li>
                            </ul>
                            
                            <div class="alert alert-info mt-3">
                                <i class="fa fa-info-circle"></i> 
                                Khi xe có lịch bảo trì "Đã lên lịch" hoặc "Đang bảo trì", 
                                hệ thống sẽ tự động chặn không cho khách hàng đặt xe trong khoảng thời gian đó.
                            </div>
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
        // Set minimum date to today for service date
        document.addEventListener('DOMContentLoaded', function() {
            var today = new Date().toISOString().split('T')[0];
            document.getElementById('serviceDate').setAttribute('min', today);
            
            // Set minimum next service date based on service date
            document.getElementById('serviceDate').addEventListener('change', function() {
                document.getElementById('nextServiceDate').setAttribute('min', this.value);
            });
        });
    </script>
</body>
</html>
