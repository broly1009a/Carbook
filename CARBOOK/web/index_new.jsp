<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>CarBook - Thuê xe ô tô giá rẻ</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    
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
</head>
<body>
    
<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
    <div class="container">
        <a class="navbar-brand" href="home">Car<span>Book</span></a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav">
            <span class="oi oi-menu"></span> Menu
        </button>

        <div class="collapse navbar-collapse" id="ftco-nav">
            <ul class="navbar-nav ml-auto">
                <li class="nav-item active"><a href="home" class="nav-link">Trang chủ</a></li>
                <li class="nav-item"><a href="cars" class="nav-link">Xe cho thuê</a></li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item"><a href="bookings" class="nav-link">Đặt xe của tôi</a></li>
                        <li class="nav-item"><a href="logout" class="nav-link">Đăng xuất (${sessionScope.user.fullName})</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a href="login.jsp" class="nav-link">Đăng nhập</a></li>
                        <li class="nav-item"><a href="register.jsp" class="nav-link">Đăng ký</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>

<!-- Hero Section -->
<div class="hero-wrap ftco-degree-bg" style="background-image: url('images/bg_1.jpg');" data-stellar-background-ratio="0.5">
    <div class="overlay"></div>
    <div class="container">
        <div class="row no-gutters slider-text justify-content-start align-items-center justify-content-center">
            <div class="col-lg-8 ftco-animate">
                <div class="text w-100 text-center mb-md-5 pb-md-5">
                    <h1 class="mb-4">Thuê Xe Dễ Dàng &amp; Nhanh Chóng</h1>
                    <p style="font-size: 18px;">CarBook - Nền tảng cho thuê xe ô tô hàng đầu Việt Nam. Đa dạng các dòng xe, giá cả hợp lý, thủ tục đơn giản.</p>
                    <a href="cars" class="btn btn-primary btn-lg mt-4 px-5 py-3">Xem Danh Sách Xe</a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Search Form Section -->
<section class="ftco-section ftco-no-pt bg-light">
    <div class="container">
        <div class="row no-gutters">
            <div class="col-md-12 featured-top">
                <div class="row no-gutters">
                    <div class="col-md-4 d-flex align-items-center">
                        <form action="cars" method="get" class="request-form ftco-animate bg-primary">
                            <h2>Tìm kiếm xe</h2>
                            <div class="form-group">
                                <label class="label">Từ khóa</label>
                                <input type="text" name="keyword" class="form-control" placeholder="Tìm theo biển số, mô tả...">
                            </div>
                            <div class="form-group">
                                <label class="label">Giá tối đa (VNĐ)</label>
                                <input type="number" name="maxPrice" class="form-control" placeholder="Ví dụ: 1000000">
                            </div>
                            <div class="form-group">
                                <input type="submit" value="Tìm kiếm xe" class="btn btn-secondary py-3 px-4">
                            </div>
                            <div class="form-group">
                                <a href="cars" class="btn btn-outline-light py-3 px-4 btn-block">Xem tất cả xe</a>
                            </div>
                        </form>
                    </div>
                    <div class="col-md-8 d-flex align-items-center">
                        <div class="services-wrap rounded-right w-100">
                            <h3 class="heading-section mb-4">Cách Tốt Nhất Để Thuê Xe Hoàn Hảo</h3>
                            <div class="row d-flex mb-4">
                                <div class="col-md-4 d-flex align-self-stretch ftco-animate">
                                    <div class="services w-100 text-center">
                                        <div class="icon d-flex align-items-center justify-content-center"><span class="flaticon-route"></span></div>
                                        <div class="text w-100">
                                            <h3 class="heading mb-2">Chọn Xe Phù Hợp</h3>
                                        </div>
                                    </div>      
                                </div>
                                <div class="col-md-4 d-flex align-self-stretch ftco-animate">
                                    <div class="services w-100 text-center">
                                        <div class="icon d-flex align-items-center justify-content-center"><span class="flaticon-handshake"></span></div>
                                        <div class="text w-100">
                                            <h3 class="heading mb-2">Giá Cả Tốt Nhất</h3>
                                        </div>
                                    </div>      
                                </div>
                                <div class="col-md-4 d-flex align-self-stretch ftco-animate">
                                    <div class="services w-100 text-center">
                                        <div class="icon d-flex align-items-center justify-content-center"><span class="flaticon-rent"></span></div>
                                        <div class="text w-100">
                                            <h3 class="heading mb-2">Đặt Xe Ngay</h3>
                                        </div>
                                    </div>      
                                </div>
                            </div>
                            <p><a href="cars" class="btn btn-primary py-3 px-4">Xem Danh Sách Xe</a></p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Featured Cars Section -->
