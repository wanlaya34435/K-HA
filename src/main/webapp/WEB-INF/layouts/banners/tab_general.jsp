<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!-- /.row -->
<div class="row">
    <!-- ################################################################################## -->
    <!-- [ <'LEFT' FORM AREA> ] -->
    <!-- ################################################################################## -->
    <div class="col-sm-8">
        <div class="panel panel-default">
            <div class="panel-heading"><spring:message code="tabs.general"/>${pageHeader }</div>
            <div class="panel-body">

                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label" ><spring:message code="${context}.type"/></label>
                            <select id="bannerType" name="bannerType" class="form-control" >
                                <c:forEach items="${bannerTypeMap}" var="itemMap">
                                    <c:set var="selected" value="" />
                                    <c:if test="${ item.bannerType eq itemMap.id }">
                                        <c:set var="selected" value="selected=\"selected\"" />
                                    </c:if>
                                    <option value="${itemMap.id}" ${selected}> ${itemMap.name }</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-8">
                        <div class="form-group">
                            <label class="control-label" ><spring:message code="${context}.link"/> <span class="required">*</span></label>
                            <form:input path="link" class="form-control" data-validation="required"/>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label" ><spring:message code="${context}.startdate"/> <span class="required">*</span></label>
                            <form:input path="startDate" class="form-control" data-validation="required" />
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="form-group">
                            <label class="control-label" ><spring:message code="${context}.stopdate"/> <span class="required">*</span></label>
                            <form:input path="stopDate" class="form-control" data-validation="required" />
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