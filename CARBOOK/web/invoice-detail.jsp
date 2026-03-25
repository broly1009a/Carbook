<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết hóa đơn - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .invoice-box {
            background: white;
            border: 1px solid #eee;
            border-radius: 8px;
            padding: 30px;
            margin: 20px 0;
        }
        .invoice-header {
            border-bottom: 2px solid #01d28e;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .company-info {
            text-align: right;
        }
        .invoice-title {
            font-size: 32px;
            font-weight: bold;
            color: #01d28e;
        }
        .invoice-details {
            margin: 30px 0;
        }
        .detail-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            border-bottom: 1px solid #f5f5f5;
        }
        .detail-label {
            font-weight: 600;
            color: #666;
        }
        .detail-value {
            color: #333;
        }
        .invoice-table {
            margin: 30px 0;
        }
        .invoice-table table {
            width: 100%;
        }
        .invoice-table th {
            background: #f8f9fa;
            padding: 15px;
            text-align: left;
            font-weight: 600;
        }
        .invoice-table td {
            padding: 15px;
            border-bottom: 1px solid #f5f5f5;
        }
        .totals-section {
            margin-top: 30px;
            float: right;
            width: 350px;
        }
        .total-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
        }
        .total-row.grand-total {
            border-top: 2px solid #01d28e;
            font-size: 20px;
            font-weight: bold;
            color: #01d28e;
            padding-top: 15px;
        }
        .invoice-footer {
            clear: both;
            margin-top: 80px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            text-align: center;
            color: #999;
        }
        .action-buttons {
            margin: 20px 0;
        }
        @media print {
            .action-buttons, .navbar, .footer {
                display: none;
            }
        }
    </style>
