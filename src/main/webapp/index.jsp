<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>宠物商店 - 首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <main class="container">
        <!-- 轮播图 -->
        <section class="banner">
            <div class="banner-content">
                <h1>欢迎来到宠物商店</h1>
                <p>为您提供最优质的宠物和宠物用品</p>
            </div>
        </section>

        <!-- 商品分类 -->
        <section class="categories">
            <h2>商品分类</h2>
            <div class="category-grid">
                <c:forEach var="category" items="${categories}">
                    <a href="${pageContext.request.contextPath}/product/list?categoryId=${category.id}" class="category-card">
                        <h3>${category.name}</h3>
                        <p>${category.description}</p>
                    </a>
                </c:forEach>
            </div>
        </section>

        <!-- 推荐商品 -->
        <section class="featured-products">
            <h2>推荐商品</h2>
            <div class="product-grid">
                <c:forEach var="product" items="${products}" end="7">
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
        </section>
    </main>

    <jsp:include page="/common/footer.jsp" />
</body>
</html>