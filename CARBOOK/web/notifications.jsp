<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông báo - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .notification-item {
            border-left: 3px solid #ddd;
            transition: all 0.2s;
        }
        .notification-item.unread {
            background-color: #f8f9fa;
            border-left-color: #007bff;
        }
        .notification-item:hover {
            background-color: #f0f0f0;
            cursor: pointer;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container">
            <div class="row mb-3">
                <div class="col-md-6">
                    <h2>Thông báo</h2>
                </div>
                <div class="col-md-6 text-right">
                    <a href="notification?action=markAllRead" class="btn btn-outline-primary">Đánh dấu tất cả đã đọc</a>
                    <div class="btn-group ml-2" role="group">
                        <a href="notification?action=list" class="btn btn-${empty param.filter ? 'primary' : 'outline-primary'}">Tất cả</a>
                        <a href="notification?action=list&filter=unread" class="btn btn-${param.filter == 'unread' ? 'primary' : 'outline-primary'}">Chưa đọc</a>
                    </div>
                </div>
            </div>
            
            <c:if test="${not empty success}">
                <div class="alert alert-success">${success}</div>
            </c:if>
            
            <!-- Notifications -->
            <div class="card">
                <div class="card-body p-0">
                    <c:choose>
                        <c:when test="${empty notifications}">
                            <p class="text-center p-4">Không có thông báo nào.</p>
                        </c:when>
                        <c:otherwise>
                            <div class="list-group list-group-flush">
                                <c:forEach var="notification" items="${notifications}">
                                    <div class="list-group-item notification-item ${notification.read ? '' : 'unread'}" onclick="markAsRead(${notification.notificationId})">
                                        <div class="d-flex w-100 justify-content-between">
                                            <h6 class="mb-1">
                                                <c:if test="${!notification.read}">
                                                    <span class="badge badge-primary">Mới</span>
                                                </c:if>
                                                ${notification.title}
                                            </h6>
                                            <small><fmt:formatDate value="${notification.createdAt}" pattern="dd/MM/yyyy HH:mm"/></small>
                                        </div>
                                        <p class="mb-1">${notification.message}</p>
                                    </div>
                                </c:forEach>
                            </div>
                            
                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <div class="p-3">
                                    <nav>
                                        <ul class="pagination justify-content-center mb-0">
                                            <c:forEach begin="1" end="${totalPages}" var="i">
                                                <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                    <a class="page-link" href="notification?action=list&page=${i}&filter=${param.filter}">${i}</a>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </nav>
                                </div>
                            </c:if>
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
  <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBVWaKrjvy3MaE7SQ74_uJiULgl1JY0H2s&sensor=false"></script>
  <script src="js/google-map.js"></script>
  <script src="js/main.js"></script>

    <script>
        function markAsRead(notificationId) {
            fetch('notification?action=markRead&id=' + notificationId, {
                method: 'GET'
            }).then(() => {
                // Reload page to update UI
                location.reload();
            }).catch(error => {
                console.error('Error marking notification as read:', error);
            });
        }
    </script>
</body>
</html>
