<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý xe - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row mb-3">
                <div class="col-md-6">
                    <h2>Quản lý xe</h2>
                </div>
                <div class="col-md-6 text-right">
                    <a href="car-management?action=create" class="btn btn-primary">
                        <i class="ion-plus"></i> Thêm xe mới
                    </a>
                </div>
            </div>
            
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${sessionScope.success}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${sessionScope.error}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>
            
            <!-- Filters -->
            <div class="card mb-3">
                <div class="card-body">
                    <form action="car-management" method="get" class="form-inline">
                        <input type="hidden" name="action" value="list">
                        
                        <div class="form-group mr-2">
                            <input type="text" name="search" class="form-control" placeholder="Tìm kiếm..." value="${param.search}">
                        </div>
                        
                        <div class="form-group mr-2">
                            <select name="categoryId" class="form-control">
                                <option value="">Tất cả danh mục</option>
                                <c:if test="${not empty categories}">
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}" 
                                            <c:if test="${not empty param.categoryId and param.categoryId eq category.categoryId}">selected</c:if>>
                                            ${category.categoryName}
                                        </option>
                                    </c:forEach>
                                </c:if>
                            </select>
                        </div>
                        
                        <div class="form-group mr-2">
                            <select name="status" class="form-control">
                                <option value="">Tất cả trạng thái</option>
                                <option value="Available" <c:if test="${param.status eq 'Available'}">selected</c:if>>Sẵn sàng</option>
                                <option value="Rented" <c:if test="${param.status eq 'Rented'}">selected</c:if>>Đã thuê</option>
                                <option value="Maintenance" <c:if test="${param.status eq 'Maintenance'}">selected</c:if>>Bảo trì</option>
                                <option value="Inactive" <c:if test="${param.status eq 'Inactive'}">selected</c:if>>Không hoạt động</option>
                            </select>
                        </div>                        <button type="submit" class="btn btn-primary">Lọc</button>
                        <a href="car-management?action=list" class="btn btn-secondary ml-2">Xóa lọc</a>
                    </form>
                </div>
            </div>
            <!-- Cars table -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty cars}">
                            <p class="text-center">Chưa có xe nào. Hãy thêm xe mới!</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Biển số</th>
                                            <th>Model</th>
                                            <th>Màu</th>
                                            <th>Năm</th>
                                            <th>Số chỗ</th>
                                            <th>Giá/ngày</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="car" items="${cars}">
                                            <tr>
                                               <td>${car.carId}</td>
                                                <td><strong>${car.licensePlate}</strong></td>
                                                  <td>
                                                    <c:choose>
                                                        <c:when test="${car.model != null}">
                                                            <c:choose>
                                                                <c:when test="${car.model.brand != null}">
                                                                    ${car.model.brand.brandName} ${car.model.modelName}
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${car.model.modelName}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Chưa có thông tin</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${car.color}</td>
                                                <td>${car.model.year}</td>
                                                <td>${car.seats}</td>
                                                <td><fmt:formatNumber value="${car.pricePerDay}" type="currency" currencySymbol="₫"/></td>
                                                <td>
                                                    <span class="badge badge-${car.status == 'Available' ? 'success' : car.status == 'Rented' ? 'warning' : car.status == 'Maintenance' ? 'info' : 'secondary'}">
                                                        ${car.status}
                                                    </span>
                                                </td>
                                                <td>
                                                    <a href="car-management?action=edit&id=${car.carId}" class="btn btn-sm btn-info">Sửa</a>
                                                    <a href="car-images?carId=${car.carId}" class="btn btn-sm btn-success">Quản lý ảnh</a>
                                                    <a href="car-management?action=delete&id=${car.carId}" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa xe này?')">Xóa</a>
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
                                                <a class="page-link" href="car-management?action=list&page=${i}&search=${param.search}&categoryId=${param.categoryId}&status=${param.status}">${i}</a>
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
    
    <!-- Car Modal -->
    <div class="modal fade" id="carModal" tabindex="-1" role="dialog">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <form action="car-management" method="post" id="carForm">
                    <input type="hidden" name="action" id="formAction" value="create">
                    <input type="hidden" name="carId" id="carId">
                    
                    <div class="modal-header">
                        <h5 class="modal-title" id="modalTitle">Thêm xe mới</h5>
                        <button type="button" class="close" data-dismiss="modal">
                            <span>&times;</span>
                        </button>
                    </div>
                    
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>Biển số <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="licensePlate" id="licensePlate" required>
                                </div>
                                
                                <div class="form-group">
                                    <label>Danh mục <span class="text-danger">*</span></label>
                                    <select class="form-control" name="categoryId" id="categoryId" required>
                                        <option value="">Chọn danh mục</option>
                                        <c:forEach var="category" items="${categories}">
                                            <option value="${category.categoryId}">${category.categoryName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Hãng xe <span class="text-danger">*</span></label>
                                    <select class="form-control" name="brandId" id="brandId" required onchange="loadModels()">
                                        <option value="">Chọn hãng</option>
                                        <c:forEach var="brand" items="${brands}">
                                            <option value="${brand.brandId}">${brand.brandName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Model <span class="text-danger">*</span></label>
                                    <select class="form-control" name="modelId" id="modelId" required>
                                        <option value="">Chọn hãng trước</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Màu sắc</label>
                                    <input type="text" class="form-control" name="color" id="color">
                                </div>
                                
                                <div class="form-group">
                                    <label>Năm sản xuất</label>
                                    <input type="number" class="form-control" name="year" id="year" min="1990" max="2025">
                                </div>
                            </div>
                            
                            <div class="col-md-6">
                                <div class="form-group">
                                    <label>Số chỗ</label>
                                    <input type="number" class="form-control" name="seats" id="seats" min="2" max="50">
                                </div>
                                
                                <div class="form-group">
                                    <label>Hộp số</label>
                                    <select class="form-control" name="transmission" id="transmission">
                                        <option value="">Chọn</option>
                                        <option value="Automatic">Tự động</option>
                                        <option value="Manual">Số sàn</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Nhiên liệu</label>
                                    <input type="text" class="form-control" name="fuelType" id="fuelType">
                                </div>
                                
                                <div class="form-group">
                                    <label>Giá thuê/ngày (₫) <span class="text-danger">*</span></label>
                                    <input type="number" class="form-control" name="pricePerDay" id="pricePerDay" required min="0" step="1000">
                                </div>
                                
                                <div class="form-group">
                                    <label>Trạng thái</label>
                                    <select class="form-control" name="status" id="status">
                                        <option value="Available">Sẵn sàng</option>
                                        <option value="Rented">Đã thuê</option>
                                        <option value="Maintenance">Bảo trì</option>
                                        <option value="Inactive">Không hoạt động</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label>Mô tả</label>
                                    <textarea class="form-control" name="description" id="description" rows="3"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Đóng</button>
                        <button type="submit" class="btn btn-primary">Lưu</button>
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
    <script>
        function resetCarForm() {
            document.getElementById('carForm').reset();
            document.getElementById('formAction').value = 'create';
            document.getElementById('carId').value = '';
            document.getElementById('modalTitle').textContent = 'Thêm xe mới';
        }
        
        function loadModels(selectedModelId) {
            const brandId = document.getElementById('brandId').value;
            const modelSelect = document.getElementById('modelId');
            
            if (!brandId) {
                modelSelect.innerHTML = '<option value="">Chọn hãng trước</option>';
                return;
            }
            
            fetch('car-models?action=getByBrand&brandId=' + brandId)
                .then(response => response.json())
                .then(models => {
                    modelSelect.innerHTML = '<option value="">Chọn model</option>';
                    models.forEach(model => {
                        const option = document.createElement('option');
                        option.value = model.modelId;
                        option.textContent = model.modelName;
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
    </script>
</body>
</html>
