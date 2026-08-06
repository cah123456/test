<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>后台管理 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>后台管理</h2>
        
        <div class="admin-menu">
            <div class="menu-card">
                <h3>商品管理</h3>
                <p>管理商品信息，包括新增、编辑、删除商品</p>
                <a href="${pageContext.request.contextPath}/admin/product/list" class="btn btn-primary">进入管理</a>
            </div>
            <div class="menu-card">
                <h3>订单管理</h3>
                <p>管理订单状态，查看订单详情</p>
                <a href="${pageContext.request.contextPath}/admin/order/list" class="btn btn-primary">进入管理</a>
            </div>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>