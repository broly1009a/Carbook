<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>Đăng ký - CarBook</title>
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

        <nav class="navbar navbar-expand-lg navbar-dark ftco_navbar bg-dark ftco-navbar-light" id="ftco-navbar">
            <div class="container">
                <a class="navbar-brand" href="index.html">Car<span>Book</span></a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#ftco-nav" aria-controls="ftco-nav" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="oi oi-menu"></span> Menu
                </button>

                <div class="collapse navbar-collapse" id="ftco-nav">
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item"><a href="index.html" class="nav-link">Home</a></li>
                        <li class="nav-item"><a href="about.jsp" class="nav-link">About</a></li>
                        <li class="nav-item"><a href="services.jsp" class="nav-link">Services</a></li>
                        <li class="nav-item"><a href="cars" class="nav-link">Cars</a></li>
                        <li class="nav-item"><a href="contact.html" class="nav-link">Contact</a></li>
                        <li class="nav-item active"><a href="register" class="nav-link">Register</a></li>
                    </ul>
                </div>
            </div>
        </nav>

        <section class="hero-wrap hero-wrap-2 js-fullheight" style="background-image: url('images/bg_3.jpg');" data-stellar-background-ratio="0.5">
            <div class="overlay"></div>
            <div class="container">
                <div class="row no-gutters slider-text js-fullheight align-items-end justify-content-start">
                    <div class="col-md-9 ftco-animate pb-5">
                        <p class="breadcrumbs"><span class="mr-2"><a href="index.html">Home <i class="ion-ios-arrow-forward"></i></a></span> <span>Register <i class="ion-ios-arrow-forward"></i></span></p>
                        <h1 class="mb-3 bread">Đăng ký tài khoản</h1>
                    </div>
                </div>
            </div>
        </section>

        <section class="ftco-section contact-section">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-md-8">
                        <div class="wrapper">
                            <div class="row no-gutters">
                                <div class="col-md-12">
                                    <div class="contact-wrap w-100 p-md-5 p-4">
                                        <h3 class="mb-4 text-center">Tạo tài khoản mới</h3>

                                        <c:if test="${not empty error}">
                                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                                <strong>Lỗi!</strong> ${error}
                                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                                    <span aria-hidden="true">&times;</span>
                                                </button>
                                            </div>
                                        </c:if>

                                        <form method="POST" action="register" id="registerForm" class="registerForm">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="label" for="username">Tên đăng nhập <span class="text-danger">*</span></label>
                                                        <input type="text" class="form-control" name="username" id="username" 
                                                               placeholder="Tên đăng nhập" required value="${username}">
                                                        <c:if test="${not empty errorUsername}"><small class="text-danger font-weight-bold">${errorUsername}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="label" for="email">Email <span class="text-danger">*</span></label>
                                                            <input type="email" class="form-control" name="email" id="email" 
                                                                   placeholder="Email" required value="${email}">
                                                        <c:if test="${not empty errorEmail}"><small class="text-danger font-weight-bold">${errorEmail}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="label" for="password">Mật khẩu <span class="text-danger">*</span></label>
                                                            <input type="password" class="form-control" name="password" id="password" 
                                                                   placeholder="Mật khẩu (tối thiểu 6 ký tự)" required>
                                                        <c:if test="${not empty errorPass}"><small class="text-danger font-weight-bold">${errorPass}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="label" for="confirmPassword">Xác nhận mật khẩu <span class="text-danger">*</span></label>
                                                            <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" 
                                                                   placeholder="Xác nhận mật khẩu" required>
                                                        <c:if test="${not empty errorConfirm}"><small class="text-danger font-weight-bold">${errorConfirm}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-12">
                                                        <div class="form-group">
                                                            <label class="label" for="fullName">Họ và tên <span class="text-danger">*</span></label>
                                                            <input type="text" class="form-control" name="fullName" id="fullName" 
                                                                   placeholder="Họ và tên đầy đủ" required value="${fullName}">
                                                        <c:if test="${not empty errorFullName}"><small class="text-danger font-weight-bold">${errorFullName}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="label" for="phoneNumber">Số điện thoại</label>
                                                            <input type="tel" class="form-control" name="phoneNumber" id="phoneNumber" 
                                                                   placeholder="Số điện thoại" value="${phoneNumber}">
                                                        <c:if test="${not empty errorPhone}"><small class="text-danger font-weight-bold">${errorPhone}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="label" for="dateOfBirth">Ngày sinh</label>
                                                            <input type="date" class="form-control" name="dateOfBirth" id="dateOfBirth" 
                                                                   max="<%= java.time.LocalDate.now() %>" 
                                                            value="${dateOfBirth}">
                                                        <c:if test="${not empty errorDob}">
                                                            <small class="text-danger font-weight-bold">${errorDob}</small>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <label class="label" for="address">Địa chỉ</label>
                                                        <input type="text" class="form-control" name="address" id="address" 
                                                               placeholder="Địa chỉ" value="${address}">
                                                    </div>
                                                </div>
                                                <div class="col-md-6">
                                                    <div class="form-group">
                                                        <label class="label" for="driverLicenseNumber">Số GPLX</label>
                                                        <input type="text" class="form-control" name="driverLicenseNumber" id="driverLicenseNumber" 
                                                               placeholder="Số giấy phép lái xe" value="${driverLicenseNumber}">
                                                        <c:if test="${not empty errorLicense}"><small class="text-danger font-weight-bold">${errorLicense}</small></c:if>
                                                        </div>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <div class="form-group">
                                                            <label class="label" for="driverLicenseExpiry">Ngày hết hạn GPLX</label>
                                                            <input type="date" class="form-control" name="driverLicenseExpiry" id="driverLicenseExpiry" 
                                                                   value="${driverLicenseExpiry}">
                                                        <c:if test="${not empty errorExpiry}">
                                                            <small class="text-danger font-weight-bold">${errorExpiry}</small>
                                                        </c:if>
                                                    </div>
                                                </div>
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <div class="form-check">
                                                            <input type="checkbox" class="form-check-input" id="agreeTerms" required>
                                                            <label class="form-check-label" for="agreeTerms">
                                                                Tôi đồng ý với <a href="#" class="text-primary">Điều khoản sử dụng</a> và <a href="#" class="text-primary">Chính sách bảo mật</a>
                                                            </label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="col-md-12">
                                                    <div class="form-group">
                                                        <input type="submit" value="Đăng ký" class="btn btn-primary w-100">
                                                        <div class="submitting"></div>
                                                    </div>
                                                </div>
                                                <div class="col-md-12 text-center mt-3">
                                                    <p>Đã có tài khoản? <a href="login" class="text-primary font-weight-bold">Đăng nhập ngay</a></p>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <footer class="ftco-footer ftco-bg-dark ftco-section">
            <div class="container">
                <div class="row">
                    <div class="col-md-12 text-center">
                        <p>Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | CarBook</p>
                    </div>
                </div>
            </div>
        </footer>

        <div id="ftco-loader" class="show fullscreen"><svg class="circular" width="48px" height="48px"><circle class="path-bg" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke="#eeeeee"/><circle class="path" cx="24" cy="24" r="22" fill="none" stroke-width="4" stroke-miterlimit="10" stroke="#F96D00"/></svg></div>

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
                            // Password confirmation validation
                            document.getElementById('registerForm').addEventListener('submit', function (e) {
                                var password = document.getElementById('password').value;
                                var confirmPassword = document.getElementById('confirmPassword').value;

                                if (password !== confirmPassword) {
                                    e.preventDefault();
                                    alert('Mật khẩu xác nhận không khớp!');
                                    return false;
                                }
                            });
        </script>
    </body>
</html>