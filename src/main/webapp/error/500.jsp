<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 - 服务器内部错误</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <jsp:include page="/common/header.jsp" />
    
    <div class="container" style="text-align: center; padding: 100px 0;">
        <h1>500</h1>
        <p>抱歉，服务器内部错误，请稍后重试。</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">返回首页</a>
    </div>
    
    <jsp:include page="/common/footer.jsp" />
</body>
</html>