<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
    <title>Home Page</title>
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
      
      function onMouseOver(movieID) {
    	  var popup = $("#popupdiv" + movieID);
    	  $.ajax({
    		  url: "/Project2/servlet/PopUp",
    		  dataType: "text",
    		  data: {
    			  id: movieID
    		  },
    		  success: function(data) {
    			  // add HTML information to popup
    			  popup.html(data);
    	    	  popup.css("display", "inline");
    		  }
    	  });
      }
      function onMouseOut(movieID) {
  		var popup = document.getElementById("popupdiv" + movieID);
  		popup.style.display = "none";
  	  }
    </script>
  </head>
<body>
  <div id="main">
    <jsp:include page="navbar.jsp" />
    <div id="bottom">
      <form style="text-align:center;" action="${pageContext.request.contextPath}/servlet/Result" method="post">
        <label>Sort by Title: </label>
        <input type="submit" name="orderTitle" value="ASC">
        <input type="submit" name="orderTitle" value="DESC">
        <label>Sort by Year: </label>
        <input type="submit" name="orderYear" value="ASC">
        <input type="submit" name="orderYear" value="DESC">
      </form>
      <table>
        <c:forEach var="movie" items="${movies}">
          <tr>
          	<td id="movie_img">
          	  <img src="${movie.bannerURL}"><br>
          	  <form action="${pageContext.request.contextPath}/servlet/AddCart" method="get">
          	    <input type="hidden" name="rowCount" value="${rowCount}">
          	    <button type="submit" name="addToCart" value="${movie.ID}">Add to Cart</button>
          	  </form>
          	</td>
            <td>
              <c:out value="ID: ${movie.ID}" /><br>
              <c:out value="Title: " />
              <a href="${pageContext.request.contextPath}/servlet/MoviePage?movie=${movie.ID}" id="${movie.ID}" onmouseover="onMouseOver(${movie.ID})" onmouseout="onMouseOut(${movie.ID})">${movie.title} 
              	<div class="popupdiv" id="popupdiv${movie.ID}"></div>
              	</a><br>
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
        </c:forEach>
      </table>
      <form style="text-align:center;" action="${pageContext.request.contextPath}/servlet/Result" method="post">
        <input type="hidden" name="rowCount" value="${rowCount}">
        <input type="submit" name="previous" value="previous">
        <input type="submit" name="next" value="next">
      </form>
    </div>
  </div>
</body>
</html>