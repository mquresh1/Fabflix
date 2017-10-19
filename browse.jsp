<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Browse Page</title>
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
  <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css" />
  <script>
    $(document).ready(function(){
    	$(function(){
            $("#navbarsearchinput").autocomplete({
            	source: function(request, response) {
            		$.ajax({
            			url: "/Project2/servlet/AutoComplete",
            			dataType: "json",
            			data: {
            				term: request.term
            			},
            			success: function(data) {
            				response(data);
            			}
            		});
            	},
            	minLength: 2
            });
    	});
    });
  </script>
</head>
<body>
  <div id="main">
    <jsp:include page="navbar.jsp" />
    <div id="bottom">
      <div style="text-align: center; background-color: black; border-radius: 15px; padding: 10px;margin:auto auto 10px auto;">
        <h3 style="color: white">Browse by Title</h3>
        <c:set var="alphanum" value="0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z" />
        <form action="${pageContext.request.contextPath}/servlet/Browse" method="get">
	      <c:forTokens var="letter" items="${alphanum}" delims=",">
	        <button type="submit" name="title" value="${letter}">${letter}</button>
	      </c:forTokens>
	    </form>
      </div>
      <div style="text-align: center;background-color: black; border-radius: 15px; padding:10px;margin:50px auto auto auto;">
        <h3 style="color: white">Browse by Genre</h3>
        <form action="${pageContext.request.contextPath}/servlet/Browse" method="get">
          <c:forEach var="genre" items="${genres}">
            <button type="submit" name="genre" value="${genre.key}">${genre.value}</button>
          </c:forEach>
        </form>
      </div>
    </div>
  </div>
</body>
</html>