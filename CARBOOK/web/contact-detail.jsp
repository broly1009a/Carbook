<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Liên hệ - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .contact-detail-card {
            border: none;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
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
        .message-box {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #01d28e;
            margin-bottom: 20px;
        }
        .response-box {
            background: #e7f3ff;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #0066cc;
        }
        .status-update-form {
            background: #fff3cd;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
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
                                <h2><i class="icon-envelope"></i> Chi tiết Liên hệ #${contact.contactId}</h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="contact?action=list" class="btn btn-secondary">
                                    <i class="icon-arrow-left"></i> Quay lại
                                </a>
                                <c:if test="${!contact.responded}">
                                    <a href="contact?action=respond&id=${contact.contactId}" class="btn btn-success">
                                        <i class="icon-reply"></i> Phản hồi
                                    </a>
                                </c:if>
                                <a href="contact?action=delete&id=${contact.contactId}" 
                                   class="btn btn-danger"
                                   onclick="return confirm('Bạn có chắc chắn muốn xóa liên hệ này?')">
                                    <i class="icon-trash"></i> Xóa
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
                            <!-- Contact Information -->
                            <div class="col-md-8">
                                <div class="card contact-detail-card mb-4">
                                    <div class="card-header bg-primary text-white">
                                        <h5 class="mb-0"><i class="icon-user"></i> Thông tin người gửi</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="row">
                                            <div class="col-md-6">
                                                <div class="info-label">Họ và tên:</div>
                                                <div class="info-value"><strong>${contact.fullName}</strong></div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="info-label">Email:</div>
                                                <div class="info-value">
                                                    <a href="mailto:${contact.email}">${contact.email}</a>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="info-label">Số điện thoại:</div>
                                                <div class="info-value">
                                                    <c:choose>
                                                        <c:when test="${not empty contact.phoneNumber}">
                                                            <a href="tel:${contact.phoneNumber}">${contact.phoneNumber}</a>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Không có</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <div class="col-md-6">
                                                <div class="info-label">Loại liên hệ:</div>
                                                <div class="info-value">${contact.contactTypeDisplay}</div>
                                            </div>
                                            <div class="col-md-12">
                                                <div class="info-label">Tiêu đề:</div>
                                                <div class="info-value"><strong>${contact.subject}</strong></div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Message -->
                                <div class="card contact-detail-card mb-4">
                                    <div class="card-header bg-info text-white">
                                        <h5 class="mb-0"><i class="icon-comment"></i> Nội dung liên hệ</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="message-box">
                                            ${contact.message}
                                        </div>
                                        <small class="text-muted">
                                            <i class="icon-clock-o"></i> Gửi lúc: ${contact.formattedCreatedDate}
                                        </small>
                                    </div>
                                </div>
                                
                                <!-- Response (if exists) -->
                                <c:if test="${contact.responded}">
                                    <div class="card contact-detail-card mb-4">
                                        <div class="card-header bg-success text-white">
                                            <h5 class="mb-0"><i class="icon-reply"></i> Phản hồi</h5>
                                        </div>
                                        <div class="card-body">
                                            <div class="response-box">
                                                ${contact.responseMessage}
                                            </div>
                                            <small class="text-muted">
                                                <i class="icon-user"></i> Phản hồi bởi: 
                                                <c:choose>
                                                    <c:when test="${not empty contact.responder}">
                                                        ${contact.responder.fullName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        Admin
                                                    </c:otherwise>
                                                </c:choose>
                                                <br>
                                                <i class="icon-clock-o"></i> Vào lúc: ${contact.formattedRespondedDate}
                                            </small>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                            
                            <!-- Sidebar Information -->
                            <div class="col-md-4">
                                <!-- Status & Priority Update -->
                                <div class="card contact-detail-card mb-4">
                                    <div class="card-header bg-warning text-white">
                                        <h5 class="mb-0"><i class="icon-edit"></i> Cập nhật trạng thái</h5>
                                    </div>
                                    <div class="card-body">
                                        <form action="contact" method="get">
                                            <input type="hidden" name="action" value="update-status">
                                            <input type="hidden" name="id" value="${contact.contactId}">
                                            
                                            <div class="form-group">
                                                <label>Trạng thái:</label>
                                                <select name="status" class="form-control" required>
                                                    <option value="New" ${contact.status == 'New' ? 'selected' : ''}>Mới</option>
                                                    <option value="In Progress" ${contact.status == 'In Progress' ? 'selected' : ''}>Đang xử lý</option>
                                                    <option value="Resolved" ${contact.status == 'Resolved' ? 'selected' : ''}>Đã giải quyết</option>
                                                    <option value="Closed" ${contact.status == 'Closed' ? 'selected' : ''}>Đã đóng</option>
                                                    <option value="Spam" ${contact.status == 'Spam' ? 'selected' : ''}>Spam</option>
                                                </select>
                                            </div>
                                            
                                            <div class="form-group">
                                                <label>Ưu tiên:</label>
                                                <select name="priority" class="form-control" required>
                                                    <option value="Low" ${contact.priority == 'Low' ? 'selected' : ''}>Thấp</option>
                                                    <option value="Normal" ${contact.priority == 'Normal' ? 'selected' : ''}>Bình thường</option>
                                                    <option value="High" ${contact.priority == 'High' ? 'selected' : ''}>Cao</option>
                                                    <option value="Urgent" ${contact.priority == 'Urgent' ? 'selected' : ''}>Khẩn cấp</option>
                                                </select>
                                            </div>
                                            
                                            <button type="submit" class="btn btn-primary btn-block">
                                                <i class="icon-check"></i> Cập nhật
                                            </button>
                                        </form>
                                    </div>
                                </div>
                                
                                <!-- Current Status -->
                                <div class="card contact-detail-card mb-4">
                                    <div class="card-header">
                                        <h5 class="mb-0"><i class="icon-info"></i> Trạng thái hiện tại</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="info-label">Trạng thái:</div>
                                        <div class="mb-3">
                                            <span class="badge ${contact.statusBadgeClass}" style="font-size: 14px; padding: 8px 12px;">
                                                ${contact.status}
                                            </span>
                                        </div>
                                        
                                        <div class="info-label">Ưu tiên:</div>
                                        <div class="mb-3">
                                            <span class="badge ${contact.priorityBadgeClass}" style="font-size: 14px; padding: 8px 12px;">
                                                ${contact.priority}
                                            </span>
                                        </div>
                                        
                                        <div class="info-label">Trạng thái đọc:</div>
                                        <div class="mb-3">
                                            <c:choose>
                                                <c:when test="${contact.read}">
                                                    <span class="badge badge-success">Đã đọc</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-danger">Chưa đọc</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        
                                        <div class="info-label">Phản hồi:</div>
                                        <div>
                                            <c:choose>
                                                <c:when test="${contact.responded}">
                                                    <span class="badge badge-success">Đã phản hồi</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning">Chưa phản hồi</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                                
                                <!-- Timestamps -->
                                <div class="card contact-detail-card">
                                    <div class="card-header">
                                        <h5 class="mb-0"><i class="icon-clock-o"></i> Thời gian</h5>
                                    </div>
                                    <div class="card-body">
                                        <div class="info-label">Ngày tạo:</div>
                                        <div class="info-value">${contact.formattedCreatedDate}</div>
                                        
                                        <c:if test="${contact.updatedAt != null}">
                                            <div class="info-label">Cập nhật lần cuối:</div>
                                            <div class="info-value">
                                                <fmt:formatDate value="${contact.updatedAt}" pattern="dd/MM/yyyy HH:mm" />
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
</body>
</html>
