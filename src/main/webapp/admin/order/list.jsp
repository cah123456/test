<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单管理 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>订单管理</h2>
        
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>订单号</th>
                        <th>用户</th>
                        <th>总金额</th>
                        <th>状态</th>
                        <th>下单时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td>${order.orderNumber}</td>
                            <td>${order.username}</td>
                            <td>¥${order.totalAmount}</td>
                            <td>${order.status}</td>
                            <td>${order.createdAt}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/order/detail?id=${order.id}" class="btn btn-sm btn-info">查看详情</a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>