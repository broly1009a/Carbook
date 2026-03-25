<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả thanh toán - CarBook</title>
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
        .result-container {
            max-width: 600px;
            margin: 50px auto;
            text-align: center;
        }
        .result-icon {
            font-size: 80px;
            margin-bottom: 20px;
        }
        .result-icon.success {
            color: #28a745;
        }
        .result-icon.failed {
            color: #dc3545;
        }
        .result-card {
            background: white;
            padding: 40px;
            border-radius: 10px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .transaction-info {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 5px;
            margin: 20px 0;
            text-align: left;
        }
        .info-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #dee2e6;
        }
        .info-row:last-child {
            border-bottom: none;
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
                        <span>Kết quả thanh toán <i class="ion-ios-arrow-forward"></i></span>
                    </p>
                    <h1 class="mb-3 bread">Kết quả thanh toán</h1>
                </div>
            </div>
        </div>
    </section>

    <section class="ftco-section">
        <div class="container">
            <div class="result-container">
                <div class="result-card">
                    <c:choose>
                        <c:when test="${paymentSuccess}">
                            <div class="result-icon success">
                                <i class="ion-ios-checkmark-circle"></i>
                            </div>
                            <h2 class="text-success">Thanh toán thành công!</h2>
                            <p>Cảm ơn bạn đã thanh toán. Đơn đặt xe của bạn đã được xác nhận.</p>
                            
                            <div class="transaction-info">
                                <h5 class="mb-3">Thông tin giao dịch</h5>
                                <div class="info-row">
                                    <strong>Mã giao dịch:</strong>
                                    <span>${transactionNo}</span>
                                </div>
                                <div class="info-row">
                                    <strong>Số tiền:</strong>
                                    <span class="text-success">
                                        <fmt:formatNumber value="${amount}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                    </span>
                                </div>
                                <div class="info-row">
                                    <strong>Mã đơn đặt xe:</strong>
                                    <span>${booking.bookingReference}</span>
                                </div>
                                <div class="info-row">
                                    <strong>Trạng thái:</strong>
                                    <span class="badge badge-success">Đã thanh toán</span>
                                </div>
                            </div>
                            
                            <div class="mt-4">
                                <a href="booking?action=view&id=${booking.bookingId}" class="btn btn-primary py-3 px-5 mr-2">
                                    <i class="ion-ios-document"></i> Xem chi tiết đơn
                                </a>
                                <a href="index.jsp" class="btn btn-secondary py-3 px-5">
                                    <i class="ion-ios-home"></i> Về trang chủ
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="result-icon failed">
                                <i class="ion-ios-close-circle"></i>
                            </div>
                            <h2 class="text-danger">Thanh toán thất bại!</h2>
                            <p>Rất tiếc, giao dịch của bạn không thành công. Vui lòng thử lại.</p>
                            
                            <div class="alert alert-danger mt-3">
                                <strong>Lý do:</strong> ${error != null ? error : 'Giao dịch bị hủy hoặc thất bại'}
                            </div>
                            
                            <div class="mt-4">
                                <c:if test="${booking != null}">
                                    <a href="booking?action=view&id=${booking.bookingId}" class="btn btn-warning py-3 px-5 mr-2">
                                        <i class="ion-ios-refresh"></i> Thử lại
                                    </a>
                                </c:if>
                                <a href="booking?action=list" class="btn btn-secondary py-3 px-5">
                                    <i class="ion-ios-list"></i> Danh sách đặt xe
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>
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
    
    <%@ include file="includes/footer.jsp" %>
</body>
</html>
