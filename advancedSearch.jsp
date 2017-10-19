<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Advanced Search Page</title>
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
      <div id="searchformdiv">
        <form action="${pageContext.request.contextPath}/servlet/AdvancedSearch" method="get">
          <h1 style="color:white">Advanced Search</h1><br>
  	      <label> Movie Title</label><br>
          <input type="text" id="searchformfields" name="movietitle" placeholder="Movie Title.."><br>
          <label>First Name</label><br>
          <input type="text" id="searchformfields" name="starfirst" placeholder="Star First Name.."><br>
          <label>Last Name</label><br>
	      <input type="text" id="searchformfields" name="starlast" placeholder="Star Last Name.."><br>
	      <label>Year Released</label><br>
	      <input type="text" id="searchformfields" name="year" placeholder="Year Released.."><br>
	      <label>Director</label><br>
	      <input type="text" id="searchformfields" name="director" placeholder="Director.."><br>
          <br>
          <input id="searchformsubmit" type="submit" value="Submit">
        </form>
      </div>
    </div>
  </div>
</body>
</html>