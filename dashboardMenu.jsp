<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Dashboard Menu</title>
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
  <div id="main" >
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
     <h1 style="color:white">Dashboard Menu</h1>
      <a href="${pageContext.request.contextPath}/addStar.jsp">
        <button>
          Add Star
        </button>
        </a>
        <a href="${pageContext.request.contextPath}/servlet/Dash?action=metadata">
        <button>
          Metadata
        </button>
        </a>
        <a href="${pageContext.request.contextPath}/addMovie.jsp">
        <button>
          Add Movie
        </button>
        </a>
    </div>
      
    </div>
  </div>
</body>
</html>