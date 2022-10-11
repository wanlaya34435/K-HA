<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<!-- <FILTER> -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">

			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="dataTable_wrapper">

					<!-- <FILTER> -->
					<%--<jsp:include page="../template/element_org.jsp" />--%>
					<!-- </FILTER> -->

					<div class="row">
						<div class="col-sm-4">
							<div class="row form-group">
								<label class="col-sm-4 control-label text-right"><spring:message code="${context }.type"/></label>
								<div class="col-sm-8">
									<select class="form-control select2" id="bannerType" name="bannerType">
										<option value=""></option>
										<c:forEach items="${bannerTypeMap}" var="item" varStatus="loop">
											<option value="${item.id}">${item.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>

						<div class="col-sm-4">
							<div class="row form-group">
								<label class="col-sm-4 control-label text-right"><spring:message code="${context }.startdate"/></label>
								<div class="col-sm-8">
									<input type="text" name="startDate" id="startDate" value="" class="form-control">
								</div>

							</div>
						</div>

						<div class="col-sm-4">
							<div class="row form-group">
								<label class="col-sm-4 control-label text-right"><spring:message code="${context }.stopdate"/></label>
								<div class="col-sm-8">
									<input type="text" name="stopDate" id="stopDate" value="" class="form-control">
								</div>

							</div>
						</div>

					</div>


					<div class="row">
						<div class="col-sm-4">
							<div class="row form-group">
								<label class="col-sm-4 control-label text-right"><spring:message code="${context }.status"/></label>
								<div class="col-sm-8">
									<select class="form-control select2" id="statusId" name="statusId">
										<option value=""></option>
										<c:forEach items="${statusMap}" var="item" varStatus="loop">
											<option value="${item.statusId}">${item.name}</option>
										</c:forEach>
									</select>
								</div>
							</div>
						</div>

                        <div class="col-sm-8 text-right">
                            <button class="btn btn-warning btn-mw-100" id="btnFilter">
                                <spring:message code="btn.filter" />
                            </button>
                        </div>
					</div>
                    

				</div>
			</div>

		</div>
	</div>
</div>
<!-- </FILTER> -->