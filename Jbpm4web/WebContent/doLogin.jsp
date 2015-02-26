<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
String userName=request.getParameter("userName");
if(null!=userName){
	request.getSession().setAttribute("user", userName);
	response.sendRedirect(request.getContextPath()+"/index.jsp");
}
%>
