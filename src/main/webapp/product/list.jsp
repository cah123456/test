<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品列表 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <div class="sidebar">
            <h3>商品分类</h3>
            <ul class="category-list">
                <li><a href="${pageContext.request.contextPath}/product/list" class="${empty currentCategory ? 'active' : ''}">全部分类</a></li>
                <c:forEach var="category" items="${categories}">
                    <li><a href="${pageContext.request.contextPath}/product/list?categoryId=${category.id}" class="${currentCategory eq category.id ? 'active' : ''}">${category.name}</a></li>
                </c:forEach>
            </ul>
        </div>
        
        <div class="main-content">
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
                <p style="text-align: center; padding: 50px; color: #999;">暂无商品</p>
            </c:if>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>