</head>
<body>
 
    
    <section class="ftco-section" style="padding: 50px 0;">
        <div class="container">
            <!-- Action Buttons -->
            <div class="action-buttons">
                <a href="invoice" class="btn btn-secondary">
                    <i class="fas fa-arrow-left"></i> Quay lại
                </a>
                <a href="invoice?action=print&id=${invoice.invoiceId}" class="btn btn-primary" target="_blank">
                    <i class="fas fa-print"></i> In hóa đơn
                </a>
                <button onclick="window.print()" class="btn btn-info">
                    <i class="fas fa-download"></i> Xuất PDF
                </button>
                <c:if test="${sessionScope.user.roleId == 1}">
                    <a href="invoice?action=delete&id=${invoice.invoiceId}" 
                       class="btn btn-danger" 
                       onclick="return confirm('Bạn có chắc muốn xóa hóa đơn này?')">
                        <i class="fas fa-trash"></i> Xóa
                    </a>
                </c:if>
            </div>
            
            <!-- Invoice Box -->
            <div class="invoice-box">
                <!-- Header -->
                <div class="invoice-header">
                    <div class="row">
                        <div class="col-md-6">
                            <h1 class="invoice-title">HÓA ĐƠN</h1>
                            <p style="font-size: 18px; color: #666;">
                                ${invoice.invoiceNumber}
                            </p>
                        </div>
                        <div class="col-md-6 company-info">
                            <h3 style="color: #01d28e; font-weight: bold;">CARBOOK</h3>
                            <p style="margin: 5px 0;">Dịch vụ cho thuê xe ô tô</p>
                            <p style="margin: 5px 0;">Địa chỉ: Hà Nội, Việt Nam</p>
                            <p style="margin: 5px 0;">Điện thoại: 1900 xxxx</p>
                            <p style="margin: 5px 0;">Email: contact@carbook.com</p>
                        </div>
                    </div>
                </div>
                
                <!-- Customer Info -->
                <div class="row">
                    <div class="col-md-6">
                        <h5 style="font-weight: bold; margin-bottom: 15px;">Thông tin khách hàng:</h5>
                        <p><strong>Họ tên:</strong> ${booking.customer.fullName}</p>
                        <p><strong>Email:</strong> ${booking.customer.email}</p>
                        <p><strong>Điện thoại:</strong> ${booking.customer.phoneNumber}</p>
                        <c:if test="${not empty booking.customer.address}">
                            <p><strong>Địa chỉ:</strong> ${booking.customer.address}</p>
                        </c:if>
                    </div>
                    <div class="col-md-6">
                        <h5 style="font-weight: bold; margin-bottom: 15px;">Thông tin hóa đơn:</h5>
                        <div class="detail-row">
                            <span class="detail-label">Ngày tạo:</span>
                            <span class="detail-value">
                                <fmt:formatDate value="${invoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm"/>
                            </span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Hạn thanh toán:</span>
                            <span class="detail-value">
                                <fmt:formatDate value="${invoice.dueDate}" pattern="dd/MM/yyyy"/>
                            </span>
                        </div>
                        <div class="detail-row">
                            <span class="detail-label">Trạng thái:</span>
                            <span class="detail-value">
                                <span class="badge badge-${invoice.statusBadgeClass}">
                                    ${invoice.status}
                                </span>
                            </span>
                        </div>
                    </div>
                </div>
                
                <!-- Booking Details -->
                <div class="invoice-table">
                    <h5 style="font-weight: bold; margin: 30px 0 15px 0;">Chi tiết dịch vụ:</h5>
                    <table>
                        <thead>
                            <tr>
                                <th>Mô tả</th>
                                <th style="text-align: right;">Số lượng</th>
                                <th style="text-align: right;">Đơn giá</th>
                                <th style="text-align: right;">Thành tiền</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <strong>Thuê xe ${booking.car.model.brand.brandName} ${booking.car.model.modelName}</strong><br>
                                    <small>Biển số: ${booking.car.licensePlate}</small><br>
                                    <small>Thời gian: 
                                        <fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy HH:mm"/> -
                                        <fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy HH:mm"/>
                                    </small><br>
                                    <small>Địa điểm nhận: ${booking.pickupLocation}</small><br>
                                    <small>Địa điểm trả: ${booking.returnLocation}</small>
                                </td>
                                <td style="text-align: right;">1</td>
                                <td style="text-align: right;">
                                    <fmt:formatNumber value="${invoice.subTotal}" type="number" groupingUsed="true"/> VNĐ
                                </td>
                                <td style="text-align: right;">
                                    <fmt:formatNumber value="${invoice.subTotal}" type="number" groupingUsed="true"/> VNĐ
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                
                <!-- Totals -->
                <div class="totals-section">
                    <div class="total-row">
                        <span>Tạm tính:</span>
                        <span><fmt:formatNumber value="${invoice.subTotal}" type="number" groupingUsed="true"/> VNĐ</span>
                    </div>
                    <div class="total-row">
                        <span>Thuế (VAT):</span>
                        <span><fmt:formatNumber value="${invoice.taxAmount}" type="number" groupingUsed="true"/> VNĐ</span>
                    </div>
                    <c:if test="${invoice.discountAmount > 0}">
                        <div class="total-row">
                            <span>Giảm giá:</span>
                            <span style="color: #dc3545;">
                                -<fmt:formatNumber value="${invoice.discountAmount}" type="number" groupingUsed="true"/> VNĐ
                            </span>
                        </div>
                    </c:if>
                    <div class="total-row grand-total">
                        <span>TỔNG CỘNG:</span>
                        <span><fmt:formatNumber value="${invoice.totalAmount}" type="number" groupingUsed="true"/> VNĐ</span>
                    </div>
                </div>
                
                <div style="clear: both;"></div>
                
                <!-- Payment Info -->
                <c:if test="${not empty payment}">
                    <div style="margin-top: 30px; padding: 20px; background: #f8f9fa; border-radius: 5px;">
                        <h5 style="font-weight: bold; margin-bottom: 15px;">Thông tin thanh toán:</h5>
                        <p><strong>Mã giao dịch:</strong> ${payment.transactionId}</p>
                        <p><strong>Ngày thanh toán:</strong> 
                            <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </p>
                        <p><strong>Số tiền:</strong> 
                            <fmt:formatNumber value="${payment.amount}" type="number" groupingUsed="true"/> VNĐ
                        </p>
                    </div>
                </c:if>
                
                <!-- Notes -->
                <c:if test="${not empty invoice.notes}">
                    <div style="margin-top: 30px;">
                        <h5 style="font-weight: bold; margin-bottom: 10px;">Ghi chú:</h5>
                        <p style="color: #666;">${invoice.notes}</p>
                    </div>
                </c:if>
                
                <!-- Footer -->
                <div class="invoice-footer">
                    <p>Cảm ơn quý khách đã sử dụng dịch vụ của CARBOOK!</p>
                    <p>Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi.</p>
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
