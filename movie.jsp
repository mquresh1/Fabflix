<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Movie Page</title>
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
      <h3>Movie</h3>
      <table>
        <tr>
          <td id="movie_img">
            <img src="${movie.bannerURL}">
            <form action="${pageContext.request.contextPath}/servlet/AddCart" method="get">
          	  <input type="hidden" name="mp" value="true">
          	  <button type="submit" name="addToCart" value="${movie.ID}">Add to Cart</button>
          	</form>
          </td>
          <td>
            <c:out value="ID: ${movie.ID}" /><br>
            <c:out value="Title: ${movie.title}" />
            <c:out value="Year: ${movie.year}" /><br>
            <c:out value="Director: ${movie.director}" /><br>
            <c:out value="Genres: " />
            <c:forEach var="genre" items="${movie.genres}" varStatus="loop">
              ${genre}
              <c:if test="${!loop.last}">, </c:if>
            </c:forEach>
            <br>
            <c:out value="Stars: " />
            <c:forEach var="star" items="${movie.stars}" varStatus="loop">
              <a href="${pageContext.request.contextPath}/servlet/StarPage?star=${star.key}">${star.value}</a>
              <c:if test="${!loop.last}">, </c:if>
            </c:forEach>
            <br>
            <a href="${movie.trailerURL}">Watch Trailer</a>
          </td>
        </tr>
      </table>
    </div>
  </div>
</body>
</html>