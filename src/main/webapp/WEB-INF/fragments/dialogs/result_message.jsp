<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:if test="${not empty res_msg}">
	<div class="row" id="flash-area" style="position: absolute; z-index: 999; width: 80%; margin-top: 3px;">
		<div class="col-md-5 col-md-offset-3">
		
	<!-- ####### FILL ROW ###### -->	
<!-- 	<div class="row" id="flash-area"> -->
<!-- 		<div class="col-sm-12"> -->
	<!-- ####################### -->	
			<c:set var="status" value="success" />
			
			<c:if test="${res_code != 200 }">
				<c:set var="status" value="danger" />
			</c:if>
			
			<div class="alert alert-${status } alert-dismissable">
				<button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>
				<p class="text-center">
					<strong>${res_msg }</strong>
				</p>
			</div>
		</div>
	</div>
</c:if>