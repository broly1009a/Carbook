<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm Dòng Xe - CarBook</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <%-- Thêm FontAwesome để dùng icon cảnh báo cho đẹp --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .card { border-radius: 15px; border: none; }
        .header-title { color: #01d28e; font-weight: bold; }
        .btn-save { background-color: #01d28e; border: none; color: white; transition: 0.3s; }
        .btn-save:hover { background-color: #01a871; color: white; }
        /* Style cho thông báo lỗi dưới input */
        .invalid-feedback { display: block; font-weight: 500; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container" style="margin-top:120px; max-width: 600px;">
        <div class="card shadow p-4">
            <h3 class="text-center mb-4 header-title">THÊM DÒNG XE MỚI</h3>
            
            <form action="car-models" method="post">
                <input type="hidden" name="action" value="create">

                <%-- Hãng Xe --%>
                <div class="form-group mb-3">
                    <label class="font-weight-bold">Hãng Xe:</label>
                    <select name="brandId" class="form-control" required>
                        <option value="">-- Chọn hãng xe --</option>
                        <c:forEach items="${brands}" var="b">
                            <option value="${b.brandId}" ${param.brandId == b.brandId ? 'selected' : ''}>
                                ${b.brandName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <%-- Tên Dòng Xe --%>
                <div class="form-group mb-3">
                    <label class="font-weight-bold">Tên Dòng Xe:</label>
                    <input type="text" name="modelName" 
                           class="form-control ${not empty error and error.contains('Dòng xe') ? 'is-invalid' : ''}" 
                           value="${param.modelName}" placeholder="Ví dụ: Q8, Camry..." required>
                </div>

                <%-- Năm Sản Xuất --%>
                <div class="form-group mb-4">
                    <label class="font-weight-bold">Năm Sản Xuất:</label>
                    <input type="number" name="year" 
                           class="form-control ${not empty error ? 'is-invalid' : ''}" 
                           value="${param.year}" placeholder="Ví dụ: 2024" required>
                    
                    <%-- Hiển thị thông báo lỗi ngay dưới input bị sai --%>
                    <c:if test="${not empty error}">
                        <div class="invalid-feedback">
                            <i class="fas fa-exclamation-circle"></i> ${error}
                        </div>
                    </c:if>
                </div>

                <div class="mt-4">
                    <button type="submit" class="btn btn-save btn-block py-2 shadow-sm">
                        <i class="fas fa-save"></i> Lưu Dòng Xe
                    </button>
                    <a href="car-models" class="btn btn-secondary btn-block py-2">Hủy bỏ</a>
                </div>
            </form>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>