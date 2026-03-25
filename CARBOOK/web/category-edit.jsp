<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Sửa Danh Mục Xe - CarBook</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .container { 
            margin-top: 120px; 
            max-width: 500px; 
            background: #fff; 
            padding: 30px; 
            border-radius: 10px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.1); 
        }
        .btn-update { background-color: #01d28e; border: none; color: white; font-weight: bold; }
        .btn-update:hover { background-color: #01a871; color: white; }
        .error-msg { color: #dc3545; font-size: 0.9em; margin-top: 5px; font-weight: 500; }
        .is-invalid { border-color: #dc3545 !important; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    <div class="container">
        <h3 class="text-center mb-4" style="color: #01d28e;">CHỈNH SỬA DANH MỤC</h3>
        
        <%-- Hiển thị thông báo lỗi nếu trùng tên --%>
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert" style="border-radius: 8px;">
                <i class="fas fa-exclamation-circle mr-2"></i> ${error}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>
        
        <form action="car-category" method="post">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="categoryId" value="${category.categoryId}">
            
            <div class="form-group">
                <label class="font-weight-bold">Tên danh mục:</label>
                <%-- Nếu lỗi thì input sẽ đỏ lên nhờ class is-invalid --%>
                <input type="text" name="categoryName" 
                       class="form-control ${not empty error ? 'is-invalid' : ''}" 
                       value="${not empty error ? param.categoryName : category.categoryName}" required>
            </div>
            
            <div class="form-group">
                <label class="font-weight-bold">Mô tả:</label>
                <textarea name="description" class="form-control" rows="4" 
                          placeholder="Nhập mô tả danh mục...">${not empty error ? param.description : category.description}</textarea>
            </div>
            
            <button type="submit" class="btn btn-update btn-block mt-4 py-2 shadow-sm">
                Cập nhật danh mục
            </button>
            <a href="car-category?action=list" class="btn btn-secondary btn-block py-2">Hủy</a>
        </form>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>