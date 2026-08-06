<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购物车 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>我的购物车</h2>
        
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        
        <c:choose>
            <c:when test="${empty cartItems}">
                <p style="text-align: center; padding: 50px; color: #999;">购物车是空的，快去选购商品吧！</p>
            </c:when>
            <c:otherwise>
                <div class="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>商品</th>
                                <th>单价</th>
                                <th>数量</th>
                                <th>小计</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:set var="totalAmount" value="0" />
                            <c:forEach var="item" items="${cartItems}">
                                <tr>
                                    <td>
                                        <img src="${item.productImageUrl}" alt="${item.productName}" style="width: 50px; height: 50px; object-fit: cover;">
                                        ${item.productName}
                                    </td>
                                    <td>¥${item.productPrice}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/update" method="post" style="display: inline;">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <input type="number" name="quantity" value="${item.quantity}" min="1" style="width: 60px;">
                                            <button type="submit" class="btn btn-sm btn-primary">更新</button>
                                        </form>
                                    </td>
                                    <td>¥${item.productPrice * item.quantity}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/cart/delete" method="post" style="display: inline;">
                                            <input type="hidden" name="cartItemId" value="${item.id}">
                                            <button type="submit" class="btn btn-sm btn-danger">删除</button>
                                        </form>
                                    </td>
                                </tr>
                                <c:set var="totalAmount" value="${totalAmount + item.productPrice * item.quantity}" />
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div style="text-align: right; margin-top: 20px;">
                    <h3>总计：¥${totalAmount}</h3>
                    <a href="${pageContext.request.contextPath}/cart/checkout.jsp" class="btn btn-primary">去结算</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>