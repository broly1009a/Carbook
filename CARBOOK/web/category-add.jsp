<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Danh Mục Xe Mới - CarBook</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        body { background-color: #f8f9fa; }
        .form-container { 
            margin-top: 120px; 
            background: #fff; 
            padding: 40px; 
            border-radius: 15px; 
            box-shadow: 0 4px 20px rgba(0,0,0,0.1);
            max-width: 600px;
        }
        .btn-save { background-color: #01d28e; border: none; color: white; font-weight: bold; }
        .btn-save:hover { background-color: #01a871; color: white; }
        /* Style cho input khi có lỗi */
        .is-invalid { border-color: #dc3545 !important; }
        .error-text { color: #dc3545; font-size: 0.9em; margin-top: 5px; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>

    <div class="container d-flex justify-content-center">
        <div class="form-container w-100">
            <h2 class="mb-4 text-center" style="color: #01d28e;">THÊM DANH MỤC XE MỚI</h2>
            
            <%-- Hiển thị Alert nếu có lỗi tổng quát --%>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle mr-2"></i> ${error}
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
          
            <form action="car-category" method="post">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label class="font-weight-bold">Tên danh mục:</label>
                    <input type="text" name="categoryName" 
                           class="form-control ${not empty error ? 'is-invalid' : ''}" 
                           placeholder="Ví dụ: Sedan, SUV, Xe điện..." 
                           value="${param.categoryName}" required>
                </div>
                
                <div class="form-group">
                    <label class="font-weight-bold">Mô tả danh mục:</label>
                    <textarea name="description" class="form-control" rows="4" 
                              placeholder="Mô tả đặc điểm của loại xe này...">${param.description}</textarea>
                </div>
                
                <div class="mt-4">
                    <button type="submit" class="btn btn-save btn-block py-3 shadow-sm">
                        Lưu Danh Mục
                    </button>
                    <a href="car-category?action=list" class="btn btn-secondary btn-block py-3">Hủy bỏ</a>
                </div>
            </form>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>