<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<!-- ################## -->
<!-- [Usage detail]		-->
<!-- ################## -->
<div class="panel panel-default">
    <div class="panel-heading"><spring:message code="form.usage.title"/></div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label"><spring:message code="${context}.status"/></label>
                    <select id="statusId" name="statusId" class="form-control" >
                        <c:forEach items="${statusMap}" var="status">
                            <c:set var="selected" value="" />
                            <c:if test="${ item.statusId eq status.statusId }">
                                <c:set var="selected" value="selected=\"selected\"" />
                            </c:if>
                            <option value="${status.statusId}" ${selected}> ${status.name }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label"><spring:message code="form.usage.create"/></label>
                    <p><em class="fa fa-clock-o"></em>&nbsp;${item.createDate}</p>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label"><spring:message code="form.usage.update"/></label>
                    <p><em class="fa fa-clock-o"></em>&nbsp;${item.updateDate}</p>
                </div>
            </div>
        </div>
    </div>
</div>