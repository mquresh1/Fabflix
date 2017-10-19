<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Add Movie Page</title>
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
  <script>
    function validate() {
    	var field = 'invalid';
    	var url = window.location.href;
    	if(url.indexOf('?' + field + '=') != -1) {
    		document.getElementById('errormsg').innerHTML="Incorrect field information, try again.";
    	}
    }
  </script>
</head>
<body onload="validate()">
  <div id="main">
  	<div id="navbar">
    <form>
      <ul id="navbarlist">
        <li id="navbarli" name="home"><a href="${pageContext.request.contextPath}/dashboardMenu.jsp" id="navbarlink">Home</a></li>
        <li style="float:right" name="logout"><a href="${pageContext.request.contextPath}/servlet/DashboardLogin" id="navbarlink">Logout</a></li>
      </ul>

    </form>
  </div>
  
    <div id="bottom">
      <div id="searchformdiv">
        <form action="/Project2/servlet/Dash" method="get">
          <h1 style="color:white">Add Movie</h1>
          <br>
          <label> Movie Title</label>
          <br>
          <input type="text" id="searchformfields" name="movietitle" placeholder="Movie Title..">
          <br>
          <label>Star Full Name</label>
          <br>
          <input type="text" id="searchformfields" name="star" placeholder="Star Full Name..">
          <br>
          <label>Genre</label>
          <br>
          <input type="text" id="searchformfields" name="genre" placeholder="Genre..">
          <br>
          <label>Year Released</label>
          <br>
          <input type="text" id="searchformfields" name="year" placeholder="Year Released..">
          <br>
          <label>Director</label>
          <br>
          <input type="text" id="searchformfields" name="director" placeholder="Director..">
          <br>
          <br>
          <input id="searchformsubmit" type="submit" name="action" value="Add Movie">
        </form>
        <br><br><br>
        <div id="errormsg" style="color:red; text-align:center;font-weight:bold;">  </div>
      </div>
    </div>
  </div>
</body>

</html>