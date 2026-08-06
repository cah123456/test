<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的订单 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>我的订单</h2>
        
        <div class="order-filter">
            <a href="${pageContext.request.contextPath}/order/list" class="btn ${empty currentStatus ? 'btn-primary' : ''}">全部</a>
            <a href="${pageContext.request.contextPath}/order/list?status=待支付" class="btn ${currentStatus eq '待支付' ? 'btn-primary' : ''}">待支付</a>
            <a href="${pageContext.request.contextPath}/order/list?status=已支付" class="btn ${currentStatus eq '已支付' ? 'btn-primary' : ''}">已支付</a>
            <a href="${pageContext.request.contextPath}/order/list?status=已发货" class="btn ${currentStatus eq '已发货' ? 'btn-primary' : ''}">已发货</a>
            <a href="${pageContext.request.contextPath}/order/list?status=已完成" class="btn ${currentStatus eq '已完成' ? 'btn-primary' : ''}">已完成</a>
        </div>
        
        <c:choose>
            <c:when test="${empty orders}">
                <p style="text-align: center; padding: 50px; color: #999;">暂无订单</p>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>订单号</th>
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
                                    <td>¥${order.totalAmount}</td>
                                    <td>${order.status}</td>
                                    <td>${order.createdAt}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/order/detail?id=${order.id}" class="btn btn-sm btn-info">查看详情</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>