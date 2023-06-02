<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!-- 2개의 jsp를 하나로 만듬 -->
<%@ include file="/WEB-INF/view/jspHeader.jsp" %> 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>주문완료</title>
</head>
<body>
<h2> ${sale.user.username }님이 주문하신 정보 입니다.</h2>
<h2>배송지 정보</h2>
<table>
	<tr>
		<td width="30%">주문아이디</td>
		<td width="70%">${sessionScope.loginUser.userid}</td>
	</tr>
	<tr>
		<td width="30%">이름</td>
		<td width="70%">${sessionScope.loginUser.username}</td>
	</tr>
	<tr>
		<td width="30%">우편번호</td>
		<td width="70%">${sessionScope.loginUser.postcode}</td>
	</tr>
	<tr>
		<td width="30%">주소</td>
		<td width="70%">${sessionScope.loginUser.address}</td>
	</tr>
	<tr>
		<td width="30%">전화번호</td>
		<td width="70%">${sessionScope.loginUser.phoneno}</td>
	</tr>
</table>
<h2>주문 상품</h2>
<table>
	<tr>
		<td>상품명</td>
		<td>가격</td>
		<td>수량</td>
		<td>합계금액</td>
	</tr>
	<c:forEach items="${sale.itemList}" var="saleitem">
		<tr>
			<td>${saleitem.item.name}</td>
			<td>
				<fmt:formatNumber value="${saleitem.item.price}" pattern="###,###"/>
			</td>
			<td>${saleitem.quantity}</td>
			<td>
				<fmt:formatNumber value="${saleitem.item.price * saleitem.quantity}" pattern="###,###"/>
			</td>
		</tr>
	</c:forEach>
	<tr>
		<td colspan="4" align="right">
			주문금액 : <fmt:formatNumber value="${sale.total}" pattern="###,###"/>
		</td>
	</tr>
	<tr>
		<td colspan="4">
			<a href="../item/list">상품목록</a>
		</td>
	</tr>
</table>
</body>
</html>