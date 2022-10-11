<%@ include file="../../common/taglibs.jsp"%>


<form:form method="POST" action="">
    <input type="hidden" id="delId" name="delId" />
    <div class="row">
        <div class="col-sm-9">
            <h3><spring:message code="form.list"/>${pageHeader}</h3>
        </div>

        <jsp:include page="../../fragments/buttons/create.jsp" />

        <!-- /.col-lg-12 -->
    </div>
    <!-- /.row -->
    <div class="row">
        <div class="col-lg-12">
            <div class="panel panel-default">

                <!-- /.panel-heading -->
                <div class="panel-body">
                    <div class="dataTable_wrapper">
                        <table class="table table-striped table-bordered table-hover" id="dataTables-list">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th><spring:message code="${context }.name"/></th>
                                <th><spring:message code="${context }.status"/></th>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${items}" var="item" varStatus="row">
                                <tr>
                                    <td>${item.zoneId}</td>
                                    <td><c:out value="${item.name}"/></td>
                                    <td><c:out value="${item.statusId}"/></td>
                                    <td align="center">

                                        <a href="#" onclick="onUpdate('${item.zoneId}')" title="update"><em class="fa fa-lg fa-pencil-square-o text-info"></em></a>

                                        &emsp;

                                        <%--<c:if test="${userInfo.role.pmDelete eq 'y' }">--%>
                                            <a onclick="onDelete(${item.zoneId})" class="has-tooltip cursor" title="<spring:message code="btn.delete"/>"><em class="fa fa-lg fa-trash-o text-danger"></em></a>
                                        <%--</c:if>--%>

                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>


                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
        <!-- /.col-lg-12 -->
    </div>
</form:form>

<!-- javascript for CRUD -->
<jsp:include page="../../fragments/js/crud.jsp" />
