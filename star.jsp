<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Star Page</title>
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
      <h3>Star</h3>
      <table>
        <tr>
          <td id="movie_img"><img src="${star.photoURL}"></td>
          <td>
            <c:out value="ID: ${star.ID}" /><br>
            <c:out value="Name: ${star.firstName} ${star.lastName}" /><br>
            <c:out value="Date of birth: ${star.DOB}" /><br>
            <c:out value="Movies: " />
            <c:forEach var="movie" items="${star.movies}" varStatus="loop">
              <a href="${pageContext.request.contextPath}/servlet/MoviePage?movie=${movie.key}">${movie.value}</a>
              <c:if test="${!loop.last}">, </c:if>
            </c:forEach>
          </td>
        </tr>
      </table>
    </div>
  </div>
</body>
</html>