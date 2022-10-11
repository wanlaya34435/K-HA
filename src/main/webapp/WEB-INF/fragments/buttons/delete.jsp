<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:if test="${userInfo.role.pmDelete eq 'y' }">
<%-- 	<a href="#" onclick="setDeleteItems('${item.quizId}')" data-toggle="modal" data-target="#delModal"> --%>
<!-- 		<em class="fa fa-lg fa-trash-o text-danger"></em> -->
<!-- 	</a>  -->
<%--     <jsp:include page="../../template/dialog/delete.jsp"> --%>
<%--         <jsp:param name="delId" value="${item.quizId}"/> --%>
<%--     </jsp:include> --%>
    
    <a href="#" onclick="onDelete({$1})" class="has-tooltip" title="<spring:message code="btn.delete"/>"><em class="fa fa-lg fa-trash-o text-danger"></em></a>
</c:if>
