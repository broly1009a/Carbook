<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo hóa đơn - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row">
                <div class="col-md-8 offset-md-2">
                    <h2 class="mb-4">Tạo hóa đơn mới</h2>
                    
                    <c:if test="${not empty sessionScope.error}">
                        <div class="alert alert-danger">
                            ${sessionScope.error}
                            <c:remove var="error" scope="session"/>
                        </div>
                    </c:if>
                    
                    <div class="card">
                        <div class="card-body">
                            <form action="invoice" method="post" id="invoiceForm">
                                <input type="hidden" name="action" value="create">
                                
                                <!-- Booking Selection -->
                                <div class="form-group">
                                    <label for="bookingId">Chọn đơn đặt xe <span class="text-danger">*</span></label>
                                    <select name="bookingId" id="bookingId" class="form-control" required>
                                        <option value="">-- Chọn đơn đặt xe --</option>
                                        <c:if test="${not empty booking}">
                                            <option value="${booking.bookingId}" selected>
                                                Đơn #${booking.bookingId} - Tổng tiền: 
                                                <fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="₫"/>
                                            </option>
                                        </c:if>
                                        <c:if test="${empty booking}">
                                            <c:forEach var="b" items="${bookings}">
                                                <option value="${b.bookingId}">
                                                    Đơn #${b.bookingId} - 
                                                    <fmt:formatNumber value="${b.totalAmount}" type="currency" currencySymbol="₫"/>
                                                </option>
                                            </c:forEach>
                                        </c:if>
                                    </select>
                                    <small class="form-text text-muted">
                                        Chọn đơn đặt xe cần tạo hóa đơn
                                    </small>
                                </div>
                                
                                <!-- Payment ID (Optional) -->
                                <div class="form-group">
                                    <label for="paymentId">Mã thanh toán (tùy chọn)</label>
                                    <input type="number" 
                                           name="paymentId" 
                                           id="paymentId" 
                                           class="form-control" 
                                           value="${param.paymentId}"
                                           placeholder="Nhập mã thanh toán nếu đã có">
                                    <small class="form-text text-muted">
                                        Nếu thanh toán đã được thực hiện, nhập mã thanh toán để liên kết
                                    </small>
                                </div>
                                
                                <!-- Tax Rate -->
                                <div class="form-group">
                                    <label for="taxRate">Thuế suất VAT (%)</label>
                                    <input type="number" 
                                           name="taxRate" 
                                           id="taxRate" 
                                           class="form-control" 
                                           value="10.00"
                                           step="0.01"
                                           min="0"
                                           max="100">
                                    <small class="form-text text-muted">
                                        Mặc định: 10%
                                    </small>
                                </div>
                                
                                <!-- Discount Amount -->
                                <div class="form-group">
                                    <label for="discountAmount">Giảm giá (VNĐ)</label>
                                    <input type="number" 
                                           name="discountAmount" 
                                           id="discountAmount" 
                                           class="form-control" 
                                           value="0"
                                           step="1000"
                                           min="0"
                                           placeholder="0">
                                    <small class="form-text text-muted">
                                        Số tiền giảm giá (nếu có)
                                    </small>
                                </div>
                                
                                <!-- Notes -->
                                <div class="form-group">
                                    <label for="notes">Ghi chú</label>
                                    <textarea name="notes" 
                                              id="notes" 
                                              class="form-control" 
                                              rows="3"
                                              placeholder="Nhập ghi chú cho hóa đơn (nếu có)"></textarea>
                                </div>
                                
                                <!-- Calculation Preview -->
                                <c:if test="${not empty booking}">
                                    <div class="alert alert-info">
                                        <h6>Dự tính hóa đơn:</h6>
                                        <p class="mb-1">
                                            <strong>Tạm tính:</strong> 
                                            <fmt:formatNumber value="${booking.totalAmount}" type="currency" currencySymbol="₫"/>
                                        </p>
                                        <p class="mb-1">
                                            <strong>Thuế (10%):</strong> 
                                            <fmt:formatNumber value="${booking.totalAmount * 0.1}" type="currency" currencySymbol="₫"/>
                                        </p>
                                        <hr>
                                        <p class="mb-0">
                                            <strong>Tổng cộng:</strong> 
                                            <strong class="text-success">
                                                <fmt:formatNumber value="${booking.totalAmount * 1.1}" type="currency" currencySymbol="₫"/>
                                            </strong>
                                        </p>
                                    </div>
                                </c:if>
                                
                                <!-- Action Buttons -->
                                <div class="form-group">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-save"></i> Tạo hóa đơn
                                    </button>
                                    <a href="invoice" class="btn btn-secondary">
                                        <i class="fas fa-times"></i> Hủy
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
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
    <script src="js/main.js"></script>
    
    <script>
        // Form validation
        document.getElementById('invoiceForm').addEventListener('submit', function(e) {
            const bookingId = document.getElementById('bookingId').value;
            
            if (!bookingId) {
                e.preventDefault();
                alert('Vui lòng chọn đơn đặt xe!');
                return false;
            }
            
            return confirm('Bạn có chắc muốn tạo hóa đơn cho đơn đặt xe này?');
        });
        
        // Auto-calculate total when tax or discount changes
        const taxRate = document.getElementById('taxRate');
        const discountAmount = document.getElementById('discountAmount');
        
        function updateCalculation() {
            // This would require booking amount, can be enhanced with AJAX
            console.log('Tax Rate:', taxRate.value);
            console.log('Discount:', discountAmount.value);
        }
        
        if (taxRate) {
            taxRate.addEventListener('input', updateCalculation);
        }
        
        if (discountAmount) {
            discountAmount.addEventListener('input', updateCalculation);
        }
    </script>
</body>
</html>
