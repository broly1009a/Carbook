<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Quản lý ảnh xe - Car Rental Management</title>

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
    <link rel="stylesheet" href="css/style.css">
    
    <style>
        .image-gallery {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 20px;
            margin-top: 20px;
        }
        
        .image-card {
            position: relative;
            border: 2px solid #ddd;
            border-radius: 8px;
            overflow: hidden;
            transition: all 0.3s ease;
        }
        
        .image-card:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            transform: translateY(-2px);
        }
        
        .image-card.primary {
            border-color: #01d28e;
            border-width: 3px;
        }
        
        .image-wrapper {
            position: relative;
            width: 100%;
            padding-bottom: 75%; /* 4:3 aspect ratio */
            background: #f8f9fa;
        }
        
        .image-wrapper img {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .primary-badge {
            position: absolute;
            top: 10px;
            left: 10px;
            background: #01d28e;
            color: white;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            z-index: 10;
        }
        
        .image-actions {
            padding: 10px;
            background: white;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        
        .image-actions button {
            padding: 5px 12px;
            font-size: 13px;
            border-radius: 4px;
            border: none;
            cursor: pointer;
            transition: all 0.3s;
        }
        
        .btn-set-primary {
            background: #01d28e;
            color: white;
        }
        
        .btn-set-primary:hover {
            background: #00b377;
        }
        
        .btn-delete {
            background: #dc3545;
            color: white;
        }
        
        .btn-delete:hover {
            background: #c82333;
        }
        
        .upload-section {
            background: #f8f9fa;
            padding: 30px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        
        .car-info {
            background: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #01d28e;
        }
        
        .car-info h4 {
            margin: 0 0 10px 0;
            color: #333;
        }
        
        .car-info p {
            margin: 5px 0;
            color: #666;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        
        .alert-success {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
        }
        
        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state i {
            font-size: 64px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
   
  <%@ include file="includes/navbar.jsp" %>
    <section class="hero-wrap hero-wrap-2 js-fullheight" style="background-image: url('images/bg_3.jpg');" data-stellar-background-ratio="0.5">
        <div class="overlay"></div>
        <div class="container">
            <div class="row no-gutters slider-text js-fullheight align-items-end justify-content-start">
                <div class="col-md-9 ftco-animate pb-5">
                    <p class="breadcrumbs"><span class="mr-2"><a href="dashboard.jsp">Trang chủ <i class="ion-ios-arrow-forward"></i></a></span> <span class="mr-2"><a href="car-management.jsp">Quản lý xe <i class="ion-ios-arrow-forward"></i></a></span> <span>Quản lý ảnh <i class="ion-ios-arrow-forward"></i></span></p>
                    <h1 class="mb-3 bread">Quản lý ảnh xe</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="ftco-section">
        <div class="container">
            <!-- Car Information -->
            <div class="car-info">
                <h4>
                    <c:choose>
                        <c:when test="${not empty car.model and not empty car.model.brand}">
                            ${car.model.brand.brandName} ${car.model.modelName}
                        </c:when>
                        <c:otherwise>
                            Xe ID: ${car.carId}
                        </c:otherwise>
                    </c:choose>
                </h4>
                <p><strong>Biển số:</strong> ${car.licensePlate}</p>
                <p><strong>Năm sản xuất:</strong> ${car.model.year}</p>
                <p><strong>Tổng số ảnh:</strong> ${images.size()}</p>
            </div>

            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">
                    ${sessionScope.success}
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger">
                    ${sessionScope.error}
                </div>
                <c:remove var="error" scope="session"/>
            </c:if>

            <!-- Upload Section -->
            <div class="upload-section">
                <h3 class="mb-4">Tải ảnh mới</h3>
                <form action="car-images" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="upload">
                    <input type="hidden" name="carId" value="${car.carId}">
                    
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label>Chọn file ảnh</label>
                                <input type="file" name="imageFile" class="form-control" accept="image/*" required>
                                <small class="form-text text-muted">Chấp nhận: JPG, PNG, GIF. Kích thước tối đa: 10MB</small>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="form-group">
                                <label>&nbsp;</label>
                                <div class="form-check">
                                    <input type="checkbox" name="isPrimary" value="1" class="form-check-input" id="isPrimary" ${images.size() == 0 ? 'checked disabled' : ''}>
                                    <label class="form-check-label" for="isPrimary">
                                        Đặt làm ảnh chính
                                    </label>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <button type="submit" class="btn btn-primary">
                        <i class="icon-cloud-upload"></i> Tải lên
                    </button>
                    <a href="car-management.jsp" class="btn btn-secondary">
                        <i class="icon-arrow-left"></i> Quay lại
                    </a>
                </form>
            </div>

            <!-- Image Gallery -->
            <h3 class="mb-3">Danh sách ảnh</h3>
            
            <c:choose>
                <c:when test="${empty images}">
                    <div class="empty-state">
                        <i class="icon-image"></i>
                        <h4>Chưa có ảnh nào</h4>
                        <p>Hãy tải ảnh đầu tiên cho xe này</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="image-gallery">
                        <c:forEach var="image" items="${images}">
                            <div class="image-card ${image.primary ? 'primary' : ''}">
                                <div class="image-wrapper">
                                    <c:if test="${image.primary}">
                                        <span class="primary-badge">Ảnh chính</span>
                                    </c:if>
                                    <img src="${pageContext.request.contextPath}/${image.imageURL}" 
                                         alt="Car image"
                                         onerror="this.src='images/car-placeholder.jpg'">
                                </div>
                                <div class="image-actions">
                                    <c:if test="${!image.primary}">
                                        <form action="car-images" method="post" style="display: inline;">
                                            <input type="hidden" name="action" value="setPrimary">
                                            <input type="hidden" name="carId" value="${car.carId}">
                                            <input type="hidden" name="imageId" value="${image.imageId}">
                                            <button type="submit" class="btn-set-primary" title="Đặt làm ảnh chính">
                                                <i class="icon-star"></i> Chọn chính
                                            </button>
                                        </form>
                                    </c:if>
                                    <c:if test="${image.primary}">
                                        <span style="color: #01d28e; font-weight: 600;">
                                            <i class="icon-star"></i> Ảnh chính
                                        </span>
                                    </c:if>
                                    
                                    <form action="car-images" method="post" style="display: inline;" 
                                          onsubmit="return confirm('Bạn có chắc muốn xóa ảnh này?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="carId" value="${car.carId}">
                                        <input type="hidden" name="imageId" value="${image.imageId}">
                                        <button type="submit" class="btn-delete" title="Xóa ảnh">
                                            <i class="icon-trash"></i> Xóa
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

   <%@ include file="includes/footer.jsp" %>

    <!-- loader -->
    <!-- <div id="ftco-loader" class="show fullscreen">
        <svg class="circular" width="48px" height="48px">
            <circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/>
            <circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00"/>
        </svg>
    </div> -->

   
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
</body>
</html>
