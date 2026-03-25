<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đánh giá - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .stars {
            color: #ffc107;
        }
        .stars-empty {
            color: #ddd;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row mb-3">
                <div class="col-md-6">
                    <h2>
                        <c:choose>
                            <c:when test="${param.action == 'pending'}">Đánh giá chờ duyệt</c:when>
                            <c:otherwise>Đánh giá của tôi</c:otherwise>
                        </c:choose>
                    </h2>
                </div>
                <c:if test="${sessionScope.user.roleId == 2}">
                    <div class="col-md-6 text-right">
                        <a href="review?action=myReviews" class="btn btn-${param.action == 'myReviews' ? 'primary' : 'outline-primary'}">Đánh giá của tôi</a>
                        <a href="review?action=pending" class="btn btn-${param.action == 'pending' ? 'primary' : 'outline-primary'}">Chờ duyệt</a>
                    </div>
                </c:if>
            </div>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger">${error}</div>
            </c:if>
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            
            <!-- Reviews list -->
            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty reviews}">
                            <p class="text-center">Chưa có đánh giá nào.</p>
                        </c:when>
                        <c:otherwise>
                            <c:forEach var="review" items="${reviews}">
                                <div class="card mb-3">
                                    <div class="card-body">
                                        <div class="d-flex justify-content-between">
                                            <div>
                                                <h5>
                                                    <c:forEach begin="1" end="5" var="i">
                                                        <span class="${i <= review.rating ? 'stars' : 'stars-empty'}">★</span>
                                                    </c:forEach>
                                                </h5>
                                                <p class="mb-1">
                                                    <strong>Khách hàng:</strong> ${review.customer.fullName}<br>
                                                    <strong>Xe:</strong> ${review.booking.car.licensePlate}<br>
                                                    <strong>Đơn:</strong> ${review.booking.bookingReference}<br>
                                                    <strong>Ngày:</strong> <fmt:formatDate value="${review.createdAt}" pattern="dd/MM/yyyy"/>
                                                </p>
                                            </div>
                                            <div>
                                                <span class="badge badge-${review.status == 'Pending' ? 'warning' : review.status == 'Approved' ? 'success' : 'danger'}">
                                                    ${review.status}
                                                </span>
                                            </div>
                                        </div>
                                        
                                        <p class="mt-2">${review.comment}</p>
                                        
                                        <c:if test="${review.status == 'Pending' && sessionScope.user.roleId == 2}">
                                            <div class="mt-2">
                                                <a href="review?action=approve&id=${review.reviewId}" class="btn btn-sm btn-success" onclick="return confirm('Duyệt đánh giá này?')">Duyệt</a>
                                                <button class="btn btn-sm btn-danger" data-toggle="modal" data-target="#rejectModal${review.reviewId}">Từ chối</button>
                                            </div>
                                            
                                            <!-- Reject Modal -->
                                            <div class="modal fade" id="rejectModal${review.reviewId}" tabindex="-1" role="dialog">
                                                <div class="modal-dialog" role="document">
                                                    <div class="modal-content">
                                                        <form action="review" method="get">
                                                            <input type="hidden" name="action" value="reject">
                                                            <input type="hidden" name="id" value="${review.reviewId}">
                                                            <div class="modal-header">
                                                                <h5 class="modal-title">Từ chối đánh giá</h5>
                                                                <button type="button" class="close" data-dismiss="modal">
                                                                    <span>&times;</span>
                                                                </button>
                                                            </div>
                                                            <div class="modal-body">
                                                                <div class="form-group">
                                                                    <label>Lý do từ chối:</label>
                                                                    <textarea class="form-control" name="reason" rows="3" required></textarea>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer">
                                                                <button type="button" class="btn btn-secondary" data-dismiss="modal">Hủy</button>
                                                                <button type="submit" class="btn btn-danger">Từ chối</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                        
                                        <c:if test="${review.status == 'Rejected'}">
                                            <div class="alert alert-danger mt-2 mb-0">
                                                <strong>Lý do từ chối:</strong> ${review.rejectionReason}
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </c:forEach>
                            
                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <nav>
                                    <ul class="pagination justify-content-center">
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="review?action=${param.action}&page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </nav>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </section>
    
    <%@ include file="includes/footer.jsp" %>
    
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
</body>
</html>
