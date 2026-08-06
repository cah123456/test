<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户注册 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="form-container">
        <h2>用户注册</h2>
        
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/user/register" method="post">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <label for="nickname">昵称</label>
                <input type="text" id="nickname" name="nickname">
            </div>
            <div class="form-group">
                <label for="email">邮箱</label>
                <input type="email" id="email" name="email">
            </div>
            <button type="submit" class="btn-submit">注册</button>
        </form>
        
        <p style="text-align: center; margin-top: 20px;">
            已有账号？<a href="${pageContext.request.contextPath}/user/login.jsp">立即登录</a>
        </p>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>