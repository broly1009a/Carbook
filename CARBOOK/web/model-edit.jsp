<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chỉnh Sửa Dòng Xe - CarBook</title>
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <style>
        body { background-color: #f8f9fa; }
        .container { margin-top: 100px; }
        .card { 
            border-radius: 15px; 
            border: none; 
            box-shadow: 0 10px 20px rgba(0,0,0,0.1); 
            background: #ffffff;
        }
        /* Đổi sang màu xanh #01d28e cho đồng bộ */
        .header-title { color: #01d28e; font-weight: bold; }
        .btn-update { 
            background-color: #01d28e; 
            color: white; 
            border: none; 
            font-weight: 600; 
            transition: 0.3s;
        }
        .btn-update:hover { background-color: #01a871; color: white; }
        .alert { border-radius: 10px; }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card p-4">
                    <h2 class="text-center mb-4 header-title">CHỈNH SỬA DÒNG XE</h2>

                    <%-- HIỂN THỊ THÔNG BÁO LỖI NẾU CÓ (Ví dụ lỗi trùng năm) --%>
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <strong>Lỗi!</strong> ${error}
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <%-- Không cần c:remove ở đây vì Servlet dùng forward (request scope) --%>
                    </c:if>

                    <form action="car-models" method="post">
                        <%-- Action và ID ẩn --%>
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="modelId" value="${model.modelId}">

                        <div class="form-group mb-3">
                            <label class="font-weight-bold">Hãng Xe:</label>
                            <select name="brandId" class="form-control" required>
                                <c:forEach items="${brands}" var="b">
                                    <option value="${b.brandId}" ${b.brandId == model.brandId ? 'selected' : ''}>
                                        ${b.brandName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="form-group mb-3">
                            <label class="font-weight-bold">Tên Dòng Xe:</label>
                            <%-- Lưu ý: dùng ${model.modelName} cho giá trị cũ --%>
                            <input type="text" name="modelName" class="form-control" 
                                   value="${not empty error ? param.modelName : model.modelName}" required>
                        </div>

                        <div class="form-group mb-4">
                            <label class="font-weight-bold">Năm Sản Xuất:</label>
                            <input type="number" name="year" class="form-control" 
                                   value="${not empty error ? param.year : model.year}" required>
                        </div>

                        <div class="d-flex justify-content-between">
                            <a href="car-models" class="btn btn-outline-secondary px-4">Hủy bỏ</a>
                            <button type="submit" class="btn btn-update px-4 shadow-sm">Lưu Cập Nhật</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <%-- Thêm script để đóng alert và hỗ trợ giao diện --%>
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>