<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>In hóa đơn - ${invoice.invoiceNumber}</title>
     <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Arial', sans-serif;
            font-size: 14px;
            line-height: 1.6;
            color: #333;
            padding: 20px;
        }
        
        .invoice-container {
            max-width: 800px;
            margin: 0 auto;
            background: white;
            padding: 40px;
            border: 1px solid #ddd;
        }
        
        .invoice-header {
            border-bottom: 3px solid #01d28e;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        
        .invoice-header::after {
            content: "";
            display: table;
            clear: both;
        }
        
        .company-logo {
            float: left;
            width: 50%;
        }
        
        .company-logo h1 {
            color: #01d28e;
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        .company-logo p {
            color: #666;
            font-size: 13px;
            margin: 3px 0;
        }
        
        .invoice-info {
            float: right;
            text-align: right;
            width: 45%;
        }
        
        .invoice-title {
            font-size: 32px;
            font-weight: bold;
            color: #01d28e;
            margin-bottom: 10px;
        }
        
        .invoice-number {
            font-size: 16px;
            color: #666;
            margin-bottom: 5px;
        }
        
        .invoice-date {
            font-size: 13px;
            color: #999;
        }
        
        .parties {
            margin: 30px 0;
        }
        
        .parties::after {
            content: "";
            display: table;
            clear: both;
        }
        
        .party {
            float: left;
            width: 48%;
        }
        
        .party.right {
            float: right;
        }
        
        .party h3 {
            font-size: 14px;
            color: #01d28e;
            margin-bottom: 10px;
            text-transform: uppercase;
            border-bottom: 2px solid #f5f5f5;
            padding-bottom: 5px;
        }
        
        .party p {
            font-size: 13px;
            margin: 5px 0;
        }
        
        .party strong {
            display: inline-block;
            width: 120px;
            color: #666;
        }
        
        .status-badge {
            display: inline-block;
            padding: 5px 15px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            text-transform: uppercase;
        }
        
        .status-paid {
            background: #d4edda;
            color: #155724;
        }
        
        .status-unpaid {
            background: #fff3cd;
            color: #856404;
        }
        
        .status-overdue {
            background: #f8d7da;
            color: #721c24;
        }
        
        .invoice-table {
            width: 100%;
            margin: 30px 0;
            border-collapse: collapse;
        }
        
        .invoice-table thead th {
            background: #f8f9fa;
            padding: 12px;
            text-align: left;
            font-weight: 600;
            font-size: 13px;
            border-bottom: 2px solid #01d28e;
        }
        
        .invoice-table tbody td {
            padding: 15px 12px;
            border-bottom: 1px solid #f5f5f5;
            font-size: 13px;
        }
        
        .item-description {
            font-weight: 600;
            margin-bottom: 5px;
        }
        
        .item-details {
            font-size: 12px;
            color: #666;
            margin: 3px 0;
        }
        
        .totals {
            float: right;
            width: 350px;
            margin-top: 20px;
        }
        
        .total-row {
            display: flex;
            justify-content: space-between;
            padding: 10px 0;
            font-size: 14px;
        }
        
        .total-row.subtotal {
            border-top: 1px solid #eee;
        }
        
        .total-row.grand-total {
            border-top: 2px solid #01d28e;
            margin-top: 10px;
            padding-top: 15px;
            font-size: 18px;
            font-weight: bold;
            color: #01d28e;
        }
        
        .payment-info {
            clear: both;
            margin-top: 50px;
            padding: 20px;
            background: #f8f9fa;
            border-left: 4px solid #01d28e;
        }
        
        .payment-info h3 {
            font-size: 14px;
            color: #01d28e;
            margin-bottom: 15px;
            text-transform: uppercase;
        }
        
        .payment-info p {
            font-size: 13px;
            margin: 5px 0;
        }
        
        .notes {
            margin-top: 30px;
            padding: 15px;
            background: #fff9e6;
            border-left: 4px solid #ffc107;
        }
        
        .notes h3 {
            font-size: 14px;
            color: #856404;
            margin-bottom: 10px;
        }
        
        .notes p {
            font-size: 13px;
            color: #666;
        }
        
        .signature-section {
            margin-top: 50px;
            clear: both;
        }
        
        .signature-section::after {
            content: "";
            display: table;
            clear: both;
        }
        
        .signature {
            float: left;
            width: 45%;
            text-align: center;
        }
        
        .signature.right {
            float: right;
        }
        
        .signature p {
            margin-bottom: 80px;
            font-size: 13px;
            font-weight: 600;
            color: #333;
        }
        
        .signature-line {
            border-bottom: 1px solid #333;
            width: 200px;
            margin: 0 auto;
        }
        
        .signature-name {
            font-size: 12px;
            color: #999;
            margin-top: 5px;
        }
        
        .invoice-footer {
            clear: both;
            margin-top: 50px;
            padding-top: 20px;
            border-top: 1px solid #eee;
            text-align: center;
            color: #999;
            font-size: 12px;
        }
        
        .invoice-footer p {
            margin: 5px 0;
        }
        
        @media print {
            body {
                padding: 0;
            }
            
            .invoice-container {
                border: none;
                padding: 20px;
            }
            
            .no-print {
                display: none;
            }
        }
        
        @page {
            margin: 1cm;
            size: A4;
        }
        
        
        
        
    </style>
</head>
<body>
    <!-- Print Button -->
    <div class="no-print" style="text-align: center; margin-bottom: 20px;">
        <button onclick="window.print()" style="padding: 10px 30px; font-size: 16px; background: #01d28e; color: white; border: none; border-radius: 5px; cursor: pointer;">
            In hóa đơn
        </button>
        <button onclick="window.close()" style="padding: 10px 30px; font-size: 16px; background: #6c757d; color: white; border: none; border-radius: 5px; cursor: pointer; margin-left: 10px;">
            Đóng
        </button>
    </div>
    
    <div class="invoice-container">
        <!-- Header -->
        <div class="invoice-header">
            <div class="company-logo">
                <h1>CARBOOK</h1>
                <p>Dịch vụ cho thuê xe ô tô</p>
                <p>Địa chỉ: Hà Nội, Việt Nam</p>
                <p>Điện thoại: 1900 xxxx</p>
                <p>Email: contact@carbook.com</p>
                <p>Website: www.carbook.com</p>
            </div>
            <div class="invoice-info">
                <div class="invoice-title">HÓA ĐƠN</div>
                <div class="invoice-number">${invoice.invoiceNumber}</div>
                <div class="invoice-date">
                    Ngày: <fmt:formatDate value="${invoice.invoiceDate}" pattern="dd/MM/yyyy"/>
                </div>
            </div>
        </div>
        
        <!-- Parties -->
        <div class="parties">
            <div class="party">
                <h3>Thông tin khách hàng</h3>
                <p><strong>Họ và tên:</strong> ${booking.customer.fullName}</p>
                <p><strong>Email:</strong> ${booking.customer.email}</p>
                <p><strong>Điện thoại:</strong> ${booking.customer.phoneNumber}</p>
                <c:if test="${not empty booking.customer.address}">
                    <p><strong>Địa chỉ:</strong> ${booking.customer.address}</p>
                </c:if>
            </div>
            
            <div class="party right">
                <h3>Chi tiết hóa đơn</h3>
                <p><strong>Mã hóa đơn:</strong> ${invoice.invoiceNumber}</p>
                <p><strong>Ngày tạo:</strong> 
                    <fmt:formatDate value="${invoice.invoiceDate}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
                <p><strong>Hạn thanh toán:</strong> 
                    <fmt:formatDate value="${invoice.dueDate}" pattern="dd/MM/yyyy"/>
                </p>
                <p><strong>Trạng thái:</strong>
                    <c:choose>
                        <c:when test="${invoice.status == 'Paid'}">
                            <span class="status-badge status-paid">Đã thanh toán</span>
                        </c:when>
                        <c:when test="${invoice.status == 'Unpaid'}">
                            <span class="status-badge status-unpaid">Chưa thanh toán</span>
                        </c:when>
                        <c:otherwise>
                            <span class="status-badge status-overdue">${invoice.status}</span>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
        </div>
        
        <!-- Items Table -->
        <table class="invoice-table">
            <thead>
                <tr>
                    <th style="width: 50%;">MÔ TẢ DỊCH VỤ</th>
                    <th style="width: 15%; text-align: center;">SỐ LƯỢNG</th>
                    <th style="width: 15%; text-align: right;">ĐƠN GIÁ</th>
                    <th style="width: 20%; text-align: right;">THÀNH TIỀN</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <div class="item-description">
                            Thuê xe ${booking.car.model.brand.brandName} ${booking.car.model.modelName} (${booking.car.model.year})
                        </div>
                        <div class="item-details">Biển số: ${booking.car.licensePlate}</div>
                        <div class="item-details">
                            Thời gian: <fmt:formatDate value="${booking.pickupDate}" pattern="dd/MM/yyyy HH:mm"/> 
                            đến <fmt:formatDate value="${booking.returnDate}" pattern="dd/MM/yyyy HH:mm"/>
                        </div>
                        <div class="item-details">Nhận xe: ${booking.pickupLocation}</div>
                        <div class="item-details">Trả xe: ${booking.returnLocation}</div>
                        <div class="item-details">Mã đặt xe: #${booking.bookingId}</div>
                    </td>
                    <td style="text-align: center;">1</td>
                    <td style="text-align: right;"><fmt:formatNumber value="${invoice.subTotal}" type="number" groupingUsed="true"/> VNĐ</td>
                    <td style="text-align: right;"><fmt:formatNumber value="${invoice.subTotal}" type="number" groupingUsed="true"/> VNĐ</td>
                </tr>
            </tbody>
        </table>
        
        <!-- Totals -->
        <div class="totals">
            <div class="total-row subtotal">
                <span>Tạm tính:</span>
                <span><fmt:formatNumber value="${invoice.subTotal}" type="number" groupingUsed="true"/> VNĐ</span>
            </div>
            <div class="total-row">
                <span>Thuế VAT:</span>
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
        
        <!-- Payment Info -->
        <c:if test="${not empty payment}">
            <div class="payment-info">
                <h3>Thông tin thanh toán</h3>
                <p><strong>Mã giao dịch:</strong> ${payment.transactionId}</p>
                <p><strong>Ngày thanh toán:</strong> 
                    <fmt:formatDate value="${payment.paymentDate}" pattern="dd/MM/yyyy HH:mm"/>
                </p>
                <p><strong>Số tiền đã thanh toán:</strong> 
                    <strong style="color: #01d28e;">
                        <fmt:formatNumber value="${payment.amount}" type="number" groupingUsed="true"/> VNĐ
                    </strong>
                </p>
            </div>
        </c:if>
        
        <!-- Notes -->
        <c:if test="${not empty invoice.notes}">
            <div class="notes">
                <h3>Ghi chú</h3>
                <p>${invoice.notes}</p>
            </div>
        </c:if>
        
        <!-- Signature Section -->
        <div class="signature-section">
            <div class="signature">
                <p>Người lập phiếu</p>
                <div class="signature-line"></div>
                <div class="signature-name">(Ký và ghi rõ họ tên)</div>
            </div>
            <div class="signature right">
                <p>Khách hàng</p>
                <div class="signature-line"></div>
                <div class="signature-name">(Ký và ghi rõ họ tên)</div>
            </div>
        </div>
        
        <!-- Footer -->
        <div class="invoice-footer">
            <p><strong>Cảm ơn quý khách đã sử dụng dịch vụ của CARBOOK!</strong></p>
            <p>Nếu có bất kỳ thắc mắc nào, vui lòng liên hệ với chúng tôi qua hotline 1900 xxxx hoặc email contact@carbook.com</p>
            <p style="margin-top: 15px; font-size: 11px;">
                Hóa đơn này được tạo tự động bởi hệ thống CARBOOK vào ngày <fmt:formatDate value="${invoice.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
            </p>
        </div>
    </div>
    
    <script>
        // Auto print on load (optional)
        // window.onload = function() { window.print(); }
    </script>
</body>
</html>
