<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>搜索 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>搜索结果：${keyword}</h2>
        
        <div class="product-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <a href="${pageContext.request.contextPath}/product/detail?id=${product.id}">
                        <img src="${product.imageUrl}" alt="${product.name}">
                        <h3>${product.name}</h3>
                        <p class="price">¥${product.price}</p>
                        <p class="status ${product.status eq '有货' ? 'in-stock' : 'out-of-stock'}">${product.status}</p>
                    </a>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty products}">
            <p style="text-align: center; padding: 50px; color: #999;">未找到相关商品</p>
        </c:if>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>