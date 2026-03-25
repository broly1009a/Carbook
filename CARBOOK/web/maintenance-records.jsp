<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý bảo trì - CarBook</title>
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
                    <h2>Quản lý lịch bảo trì</h2>
                    <c:if test="${not empty car}">
                        <p class="text-muted">Xe: ${car.model.brand.brandName} ${car.model.modelName} (${car.licensePlate})</p>
                    </c:if>
                </div>
                <div class="col-md-4 text-right">
                    <a href="maintenance?action=create<c:if test='${not empty car}'>&carId=${car.carId}</c:if>" class="btn btn-primary">
                        <i class="fa fa-plus"></i> Thêm lịch bảo trì
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
                <c:remove var="error" scope="session" />
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success alert-dismissible fade show">
                    ${success}
                    <button type="button" class="close" data-dismiss="alert">&times;</button>
                </div>
                <c:remove var="success" scope="session" />
            </c:if>
            
            <!-- Filter Section -->
            <c:if test="${empty car}">
                <div class="card mb-3">
                    <div class="card-body">
                        <form method="get" action="maintenance" class="form-inline">
                            <label class="mr-2">Lọc theo trạng thái:</label>
                            <select name="status" class="form-control mr-2">
                                <option value="">Tất cả</option>
                                <option value="Scheduled" ${param.status == 'Scheduled' ? 'selected' : ''}>Đã lên lịch</option>
                                <option value="In Progress" ${param.status == 'In Progress' ? 'selected' : ''}>Đang bảo trì</option>
                                <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                                <option value="Cancelled" ${param.status == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                            </select>
                            <button type="submit" class="btn btn-primary">Lọc</button>
                            <a href="maintenance" class="btn btn-secondary ml-2">Xóa lọc</a>
                        </form>
                    </div>
                </div>
            </c:if>
            
            <!-- Maintenance List -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty maintenanceList}">
                            <p class="text-center text-muted">Chưa có lịch bảo trì nào.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <c:if test="${empty car}">
                                                <th>Xe</th>
                                            </c:if>
                                            <th>Loại bảo trì</th>
                                            <th>Mô tả</th>
                                            <th>Ngày bảo trì</th>
                                            <th>Chi phí</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="maintenance" items="${maintenanceList}">
                                            <tr>
                                                <c:if test="${empty car}">
                                                    <td>
                                                        <c:set var="maintenanceCar" value="${carDAO.getCarById(maintenance.carId)}" />
                                                        ${maintenanceCar.model.brand.brandName} ${maintenanceCar.model.modelName}<br>
                                                        <small class="text-muted">${maintenanceCar.licensePlate}</small>
                                                    </td>
                                                </c:if>
                                                <td>
                                                    <strong>${maintenance.maintenanceType}</strong><br>
                                                    <c:if test="${not empty maintenance.serviceProvider}">
                                                        <small class="text-muted">${maintenance.serviceProvider}</small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${maintenance.description}
                                                    <c:if test="${not empty maintenance.notes}">
                                                        <br><small class="text-muted"><i>Ghi chú: ${maintenance.notes}</i></small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${maintenance.serviceDate}" pattern="dd/MM/yyyy"/>
                                                    <c:if test="${not empty maintenance.nextServiceDate}">
                                                        <br><small class="text-muted">Bảo trì tiếp: <fmt:formatDate value="${maintenance.nextServiceDate}" pattern="dd/MM/yyyy"/></small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty maintenance.serviceCost}">
                                                        <fmt:formatNumber value="${maintenance.serviceCost}" type="currency" currencySymbol="đ"/>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${maintenance.status == 'Scheduled'}">
                                                            <span class="badge badge-info">Đã lên lịch</span>
                                                        </c:when>
                                                        <c:when test="${maintenance.status == 'In Progress'}">
                                                            <span class="badge badge-warning">Đang bảo trì</span>
                                                        </c:when>
                                                        <c:when test="${maintenance.status == 'Completed'}">
                                                            <span class="badge badge-success">Hoàn thành</span>
                                                        </c:when>
                                                        <c:when test="${maintenance.status == 'Cancelled'}">
                                                            <span class="badge badge-danger">Đã hủy</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-secondary">${maintenance.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="maintenance?action=edit&id=${maintenance.maintenanceId}" 
                                                       class="btn btn-sm btn-warning" title="Sửa">
                                                        <i class="fa fa-edit"></i>
                                                    </a>
                                                    <c:if test="${maintenance.status != 'Completed'}">
                                                        <a href="maintenance?action=complete&id=${maintenance.maintenanceId}" 
                                                           class="btn btn-sm btn-success" 
                                                           onclick="return confirm('Đánh dấu hoàn thành bảo trì?')"
                                                           title="Hoàn thành">
                                                            <i class="fa fa-check"></i>
                                                        </a>
                                                    </c:if>
                                                    <a href="maintenance?action=delete&id=${maintenance.maintenanceId}" 
                                                       class="btn btn-sm btn-danger" 
                                                       onclick="return confirm('Bạn có chắc muốn xóa lịch bảo trì này?')"
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
                        <li><strong>Đã lên lịch (Scheduled):</strong> Lịch bảo trì đã được lên kế hoạch</li>
                        <li><strong>Đang bảo trì (In Progress):</strong> Xe đang trong quá trình bảo trì, không thể đặt</li>
                        <li><strong>Hoàn thành (Completed):</strong> Đã hoàn thành bảo trì</li>
                        <li><strong>Đã hủy (Cancelled):</strong> Lịch bảo trì đã bị hủy</li>
                        <li>Xe đang có lịch bảo trì sẽ tự động bị block và không thể đặt trong khoảng thời gian đó</li>
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
