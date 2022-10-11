<%@ include file="../../common/taglibs.jsp"%>

<form:form id="${appName }" name="${appName }" modelAttribute="item" method="POST" action="/${appName }/${action }" role="form" enctype="multipart/form-data" data-toggle="validator">
    <form:input path="${pk }" type="hidden" />
    <input type="hidden" name="method" value="${method}" />

    <!-- <hidden value> -->

    <!-- </hidden value> -->


    <jsp:include page="../../fragments/dialogs/result_message.jsp" />

    <div class="row">
        <div class="col-sm-8">
            <h3>${moduleName}</h3>
        </div>

        <jsp:include page="../../fragments/buttons/update.jsp" />

        <jsp:include page="../../fragments/buttons/cancel.jsp" />
    </div>


    <!-- /.row -->
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-default">
                <%--<div class="panel-heading">${moduleName}</div>--%>
                <!-- /.panel-heading -->
                <div class="panel-body">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs">
                        <li class="active">
                            <a href="#tabs_general" data-toggle="tab"><spring:message code="tabs.general"/></a>
                        </li>
                        <li>
                            <a href="#tabs_image" data-toggle="tab"><spring:message code="tabs.image"/></a>
                        </li>
                    </ul>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div class="tab-pane fade in active" id="tabs_general">
                            <br>
                            <jsp:include page="tab_general.jsp" />
                        </div>

                        <div class="tab-pane fade" id="tabs_image">
                            <br>
                            <jsp:include page="tab_image.jsp" />
                        </div>

                    </div>
                </div>
                <!-- /.panel-body -->
            </div>
            <!-- /.panel -->
        </div>
    </div>



</form:form>




<!-- ####### validate form ######## -->
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery-form-validator/2.2.43/jquery.form-validator.min.js"></script>
<script>
    $.validate({
        modules : 'security, file',
    });


    $(document).ready(function () {

        // display selected question
        <c:forEach items="${previews}" var="item" varStatus="previewCount">
        // display section
        addPreview();
        $("#previewTags > div:last [name=previewId]").val('${item.bannerImageId}');

        // check & display attach file
        <c:if test="${not empty item.imgLocation }">
        $("#previewTags > div:last [id=previewAttachDiv]").attr('style','display: block;');
        $("#previewTags > div:last [name=imgLocation]").attr('href', '${item.imgLocation}');
        $("#previewTags > div:last [id=imgUrl]").attr('href', '${url}${item.imgUrl}');
        </c:if>

        </c:forEach>

    });
</script>


