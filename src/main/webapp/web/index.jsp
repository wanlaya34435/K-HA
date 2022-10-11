<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>JSP - Context Path</title>
</head>

<body>
Request Context Path: <%= request.getContextPath() %><br>
Request URI:          <%= request.getRequestURI() %><br>
Request URL:          <%= request.getRequestURL().toString() %><br>

<br>

<%
    String requestURL = request.getRequestURL().toString();

    if(requestURL.indexOf("amu") > -1){
        response.sendRedirect("/amu");
    }
    else if(requestURL.indexOf("amr") > -1){
        response.sendRedirect("/app");
    }
    else{
        response.sendRedirect("/app");
    }
%>


</body>
</html>