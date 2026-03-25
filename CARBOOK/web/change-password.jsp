<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đổi mật khẩu - CarBook</title>
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
        .password-card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            padding: 40px;
            margin: 50px auto;
            max-width: 600px;
        }
        .password-header {
            text-align: center;
            padding-bottom: 30px;
            border-bottom: 2px solid #01d28e;
            margin-bottom: 30px;
        }
        .password-header h2 {
            color: #01d28e;
            font-weight: 600;
        }
        .form-control:focus {
            border-color: #01d28e;
            box-shadow: 0 0 0 0.2rem rgba(1, 210, 142, 0.25);
        }
        .password-requirements {
            background: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        .password-requirements ul {
            margin-bottom: 0;
            padding-left: 20px;
        }
        .password-requirements li {
            margin-bottom: 5px;
        }
        .btn-change-password {
            background: #01d28e;
            border: none;
            padding: 12px 40px;
            font-weight: 600;
        }
        .btn-change-password:hover {
            background: #01b876;
        }
        .password-toggle {
            cursor: pointer;
            position: absolute;
            right: 15px;
            top: 38px;
            color: #6c757d;
        }
        .password-toggle:hover {
            color: #01d28e;
        }
        .input-group-password {
            position: relative;
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
                    <p class="breadcrumbs">
                        <span class="mr-2"><a href="index.jsp">Trang chủ <i class="ion-ios-arrow-forward"></i></a></span>
                        <span class="mr-2"><a href="profile">Hồ sơ <i class="ion-ios-arrow-forward"></i></a></span>
                        <span>Đổi mật khẩu <i class="ion-ios-arrow-forward"></i></span>
                    </p>
                    <h1 class="mb-3 bread">Đổi mật khẩu</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="ftco-section">
        <div class="container">
            <div class="password-card">
                <div class="password-header">
                    <h2><i class="ion-ios-lock"></i> Thay đổi mật khẩu</h2>
                    <p class="text-muted mb-0">Cập nhật mật khẩu của bạn để bảo mật tài khoản</p>
                </div>

                <!-- Alert Messages -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <strong><i class="ion-ios-checkmark-circle"></i> Thành công!</strong> ${success}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                </c:if>
                
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong><i class="ion-ios-close-circle"></i> Lỗi!</strong> ${error}
                        <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                </c:if>

                <div class="password-requirements">
                    <strong><i class="ion-ios-information-circle"></i> Yêu cầu mật khẩu:</strong>
                    <ul>
                        <li>Mật khẩu phải có ít nhất <strong>6 ký tự</strong></li>
                        <li>Mật khẩu mới phải <strong>khác</strong> mật khẩu cũ</li>
                        <li>Nên sử dụng kết hợp chữ hoa, chữ thường, số và ký tự đặc biệt</li>
                    </ul>
                </div>

                <form action="change-password" method="post" id="changePasswordForm">
                    <div class="form-group input-group-password">
                        <label for="oldPassword">Mật khẩu hiện tại <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="oldPassword" name="oldPassword" 
                               placeholder="Nhập mật khẩu hiện tại" required>
                        <i class="ion-ios-eye password-toggle" onclick="togglePassword('oldPassword')"></i>
                    </div>

                    <div class="form-group input-group-password">
                        <label for="newPassword">Mật khẩu mới <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="newPassword" name="newPassword" 
                               placeholder="Nhập mật khẩu mới" required minlength="6">
                        <i class="ion-ios-eye password-toggle" onclick="togglePassword('newPassword')"></i>
                        <small class="form-text text-muted">Tối thiểu 6 ký tự</small>
                    </div>

                    <div class="form-group input-group-password">
                        <label for="confirmPassword">Xác nhận mật khẩu mới <span class="text-danger">*</span></label>
                        <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" 
                               placeholder="Nhập lại mật khẩu mới" required minlength="6">
                        <i class="ion-ios-eye password-toggle" onclick="togglePassword('confirmPassword')"></i>
                    </div>

                    <div id="passwordMatchMessage" class="text-danger mb-3" style="display: none;">
                        <i class="ion-ios-close-circle"></i> Mật khẩu xác nhận không khớp
                    </div>

                    <hr class="my-4">

                    <div class="form-group text-center">
                        <button type="submit" class="btn btn-primary btn-change-password" id="submitBtn">
                            <i class="ion-ios-checkmark-circle"></i> Đổi mật khẩu
                        </button>
                        <a href="profile" class="btn btn-secondary py-2 px-4 ml-2">
                            <i class="ion-ios-arrow-back"></i> Quay lại
                        </a>
                    </div>

                    <div class="text-center mt-3">
                        <small class="text-muted">
                            <i class="ion-ios-lock"></i> Mật khẩu của bạn được mã hóa an toàn
                        </small>
                    </div>
                </form>
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

            // Password match validation
            $('#confirmPassword').on('keyup', function() {
                var newPassword = $('#newPassword').val();
                var confirmPassword = $(this).val();
                
                if (confirmPassword.length > 0) {
                    if (newPassword === confirmPassword) {
                        $('#passwordMatchMessage').hide();
                        $('#confirmPassword').removeClass('is-invalid').addClass('is-valid');
                    } else {
                        $('#passwordMatchMessage').show();
                        $('#confirmPassword').removeClass('is-valid').addClass('is-invalid');
                    }
                } else {
                    $('#passwordMatchMessage').hide();
                    $('#confirmPassword').removeClass('is-valid is-invalid');
                }
            });

            // Form validation before submit
            $('#changePasswordForm').on('submit', function(e) {
                var newPassword = $('#newPassword').val();
                var confirmPassword = $('#confirmPassword').val();
                var oldPassword = $('#oldPassword').val();

                if (newPassword !== confirmPassword) {
                    e.preventDefault();
                    $('#passwordMatchMessage').show();
                    $('#confirmPassword').addClass('is-invalid');
                    return false;
                }

                if (newPassword === oldPassword) {
                    e.preventDefault();
                    alert('Mật khẩu mới phải khác mật khẩu cũ!');
                    return false;
                }

                if (newPassword.length < 6) {
                    e.preventDefault();
                    alert('Mật khẩu mới phải có ít nhất 6 ký tự!');
                    return false;
                }

                return true;
            });

            // Clear validation on new password change
            $('#newPassword').on('keyup', function() {
                $('#confirmPassword').trigger('keyup');
            });
        });

        // Toggle password visibility
        function togglePassword(fieldId) {
            var field = document.getElementById(fieldId);
            var icon = field.nextElementSibling;
            
            if (field.type === 'password') {
                field.type = 'text';
                icon.classList.remove('ion-ios-eye');
                icon.classList.add('ion-ios-eye-off');
            } else {
                field.type = 'password';
                icon.classList.remove('ion-ios-eye-off');
                icon.classList.add('ion-ios-eye');
            }
        }
    </script>
    
    <%@ include file="includes/footer.jsp" %>
</body>
</html>
