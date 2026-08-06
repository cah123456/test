<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>商品管理 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
            <h2>商品管理</h2>
            <a href="${pageContext.request.contextPath}/admin/product/add" class="btn btn-primary">新增商品</a>
        </div>
        
        <div class="table-container">
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>名称</th>
                        <th>分类</th>
                        <th>价格</th>
                        <th>库存</th>
                        <th>状态</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="product" items="${products}">
                        <tr>
                            <td>${product.id}</td>
                            <td>${product.name}</td>
                            <td>${product.categoryName}</td>
                            <td>¥${product.price}</td>
                            <td>${product.stock}</td>
                            <td>${product.status}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/product/edit?id=${product.id}" class="btn btn-sm btn-warning">编辑</a>
                                <form action="${pageContext.request.contextPath}/admin/product/delete" method="post" style="display: inline;">
                                    <input type="hidden" name="id" value="${product.id}">
                                    <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('确定删除该商品？')">删除</button>
                                </form>
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