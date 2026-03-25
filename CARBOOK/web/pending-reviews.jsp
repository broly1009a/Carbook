<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đánh giá chờ duyệt - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .stars {
            color: #ffc107;
        }
        .stars-empty {
            color: #ddd;
        }
        .review-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            background: #fff;
        }
        .review-header {
            display: flex;
            justify-content: space-between;
            align-items: start;
            margin-bottom: 15px;
        }
        .review-info {
            flex: 1;
        }
        .review-actions {
            display: flex;
            gap: 10px;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row mb-4">
                <div class="col-md-12">
                    <h2>Đánh giá chờ duyệt</h2>
                    <p class="text-muted">Quản lý các đánh giá từ khách hàng</p>
                </div>
            </div>
            
            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible fade show">
                    ${param.success}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>
            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${param.error}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>
            
            <!-- Pending Reviews List -->
            <div class="row">
                <div class="col-md-12">
                    <c:choose>
                        <c:when test="${empty pendingReviews}">
                            <div class="card">
                                <div class="card-body text-center py-5">
                                    <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                    <p class="text-muted">Không có đánh giá nào chờ duyệt.</p>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="review" items="${pendingReviews}">
                                <div class="review-card">
                                    <div class="review-header">
                                        <div class="review-info">
                                            <!-- Rating Stars -->
                                            <div class="mb-2">
                                                <c:forEach begin="1" end="5" var="i">
                                                    <span class="${i <= review.rating ? 'stars' : 'stars-empty'}" style="font-size: 24px;">★</span>
                                                </c:forEach>
                                                <span class="ml-2 text-muted">${review.rating}/5</span>
                                            </div>
                                            
                                            <!-- Customer and Booking Info -->
                                            <div class="mb-2">
                                                <strong>Khách hàng:</strong> 
                                                <c:choose>
                                                    <c:when test="${not empty review.customer}">
                                                        ${review.customer.fullName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Không xác định
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                            
                                            <c:if test="${not empty review.booking}">
                                                <div class="mb-2">
                                                    <strong>Mã đặt xe:</strong> ${review.booking.bookingReference}
                                                </div>
                                                
                                                <c:if test="${not empty review.booking.car}">
                                                    <div class="mb-2">
                                                        <strong>Xe:</strong> ${review.booking.car.brand} ${review.booking.car.model} 
                                                        <span class="text-muted">(${review.booking.car.licensePlate})</span>
                                                    </div>
                                                </c:if>
                                            </c:if>
                                            
                                            <div class="text-muted small">
                                                <i class="far fa-clock"></i> 
                                                <fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                            </div>
                                        </div>
                                        
                                        <!-- Status Badge -->
                                        <div>
                                            <span class="badge badge-warning" style="font-size: 14px;">Chờ duyệt</span>
                                        </div>
                                    </div>
                                    
                                    <!-- Review Comment -->
                                    <div class="mb-3">
                                        <strong>Nội dung đánh giá:</strong>
                                        <p class="mt-2 mb-0">${review.comment}</p>
                                    </div>
                                    
                                    <!-- Action Buttons -->
                                    <div class="review-actions">
                                        <a href="review?action=approve&id=${review.reviewId}" 
                                           class="btn btn-success" 
                                           onclick="return confirm('Bạn có chắc muốn duyệt đánh giá này?')">
                                            <i class="fas fa-check"></i> Duyệt
                                        </a>
                                        <a href="review?action=delete&id=${review.reviewId}" 
                                           class="btn btn-danger" 
                                           onclick="return confirm('Bạn có chắc muốn xóa đánh giá này?')">
                                            <i class="fas fa-trash"></i> Xóa
                                        </a>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
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
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBVWaKrjvy3MaE7SQ74_uJiULgl1JY0H2s&sensor=false"></script>
    <script src="js/google-map.js"></script>
    <script src="js/main.js"></script>
</body>
</html>
