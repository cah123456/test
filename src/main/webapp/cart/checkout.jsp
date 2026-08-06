<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>结算 - 宠物商店</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container">
        <h2>订单结算</h2>
        
        <c:if test="${not empty error}">
            <div class="error-message">${error}</div>
        </c:if>
        
        <div class="form-container" style="max-width: 600px;">
            <form action="${pageContext.request.contextPath}/order/create" method="post">
                <div class="form-group">
                    <label for="shippingAddress">收货地址</label>
                    <input type="text" id="shippingAddress" name="shippingAddress" required>
                </div>
                <div class="form-group">
                    <label for="contactPhone">联系电话</label>
                    <input type="text" id="contactPhone" name="contactPhone" required>
                </div>
                <button type="submit" class="btn-submit">提交订单</button>
            </form>
        </div>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>