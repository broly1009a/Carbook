<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý vai trò - CarBook</title>
        <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="css/open-iconic-bootstrap.min.css">
        <link rel="stylesheet" href="css/animate.css">
        <link rel="stylesheet" href="css/owl.carousel.min.css">
        <link rel="stylesheet" href="css/owl.theme.default.min.css">
        <link rel="stylesheet" href="css/magnific-popup.css">
        <link rel="stylesheet" href="css/aos.css">
        <link rel="stylesheet" href="css/ionicons.min.css">
        <link rel="stylesheet" href="css/bootstrap-datepicker.css">
        <link rel="stylesheet" href="css/jquery.timepicker.css">
        <link rel="stylesheet" href="css/flaticon.css">
        <link rel="stylesheet" href="css/icomoon.css">
        <link rel="stylesheet" href="css/style.css">
        <style>
            .role-card {
                border: 1px solid #e0e0e0;
                border-radius: 8px;
                padding: 20px;
                margin-bottom: 20px;
                transition: all 0.3s;
            }
            .role-card:hover {
                box-shadow: 0 4px 8px rgba(0,0,0,0.1);
                transform: translateY(-2px);
            }
            .role-badge {
                font-size: 16px;
                padding: 8px 16px;
                border-radius: 20px;
            }
            .role-actions {
                display: flex;
                gap: 10px;
            }
            .table-role {
                background: white;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            }
            .badge-admin {
                background: #dc3545;
            }
            .badge-owner {
                background: #007bff;
            }
            .badge-customer {
                background: #28a745;
            }
            .badge-guest {
                background: #6c757d;
            }
            .btn-edit {
                background: #ffc107;
                color: #000;
            }
            .btn-edit:hover {
                background: #e0a800;
                color: #000;
            }
            .system-role {
                background-color: #f8f9fa;
            }
        </style>
    </head>
    <body>
        <%@ include file="includes/navbar.jsp" %>

        <section class="hero-wrap hero-wrap-2 js-fullheight" style="background-image: url('images/bg_3.jpg');" data-stellar-background-ratio="0.5">
            <div class="overlay"></div>
            <div class="container">
                <div class="row no-gutters slider-text js-fullheight align-items-end justify-content-start">
                    <div class="col-md-9 ftco-animate pb-5">
                        <p class="breadcrumbs">
                            <span class="mr-2"><a href="index.jsp">Trang chủ <i class="ion-ios-arrow-forward"></i></a></span>
                            <span class="mr-2"><a href="dashboard">Dashboard <i class="ion-ios-arrow-forward"></i></a></span>
                            <span>Quản lý vai trò <i class="ion-ios-arrow-forward"></i></span>
                        </p>
                        <h1 class="mb-3 bread">Quản lý vai trò</h1>
                    </div>
                </div>
            </div>
        </section>


        <section class="ftco-section bg-light">
            <div class="container">
                <!-- Header with Add Button -->
                <div class="row mb-4">
                    <div class="col-md-6">
                        <h3><i class="ion-ios-people"></i> Danh sách vai trò</h3>
                        <p class="text-muted">Quản lý các vai trò người dùng trong hệ thống</p>
                    </div>
                    <div class="col-md-6 text-right">
                        <button class="btn btn-primary" data-toggle="modal" data-target="#roleModal" onclick="openAddRoleForm()">
                            <i class="ion-ios-add-circle"></i> Thêm vai trò mới
                        </button>
                    </div>
                </div>

                <!-- Alert Messages -->
                <c:if test="${not empty sessionScope.success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <strong><i class="ion-ios-checkmark-circle"></i> Thành công!</strong> 
                        ${sessionScope.success}
                        <button type="button" class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                    </div>
                    <c:remove var="success" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <strong><i class="ion-ios-close-circle"></i> Lỗi!</strong> 
                        ${sessionScope.error}
                        <button type="button" class="close" data-dismiss="alert">
                            <span>&times;</span>
                        </button>
                    </div>
                    <c:remove var="error" scope="session"/>
                </c:if>

                <!-- Search Form -->
                <div class="card mb-4 shadow-sm">
                    <div class="card-body">
                        <form method="get" action="role-management">
                            <input type="hidden" name="action" value="search">

                            <div class="row align-items-center">

                                <!-- Input -->
                                <div class="col-md-5">
                                    <input type="text"
                                           class="form-control"
                                           name="keyword"
                                           value="${param.keyword}"
                                           placeholder="🔍 Nhập tên vai trò cần tìm...">
                                </div>

                                <!-- Buttons -->
                                <div class="col-md-7 text-right">
                                    <button type="submit" class="btn btn-primary mr-2 px-4">
                                        <i class="ion-ios-search mr-1"></i> Tìm kiếm
                                    </button>

                                    <a href="role-management" class="btn btn-outline-secondary px-4">
                                        <i class="ion-ios-refresh mr-1"></i> Reset
                                    </a>
                                </div>

                            </div>
                        </form>
                    </div>
                </div>           
                <!-- Roles Table -->
                <div class="card table-role">
                    <div class="card-body p-0">
                        <c:choose>
                            <c:when test="${empty roles}">
                                <div class="text-center p-5">
                                    <i class="ion-ios-people" style="font-size: 64px; color: #ccc;"></i>
                                    <p class="mt-3 text-muted">Chưa có vai trò nào trong hệ thống</p>
                                    <button class="btn btn-primary mt-2" data-toggle="modal" data-target="#roleModal" onclick="openAddRoleForm()">
                                        <i class="ion-ios-add-circle"></i> Thêm vai trò đầu tiên
                                    </button>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="table-responsive">
                                    <table class="table table-hover mb-0">
                                        <thead class="thead-light">
                                            <tr>
                                                <th width="10%">ID</th>
                                                <th width="25%">Tên vai trò</th>
                                                <th width="40%">Mô tả</th>
                                                <th width="15%" class="text-center">Trạng thái</th>
                                                <th width="10%" class="text-center">Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="role" items="${roles}">
                                                <tr class="${role.roleId <= 4 ? 'system-role' : ''}">
                                                    <td><strong>${role.roleId}</strong></td>
                                                    <td>
                                                        <strong>${role.roleName}</strong>
                                                        <c:if test="${role.roleId <= 4}">
                                                            <span class="badge badge-secondary ml-2">Hệ thống</span>
                                                        </c:if>
                                                    </td>
                                                    <td>${role.description}</td>
                                                    <td class="text-center">
                                                        <c:choose>
                                                            <c:when test="${role.roleId == 1}">
                                                                <span class="badge badge-admin role-badge">Admin</span>
                                                            </c:when>
                                                            <c:when test="${role.roleId == 2}">
                                                                <span class="badge badge-owner role-badge">Chủ xe</span>
                                                            </c:when>
                                                            <c:when test="${role.roleId == 3}">
                                                                <span class="badge badge-customer role-badge">Khách hàng</span>
                                                            </c:when>
                                                            <c:when test="${role.roleId == 4}">
                                                                <span class="badge badge-guest role-badge">Khách</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge badge-info role-badge">Tùy chỉnh</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td class="text-center">
                                                        <div class="btn-group" role="group">
                                                            <button class="btn btn-sm btn-edit" 
                                                                    data-toggle="modal" 
                                                                    data-target="#roleModal"
                                                                    onclick="openEditRoleForm(${role.roleId}, '${role.roleName}', '${role.description}')">
                                                                <i class="ion-ios-create"></i>
                                                            </button>
                                                            <c:if test="${role.roleId > 4}">
                                                                <button class="btn btn-sm btn-danger" 
                                                                        onclick="confirmDelete(${role.roleId}, '${role.roleName}')">
                                                                    <i class="ion-ios-trash"></i>
                                                                </button>
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <!-- Information Box -->
                <div class="alert alert-info mt-4">
                    <h5><i class="ion-ios-information-circle"></i> Lưu ý:</h5>
                    <ul class="mb-0">
                        <li><strong>Vai trò hệ thống</strong> (ID 1-4) không thể xóa nhưng có thể chỉnh sửa mô tả</li>
                        <li><strong>Admin (ID: 1):</strong> Toàn quyền quản trị hệ thống</li>
                        <li><strong>Chủ xe (ID: 2):</strong> Quản lý xe và đơn đặt xe của mình</li>
                        <li><strong>Khách hàng (ID: 3):</strong> Thuê xe và quản lý booking</li>
                        <li><strong>Khách (ID: 4):</strong> Chỉ xem thông tin, không thể đặt xe</li>
                    </ul>
                </div>
            </div>
        </section>
        <!-- Bottom Action -->
        <div class="d-flex justify-content-end mt-5 mb-3">
            <a href="home" class="btn btn-outline-success px-4 py-2 shadow-sm">
                <i class="ion-ios-home mr-2"></i> Về trang chủ
            </a>
        </div>

        <!-- Role Modal -->
        <div class="modal fade" id="roleModal" tabindex="-1" role="dialog" aria-labelledby="roleModalLabel" aria-hidden="true">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="roleModalLabel">Thêm vai trò mới</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <form id="roleForm" method="post" action="role-management">
                        <div class="modal-body">
                            <input type="hidden" name="action" id="formAction" value="create">
                            <input type="hidden" name="roleId" id="roleId">

                            <div class="form-group">
                                <label for="roleName">Tên vai trò <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="roleName" name="roleName" required 
                                       placeholder="Nhập tên vai trò">
                            </div>

                            <div class="form-group">
                                <label for="description">Mô tả</label>
                                <textarea class="form-control" id="description" name="description" rows="3" 
                                          placeholder="Mô tả về vai trò này"></textarea>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                                <i class="ion-ios-close"></i> Đóng
                            </button>
                            <button type="submit" class="btn btn-primary">
                                <i class="ion-ios-checkmark"></i> Lưu
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Scripts must be loaded BEFORE footer to prevent loader issues -->
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
        <script src="js/scrollax.min.js"></script>
        <script src="js/main.js"></script>

        <script>
                                                                            $(document).ready(function () {
                                                                                // Auto hide alerts after 5 seconds
                                                                                setTimeout(function () {
                                                                                    $('.alert').fadeOut('slow');
                                                                                }, 5000);
                                                                            });

                                                                            function openAddRoleForm() {
                                                                                $('#roleModalLabel').text('Thêm vai trò mới');
                                                                                $('#formAction').val('create');
                                                                                $('#roleForm')[0].reset();
                                                                                $('#roleId').val('');
                                                                            }

                                                                            function openEditRoleForm(roleId, roleName, description) {
                                                                                $('#roleModalLabel').text('Chỉnh sửa vai trò');
                                                                                $('#formAction').val('update');
                                                                                $('#roleId').val(roleId);
                                                                                $('#roleName').val(roleName);
                                                                                $('#description').val(description);
                                                                            }

                                                                            function confirmDelete(roleId, roleName) {
                                                                                if (roleId <= 4) {
                                                                                    alert('Không thể xóa vai trò hệ thống!');
                                                                                    return;
                                                                                }

                                                                                if (confirm('Bạn có chắc chắn muốn xóa vai trò "' + roleName + '"?\n\nLưu ý: Tất cả người dùng có vai trò này sẽ bị ảnh hưởng.')) {
                                                                                    window.location.href = 'role-management?action=delete&id=' + roleId;
                                                                                }
                                                                            }
        </script>

        <%@ include file="includes/footer.jsp" %>
    </body>
</html>
