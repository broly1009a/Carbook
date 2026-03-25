<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty blog ? 'Thêm' : 'Sửa'} bài viết - CarBook</title>
    <link href="https://fonts.googleapis.com/css?family=Poppins:200,300,400,500,600,700,800&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="css/bootstrap.min.css">
    <link rel="stylesheet" href="css/ionicons.min.css">
    <link rel="stylesheet" href="css/style.css">
    <style>
        .form-section {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .form-label {
            font-weight: 600;
            color: #333;
        }
        .required::after {
            content: " *";
            color: red;
        }
        .image-preview {
            max-width: 300px;
            max-height: 200px;
            margin-top: 10px;
            border-radius: 4px;
            display: none;
        }
        .char-counter {
            font-size: 12px;
            color: #666;
            float: right;
        }
        .form-help {
            font-size: 13px;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <%@ include file="includes/navbar.jsp" %>
    
    <section class="ftco-section">
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 px-0">
                    <%@ include file="includes/sidebar.jsp" %>
                </div>
                
                <!-- Main Content -->
                <div class="col-md-9 col-lg-10">
                    <div class="container-fluid">
                        <!-- Header -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <h2>
                                    <i class="icon-${empty blog ? 'plus' : 'pencil'}"></i> 
                                    ${empty blog ? 'Thêm bài viết mới' : 'Sửa bài viết'}
                                </h2>
                            </div>
                            <div class="col-md-6 text-right">
                                <a href="blog-management" class="btn btn-secondary">
                                    <i class="icon-arrow-left"></i> Quay lại
                                </a>
                            </div>
                        </div>
                        
                        <!-- Error Messages -->
                        <c:if test="${not empty sessionScope.error}">
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="icon-close"></i> ${sessionScope.error}
                                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                    <span aria-hidden="true">&times;</span>
                                </button>
                            </div>
                            <c:remove var="error" scope="session" />
                        </c:if>
                        
                        <!-- Blog Form -->
                        <div class="row">
                            <div class="col-md-12">
                                <form action="blog-management" method="post" id="blogForm" class="form-section">
                                    <input type="hidden" name="action" value="${empty blog ? 'create' : 'update'}">
                                    <c:if test="${not empty blog}">
                                        <input type="hidden" name="blogId" value="${blog.blogId}">
                                    </c:if>
                                    
                                    <!-- Title -->
                                    <div class="form-group">
                                        <label for="title" class="form-label required">Tiêu đề bài viết</label>
                                        <input type="text" 
                                               class="form-control" 
                                               id="title" 
                                               name="title" 
                                               value="${blog.title}"
                                               maxlength="200"
                                               required
                                               placeholder="Nhập tiêu đề bài viết">
                                        <span class="char-counter">
                                            <span id="titleCounter">0</span>/200
                                        </span>
                                    </div>
                                    
                                    <!-- Summary -->
                                    <div class="form-group">
                                        <label for="summary" class="form-label">Tóm tắt</label>
                                        <textarea class="form-control" 
                                                  id="summary" 
                                                  name="summary" 
                                                  rows="3"
                                                  maxlength="500"
                                                  placeholder="Nhập tóm tắt ngắn gọn về bài viết (tùy chọn)">${blog.summary}</textarea>
                                        <span class="char-counter">
                                            <span id="summaryCounter">0</span>/500
                                        </span>
                                        <small class="form-help d-block mt-1">
                                            Tóm tắt ngắn sẽ hiển thị trong danh sách bài viết. Nếu để trống, hệ thống sẽ tự động tạo từ nội dung.
                                        </small>
                                    </div>
                                    
                                    <!-- Content -->
                                    <div class="form-group">
                                        <label for="content" class="form-label required">Nội dung</label>
                                        <textarea class="form-control" 
                                                  id="content" 
                                                  name="content" 
                                                  rows="15"
                                                  required
                                                  placeholder="Nhập nội dung bài viết (hỗ trợ HTML)">${blog.content}</textarea>
                                        <small class="form-help d-block mt-1">
                                            Bạn có thể sử dụng HTML tags như &lt;p&gt;, &lt;h2&gt;, &lt;strong&gt;, &lt;ul&gt;, &lt;ol&gt;, &lt;img&gt; để định dạng nội dung.
                                        </small>
                                    </div>
                                    
                                    <div class="row">
                                        <!-- Category -->
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="categoryName" class="form-label">Danh mục</label>
                                                <select class="form-control" id="categoryName" name="categoryName">
                                                    <option value="">-- Chọn danh mục --</option>
                                                    <option value="Mẹo thuê xe" ${blog.categoryName == 'Mẹo thuê xe' ? 'selected' : ''}>Mẹo thuê xe</option>
                                                    <option value="An toàn lái xe" ${blog.categoryName == 'An toàn lái xe' ? 'selected' : ''}>An toàn lái xe</option>
                                                    <option value="Bảo dưỡng xe" ${blog.categoryName == 'Bảo dưỡng xe' ? 'selected' : ''}>Bảo dưỡng xe</option>
                                                    <option value="Du lịch" ${blog.categoryName == 'Du lịch' ? 'selected' : ''}>Du lịch</option>
                                                    <option value="Kinh nghiệm lái xe" ${blog.categoryName == 'Kinh nghiệm lái xe' ? 'selected' : ''}>Kinh nghiệm lái xe</option>
                                                    <option value="Tin tức" ${blog.categoryName == 'Tin tức' ? 'selected' : ''}>Tin tức</option>
                                                    <option value="Khác" ${blog.categoryName == 'Khác' ? 'selected' : ''}>Khác</option>
                                                </select>
                                            </div>
                                        </div>
                                        
                                        <!-- Image URL -->
                                        <div class="col-md-6">
                                            <div class="form-group">
                                                <label for="imageURL" class="form-label">URL hình ảnh</label>
                                                <input type="text" 
                                                       class="form-control" 
                                                       id="imageURL" 
                                                       name="imageURL" 
                                                       value="${blog.imageURL}"
                                                       placeholder="https://example.com/image.jpg"
                                                       onchange="previewImage()">
                                                <small class="form-help d-block mt-1">
                                                    Nhập URL hình ảnh đại diện cho bài viết
                                                </small>
                                                <img id="imagePreview" class="image-preview" alt="Preview">
                                            </div>
                                        </div>
                                    </div>
                                    
                                    <!-- Published Status -->
                                    <div class="form-group">
                                        <div class="custom-control custom-checkbox">
                                            <input type="checkbox" 
                                                   class="custom-control-input" 
                                                   id="isPublished" 
                                                   name="isPublished" 
                                                   value="true"
                                                   ${blog.published ? 'checked' : ''}>
                                            <label class="custom-control-label" for="isPublished">
                                                <strong>Xuất bản bài viết</strong>
                                                <br>
                                                <small class="text-muted">
                                                    Nếu không chọn, bài viết sẽ được lưu dạng nháp và không hiển thị công khai
                                                </small>
                                            </label>
                                        </div>
                                    </div>
                                    
                                    <!-- Additional Info -->
                                    <c:if test="${not empty blog}">
                                        <div class="alert alert-info">
                                            <div class="row">
                                                <div class="col-md-4">
                                                    <strong>ID:</strong> ${blog.blogId}
                                                </div>
                                                <div class="col-md-4">
                                                    <strong>Lượt xem:</strong> ${blog.viewCount}
                                                </div>
                                                <div class="col-md-4">
                                                    <strong>Ngày tạo:</strong> 
                                                    <fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy HH:mm" />
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                    
                                    <!-- Buttons -->
                                    <div class="form-group mt-4">
                                        <button type="submit" class="btn btn-primary btn-lg">
                                            <i class="icon-save"></i> ${empty blog ? 'Tạo bài viết' : 'Cập nhật'}
                                        </button>
                                        <a href="blog-management" class="btn btn-secondary btn-lg ml-2">
                                            <i class="icon-x"></i> Hủy
                                        </a>
                                        <c:if test="${not empty blog}">
                                            <a href="blog?action=view&id=${blog.blogId}" 
                                               class="btn btn-info btn-lg ml-2"
                                               target="_blank">
                                                <i class="icon-eye"></i> Xem trước
                                            </a>
                                        </c:if>
                                    </div>
                                </form>
                            </div>
                        </div>
                        
                    </div>
                </div>
            </div>
        </div>
    </section>
    
    <script src="js/jquery.min.js"></script>
    <script src="js/popper.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    
    <script>
        // Character counter for title
        function updateTitleCounter() {
            var length = $('#title').val().length;
            $('#titleCounter').text(length);
        }
        
        // Character counter for summary
        function updateSummaryCounter() {
            var length = $('#summary').val().length;
            $('#summaryCounter').text(length);
        }
        
        // Image preview
        function previewImage() {
            var url = $('#imageURL').val();
            if (url) {
                $('#imagePreview').attr('src', url).show();
            } else {
                $('#imagePreview').hide();
            }
        }
        
        // Initialize on page load
        $(document).ready(function() {
            updateTitleCounter();
            updateSummaryCounter();
            previewImage();
            
            $('#title').on('input', updateTitleCounter);
            $('#summary').on('input', updateSummaryCounter);
            
            // Form validation
            $('#blogForm').on('submit', function(e) {
                var title = $('#title').val().trim();
                var content = $('#content').val().trim();
                
                if (!title) {
                    alert('Vui lòng nhập tiêu đề bài viết');
                    e.preventDefault();
                    return false;
                }
                
                if (!content) {
                    alert('Vui lòng nhập nội dung bài viết');
                    e.preventDefault();
                    return false;
                }
                
                return true;
            });
        });
    </script>
</body>
</html>
