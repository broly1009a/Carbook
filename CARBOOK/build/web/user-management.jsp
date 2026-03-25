<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý người dùng - CarBook</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <style>
        body { background-color: #f8f9fa; }
        .main-container { margin-top: 50px; }
        /* Màu xanh đặc trưng CarBook */
        .bg-carbook { background-color: #01d28e !important; }
        .text-carbook { color: #01d28e !important; }
        .btn-carbook { background-color: #01d28e; color: white; border: none; }
        .btn-carbook:hover { background-color: #01a36e; color: white; }
        
        .card { border-radius: 15px; overflow: hidden; }
        .table thead { background-color: #343a40; color: white; }
        .badge-active { background-color: #01d28e; color: white; }
        
        /* Hiệu ứng vô hiệu hóa nút */
        .btn:disabled { cursor: not-allowed; opacity: 0.5; }
    </style>
</head>

<body>

<jsp:include page="/includes/navbar.jsp"/>

<div class="container main-container mt-5">

    <div class="card shadow border-0">
        <div class="card-header text-white bg-carbook p-3">
            <h4 class="mb-0"><i class="fas fa-users-cog me-2"></i>QUẢN LÝ NGƯỜI DÙNG</h4>
        </div>

        <div class="card-body p-4">

            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    <strong>Thành công!</strong> 
                    <c:choose>
                        <c:when test="${param.success == 'activate'}">Tài khoản đã được mở khóa và có thể truy cập lại.</c:when>
                        <c:when test="${param.success == 'deactivate'}">Tài khoản đã bị đình chỉ hoạt động.</c:when>
                        <c:when test="${param.success == 'delete'}">Dữ liệu người dùng đã được dọn dẹp khỏi hệ thống.</c:when>
                        <c:otherwise>Thao tác của bạn đã được thực hiện.</c:otherwise>
                    </c:choose>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    <strong>Lỗi!</strong> Không thể thực hiện thao tác. Vui lòng thử lại sau.
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>

            <form action="users" method="get" class="row g-3 mb-4">
                <input type="hidden" name="action" value="search"/>
                <div class="col-md-9">
                    <div class="input-group">
                        <span class="input-group-text bg-white"><i class="fas fa-search text-muted"></i></span>
                        <input type="text" class="form-control border-start-0" name="keyword"
                               placeholder="Tìm theo tên hoặc email người dùng..." value="${keyword}">
                    </div>
                </div>
                <div class="col-md-3">
                    <button class="btn btn-carbook w-100 fw-bold">TÌM KIẾM</button>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover text-center align-middle border">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Họ và Tên</th>
                            <th>Email</th>
                            <th>Trạng thái</th>
                            <th width="300px">Hành động</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td class="text-muted fw-bold">${u.userId}</td>
                                <td class="text-start ps-4 fw-semibold">${u.fullName}</td>
                                <td class="text-start">${u.email}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.active}">
                                            <span class="badge badge-active px-3 py-2">
                                                <i class="fas fa-check-circle me-1"></i> Hoạt động
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger px-3 py-2">
                                                <i class="fas fa-ban me-1"></i> Bị khóa
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <form action="users" method="post" class="d-flex justify-content-center gap-2">
                                        <input type="hidden" name="userId" value="${u.userId}" />

                                        <button name="action" value="activate" 
                                                class="btn btn-sm btn-outline-success"
                                                ${u.active ? 'disabled' : ''}
                                                onclick="return confirm('Xác nhận mở lại quyền truy cập cho người dùng này?')">
                                            <i class="fas fa-unlock"></i> Mở
                                        </button>

                                        <button name="action" value="deactivate" 
                                                class="btn btn-sm btn-outline-warning"
                                                ${!u.active ? 'disabled' : ''}
                                                onclick="return confirm('Bạn có chắc muốn tạm dừng hoạt động của người dùng này?')">
                                            <i class="fas fa-user-slash"></i> Khóa
                                        </button>

                                        <button name="action" value="delete" 
                                                class="btn btn-sm btn-outline-danger"
                                                onclick="return confirm('CẢNH BÁO: Xóa người dùng sẽ không thể hoàn tác. Bạn vẫn muốn tiếp tục?')">
                                            <i class="fas fa-trash-alt"></i> Xóa
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty users}">
                            <tr>
                                <td colspan="5" class="py-5 text-muted">
                                    <i class="fas fa-folder-open fa-3x d-block mb-2"></i>
                                    Không tìm thấy dữ liệu người dùng phù hợp
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
                    <div class="d-flex justify-content-end mt-5 mb-3">
            <a href="home" class="btn btn-outline-success px-4 py-2 shadow-sm">
                <i class="ion-ios-home mr-2"></i> Về trang chủ
            </a>
        </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>