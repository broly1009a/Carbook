<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Phản hồi Liên hệ - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .respond-card {
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .original-message {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #01d28e;
            margin-bottom: 20px;
        }
        .info-label {
            font-weight: 600;
            color: #666;
            margin-bottom: 5px;
        }
        .info-value {
            color: #333;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 px-0">
                    <%@ include file="includes/sidebar.jsp" %>
                </div>
                
                <!-- Main Content -->
                <div class="col-md-9 col-lg-10">
                    <div class="container-fluid">
                        <!-- Header -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h2><i class="icon-reply"></i> Phản hồi Liên hệ #${contact.contactId}</h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="contact?action=view&id=${contact.contactId}" class="btn btn-secondary">
                                    <i class="icon-arrow-left"></i> Quay lại
                                </a>
                            </div>
                        </div>
                        
                        <!-- Success/Error Messages -->
                        <c:if test="${not empty sessionScope.success}">
                            <div class="alert alert-success alert-dismissible fade show" role="alert">
                                <i class="icon-check"></i> ${sessionScope.success}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:remove var="success" scope="session" />
                        </c:if>
                        
                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="icon-close"></i> ${sessionScope.error}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:remove var="error" scope="session" />
                        </c:if>
                        
                        <div class="row">
                            <div class="col-md-8">
                                <!-- Original Message -->
                                <div class="card respond-card mb-4">
                                    <div class="card-header bg-info text-white">
                                        <h5 class="mb-0"><i class="icon-comment"></i> Nội dung liên hệ gốc</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="row mb-3">
                                            <div class="col-md-6">
                                                <div class="info-label">Người gửi:</div>
                                                <div class="info-value"><strong>${contact.fullName}</strong></div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="info-label">Email:</div>
                                                <div class="info-value">
                                                    <a href="mailto:${contact.email}">${contact.email}</a>
                                                </div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="info-label">Tiêu đề:</div>
                                                <div class="info-value"><strong>${contact.subject}</strong></div>
                                            </div>
                                        </div>
                                        
                                        <div class="info-label">Nội dung:</div>
                                        <div class="original-message">
                                            ${contact.message}
                                        </div>
                                        
                                        <small class="text-muted">
                                            <i class="icon-clock-o"></i> Gửi lúc: ${contact.formattedCreatedDate}
                                        </small>
                                    </div>
                                </div>
                                
                                <!-- Response Form -->
                                <div class="card respond-card">
                                    <div class="card-header bg-success text-white">
                                        <h5 class="mb-0"><i class="icon-pencil"></i> Viết phản hồi</h5>
                                    </div>
                                    <div class="card-body">
                                        <form action="contact" method="post" onsubmit="return validateForm()">
                                            <input type="hidden" name="action" value="respond">
                                            <input type="hidden" name="contactId" value="${contact.contactId}">
                                            
                                            <div class="form-group">
                                                <label for="responseMessage">Nội dung phản hồi: <span class="text-danger">*</span></label>
                                                <textarea name="responseMessage" 
                                                          id="responseMessage"
                                                          class="form-control" 
                                                          rows="10" 
                                                          required
                                                          placeholder="Nhập nội dung phản hồi cho khách hàng..."></textarea>
                                                <small class="form-text text-muted">
                                                    Hãy viết phản hồi chuyên nghiệp và rõ ràng. Email sẽ được gửi tự động đến khách hàng.
                                                </small>
                                            </div>
                                            
                                            <div class="alert alert-info">
                                                <i class="icon-info"></i>
                                                <strong>Lưu ý:</strong> Sau khi gửi phản hồi, trạng thái liên hệ sẽ tự động chuyển sang "Đã giải quyết".
                                            </div>
                                            
                                            <div class="form-group">
                                                <button type="submit" class="btn btn-success btn-lg">
                                                    <i class="icon-paper-plane"></i> Gửi phản hồi
                                                </button>
                                                <a href="contact?action=view&id=${contact.contactId}" class="btn btn-secondary btn-lg">
                                                    <i class="icon-close"></i> Hủy
                                                </a>
                                            </div>
                                        </form>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Sidebar -->
                            <div class="col-md-4">
                                <!-- Quick Templates -->
                                <div class="card respond-card mb-4">
                                    <div class="card-header bg-warning text-white">
                                        <h5 class="mb-0"><i class="icon-file-text"></i> Mẫu phản hồi nhanh</h5>
                                    </div>
                                    <div class="card-body">
                                        <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-2" 
                                                onclick="insertTemplate('thankyou')">
                                            Cảm ơn & xác nhận
                                        </button>
                                        <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-2" 
                                                onclick="insertTemplate('inquiry')">
                                            Trả lời thắc mắc
                                        </button>
                                        <button type="button" class="btn btn-outline-primary btn-sm btn-block mb-2" 
                                                onclick="insertTemplate('resolved')">
                                            Đã giải quyết
                                        </button>
                                        <button type="button" class="btn btn-outline-primary btn-sm btn-block" 
                                                onclick="insertTemplate('followup')">
                                            Yêu cầu thông tin thêm
                                        </button>
                                    </div>
                                </div>
                                
                                <!-- Contact Info -->
                                <div class="card respond-card">
                                    <div class="card-header">
                                        <h5 class="mb-0"><i class="icon-info"></i> Thông tin liên hệ</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="info-label">Loại:</div>
                                        <div class="info-value">${contact.contactTypeDisplay}</div>
                                        
                                        <div class="info-label">Trạng thái:</div>
                                        <div class="mb-3">
                                            <span class="badge ${contact.statusBadgeClass}">${contact.status}</span>
                                        </div>
                                        
                                        <div class="info-label">Ưu tiên:</div>
                                        <div class="mb-3">
                                            <span class="badge ${contact.priorityBadgeClass}">${contact.priority}</span>
                                        </div>
                                        
                                        <c:if test="${not empty contact.phoneNumber}">
                                            <div class="info-label">Số điện thoại:</div>
                                            <div class="info-value">
                                                <a href="tel:${contact.phoneNumber}">${contact.phoneNumber}</a>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <script src="js/jquery.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
    <script>
        function validateForm() {
            var message = document.getElementById('responseMessage').value.trim();
            if (message.length < 10) {
                alert('Nội dung phản hồi quá ngắn. Vui lòng nhập ít nhất 10 ký tự.');
                return false;
            }
            return confirm('Bạn có chắc chắn muốn gửi phản hồi này?');
        }
        
        function insertTemplate(type) {
            var textarea = document.getElementById('responseMessage');
            var template = '';
            
            switch(type) {
                case 'thankyou':
                    template = 'Kính chào ${contact.fullName},\n\n' +
                               'Cảm ơn bạn đã liên hệ với CarBook. Chúng tôi đã nhận được yêu cầu của bạn về "${contact.subject}".\n\n' +
                               'Chúng tôi sẽ xem xét và phản hồi lại trong thời gian sớm nhất.\n\n' +
                               'Trân trọng,\n' +
                               'Đội ngũ CarBook';
                    break;
                case 'inquiry':
                    template = 'Kính chào ${contact.fullName},\n\n' +
                               'Cảm ơn bạn đã quan tâm đến dịch vụ của CarBook.\n\n' +
                               'Về câu hỏi "${contact.subject}" của bạn, chúng tôi xin trả lời như sau:\n\n' +
                               '[Nhập nội dung trả lời]\n\n' +
                               'Nếu bạn có thêm câu hỏi nào khác, vui lòng liên hệ lại với chúng tôi.\n\n' +
                               'Trân trọng,\n' +
                               'Đội ngũ CarBook';
                    break;
                case 'resolved':
                    template = 'Kính chào ${contact.fullName},\n\n' +
                               'Chúng tôi xin thông báo rằng vấn đề "${contact.subject}" của bạn đã được giải quyết.\n\n' +
                               '[Chi tiết giải pháp]\n\n' +
                               'Nếu vẫn còn vướng mắc, vui lòng liên hệ lại với chúng tôi.\n\n' +
                               'Trân trọng,\n' +
                               'Đội ngũ CarBook';
                    break;
                case 'followup':
                    template = 'Kính chào ${contact.fullName},\n\n' +
                               'Cảm ơn bạn đã liên hệ với CarBook về "${contact.subject}".\n\n' +
                               'Để có thể hỗ trợ bạn tốt hơn, chúng tôi cần thêm một số thông tin:\n' +
                               '- [Thông tin 1]\n' +
                               '- [Thông tin 2]\n\n' +
                               'Vui lòng cung cấp thêm thông tin hoặc liên hệ trực tiếp qua số điện thoại: +2 392 3929 210\n\n' +
                               'Trân trọng,\n' +
                               'Đội ngũ CarBook';
                    break;
            }
            
            textarea.value = template;
            textarea.focus();
        }
    </script>
</body>
</html>
