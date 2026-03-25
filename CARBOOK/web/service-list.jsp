<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Dịch vụ - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .service-icon {
            font-size: 32px;
            color: #01d28e;
        }
        .status-badge {
            font-size: 12px;
            padding: 4px 8px;
        }
        .actions-cell {
            white-space: nowrap;
        }
        .actions-cell .btn {
            padding: 4px 8px;
            font-size: 12px;
            margin-right: 4px;
        }
        .price-cell {
            font-weight: 600;
            color: #01d28e;
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
                                <h2><i class="icon-briefcase"></i> Quản lý Dịch vụ</h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="service-management?action=create" class="btn btn-primary">
                                    <i class="icon-plus"></i> Thêm dịch vụ mới
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
                        
                        <!-- Service List -->
                        <div class="row">
                            <div class="col-md-12">
                                <div class="card">
                                    <div class="card-header">
                                        <h5 class="mb-0">Danh sách dịch vụ</h5>
                                    </div>
                                    <div class="card-body p-0">
                                        <c:choose>
                                            <c:when test="${not empty services}">
                                                <div class="table-responsive">
                                                    <table class="table table-hover mb-0">
                                                        <thead class="thead-light">
                                                            <tr>
                                                                <th>ID</th>
                                                                <th>Icon</th>
                                                                <th>Tên dịch vụ</th>
                                                                <th>Mô tả ngắn</th>
                                                                <th>Giá</th>
                                                                <th>Thứ tự</th>
                                                                <th>Trạng thái</th>
                                                                <th>Ngày tạo</th>
                                                                <th>Thao tác</th>
                                                            </tr>
                                                        </thead>
                                                        <tbody>
                                                            <c:forEach var="service" items="${services}">
                                                                <tr>
                                                                    <td>${service.serviceId}</td>
                                                                    <td>
                                                                        <span class="${service.iconClassOrDefault} service-icon"></span>
                                                                    </td>
                                                                    <td>
                                                                        <strong>${service.serviceName}</strong>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${not empty service.shortDescription}">
                                                                                ${service.getTruncatedDescription(80)}
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted">-</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td class="price-cell">
                                                                        <c:choose>
                                                                            <c:when test="${service.price > 0}">
                                                                                <fmt:formatNumber value="${service.price}" pattern="#,##0" /> đ
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="text-muted">Liên hệ</span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <span class="badge badge-secondary">${service.displayOrder}</span>
                                                                    </td>
                                                                    <td>
                                                                        <c:choose>
                                                                            <c:when test="${service.active}">
                                                                                <span class="badge badge-success status-badge">
                                                                                    <i class="icon-check"></i> Hoạt động
                                                                                </span>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <span class="badge badge-secondary status-badge">
                                                                                    <i class="icon-eye-off"></i> Ẩn
                                                                                </span>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </td>
                                                                    <td>
                                                                        <fmt:formatDate value="${service.createdAt}" pattern="dd/MM/yyyy" />
                                                                        <br>
                                                                        <small class="text-muted">
                                                                            <fmt:formatDate value="${service.createdAt}" pattern="HH:mm" />
                                                                        </small>
                                                                    </td>
                                                                    <td class="actions-cell">
                                                                        <a href="services?action=view&id=${service.serviceId}" 
                                                                           class="btn btn-sm btn-info" 
                                                                           target="_blank"
                                                                           title="Xem">
                                                                            <i class="icon-eye"></i>
                                                                        </a>
                                                                        <a href="service-management?action=edit&id=${service.serviceId}" 
                                                                           class="btn btn-sm btn-warning"
                                                                           title="Sửa">
                                                                            <i class="icon-pencil"></i>
                                                                        </a>
                                                                        <a href="service-management?action=toggle-status&id=${service.serviceId}" 
                                                                           class="btn btn-sm ${service.active ? 'btn-secondary' : 'btn-success'}"
                                                                           onclick="return confirm('${service.active ? 'Ẩn' : 'Hiển thị'} dịch vụ này?')"
                                                                           title="${service.active ? 'Ẩn' : 'Hiển thị'}">
                                                                            <i class="icon-${service.active ? 'eye-off' : 'check'}"></i>
                                                                        </a>
                                                                        <a href="service-management?action=delete&id=${service.serviceId}" 
                                                                           class="btn btn-sm btn-danger"
                                                                           onclick="return confirm('Bạn có chắc chắn muốn xóa dịch vụ này?')"
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
                                                    <i class="icon-briefcase" style="font-size: 48px; color: #ccc;"></i>
                                                    <h4 class="mt-3">Chưa có dịch vụ nào</h4>
                                                    <p class="text-muted">Bắt đầu bằng cách tạo dịch vụ đầu tiên</p>
                                                    <a href="service-management?action=create" class="btn btn-primary mt-3">
                                                        <i class="icon-plus"></i> Tạo dịch vụ mới
                                                    </a>
                                                </div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Statistics -->
                        <c:if test="${not empty services}">
                            <div class="row mt-4">
                                <div class="col-md-4">
                                    <div class="card text-white bg-primary">
                                        <div class="card-body">
                                            <h5 class="card-title">Tổng số dịch vụ</h5>
                                            <h2>${services.size()}</h2>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="card text-white bg-success">
                                        <div class="card-body">
                                            <h5 class="card-title">Đang hoạt động</h5>
                                            <h2>
                                                <c:set var="activeCount" value="0" />
                                                <c:forEach var="service" items="${services}">
                                                    <c:if test="${service.active}">
                                                        <c:set var="activeCount" value="${activeCount + 1}" />
                                                    </c:if>
                                                </c:forEach>
                                                ${activeCount}
                                            </h2>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="card text-white bg-secondary">
                                        <div class="card-body">
                                            <h5 class="card-title">Đang ẩn</h5>
                                            <h2>${services.size() - activeCount}</h2>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                        
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
