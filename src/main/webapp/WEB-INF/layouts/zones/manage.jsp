<%@ include file="../../common/taglibs.jsp"%>

<form:form id="${appName }" name="${appName }" modelAttribute="item" method="POST" action="/${appName }/${action }" role="form" enctype="multipart/form-data" data-toggle="validator">
    <form:input path="${pk }" type="hidden" />
    <input type="hidden" name="method" value="${method}" />

    <!-- <hidden value> -->
    <input type="hidden" name="delPreviewId" id="delPreviewId" value="" />

    <!-- </hidden value> -->


    <jsp:include page="../../fragments/dialogs/result_message.jsp" />

    <div class="row">
        <div class="col-sm-8">
            <h3>${pageHeader}</h3>
        </div>

        <jsp:include page="../../fragments/buttons/update.jsp" />

        <jsp:include page="../../fragments/buttons/cancel.jsp" />
    </div>
    <!-- /.row -->
    <div class="row">

        <!-- ################################################################################## -->
        <!-- [ <'LEFT' FORM AREA> ] -->
        <!-- ################################################################################## -->
        <div class="col-sm-8">
            <div class="panel panel-default">
                <div class="panel-heading"><spring:message code="${context}.name"/>${pageHeader }</div>
                <div class="panel-body">

                    <div class="row">
                        <div class="col-sm-9">
                            <div class="form-group">
                                <label class="control-label" ><spring:message code="${context}.name"/> <span class="required">*</span></label>
                                <form:input path="name" class="form-control" data-validation="required" />
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-3">
                            <div class="form-group">
                                <label class="control-label" ><spring:message code="${context}.type"/></label>
                                <select id="statusId" name="statusId" class="form-control" >
                                    <c:forEach items="${statusMap}" var="itemMap">
                                        <c:set var="selected" value="" />
                                        <c:if test="${ item.statusId eq itemMap.statusId }">
                                            <c:set var="selected" value="selected=\"selected\"" />
                                        </c:if>
                                        <option value="${itemMap.statusId}" ${selected}> ${itemMap.name }</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                    </div>


                </div>
            </div>

        </div>

        <!-- ################################################################################## -->
        <!-- [ </'LEFT' FORM AREA> ] -->
        <!-- ################################################################################## -->



        <!-- #################################################################################### -->
        <!-- [ <'RIGHT' FORM AREA> ] -->
        <!-- #################################################################################### -->
        <div class="col-sm-4">

            <!-- ################## -->
            <!-- [Usage detail]		-->
            <!-- ################## -->
            <jsp:include page="../templates/_status.jsp" />

        </div>
        <!-- #################################################################################### -->
        <!-- [ </'RIGHT' FORM AREA> ] -->
        <!-- #################################################################################### -->

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


