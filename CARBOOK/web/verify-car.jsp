<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Phê duyệt xe - CarBook Admin</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="h3 mb-0">🔍 Phê duyệt xe mới</h2>
                <a href="home" class="btn btn-outline-dark btn-sm">Quay về Trang chủ</a>
            </div>

            <div class="card mb-4 border-0 shadow-sm">
                <div class="card-body">
                    <form action="verify" method="get" class="form-inline">
                        <input type="text" name="search" class="form-control mr-2" placeholder="Tìm biển số..." value="${param.search}">
                        <select name="categoryId" class="form-control mr-2">
                            <option value="">-- Tất cả loại xe --</option>
                            <c:forEach items="${categories}" var="cat">
                                <option value="${cat.categoryId}" ${param.categoryId == cat.categoryId ? 'selected' : ''}>${cat.categoryName}</option>
                            </c:forEach>
                        </select>
                        <button type="submit" class="btn btn-primary px-4">Lọc</button>
                        <a href="verify" class="btn btn-link text-secondary">Xóa lọc</a>
                    </form>
                </div>
            </div>

            <c:if test="${not empty sessionScope.success}">
                <div class="alert alert-success">${sessionScope.success}</div>
                <c:remove var="success" scope="session"/>
            </c:if>

            <div class="card border-0 shadow-sm">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty pendingCars}">
                            <p class="text-center py-5 text-muted">Không có xe nào đang chờ duyệt.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead class="thead-light">
                                        <tr>
                                            <th>Ảnh</th>
                                            <th>Thông tin</th>
                                            <th>Chủ xe</th>
                                            <th>Giá thuê</th>
                                            <th class="text-center">Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="car" items="${pendingCars}">
                                            <tr>
                                                <td><img src="${car.imageUrl}" class="rounded" style="width: 100px; height: 60px; object-fit: cover;"></td>
                                                <td>
                                                    <strong>${car.model.modelName}</strong><br>
                                                    <small class="text-info">${car.licensePlate}</small>
                                                </td>
                                                <td>${car.owner.fullName}</td>
                                                <td><fmt:formatNumber value="${car.pricePerDay}" type="currency" currencySymbol="₫"/></td>
                                                <td class="text-center">
                                                    <form action="verify" method="POST" class="d-inline">
                                                        <input type="hidden" name="carId" value="${car.carId}">
                                                        <button type="submit" name="action" value="approve" class="btn btn-success btn-sm px-3" onclick="return confirm('Duyệt xe này?')">Duyệt</button>
                                                        <button type="submit" name="action" value="reject" class="btn btn-outline-danger btn-sm px-3" onclick="return confirm('Từ chối xe này?')">Từ chối</button>
                                                    </form>
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