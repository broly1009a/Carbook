<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý lịch không khả dụng - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row mb-4">
                <div class="col-md-8">
                    <h2>Quản lý lịch không khả dụng</h2>
                    <c:if test="${not empty car}">
                        <p class="text-muted">Xe: ${car.model.brand.brandName} ${car.model.modelName} (${car.licensePlate})</p>
                    </c:if>
                </div>
                <div class="col-md-4 text-right">
                    <a href="car-availability?action=create<c:if test='${not empty car}'>&carId=${car.carId}</c:if>" class="btn btn-primary">
                        <i class="fa fa-plus"></i> Thêm lịch mới
                    </a>
                    <c:if test="${not empty car}">
                        <a href="car-management?action=edit&id=${car.carId}" class="btn btn-secondary">Quay lại xe</a>
                    </c:if>
                </div>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    ${error}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show">
                    ${success}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
            </c:if>
            
            <!-- Availability List -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty availabilityList}">
                            <p class="text-center text-muted">Chưa có lịch không khả dụng nào.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <c:if test="${empty car}">
                                                <th>Xe</th>
                                            </c:if>
                                            <th>Ngày bắt đầu</th>
                                            <th>Ngày kết thúc</th>
                                            <th>Trạng thái</th>
                                            <th>Lý do</th>
                                            <th>Ngày tạo</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="availability" items="${availabilityList}">
                                            <tr>
                                                <c:if test="${empty car}">
                                                    <td>
                                                        ${availability.carName}<br>
                                                        <small class="text-muted">${availability.licensePlate}</small>
                                                    </td>
                                                </c:if>
                                                <td><fmt:formatDate value="${availability.startDate}" pattern="dd/MM/yyyy"/></td>
                                                <td><fmt:formatDate value="${availability.endDate}" pattern="dd/MM/yyyy"/></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${availability.available}">
                                                            <span class="badge badge-success">Khả dụng</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-danger">Không khả dụng</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>${availability.reason}</td>
                                                <td><fmt:formatDate value="${availability.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                                <td>
                                                    <a href="car-availability?action=edit&id=${availability.availabilityId}" 
                                                       class="btn btn-sm btn-warning" title="Sửa">
                                                        <i class="fa fa-edit"></i>
                                                    </a>
                                                    <a href="car-availability?action=delete&id=${availability.availabilityId}" 
                                                       class="btn btn-sm btn-danger" 
                                                       onclick="return confirm('Bạn có chắc muốn xóa lịch này?')"
                                                       title="Xóa">
                                                        <i class="fa fa-trash"></i>
                                                    </a>
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
            
            <!-- Info Box -->
            <div class="card mt-3">
                <div class="card-body">
                    <h5 class="card-title">Hướng dẫn</h5>
                    <ul>
                        <li><strong>Không khả dụng:</strong> Xe sẽ không thể được đặt trong khoảng thời gian này (ví dụ: bảo trì, sửa chữa)</li>
                        <li><strong>Khả dụng:</strong> Đánh dấu khoảng thời gian xe có thể được đặt (dùng để override các block trước đó)</li>
                        <li>Lịch không khả dụng sẽ được kiểm tra tự động khi khách hàng đặt xe</li>
                    </ul>
                </div>
            </div>
        </div>
    </section>
    
    <%@ include file="includes/footer.jsp" %>
    

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
    <script src="js/jquery.timepicker.min.js"></script>
    <script src="js/scrollax.min.js"></script>
    <script src="js/main.js"></script>
</body>
</html>
