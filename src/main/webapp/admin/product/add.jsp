<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>新增商品 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>新增商品</h2>
        
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        
        <div class="form-container" style="max-width: 600px;">
            <form action="${pageContext.request.contextPath}/admin/product/add" method="post">
                <div class="form-group">
                    <label for="name">商品名称</label>
                    <input type="text" id="name" name="name" required>
                </div>
                <div class="form-group">
                    <label for="categoryId">分类</label>
                    <select id="categoryId" name="categoryId" required>
                        <option value="">请选择分类</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="price">价格</label>
                    <input type="number" id="price" name="price" step="0.01" required>
                </div>
                <div class="form-group">
                    <label for="stock">库存</label>
                    <input type="number" id="stock" name="stock" required>
                </div>
                <div class="form-group">
                    <label for="imageUrl">图片URL</label>
                    <input type="text" id="imageUrl" name="imageUrl">
                </div>
                <div class="form-group">
                    <label for="breed">品种</label>
                    <input type="text" id="breed" name="breed">
                </div>
                <div class="form-group">
                    <label for="age">年龄（月）</label>
                    <input type="number" id="age" name="age">
                </div>
                <div class="form-group">
                    <label for="description">商品描述</label>
                    <textarea id="description" name="description" rows="4"></textarea>
                </div>
                <button type="submit" class="btn-submit">提交</button>
            </form>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>