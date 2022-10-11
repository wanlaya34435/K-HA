<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<div class="navbar-default sidebar" role="navigation">
	<div class="sidebar-nav navbar-collapse">
		<ul class="nav" id="side-menu">
            <li>
				<a href="/${appName }/dashboard/viewform" class="menu">
 					<em class="fa fa-tachometer" ></em>&nbsp;<spring:message code="dashboard.title"/>
				</a>
			</li>
			
			<li>
				<a href="#" class="menu">
 					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.content"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.content.manage"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/promotes">&nbsp;<spring:message code="menu.promote"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.banner"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/hashtags">&nbsp;<spring:message code="menu.hashtag"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.cvs"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.cvs.group"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/promotes">&nbsp;<spring:message code="menu.cvs.wait"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.cvs.history"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/hashtags">&nbsp;<spring:message code="menu.cvs.setting"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.user"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.user.manage"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/promotes">&nbsp;<spring:message code="menu.user.group"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.user.permission"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/hashtags">&nbsp;<spring:message code="menu.user.menu"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.feedback"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.feedback.review"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/promotes">&nbsp;<spring:message code="menu.feedback.inappropriate"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.feedback.recommend"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.category"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.category.dewey"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/promotes">&nbsp;<spring:message code="menu.category.substance"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.category.stdknowledge"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.category.indicator"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.category.subject"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/banners">&nbsp;<spring:message code="menu.category.class"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.other"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.other.author"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.other.publisher"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.other.attribute"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.other.illegal"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.other.menu"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.report"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.report.banner"/></a></li>
				</ul>
			</li>

			<li>
				<a href="#" class="menu">
					<em class="fa fa-cogs" ></em>&nbsp;<spring:message code="menu.profile"/>&nbsp;<span class="fa arrow"></span>
				</a>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.profile.download"/></a></li>
				</ul>
				<ul class="nav nav-third-level">
					<li><a href="/${appName }/contents">&nbsp;<spring:message code="menu.profile.upload"/></a></li>
				</ul>
			</li>

		</ul>
		
	</div>
	<!-- /.sidebar-collapse -->
</div>
<!-- /.navbar-static-side -->

