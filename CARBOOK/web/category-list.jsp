<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Quản Lý Danh Mục Xe - CarBook</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    
    <style>
        body { background-color: #f8f9fa; }
        .main-container { 
            margin-top: 50px; 
            padding: 30px; 
            background: #fff; 
            border-radius: 15px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.05); 
        }
        /* Header & Table Styling */
        .table thead { background-color: #01d28e; color: white; border: none; }
        .btn-add { background-color: #01d28e; color: white; border-radius: 5px; padding: 8px 15px; font-weight: bold; transition: 0.3s; }
        .btn-add:hover { background-color: #01a871; color: white; transform: translateY(-2px); }
        .cat-name { font-weight: 600; color: #2c3e50; }
        .desc-truncate { max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
        .action-btns .btn { border-radius: 20px; padding: 4px 12px; }
        
        /* Custom Search Box */
        .search-box { max-width: 350px; }
        .search-box .form-control { border-radius: 20px 0 0 20px; border-right: none; border-color: #ced4da; }
        .search-box .form-control:focus { box-shadow: none; border-color: #01d28e; }
        .search-box .btn-search { 
            border-radius: 0 20px 20px 0; 
            background: #01d28e; 
            color: white; 
            border: none; 
            padding: 0 15px;
        }
        .search-box .btn-search:hover { background: #01a871; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container main-container">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2 class="m-0" style="font-weight: bold; color: #333;">DANH SÁCH DANH MỤC</h2>
                <small class="text-muted">Quản lý các loại xe (Sedan, SUV, Hatchback...)</small>
            </div>

            <div class="d-flex align-items-center">
                <form action="car-category" method="get" class="search-box input-group mr-3">
                    <input type="hidden" name="action" value="list">
                    <input type="text" name="txtSearch" class="form-control" 
                           placeholder="Tìm theo tên danh mục..." value="${txtSearch}">
                    <div class="input-group-append">
                        <button class="btn btn-search" type="submit">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                </form>

                <a href="car-category?action=add" class="btn btn-add shadow-sm">
                    <i class="fas fa-plus-circle"></i> Thêm Mới
                </a>
            </div>
        </div>

        <%-- Thông báo Success/Error từ Session --%>
        <c:if test="${not empty sessionScope.success}">
            <div class="alert alert-success alert-dismissible fade show border-0 shadow-sm">
                <i class="fas fa-check-circle mr-2"></i> ${sessionScope.success}
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <c:remove var="success" scope="session"/>
            </div>
        </c:if>
        
        <c:if test="${not empty sessionScope.error}">
            <div class="alert alert-danger alert-dismissible fade show border-0 shadow-sm">
                <i class="fas fa-exclamation-triangle mr-2"></i> ${sessionScope.error}
                <button type="button" class="close" data-dismiss="alert">&times;</button>
                <c:remove var="error" scope="session"/>
            </div>
        </c:if>

        <%-- Bảng Dữ Liệu --%>
        <div class="table-responsive mt-2">
            <table class="table table-hover align-middle">
                <thead>
                    <tr class="text-center">
                        <th style="width: 80px;">ID</th>
                        <th style="width: 220px;">Tên Danh Mục</th>
                        <th>Mô Tả</th>
                        <th style="width: 150px;">Ngày Tạo</th>
                        <th style="width: 180px;">Thao Tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty categories}">
                            <c:forEach items="${categories}" var="cat">
                                <tr>
                                    <td class="text-center text-muted font-weight-bold">#${cat.categoryId}</td>
                                    <td class="cat-name">
                                        <i class="fas fa-tag text-success mr-2 small"></i> ${cat.categoryName}
                                    </td>
                                    <td>
                                        <div class="desc-truncate" title="${cat.description}">
                                            ${not empty cat.description ? cat.description : '<span class="text-italic text-muted">Không có mô tả</span>'}
                                        </div>
                                    </td>
                                    <td class="text-center small">
                                        <fmt:formatDate value="${cat.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                    </td>
                                    <td class="text-center action-btns">
                                        <a href="car-category?action=edit&id=${cat.categoryId}" 
                                           class="btn btn-sm btn-outline-warning" title="Sửa">
                                            <i class="fas fa-edit"></i>
                                        </a>
                                        <a href="car-category?action=delete&id=${cat.categoryId}" 
                                           class="btn btn-sm btn-outline-danger" 
                                           onclick="return confirm('Bạn có chắc chắn muốn xóa danh mục này?')">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center py-5 text-muted">
                                    <i class="fas fa-search fa-3x mb-3 d-block" style="opacity: 0.3;"></i>
                                    <c:choose>
                                        <c:when test="${not empty txtSearch}">
                                            Không tìm thấy danh mục nào khớp với từ khóa: <strong>"${txtSearch}"</strong>
                                        </c:when>
                                        <c:otherwise>
                                            Danh sách danh mục đang trống.
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
        
        <%-- Footer Actions --%>
        <div class="mt-4 border-top pt-3 d-flex justify-content-between align-items-center">
            <a href="car-management" class="btn btn-link text-secondary p-0">
                <i class="fas fa-arrow-left"></i> Quay lại quản lý xe
            </a>
            
            <c:if test="${not empty txtSearch}">
                <a href="car-category?action=list" class="btn btn-sm btn-outline-info px-3" style="border-radius: 20px;">
                    <i class="fas fa-undo mr-1"></i> Hiển thị tất cả danh mục
                </a>
            </c:if>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        $(document).ready(function() {
            // Tự động ẩn thông báo sau 3 giây
            setTimeout(function() {
                $(".alert").fadeTo(500, 0).slideUp(500, function(){
                    $(this).remove(); 
                });
            }, 3000);
        });
    </script>
</body>
</html>