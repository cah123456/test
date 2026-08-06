<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单详情 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>订单详情</h2>
        
        <div class="order-info">
            <p><strong>订单号：</strong>${order.orderNumber}</p>
            <p><strong>订单状态：</strong>${order.status}</p>
            <p><strong>收货地址：</strong>${order.shippingAddress}</p>
            <p><strong>联系电话：</strong>${order.contactPhone}</p>
            <p><strong>下单时间：</strong>${order.createdAt}</p>
        </div>
        
        <h3>订单商品</h3>
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>商品名称</th>
                        <th>单价</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${order.orderItems}">
                        <tr>
                            <td>${item.productName}</td>
                            <td>¥${item.productPrice}</td>
                            <td>${item.quantity}</td>
                            <td>¥${item.subtotal}</td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" style="text-align: right;"><strong>总计：</strong></td>
                        <td><strong>¥${order.totalAmount}</strong></td>
                    </tr>
                </tfoot>
            </table>
        </div>
        
        <div style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/order/list" class="btn btn-primary">返回订单列表</a>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>