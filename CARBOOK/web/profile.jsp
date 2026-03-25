<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - CarBook</title>
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
        .profile-img {
            width: 150px;
            height: 150px;
            border-radius: 50%;
            object-fit: cover;
            border: 3px solid #01d28e;
        }
        .profile-card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            padding: 30px;
            margin-bottom: 30px;
        }
        .profile-header {
            text-align: center;
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
            margin-bottom: 20px;
        }
        .info-item {
            padding: 10px 0;
            border-bottom: 1px solid #f5f5f5;
        }
        .info-item:last-child {
            border-bottom: none;
        }
        .info-label {
            font-weight: 600;
            color: #666;
            margin-bottom: 5px;
        }
        .info-value {
            color: #333;
        }
        .badge-role {
            font-size: 14px;
            padding: 5px 15px;
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
                    <p class="breadcrumbs"><span class="mr-2"><a href="index.jsp">Trang chủ <i class="ion-ios-arrow-forward"></i></a></span> <span>Hồ sơ <i class="ion-ios-arrow-forward"></i></span></p>
                    <h1 class="mb-3 bread">Hồ sơ cá nhân</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="ftco-section">
        <div class="container">
            <!-- Alert Messages -->
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <strong>Thành công!</strong> ${success}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <strong>Lỗi!</strong> ${error}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>

            <div class="row">
                <!-- Profile Info Card -->
                <div class="col-md-4">
                    <div class="profile-card">
                        <div class="profile-header">
                            <img src="${not empty user.profileImageURL ? user.profileImageURL : 'images/person_1.jpg'}" 
                                 alt="Profile Image" 
                                 class="profile-img mb-3">
                            <h4>${user.fullName}</h4>
                            <p class="text-muted">${user.email}</p>
                            <c:choose>
                                <c:when test="${user.roleId == 1}">
                                    <span class="badge badge-danger badge-role">Quản trị viên</span>
                                </c:when>
                                <c:when test="${user.roleId == 2}">
                                    <span class="badge badge-primary badge-role">Chủ xe</span>
                                </c:when>
                                <c:when test="${user.roleId == 3}">
                                    <span class="badge badge-success badge-role">Khách hàng</span>
                                </c:when>
                            </c:choose>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Tên đăng nhập</div>
                            <div class="info-value">${user.username}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Số điện thoại</div>
                            <div class="info-value">${not empty user.phoneNumber ? user.phoneNumber : 'Chưa cập nhật'}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Ngày sinh</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${not empty user.dateOfBirth}">
                                        <fmt:formatDate value="${user.dateOfBirth}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>Chưa cập nhật</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Địa chỉ</div>
                            <div class="info-value">${not empty user.address ? user.address : 'Chưa cập nhật'}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Số GPLX</div>
                            <div class="info-value">${not empty user.driverLicenseNumber ? user.driverLicenseNumber : 'Chưa cập nhật'}</div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">GPLX hết hạn</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${not empty user.driverLicenseExpiry}">
                                        <fmt:formatDate value="${user.driverLicenseExpiry}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>Chưa cập nhật</c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        
                        <div class="info-item">
                            <div class="info-label">Ngày tạo tài khoản</div>
                            <div class="info-value">
                                <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Edit Profile Form -->
                <div class="col-md-8">
                    <div class="profile-card">
                        <h3 class="mb-4">Chỉnh sửa thông tin</h3>
                        
                        <form action="profile" method="post">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="fullName">Họ và tên <span class="text-danger">*</span></label>
                                        <input type="text" class="form-control" id="fullName" name="fullName" 
                                               value="${user.fullName}" required>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="phoneNumber">Số điện thoại</label>
                                        <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" 
                                               value="${user.phoneNumber}" placeholder="0123456789">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="dateOfBirth">Ngày sinh</label>
                                        <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" 
                                               value="${user.dateOfBirth}">
                                    </div>
                                </div>
                                
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="address">Địa chỉ</label>
                                        <textarea class="form-control" id="address" name="address" 
                                                  rows="2" placeholder="Nhập địa chỉ đầy đủ">${user.address}</textarea>
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="driverLicenseNumber">Số giấy phép lái xe</label>
                                        <input type="text" class="form-control" id="driverLicenseNumber" 
                                               name="driverLicenseNumber" value="${user.driverLicenseNumber}" 
                                               placeholder="Nhập số GPLX">
                                    </div>
                                </div>
                                
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label for="driverLicenseExpiry">Ngày hết hạn GPLX</label>
                                        <input type="date" class="form-control" id="driverLicenseExpiry" 
                                               name="driverLicenseExpiry" value="${user.driverLicenseExpiry}">
                                    </div>
                                </div>
                                
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label for="profileImageURL">URL ảnh đại diện</label>
                                        <input type="url" class="form-control" id="profileImageURL" 
                                               name="profileImageURL" value="${user.profileImageURL}" 
                                               placeholder="https://example.com/image.jpg">
                                        <small class="form-text text-muted">Nhập URL ảnh đại diện của bạn</small>
                                    </div>
                                </div>
                                
                                <div class="col-md-12">
                                    <hr class="my-4">
                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary py-3 px-5">
                                            <i class="ion-ios-checkmark-circle"></i> Cập nhật thông tin
                                        </button>
                                        <a href="dashboard" class="btn btn-secondary py-3 px-5 ml-2">
                                            <i class="ion-ios-arrow-back"></i> Quay lại Dashboard
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </form>
                        
                        <hr class="my-4">
                        
                        <div class="row">
                            <div class="col-md-12">
                                <h4 class="mb-3">Đổi mật khẩu</h4>
                                <a href="change-password.jsp" class="btn btn-warning">
                                    <i class="ion-ios-lock"></i> Thay đổi mật khẩu
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Scripts must be loaded BEFORE footer to prevent loader issues -->
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
    <script src="js/scrollax.min.js"></script>
    <script src="js/main.js"></script>
    
    <script>
        $(document).ready(function(){
            // Auto hide alerts after 5 seconds
            setTimeout(function() {
                $('.alert').fadeOut('slow');
            }, 5000);
        });
    </script>
    
    <%@ include file="includes/footer.jsp" %>
</body>
</html>
