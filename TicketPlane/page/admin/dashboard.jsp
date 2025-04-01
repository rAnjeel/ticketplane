<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
    <h2>Admin Dashboard</h2>
    <p>Welcome admin: ${sessionScope.username}</p>
    
    <div>
        <h3>Admin Actions:</h3>
        <ul>
            <li><a href="../user/list">Manage Users</a></li>
            <li><a href="../admin/settings">System Settings</a></li>
        </ul>
    </div>
    
    <a href="../">Back to Home</a>
</body>
</html> 