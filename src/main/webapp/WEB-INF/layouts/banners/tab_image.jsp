<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- /.row -->
<div class="row">
    <!-- ################################################################################## -->
    <!-- [ <FORM AREA> ] -->
    <!-- ################################################################################## -->
    <div class="col-sm-12">

        <div class="panel panel-default">
            <div class="panel-heading"><spring:message code="${context}.image"/></div>
            <div class="panel-body">

                <div class="row">
                    <div class="col-sm-8">
                        <div class="form-group">
                            <label class="control-label" ><spring:message code="${context }.attach"/></label>
                            <input name="imgAttachfile" class="form-control uploadFileImages" type="file"/>
                            <span class="required">* <spring:message code="form.validate.img"/></span>

                            <input name="imgLocation" value="${itemImage.imgLocation }" type="hidden"/>
                            <input name="imgUrl" value="${itemImage.imgUrl }" type="hidden"/>
                            <c:if test="${not empty itemImage.imgUrl}">
                                <div>
                                    <em class="fa fa-file-archive-o" aria-hidden="true"></em>&nbsp;<a href="${url }${itemImage.imgUrl }" id="image_URL" target="_blank"><spring:message code="${context }.attach"/></a>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>


            </div>
        </div>

    </div>
</div>
