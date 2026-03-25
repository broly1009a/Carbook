<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Quản Lý Hãng Xe - CarBook</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body { background-color: #f8f9fa; }
        .main-container { 
            margin-top: 120px; 
            background: #ffffff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        .header-title {
            color: #01d28e; 
            font-weight: 700;
            margin-bottom: 30px;
            border-bottom: 2px solid #eee;
            padding-bottom: 10px;
        }
        .brand-logo-img {
            width: 60px;
            height: auto;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .btn-custom {
            padding: 5px 15px;
            border-radius: 5px;
            font-size: 14px;
            transition: all 0.3s;
        }
        .btn-custom:hover {
            transform: translateY(-2px);
        }
        .alert {
            border-radius: 10px;
            font-weight: 500;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container main-container">
    
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle mr-2"></i> ${sessionScope.success}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <c:remove var="success" scope="session"/>
            </div>
        </c:if>

        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle mr-2"></i> ${sessionScope.error}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <c:remove var="error" scope="session"/>
            </div>
        </c:if>

        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="header-title">DANH SÁCH HÃNG XE</h2>
            <c:if test="${sessionScope.user.roleId == 1 || sessionScope.user.roleId == 2}">
                <a href="brand?action=add" class="btn btn-success shadow-sm">
                    <i class="fas fa-plus mr-1"></i> Thêm Hãng Mới
                </a>
            </c:if>
        </div>
<div class="card mb-4 shadow-sm">
    <div class="card-body">
        <form action="brand" method="get">
            <input type="hidden" name="action" value="search"/>

            <div class="row">
                <div class="col-md-4">
                    <input type="text"
                           name="nameSearch"
                           value="${nameSearch}"
                           class="form-control"
                           placeholder="Tìm theo tên hãng...">
                </div>

                <div class="col-md-4">
                    <input type="text"
                           name="countrySearch"
                           value="${countrySearch}"
                           class="form-control"
                           placeholder="Tìm theo quốc gia...">
                </div>

                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary">
                        Tìm kiếm
                    </button>

                    <a href="brand?action=list" class="btn btn-secondary">
                        Reset
                    </a>
                </div>
            </div>
        </form>
    </div>
</div>

        <div class="table-responsive">
            <table class="table table-hover border">
                <thead class="thead-dark">
                    <tr>
                        <th>ID</th>
                        <th>Logo</th>
                        <th>Tên hãng</th>
                        <th>Quốc gia</th>
                        <th>Ngày tạo</th>
                        <th class="text-center">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${brands}" var="b">
                        <tr>
                            <td class="align-middle">${b.brandId}</td>
                            <td class="align-middle">
                                <c:choose>
                                    <c:when test="${not empty b.logoURL}">
                                        <img src="${b.logoURL}" class="brand-logo-img" alt="logo">
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-light">No Image</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="align-middle text-uppercase"><strong>${b.brandName}</strong></td>
                            <td class="align-middle">${b.country}</td>
                            <td class="align-middle">
                                <fmt:formatDate value="${b.createdAt}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td class="align-middle text-center">
                                <a href="brand?action=edit&id=${b.brandId}" class="btn btn-warning btn-custom shadow-sm">Sửa</a>
                                <button onclick="confirmDelete(${b.brandId}, '${b.brandName}')" class="btn btn-danger btn-custom shadow-sm">Xóa</button>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="mt-4">
            <a href="car-management" class="btn btn-outline-secondary">
                &larr; Quay lại quản lý xe
            </a>
        </div>
    </div>

    <script>
        function confirmDelete(id, name) {
            if (confirm('Bạn có chắc chắn muốn xóa hãng xe "' + name + '" không? \nLưu ý: Không thể xóa hãng đang có dòng xe hoạt động.')) {
                window.location.href = 'brand?action=delete&id=' + id;
            }
        }
    </script>
    
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
</body>
</html>