<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header class="header">
    <div class="header-container">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/">宠物商店</a>
        </div>
        <nav class="nav">
            <ul>
                <li><a href="${pageContext.request.contextPath}/">首页</a></li>
                <li><a href="${pageContext.request.contextPath}/product/list">商品列表</a></li>
                <c:if test="${not empty sessionScope.user}">
                    <li><a href="${pageContext.request.contextPath}/cart/list">购物车</a></li>
                    <li><a href="${pageContext.request.contextPath}/order/list">我的订单</a></li>
                </c:if>
            </ul>
        </nav>
        <div class="user-actions">
            <c:choose>
                <c:when test="${not empty sessionScope.user}">
                    <span class="username">欢迎，${sessionScope.user.nickname}</span>
                    <c:if test="${sessionScope.user.role eq 'admin'}">
                        <a href="${pageContext.request.contextPath}/admin/index.jsp" class="btn-admin">后台管理</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/user/logout" class="btn-logout">退出</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn-login">登录</a>
                    <a href="${pageContext.request.contextPath}/user/register.jsp" class="btn-register">注册</a>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</header>