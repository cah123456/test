<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户登录 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="form-container">
        <h2>用户登录</h2>
        
        <c:if test="${param.registered eq 'true'}">
            <div class="success-message">注册成功！请登录。</div>
        </c:if>
        
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        
        <form action="${pageContext.request.contextPath}/user/login" method="post">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn-submit">登录</button>
        </form>
        
        <p style="text-align: center; margin-top: 20px;">
            还没有账号？<a href="${pageContext.request.contextPath}/user/register.jsp">立即注册</a>
        </p>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>