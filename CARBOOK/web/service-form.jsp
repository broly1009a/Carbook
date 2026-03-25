<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty service ? 'Thêm' : 'Sửa'} dịch vụ - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/flaticon.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .form-section {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-label {
            font-weight: 600;
            color: #333;
        }
        .required::after {
            content: " *";
            color: red;
        }
        .icon-preview {
            font-size: 48px;
            color: #01d28e;
            margin-top: 10px;
        }
        .icon-list {
            max-height: 200px;
            overflow-y: auto;
        }
        .form-help {
            font-size: 13px;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 px-0">
                    <%@ include file="includes/sidebar.jsp" %>
                </div>
                
                <!-- Main Content -->
                <div class="col-md-9 col-lg-10">
                    <div class="container-fluid">
                        <!-- Header -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h2>
                                    <i class="icon-${empty service ? 'plus' : 'pencil'}"></i> 
                                    ${empty service ? 'Thêm dịch vụ mới' : 'Sửa dịch vụ'}
                                </h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="service-management" class="btn btn-secondary">
                                    <i class="icon-arrow-left"></i> Quay lại
                                </a>
                            </div>
                        </div>
                        
                        <!-- Error Messages -->
                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="icon-close"></i> ${sessionScope.error}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:remove var="error" scope="session" />
                        </c:if>
                        
                        <!-- Service Form -->
                        <div class="row">
                            <div class="col-md-12">
                                <form action="service-management" method="post" id="serviceForm" class="form-section">
                                    <input type="hidden" name="action" value="${empty service ? 'create' : 'update'}">
                                    <c:if test="${not empty service}">
                                        <input type="hidden" name="serviceId" value="${service.serviceId}">
                                    </c:if>
                                    
                                    <!-- Service Name -->
                                    <div class="form-group">
                                        <label for="serviceName" class="form-label required">Tên dịch vụ</label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="serviceName" 
                                               name="serviceName" 
                                               value="${service.serviceName}"
                                               maxlength="200"
                                               required
                                               placeholder="Ví dụ: Dịch vụ xe cưới cao cấp">
                                    </div>
                                    
                                    <!-- Short Description -->
                                    <div class="form-group">
                                        <label for="shortDescription" class="form-label">Mô tả ngắn</label>
                                        <textarea class="form-control" 
                                                  id="shortDescription" 
                                                  name="shortDescription" 
                                                  rows="3"
                                                  maxlength="500"
                                                  placeholder="Mô tả ngắn gọn về dịch vụ (hiển thị trên trang danh sách)">${service.shortDescription}</textarea>
                                        <small class="form-help d-block mt-1">
                                            Mô tả ngắn sẽ hiển thị trên thẻ dịch vụ trang chủ
                                        </small>
                                    </div>
                                    
                                    <!-- Full Description -->
                                    <div class="form-group">
                                        <label for="description" class="form-label">Mô tả chi tiết</label>
                                        <textarea class="form-control" 
                                                  id="description" 
                                                  name="description" 
                                                  rows="10"
                                                  placeholder="Mô tả chi tiết về dịch vụ (hỗ trợ HTML)">${service.description}</textarea>
                                        <small class="form-help d-block mt-1">
                                            Bạn có thể sử dụng HTML tags như &lt;p&gt;, &lt;h3&gt;, &lt;ul&gt;, &lt;li&gt; để định dạng
                                        </small>
                                    </div>
                                    
                                    <div class="row">
                                        <!-- Icon Class -->
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="iconClass" class="form-label">Icon CSS Class</label>
                                                <select class="form-control" id="iconClass" name="iconClass" onchange="previewIcon()">
                                                    <option value="flaticon-route" ${service.iconClass == 'flaticon-route' ? 'selected' : ''}>Route (Đường đi)</option>
                                                    <option value="flaticon-wedding-car" ${service.iconClass == 'flaticon-wedding-car' ? 'selected' : ''}>Wedding Car (Xe cưới)</option>
                                                    <option value="flaticon-airport" ${service.iconClass == 'flaticon-airport' ? 'selected' : ''}>Airport (Sân bay)</option>
                                                    <option value="flaticon-map" ${service.iconClass == 'flaticon-map' ? 'selected' : ''}>Map (Bản đồ)</option>
                                                    <option value="flaticon-user" ${service.iconClass == 'flaticon-user' ? 'selected' : ''}>User (Người dùng)</option>
                                                    <option value="flaticon-road" ${service.iconClass == 'flaticon-road' ? 'selected' : ''}>Road (Con đường)</option>
                                                    <option value="flaticon-calendar" ${service.iconClass == 'flaticon-calendar' ? 'selected' : ''}>Calendar (Lịch)</option>
                                                    <option value="flaticon-car" ${service.iconClass == 'flaticon-car' ? 'selected' : ''}>Car (Xe hơi)</option>
                                                </select>
                                                <div class="text-center">
                                                    <span id="iconPreview" class="${service.iconClassOrDefault} icon-preview"></span>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <!-- Price -->
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="price" class="form-label">Giá (VNĐ)</label>
                                                <input type="number" 
                                                       class="form-control" 
                                                       id="price" 
                                                       name="price" 
                                                       value="${service.price}"
                                                       min="0"
                                                       step="1000"
                                                       placeholder="0">
                                                <small class="form-help d-block mt-1">
                                                    Để trống hoặc 0 nếu giá liên hệ
                                                </small>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <div class="row">
                                        <!-- Image URL -->
                                        <div class="col-md-8">
                                            <div class="form-group">
                                                <label for="imageURL" class="form-label">URL hình ảnh</label>
                                                <input type="text" 
                                                       class="form-control" 
                                                       id="imageURL" 
                                                       name="imageURL" 
                                                       value="${service.imageURL}"
                                                       placeholder="https://example.com/image.jpg">
                                                <small class="form-help d-block mt-1">
                                                    Nhập URL hình ảnh đại diện cho dịch vụ (tùy chọn)
                                                </small>
                                            </div>
                                        </div>
                                        
                                        <!-- Display Order -->
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label for="displayOrder" class="form-label">Thứ tự hiển thị</label>
                                                <input type="number" 
                                                       class="form-control" 
                                                       id="displayOrder" 
                                                       name="displayOrder" 
                                                       value="${service.displayOrder}"
                                                       min="0"
                                                       placeholder="0">
                                                <small class="form-help d-block mt-1">
                                                    Số càng nhỏ hiển thị càng trước
                                                </small>
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <!-- Active Status -->
                                    <div class="form-group">
                                        <div class="custom-control custom-checkbox">
                                            <input type="checkbox" 
                                                   class="custom-control-input" 
                                                   id="isActive" 
                                                   name="isActive" 
                                                   value="true"
                                                   ${service.active || empty service ? 'checked' : ''}>
                                            <label class="custom-control-label" for="isActive">
                                                <strong>Kích hoạt dịch vụ</strong>
                                                <br>
                                                <small class="text-muted">
                                                    Nếu không chọn, dịch vụ sẽ bị ẩn và không hiển thị công khai
                                                </small>
                                            </label>
                                        </div>
                                    </div>
                                    
                                    <!-- Additional Info -->
                                    <c:if test="${not empty service}">
                                        <div class="alert alert-info">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <strong>ID:</strong> ${service.serviceId}
                                                </div>
                                                <div class="col-md-4">
                                                    <strong>Ngày tạo:</strong> 
                                                    <fmt:formatDate value="${service.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                                </div>
                                                <div class="col-md-4">
                                                    <strong>Cập nhật:</strong> 
                                                    <fmt:formatDate value="${service.updatedAt}" pattern="dd/MM/yyyy HH:mm" />
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Buttons -->
                                    <div class="form-group mt-4">
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="icon-save"></i> ${empty service ? 'Tạo dịch vụ' : 'Cập nhật'}
                                        </button>
                                        <a href="service-management" class="btn btn-secondary btn-lg ml-2">
                                            <i class="icon-x"></i> Hủy
                                        </a>
                                        <c:if test="${not empty service}">
                                            <a href="services?action=view&id=${service.serviceId}" 
                                               class="btn btn-info btn-lg ml-2"
                                               target="_blank">
                                                <i class="icon-eye"></i> Xem trước
                                            </a>
                                        </c:if>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <script src="js/jquery.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
    <script>
        // Icon preview
        function previewIcon() {
            var iconClass = $('#iconClass').val();
            $('#iconPreview').attr('class', iconClass + ' icon-preview');
        }
        
        // Initialize on page load
        $(document).ready(function() {
            previewIcon();
            
            // Form validation
            $('#serviceForm').on('submit', function(e) {
                var serviceName = $('#serviceName').val().trim();
                
                if (!serviceName) {
                    alert('Vui lòng nhập tên dịch vụ');
                    e.preventDefault();
                    return false;
                }
                
                return true;
            });
        });
    </script>
</body>
</html>