<section class="ftco-section ftco-no-pt bg-light">
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-12 heading-section text-center ftco-animate mb-5">
                <span class="subheading">Xe nổi bật</span>
                <h2 class="mb-2">Các Xe Cho Thuê Phổ Biến</h2>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <c:choose>
                    <c:when test="${not empty featuredCars}">
                        <div class="carousel-car owl-carousel">
                            <c:forEach var="car" items="${featuredCars}">
                            <div class="item">
                                <div class="car-wrap rounded ftco-animate">
                                    <div class="img rounded d-flex align-items-end" style="background-image: url(${not empty car.imageUrl ? car.imageUrl : 'images/car-1.jpg'});">
                                    </div>
                                    <div class="text">
                                        <h2 class="mb-0"><a href="car-detail?id=${car.carId}">${car.model.brand.brandName} ${car.model.modelName}</a></h2>
                                        <div class="d-flex mb-3">
                                            <span class="cat">${car.category.categoryName}</span>
                                            <p class="price ml-auto"><fmt:formatNumber value="${car.pricePerDay}" type="currency" currencySymbol="₫" maxFractionDigits="0"/> <span>/ngày</span></p>
                                        </div>
                                        <p class="d-flex mb-0 d-block">
                                            <c:choose>
                                                <c:when test="${not empty sessionScope.user}">
                                                    <a href="booking?action=create&carId=${car.carId}" class="btn btn-primary py-2 mr-1">Đặt ngay</a>
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="login.jsp" class="btn btn-primary py-2 mr-1">Đặt ngay</a>
                                                </c:otherwise>
                                            </c:choose>
                                            <a href="car-detail?id=${car.carId}" class="btn btn-secondary py-2 ml-1">Chi tiết</a>
                                        </p>
                                    </div>
                                </div>
                            </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info text-center">Chưa có xe nào trong hệ thống</div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</section>

<!-- Statistics Counter Section -->
<section class="ftco-counter ftco-section img bg-light" id="section-counter">
    <div class="overlay"></div>
    <div class="container">
        <div class="row">
            <div class="col-md-6 col-lg-3 justify-content-center counter-wrap ftco-animate">
                <div class="block-18">
                    <div class="text text-border d-flex align-items-center">
                        <strong class="number" data-number="${totalCars > 0 ? totalCars : 0}">${totalCars > 0 ? totalCars : 0}</strong>
                        <span>Tổng số <br>Xe</span>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3 justify-content-center counter-wrap ftco-animate">
                <div class="block-18">
                    <div class="text text-border d-flex align-items-center">
                        <strong class="number" data-number="10">10</strong>
                        <span>Năm <br>Kinh nghiệm</span>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3 justify-content-center counter-wrap ftco-animate">
                <div class="block-18">
                    <div class="text text-border d-flex align-items-center">
                        <strong class="number" data-number="2500">2500</strong>
                        <span>Khách hàng <br>Hài lòng</span>
                    </div>
                </div>
            </div>
            <div class="col-md-6 col-lg-3 justify-content-center counter-wrap ftco-animate">
                <div class="block-18">
                    <div class="text d-flex align-items-center">
                        <strong class="number" data-number="5">5</strong>
                        <span>Chi nhánh <br>Toàn quốc</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Footer -->
<footer class="ftco-footer ftco-bg-dark ftco-section">
    <div class="container">
        <div class="row mb-5">
            <div class="col-md">
                <div class="ftco-footer-widget mb-4">
                    <h2 class="ftco-heading-2"><a href="home" class="logo">Car<span>Book</span></a></h2>
                    <p>Nền tảng cho thuê xe ô tô uy tín, chất lượng hàng đầu Việt Nam.</p>
                </div>
            </div>
            <div class="col-md">
                <div class="ftco-footer-widget mb-4 ml-md-5">
                    <h2 class="ftco-heading-2">Thông tin</h2>
                    <ul class="list-unstyled">
                        <li><a href="cars" class="py-2 d-block">Xe cho thuê</a></li>
                        <li><a href="login.jsp" class="py-2 d-block">Đăng nhập</a></li>
                        <li><a href="register.jsp" class="py-2 d-block">Đăng ký</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12 text-center">
                <p>Copyright &copy;<script>document.write(new Date().getFullYear());</script> CarBook. All rights reserved.</p>
            </div>
        </div>
    </div>
</footer>

<!-- Loader -->
<div id="ftco-loader" class="show fullscreen">
    <svg class="circular" width="48px" height="48px">
        <circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/>
        <circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00"/>
    </svg>
</div>

<!-- Scripts -->
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
