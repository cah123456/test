<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>编辑商品 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>编辑商品</h2>
        
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        
        <div class="form-container" style="max-width: 600px;">
            <form action="${pageContext.request.contextPath}/admin/product/edit" method="post">
                <input type="hidden" name="id" value="${product.id}">
                <div class="form-group">
                    <label for="name">商品名称</label>
                    <input type="text" id="name" name="name" value="${product.name}" required>
                </div>
                <div class="form-group">
                    <label for="categoryId">分类</label>
                    <select id="categoryId" name="categoryId" required>
                        <option value="">请选择分类</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}" ${product.categoryId eq category.id ? 'selected' : ''}>${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="price">价格</label>
                    <input type="number" id="price" name="price" step="0.01" value="${product.price}" required>
                </div>
                <div class="form-group">
                    <label for="stock">库存</label>
                    <input type="number" id="stock" name="stock" value="${product.stock}" required>
                </div>
                <div class="form-group">
                    <label for="imageUrl">图片URL</label>
                    <input type="text" id="imageUrl" name="imageUrl" value="${product.imageUrl}">
                </div>
                <div class="form-group">
                    <label for="breed">品种</label>
                    <input type="text" id="breed" name="breed" value="${product.breed}">
                </div>
                <div class="form-group">
                    <label for="age">年龄（月）</label>
                    <input type="number" id="age" name="age" value="${product.age}">
                </div>
                <div class="form-group">
                    <label for="description">商品描述</label>
                    <textarea id="description" name="description" rows="4">${product.description}</textarea>
                </div>
                <button type="submit" class="btn-submit">保存</button>
            </form>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>