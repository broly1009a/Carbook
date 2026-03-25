<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Liên hệ - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .contact-icon {
            font-size: 24px;
            color: #01d28e;
        }
        .status-badge, .priority-badge {
            font-size: 11px;
            padding: 4px 8px;
        }
        .unread-row {
            background-color: #f0f8ff;
            font-weight: 500;
        }
        .message-preview {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .actions-cell {
            white-space: nowrap;
        }
        .actions-cell .btn {
            padding: 4px 8px;
            font-size: 12px;
            margin-right: 4px;
        }
        .stats-card {
            transition: transform 0.2s;
        }
        .stats-card:hover {
            transform: translateY(-5px);
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
                                <h2><i class="icon-envelope"></i> Quản lý Liên hệ</h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="contact.jsp" class="btn btn-outline-primary" target="_blank">
                                    <i class="icon-eye"></i> Xem form liên hệ
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
                        
                        <!-- Statistics Cards -->
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="card text-white bg-info stats-card">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h6 class="card-title">Mới</h6>
                                                <h2 class="mb-0">${newCount}</h2>
                                            </div>
                                            <div>
                                                <i class="icon-envelope-open" style="font-size: 48px; opacity: 0.3;"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="contact?action=list&status=New" class="card-footer text-white d-block">
                                        <small>Xem chi tiết <i class="icon-arrow-right"></i></small>
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-white bg-warning stats-card">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h6 class="card-title">Đang xử lý</h6>
                                                <h2 class="mb-0">${inProgressCount}</h2>
                                            </div>
                                            <div>
                                                <i class="icon-clock-o" style="font-size: 48px; opacity: 0.3;"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="contact?action=list&status=In Progress" class="card-footer text-white d-block">
                                        <small>Xem chi tiết <i class="icon-arrow-right"></i></small>
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-white bg-success stats-card">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h6 class="card-title">Đã giải quyết</h6>
                                                <h2 class="mb-0">${resolvedCount}</h2>
                                            </div>
                                            <div>
                                                <i class="icon-check-circle" style="font-size: 48px; opacity: 0.3;"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="contact?action=list&status=Resolved" class="card-footer text-white d-block">
                                        <small>Xem chi tiết <i class="icon-arrow-right"></i></small>
                                    </a>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card text-white bg-danger stats-card">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h6 class="card-title">Chưa đọc</h6>
                                                <h2 class="mb-0">${unreadCount}</h2>
                                            </div>
                                            <div>
                                                <i class="icon-bell" style="font-size: 48px; opacity: 0.3;"></i>
                                            </div>
                                        </div>
                                    </div>
                                    <a href="contact?action=list" class="card-footer text-white d-block">
                                        <small>Xem tất cả <i class="icon-arrow-right"></i></small>
                                    </a>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Filter Bar -->
                        <div class="row mb-3">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-body">
                                        <form method="get" action="contact" class="form-inline">
                                            <input type="hidden" name="action" value="list">
                                            <label class="mr-2">Lọc theo trạng thái:</label>
                                            <select name="status" class="form-control mr-2" onchange="this.form.submit()">
                                                <option value="">Tất cả</option>
                                                <option value="New" ${statusFilter == 'New' ? 'selected' : ''}>Mới</option>
                                                <option value="In Progress" ${statusFilter == 'In Progress' ? 'selected' : ''}>Đang xử lý</option>
                                                <option value="Resolved" ${statusFilter == 'Resolved' ? 'selected' : ''}>Đã giải quyết</option>
                                                <option value="Closed" ${statusFilter == 'Closed' ? 'selected' : ''}>Đã đóng</option>
                                                <option value="Spam" ${statusFilter == 'Spam' ? 'selected' : ''}>Spam</option>
                                            </select>
                                            <a href="contact?action=list" class="btn btn-secondary btn-sm">Xóa lọc</a>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Contact List -->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0">Danh sách liên hệ</h5>
                                    </div>
                                    <div class="card-body p-0">
                                        <c:choose>
                                            <c:when test="${not empty contacts}">
                                                <div class="table-responsive">
                                                    <table class="table table-hover mb-0">
                                                        <thead class="thead-light">
                                                            <tr>
                                                                <th width="50">ID</th>
                                                                <th>Họ tên</th>
                                                                <th>Email</th>
                                                                <th>Tiêu đề</th>
                                                                <th>Nội dung</th>
                                                                <th>Loại</th>
                                                                <th>Ưu tiên</th>
                                                                <th>Trạng thái</th>
                                                                <th>Ngày gửi</th>
                                                                <th>Thao tác</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="contact" items="${contacts}">
                                                                <tr class="${!contact.read ? 'unread-row' : ''}">
                                                                    <td>
                                                                        ${contact.contactId}
                                                                        <c:if test="${!contact.read}">
                                                                            <br><span class="badge badge-danger badge-sm">New</span>
                                                                        </c:if>
                                                                    </td>
                                                                    <td>
                                                                        <strong>${contact.fullName}</strong>
                                                                        <c:if test="${not empty contact.phoneNumber}">
                                                                            <br><small class="text-muted">${contact.phoneNumber}</small>
                                                                        </c:if>
                                                                    </td>
                                                                    <td>
                                                                        <a href="mailto:${contact.email}">${contact.email}</a>
                                                                    </td>
                                                                    <td>
                                                                        <strong>${contact.subject}</strong>
                                                                    </td>
                                                                    <td>
                                                                        <div class="message-preview" title="${contact.message}">
                                                                            ${contact.getTruncatedMessage(50)}
                                                                        </div>
                                                                    </td>
                                                                    <td>
                                                                        <small>${contact.contactTypeDisplay}</small>
                                                                    </td>
                                                                    <td>
                                                                        <span class="badge ${contact.priorityBadgeClass} priority-badge">
                                                                            ${contact.priority}
                                                                        </span>
                                                                    </td>
                                                                    <td>
                                                                        <span class="badge ${contact.statusBadgeClass} status-badge">
                                                                            ${contact.status}
                                                                        </span>
                                                                    </td>
                                                                    <td>
                                                                        <small>${contact.formattedCreatedDate}</small>
                                                                    </td>
                                                                    <td class="actions-cell">
                                                                        <a href="contact?action=view&id=${contact.contactId}" 
                                                                           class="btn btn-sm btn-info"
                                                                           title="Xem chi tiết">
                                                                            <i class="icon-eye"></i>
                                                                        </a>
                                                                        <c:if test="${!contact.responded}">
                                                                            <a href="contact?action=respond&id=${contact.contactId}" 
                                                                               class="btn btn-sm btn-success"
                                                                               title="Phản hồi">
                                                                                <i class="icon-reply"></i>
                                                                            </a>
                                                                        </c:if>
                                                                        <a href="contact?action=delete&id=${contact.contactId}" 
                                                                           class="btn btn-sm btn-danger"
                                                                           onclick="return confirm('Bạn có chắc chắn muốn xóa liên hệ này?')"
                                                                           title="Xóa">
                                                                            <i class="icon-trash"></i>
                                                                        </a>
                                                                    </td>
                                                                </tr>
                                                            </c:forEach>
                                                        </tbody>
                                                    </table>
                                                </div>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="p-5 text-center">
                                                    <i class="icon-envelope" style="font-size: 48px; color: #ccc;"></i>
                                                    <h4 class="mt-3">Chưa có liên hệ nào</h4>
                                                    <p class="text-muted">
                                                        <c:choose>
                                                            <c:when test="${not empty statusFilter}">
                                                                Không có liên hệ nào với trạng thái "${statusFilter}"
                                                            </c:when>
                                                            <c:otherwise>
                                                                Các liên hệ từ khách hàng sẽ xuất hiện ở đây
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
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
