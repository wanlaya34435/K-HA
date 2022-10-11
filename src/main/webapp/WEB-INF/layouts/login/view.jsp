<%@ include file="../../common/taglibs.jsp"%>


<form:form id="${appName }" name="${appName }" modelAttribute="item" method="POST" action="${__BASEURL__}/login/dologin" role="form" enctype="multipart/form-data" data-toggle="validator">
    <div class="container">
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel panel-default">
                    <div class="panel-heading">
                        <h3 class="panel-title"><spring:message code="login.title"/></h3>
                    </div>
                    <div class="panel-body">
                        <form role="form">
                            <fieldset>
                                <div class="form-group">
                                    <input class="form-control" placeholder="<spring:message code="login.email"/>" name="email" type="text" autofocus>
                                </div>
                                <div class="form-group">
                                    <input class="form-control" placeholder="<spring:message code="login.password"/>" name="password" type="password" value="">
                                </div>
                                <c:if test="${not empty respMsg}">
                                    <div class="alert alert-danger alert-dismissable">
                                        <!-- 		                                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> -->
                                            ${respMsg }
                                    </div>
                                </c:if>

                                <!--
	                            <div class="checkbox">
	                                <label>
	                                    <input name="remember" type="checkbox"><spring:message code="login.remember"/>
	                                </label>
	                            </div>
	                             -->
                                <!-- Change this to a button or input when using this as a form -->
                                <div>
                                    <button type="submit" class="btn btn-success btn-block" ><em class="fa fa-sign-in" aria-hidden="true"></em>&nbsp;<spring:message code="btn.login"/></button>
                                        <%-- 		                            <button type="button" class="btn btn-info btn-block" onclick="register()" ><em class="fa fa-registered" aria-hidden="true"></em>&nbsp;<spring:message code="btn.register"/></button> --%>
                                </div>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</form:form>

<script type="text/javascript" >
    function register(){
        window.location="/${appName }/authen/registerform";
    }
</script>