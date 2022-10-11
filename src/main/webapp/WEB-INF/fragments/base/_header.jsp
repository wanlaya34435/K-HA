<%@ include file="../../common/taglibs.jsp"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<%

// Subject currentUsers         = SecurityUtils.getSubject();
// Users user = (Users)currentUsers.getSession().getAttribute("userInfo");

%>


<style>
	@media (min-width: 768px){
		.sidebar {
		    margin-top: 100px;
		}
	}
	
	.navbar {
	    height: 100px;
	}
	a.navbar-brand.title {
	    margin-top: 45px;
	    font-size: 25px;
	}
</style>

<div class="navbar-header">
	<div class="row">
		<button type="button" class="navbar-toggle" data-toggle="collapse"
			data-target=".navbar-collapse">
			<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span>
			<span class="icon-bar"></span> <span class="icon-bar"></span>
		</button>
<!-- 		<div class="col-lg-1"> -->
<%-- 			<img alt="" src="${__BASEURL__}/assets/plugins/images/dld_icon.png" width="50px" height="50px"> --%>
<!-- 		</div> -->
		<div class="col-lg-12">
			<a class="navbar-brand" href="${__BASEURL__}/dashboard/viewform"><img class="img-responsive" src="${__BASEURL__}/assets/plugins/images/images-content/logo.png" alt="v-coruse"/></a>
			<a class="navbar-brand title hidden-xs" href="${__BASEURL__}/dashboard/viewform"><spring:message code="app.title"/></a>
		</div>
<!-- 		<div class="col-lg-12"> -->
<%-- 		 	<a class="navbar-brand" href="${__BASEURL__}/dashboard/viewform"><spring:message code="app.fullname"/></a> --%>
<!-- 		</div> -->
	</div>
</div>
<!-- /.navbar-header -->

<ul class="nav navbar-top-links navbar-right">
	
	<!-- /.dropdown -->
	<li class="dropdown" style="margin-top: 25px;">
		<a class="dropdown-toggle" id="user-profile" data-toggle="dropdown" href="#" style="color:#E37310;">
				<img class="img-responsive" src="${__BASEURL__}/assets/plugins/images/images-content/user_or.png" alt="v-coruse" style="display: inline-block;"/><em class="fa fa-caret-down"></em>
<!-- 				<span class="fa fa-caret-dow"></span> -->
<!-- 				<em class="fa fa-user fa-fw"></em> -->
<!-- 				<em class="fa fa-caret-down"></em> -->
		</a>
		<ul class="dropdown-menu dropdown-user">
			<li>
<%-- 				<a href="#"><em class="fa fa-user fa-fw"></em>${userInfo.firstname}&nbsp;${userInfo.lastname }</a> --%>
					<a href="${__BASEURL__}/user/updateform/userId/${userInfo.userId }?edit=y"><em class="fa fa-user fa-fw"></em>${userInfo.title} ${userInfo.firstname} ${userInfo.lastname }</a>
			</li>
<!-- 			<li> -->
<!-- 				<a href="#"><em class="fa fa-gear fa-fw"></em> Settings</a></li> -->
<!-- 			<li class="divider"></li> -->
			<li>
				<a href="${__BASEURL__}/authen/logout"><em class="fa fa-sign-out fa-fw"></em><spring:message code="form.logout"/></a>
			</li>
		</ul> <!-- /.dropdown-user --></li>
	<!-- /.dropdown -->
</ul>
<!-- /.navbar-top-links -->