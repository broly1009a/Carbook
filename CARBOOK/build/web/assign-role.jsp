<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý quyền - CarBook</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    
    <style>
        /* CSS CHẶN LỖI QUAY TÍT */
        #ftco-loader { display: none !important; } 
        .ftco-animate { opacity: 1 !important; visibility: visible !important; } 
        
        body { background-color: #f4f7f6; }
        .main-content { padding-top: 120px; padding-bottom: 60px; }
        .card-admin { background: #fff; border: none; border-radius: 15px; box-shadow: 0 8px 30px rgba(0,0,0,0.05); }
        .table thead th { background-color: #2c3e50; color: white; border: none; }
        .btn-search { background-color: #01d28e; color: white; border-radius: 5px; border: none; }
        .btn-search:hover { background-color: #019e6b; color: white; }
        .status-badge { font-size: 11px; padding: 5px 10px; border-radius: 20px; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="main-content container">
        <div class="card-admin p-4">
            <h2 class="mb-4 font-weight-bold text-dark">Phân Quyền & Quản Lý User</h2>

            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="icon ion-md-checkmark-circle"></i> ${sessionScope.success}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <form action="assign-role" method="get" class="form-inline mb-4">
                <input type="text" name="keyword" class="form-control mr-2 w-50" 
                       placeholder="Tìm kiếm theo tên hoặc email..." value="${keyword}">
                <button type="submit" class="btn btn-search px-4 shadow-sm">
                    <i class="icon ion-ios-search"></i> Tìm kiếm
                </button>
                <a href="assign-role" class="btn btn-outline-secondary ml-2">
                    <i class="icon ion-ios-refresh"></i> Làm mới
                </a>
            </form>

            <div class="table-responsive">
                <table class="table table-hover border-bottom">
                    <thead>
                        <tr>
                            <th>Họ và tên</th>
                            <th>Email</th>
                            <th>Cấp quyền</th>
                            <th>Trạng thái & Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${userList}">
                            <tr>
                                <td class="align-middle"><span class="font-weight-bold text-dark">${u.fullName}</span></td>
                                <td class="align-middle">${u.email}</td>
                                <td class="align-middle">
                                    <form action="assign-role" method="post" class="form-inline">
                                        <input type="hidden" name="userId" value="${u.userId}">
                                        <select name="roleId" class="form-control form-control-sm mr-2" style="width: 140px;">
                                            <c:forEach var="r" items="${roleList}">
                                                <option value="${r.roleId}" ${u.roleId == r.roleId ? 'selected' : ''}>
                                                    ${r.roleName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <button type="submit" class="btn btn-sm btn-info px-3">Lưu</button>
                                    </form>
                                </td>
                                <td class="align-middle">
                                    <c:choose>
                                        <c:when test="${u.active}">
                                            <span class="badge badge-success mb-2 status-badge">Đang hoạt động</span><br/>
                                            <a href="assign-role?action=delete&id=${u.userId}" 
                                               class="btn btn-sm btn-outline-danger"
                                               onclick="return confirm('Bạn có chắc chắn muốn KHÓA tài khoản này?')">
                                                <i class="icon ion-ios-lock"></i> Khóa Account
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-danger mb-2 status-badge">Đã bị khóa</span><br/>
                                            <a href="assign-role?action=unblock&id=${u.userId}" 
                                               class="btn btn-sm btn-success"
                                               onclick="return confirm('Bạn có muốn MỞ KHÓA cho tài khoản này?')">
                                                <i class="icon ion-ios-unlock"></i> Mở khóa
                                            </a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty userList}">
                    <div class="text-center py-5 text-muted">
                        <i class="icon ion-ios-information-circle-outline" style="font-size: 40px;"></i>
                        <p>Không tìm thấy người dùng nào phù hợp với từ khóa.</p>
                    </div>
                </c:if>
            </div>
            
            <hr>
            <div class="d-flex justify-content-between align-items-center">
                <span class="text-muted small">Tổng số: ${userList.size()} người dùng</span>
                <a href="home" class="btn btn-dark btn-sm px-4">
                    <i class="icon ion-ios-home"></i> Quay lại Home
                </a>
            </div>
        </div>
    </div>

    <footer style="background: #232323; color: rgba(255,255,255,0.5); padding: 30px 0; text-align: center;">
        <div class="container">
            <p class="mb-0">&copy; 2026 CarBook Admin Dashboard - Quản lý an toàn</p>
        </div>
    </footer>

    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>