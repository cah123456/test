<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人资料 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>个人资料</h2>
        <div class="form-container" style="max-width: 600px;">
            <div class="form-group">
                <label>用户名</label>
                <p>${user.username}</p>
            </div>
            <div class="form-group">
                <label>昵称</label>
                <p>${user.nickname}</p>
            </div>
            <div class="form-group">
                <label>邮箱</label>
                <p>${user.email}</p>
            </div>
            <div class="form-group">
                <label>角色</label>
                <p>${user.role eq 'admin' ? '管理员' : '普通用户'}</p>
            </div>
            <div class="form-group">
                <label>注册时间</label>
                <p>${user.createdAt}</p>
            </div>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>