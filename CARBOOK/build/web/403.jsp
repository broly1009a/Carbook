<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Truy cập bị từ chối - 403</title>
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            text-align: center;
            background: white;
            padding: 50px;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.1);
        }
        h1 {
            font-size: 80px;
            color: #dc3545;
            margin: 0;
        }
        h2 {
            color: #343a40;
            margin-bottom: 20px;
        }
        p {
            color: #6c757d;
            font-size: 18px;
            margin-bottom: 30px;
        }
        .btn {
            display: inline-block;
            padding: 12px 25px;
            background-color: #007bff;
            color: white;
            text-decoration: none;
            border-radius: 5px;
            transition: background 0.3s;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        .icon {
            font-size: 60px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="icon">🚫</div>
        <h1>403</h1>
        <h2>Rất tiếc! Bạn không có quyền</h2>
        <p>Bạn không được phép truy cập vào trang này. Vui lòng liên hệ Admin nếu bạn cho rằng đây là một sự nhầm lẫn.</p>
        <a href="home" class="btn">Quay lại trang chủ</a>
    </div>
</body>
</html>