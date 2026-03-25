<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quản Lý Dòng Xe - CarBook</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .container { margin-top: 50px; }
        .table-container { 
            background: #fff; 
            padding: 30px; 
            border-radius: 15px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.05); 
        }
        .header-title { color: #01d28e; font-weight: bold; }
        .btn-add { background-color: #01d28e; color: white; border-radius: 5px; }
        .btn-add:hover { background-color: #01a871; color: white; }
        .table thead { background-color: #01d28e; color: white; }
        .badge-year { font-size: 0.9em; background-color: #e3f2fd; color: #0d47a1; }
        .action-btns .btn { margin-right: 5px; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container">
        <div class="table-container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="header-title m-0">DANH SÁCH DÒNG XE</h2>
                <a href="car-models" class="btn btn-outline-secondary px-4">
                                      
                           Reset         </a>
                <a href="car-models?action=add" class="btn btn-add shadow-sm">+ Thêm Dòng Xe</a>
            </div>

            <%-- Hiển thị thông báo Success/Error từ Session --%>
            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success alert-dismissible fade show">
                    <strong>Thành công!</strong> ${sessionScope.success}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>
            
            <c:if test="${not empty sessionScope.error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <strong>Lỗi!</strong> ${sessionScope.error}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
                <c:remove var="error" scope="session" />
            </c:if>

            <%-- Bộ lọc tìm kiếm --%>
            <form action="car-models" method="get" class="row mb-4">
                <div class="col-md-4">
                    <input type="text" name="brandName" class="form-control" placeholder="Tên hãng..." value="${param.brandName}">
                </div>
                <div class="col-md-4">
                    <input type="text" name="modelName" class="form-control" placeholder="Tên dòng xe..." value="${param.modelName}">
                </div>
                <div class="col-md-2">
                    <input type="number" name="year" class="form-control" placeholder="Năm..." value="${param.year}">
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-dark btn-block">Lọc</button>
                </div>
            </form>

            <div class="table-responsive">
                <table class="table table-hover border">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Hãng Xe</th>
                            <th>Dòng Xe</th>
                            <th>Năm Sản Xuất</th>
                            <th class="text-center">Thao Tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${models}" var="m">
                            <tr>
                                <td>#${m.modelId}</td>
                                <td><strong>${m.brand.brandName}</strong></td>
                                <td>${m.modelName}</td>
                                <td><span class="badge badge-year px-3 py-2">${m.year}</span></td>
                                <td class="text-center action-btns">
                                    <a href="car-models?action=edit&id=${m.modelId}" class="btn btn-outline-primary btn-sm">Sửa</a>
                                    <button onclick="confirmDelete(${m.modelId})" class="btn btn-outline-danger btn-sm">Xóa</button>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty models}">
                            <tr>
                                <td colspan="5" class="text-center text-muted py-4">Không tìm thấy dòng xe nào khớp với tìm kiếm.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
                
                <div class="d-flex justify-content-end mt-5 mb-3">
            <a href="home" class="btn btn-outline-success px-4 py-2 shadow-sm">
                <i class="ion-ios-home mr-2"></i> Về trang chủ
            </a>
        </div>
            </div>
        </div>
    </div>

    <script>
        function confirmDelete(id) {
            if (confirm('Dòng xe này sẽ bị xóa vĩnh viễn. Bạn có chắc không?')) {
                window.location.href = 'car-models?action=delete&id=' + id;
            }
        }
    </script>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>