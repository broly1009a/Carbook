<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đánh giá - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .star-rating {
            font-size: 2rem;
            color: #ddd;
            cursor: pointer;
        }
        .star-rating .star {
            display: inline-block;
            transition: color 0.2s;
        }
        .star-rating .star.filled {
            color: #ffc107;
        }
        .star-rating .star:hover,
        .star-rating .star:hover ~ .star {
            color: #ffc107;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <h2>Đánh giá</h2>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            
            <div class="row">
                <div class="col-md-8">
                    <div class="card mb-3">
                        <div class="card-header">
                            <h5>Thông tin đơn đặt xe</h5>
                        </div>
                        <div class="card-body">
                            <p>
                                <strong>Mã đơn:</strong> ${booking.bookingReference}<br>
                                <strong>Xe:</strong> ${booking.car.licensePlate}<br>
                                <strong>Ngày nhận:</strong> <fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy"/><br>
                                <strong>Ngày trả:</strong> <fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy"/>
                            </p>
                        </div>
                    </div>
                    
                    <form action="review" method="post">
                        <input type="hidden" name="action" value="create">
                        <input type="hidden" name="bookingId" value="${booking.bookingId}">
                        <input type="hidden" name="rating" id="ratingValue" value="5">
                        
                        <div class="card mb-3">
                            <div class="card-header">
                                <h5>Đánh giá của bạn</h5>
                            </div>
                            <div class="card-body">
                                <div class="form-group">
                                    <label>Chất lượng xe <span class="text-danger">*</span></label>
                                    <div class="star-rating" id="starRating">
                                        <span class="star filled" data-value="1">★</span>
                                        <span class="star filled" data-value="2">★</span>
                                        <span class="star filled" data-value="3">★</span>
                                        <span class="star filled" data-value="4">★</span>
                                        <span class="star filled" data-value="5">★</span>
                                    </div>
                                </div>
                                
                                <div class="form-group">
                                    <label>Nhận xét <span class="text-danger">*</span></label>
                                    <textarea class="form-control" name="comment" rows="5" required placeholder="Chia sẻ trải nghiệm của bạn về xe và dịch vụ..."></textarea>
                                </div>
                            </div>
                        </div>
                        
                        <button type="submit" class="btn btn-primary btn-lg btn-block">Gửi đánh giá</button>
                        <a href="booking?action=detail&id=${booking.bookingId}" class="btn btn-secondary btn-lg btn-block">Quay lại</a>
                    </form>
                </div>
                
                <div class="col-md-4">
                    <div class="card">
                        <div class="card-header">
                            <h5>Lưu ý</h5>
                        </div>
                        <div class="card-body">
                            <ul class="list-unstyled">
                                <li>✓ Đánh giá trung thực</li>
                                <li>✓ Nội dung lịch sự</li>
                                <li>✓ Đánh giá sẽ được kiểm duyệt</li>
                                <li>✓ Hiển thị sau khi được duyệt</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <%@ include file="includes/footer.jsp" %>
    
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function() {
            const stars = $('.star-rating .star');
            const ratingValue = $('#ratingValue');
            
            stars.on('click', function() {
                const value = $(this).data('value');
                ratingValue.val(value);
                
                stars.removeClass('filled');
                stars.each(function() {
                    if ($(this).data('value') <= value) {
                        $(this).addClass('filled');
                    }
                });
            });
            
            stars.on('mouseenter', function() {
                const value = $(this).data('value');
                stars.removeClass('filled');
                stars.each(function() {
                    if ($(this).data('value') <= value) {
                        $(this).addClass('filled');
                    }
                });
            });
            
            $('.star-rating').on('mouseleave', function() {
                const currentValue = ratingValue.val();
                stars.removeClass('filled');
                stars.each(function() {
                    if ($(this).data('value') <= currentValue) {
                        $(this).addClass('filled');
                    }
                });
            });
        });
    </script>
</body>
</html>
