<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="col-sm-2">
	<c:set var="onclick" value="onclick=window.location.href='/${appName }/${viewform}'" />	
	<c:if test="${method eq 'approve' }">
		<c:set var="onclick" value="onclick=window.location.href='/${appName }/approveitem/viewform'" />	
	</c:if>
	<c:if test="${not empty edit}">
		<c:set var="onclick" value="onclick=javascript:history.back()" />
	</c:if>
	
	<h3><button type="button" class="btn btn-danger btn-block"  ${onclick }><em class="fa fa-times"></em>&nbsp;<spring:message code="btn.cancel"/></button></h3>
	
</div>