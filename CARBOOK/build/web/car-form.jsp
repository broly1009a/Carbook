<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty car ? 'Thêm xe mới' : 'Chỉnh sửa xe'} - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row mb-3">
                <div class="col-md-12">
                    <h2>${empty car ? 'Thêm xe mới' : 'Chỉnh sửa xe'}</h2>
                </div>
            </div>
            
            <!-- Success message from request or session -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Thành công!</strong> ${success}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Thành công!</strong> ${sessionScope.success}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>
            
            <!-- Error message from request or session -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Lỗi!</strong> ${error}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Lỗi!</strong> ${sessionScope.error}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
            
            <div class="card">
                <div class="card-body">
                    <form action="car-management" method="post" id="carForm">
                        <input type="hidden" name="action" value="${empty car ? 'create' : 'update'}">
                        <c:if test="${not empty car}">
                            <input type="hidden" name="carId" value="${car.carId}">
                        </c:if>
                        
                        <div class="row">
                            <!-- Cột trái -->
                            <div class="col-md-6">
                                <h5 class="mb-3">Thông tin cơ bản</h5>
                                
                                <div class="form-group">
                                    <label>Biển số <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="licensePlate" 
                                           value="${car.licensePlate}" required 
                                           placeholder="VD: 30A-12345">
                                </div>
                                
                                <div class="form-group">
                                    <label>Số VIN</label>
                                    <input type="text" class="form-control" name="vinNumber" 
                                           value="${car.vinNumber}" 
                                           placeholder="Vehicle Identification Number">
                                </div>
                                
                                <div class="form-group">
                                    <label>Danh mục <span class="text-danger">*</span></label>
                                    <select class="form-control" name="categoryId" required>
                                        <option value="">Chọn danh mục</option>
                                        <c:forEach var="category" items="${categories}">
                                            <option value="${category.categoryId}" 
                                                <c:if test="${car.categoryId == category.categoryId}">selected</c:if>>
                                                ${category.categoryName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Hãng xe <span class="text-danger">*</span></label>
                                    <select class="form-control" name="brandId" id="brandId" required onchange="loadModels()">
                                        <option value="">Chọn hãng</option>
                                        <c:forEach var="brand" items="${brands}">
                                            <option value="${brand.brandId}"
                                                <c:if test="${not empty car and not empty car.model and car.model.brandId == brand.brandId}">selected</c:if>>
                                                ${brand.brandName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Model <span class="text-danger">*</span></label>
                                    <select class="form-control" name="modelId" id="modelId" required>
                                        <option value="">Chọn hãng trước</option>
                                        <c:if test="${not empty models}">
                                            <c:forEach var="model" items="${models}">
                                                <option value="${model.modelId}"
                                                    <c:if test="${car.modelId == model.modelId}">selected</c:if>>
                                                    ${model.modelName}
                                                </option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Màu sắc</label>
                                    <input type="text" class="form-control" name="color" 
                                           value="${car.color}" 
                                           placeholder="VD: Đen, Trắng, Bạc">
                                </div>
                                
                                <div class="form-group">
                                    <label>Số chỗ <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" name="seats" 
                                           value="${car.seats}" required 
                                           min="2" max="50" placeholder="VD: 4, 7, 16">
                                </div>
                                
                                <div class="form-group">
                                    <label>Hộp số</label>
                                    <select class="form-control" name="transmission">
                                        <option value="">Chọn loại hộp số</option>
                                        <option value="Automatic" <c:if test="${car.transmission == 'Automatic'}">selected</c:if>>Tự động</option>
                                        <option value="Manual" <c:if test="${car.transmission == 'Manual'}">selected</c:if>>Số sàn</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Nhiên liệu</label>
                                    <input type="text" class="form-control" name="fuelType" 
                                           value="${car.fuelType}" 
                                           placeholder="VD: Xăng, Dầu Diesel, Điện">
                                </div>
                            </div>
                            
                            <!-- Cột phải -->
                            <div class="col-md-6">
                                <h5 class="mb-3">Thông tin giá & địa điểm</h5>
                                
                                <div class="form-group">
                                    <label>Giá thuê/ngày (₫) <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" name="pricePerDay" 
                                           value="${car.pricePerDay}" required 
                                           min="0" step="1000" placeholder="VD: 500000">
                                </div>
                                
                                <div class="form-group">
                                    <label>Giá thuê/giờ (₫)</label>
                                    <input type="number" class="form-control" name="pricePerHour" 
                                           value="${car.pricePerHour}" 
                                           min="0" step="1000" placeholder="VD: 50000">
                                </div>
                                
                                <div class="form-group">
                                    <label>Số km đã đi</label>
                                    <input type="number" class="form-control" name="mileage" 
                                           value="${car.mileage}" 
                                           min="0" step="100" placeholder="VD: 50000">
                                </div>
                                
                                <div class="form-group">
                                    <label>Địa điểm</label>
                                    <input type="text" class="form-control" name="location" 
                                           value="${car.location}" 
                                           placeholder="VD: Hà Nội, TP.HCM">
                                </div>
                                
                                <div class="form-group">
                                    <label>Trạng thái <span class="text-danger">*</span></label>
                                    <select class="form-control" name="status" required>
                                        <option value="Available" <c:if test="${car.status == 'Available'}">selected</c:if> <c:if test="${empty car}">selected</c:if>>Sẵn sàng</option>
                                        <option value="Rented" <c:if test="${car.status == 'Rented'}">selected</c:if>>Đã thuê</option>
                                        <option value="Maintenance" <c:if test="${car.status == 'Maintenance'}">selected</c:if>>Bảo trì</option>
                                        <option value="Inactive" <c:if test="${car.status == 'Inactive'}">selected</c:if>>Không hoạt động</option>
                                    </select>
                                </div>
                                
                                <h5 class="mb-3 mt-4">Giấy tờ & bảo hiểm</h5>
                                
                                <div class="form-group">
                                    <label>Ngày hết hạn bảo hiểm</label>
                                    <input type="date" class="form-control" name="insuranceExpiryDate" 
                                           value="${car.insuranceExpiryDate}">
                                </div>
                                
                                <div class="form-group">
                                    <label>Ngày hết hạn đăng kiểm</label>
                                    <input type="date" class="form-control" name="registrationExpiryDate" 
                                           value="${car.registrationExpiryDate}">
                                </div>
                                
                                <h5 class="mb-3 mt-4">Mô tả & tính năng</h5>
                                
                                <div class="form-group">
                                    <label>Mô tả</label>
                                    <textarea class="form-control" name="description" rows="3" 
                                              placeholder="Mô tả chi tiết về xe...">${car.description}</textarea>
                                </div>
                                
                                <div class="form-group">
                                    <label>Tính năng</label>
                                    <textarea class="form-control" name="features" rows="3" 
                                              placeholder="VD: Camera lùi, Cảm biến lùi, Màn hình cảm ứng...">${car.features}</textarea>
                                </div>
                            </div>
                        </div>
                        
                        <hr class="my-4">
                        
                        <div class="row">
                            <div class="col-md-12 text-right">
                                <a href="car-management" class="btn btn-secondary">
                                    <i class="ion-ios-arrow-back"></i> Hủy
                                </a>
                                <button type="submit" class="btn btn-primary">
                                    <i class="ion-ios-checkmark"></i> ${empty car ? 'Thêm xe' : 'Cập nhật'}
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
    
    <%@ include file="includes/footer.jsp" %>
    
    <script src="js/jquery-3.2.1.min.js"></script>
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
        function loadModels(selectedModelId) {
            const brandId = document.getElementById('brandId').value;
            const modelSelect = document.getElementById('modelId');
            
            if (!brandId) {
                modelSelect.innerHTML = '<option value="">Chọn hãng trước</option>';
                return;
            }
            
            // Show loading state
            modelSelect.innerHTML = '<option value="">Đang tải...</option>';
            
            fetch('car-models?action=getByBrand&brandId=' + brandId)
                .then(response => response.json())
                .then(models => {
                    modelSelect.innerHTML = '<option value="">Chọn model</option>';
                    models.forEach(model => {
                        const option = document.createElement('option');
                        option.value = model.modelId;
                        option.textContent = model.modelName;
                        // Pre-select the model if editing
                        if (selectedModelId && model.modelId == selectedModelId) {
                            option.selected = true;
                        }
                        modelSelect.appendChild(option);
                    });
                })
                .catch(error => {
                    console.error('Error loading models:', error);
                    modelSelect.innerHTML = '<option value="">Lỗi tải dữ liệu</option>';
                });
        }
        
        // Load models on page load if editing
        <c:if test="${not empty car and not empty car.model}">
        document.addEventListener('DOMContentLoaded', function() {
            // Pass the current model ID to pre-select it
            loadModels(${car.modelId});
        });
        </c:if>
    </script>
</body>
</html>
