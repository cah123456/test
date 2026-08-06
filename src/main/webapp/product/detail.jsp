<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product.name} - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <div class="product-detail">
            <div class="product-image">
                <img src="${product.imageUrl}" alt="${product.name}">
            </div>
            <div class="product-info">
                <h1>${product.name}</h1>
                <p class="price">¥${product.price}</p>
                <p class="status ${product.status eq '有货' ? 'in-stock' : 'out-of-stock'}">${product.status}</p>
                
                <div class="product-meta">
                    <p><strong>分类：</strong>${product.categoryName}</p>
                    <p><strong>品种：</strong>${product.breed}</p>
                    <p><strong>年龄：</strong>${product.age}个月</p>
                    <p><strong>库存：</strong>${product.stock}</p>
                </div>
                
                <div class="product-description">
                    <h3>商品描述</h3>
                    <p>${product.description}</p>
                </div>
                
                <c:if test="${product.status eq '有货'}">
                    <form action="${pageContext.request.contextPath}/cart/add" method="post" class="add-to-cart-form">
                        <input type="hidden" name="productId" value="${product.id}">
                        <div class="form-group">
                            <label for="quantity">数量</label>
                            <input type="number" id="quantity" name="quantity" value="1" min="1" max="${product.stock}">
                        </div>
                        <button type="submit" class="btn-submit">加入购物车</button>
                    </form>
                </c:if>
            </div>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>