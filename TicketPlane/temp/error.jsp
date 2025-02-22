<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        .error-container {
            max-width: 600px;
            margin: 50px auto;
            padding: 20px;
            text-align: center;
            background-color: #fff3f3;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .error-message {
            color: #dc3545;
            font-size: 24px;
            margin-bottom: 20px;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            padding: 10px 20px;
            background-color: #dc3545;
            color: white;
            text-decoration: none;
            border-radius: 4px;
        }
        .back-link:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>
    <div class="error-container">
        <div class="error-message">
            <h1>⚠️ Error</h1>
            <p><%= request.getAttribute("errorMessage") %></p>
        </div>
        
        <a href="${pageContext.request.contextPath}/register.jsp" class="back-link">Return to Registration Form</a>
    </div>
</body>
</html> 