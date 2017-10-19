<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
  <link href="${pageContext.request.contextPath}/homeStyle.css" rel="stylesheet" type="text/css">
  <title>Checkout</title>
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
      <div id="creditcarddiv">
        <form action="${pageContext.request.contextPath}/servlet/Checkout" method="post">
          <h1 style="color:white">Credit Card Info</h1>
          <br>
  	      <label> First Name</label>
          <br>
          <input type="text" id="creditcardfields" name="ccfirstname" placeholder="First Name..">
          <label> Last Name</label>
          <br>
          <input type="text" id="creditcardfields" name="cclastname" placeholder="Last Name..">
          <label> Credit Card Number</label>
          <br>
          <input type="text" id="creditcardfields" name="ccnumber" placeholder="#### - #### - #### - ####..">
          <label> Credit Card Expiration</label>
          <br>
          <input type="text" id="creditcardfields" name="ccexpiration" placeholder="YY/MM/DD..">
          <br><br>
          <button id="creditcardsubmit" type="submit" name="order" value="order">Submit</button>
        </form>
      </div>
      <div id="cartdisplaydiv">
        <table id="checkouttable">
          <c:forEach var="item" items="${items}">
            <tr>
              <td id="movie_img"><img src="${item.key.bannerURL}"><br>
                <form action="${pageContext.request.contextPath}/servlet/AddCart" method="get">
          	      <button type="submit" name="removeFromCart" value="${item.key.ID}">Remove from Cart</button>
          	    </form>
          	  </td>
          	  <td>
          	    <c:out value="ID: ${item.key.ID}" /><br>
                <c:out value="Title: ${item.key.title}" />
                <c:out value="Year: ${item.key.year}" /><br>
                <c:out value="Director: ${item.key.director}" /><br>
                <c:out value="Quantity: ${item.value}" />
          	  </td>
            </tr>
          </c:forEach>
        </table>
      </div>
    </div>
  </div>
</body>
</html>