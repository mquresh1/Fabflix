<div id="navbar">
  <form>
  <ul id="navbarlist">
    <li id="navbarli" name="home"><a href="${pageContext.request.contextPath}/servlet/Result?home=true" id="navbarlink">FabFlix</a></li>
    <li id="navbarli" name="browse"><a href="${pageContext.request.contextPath}/servlet/Browse" id="navbarlink">Browse</a></li>
    <li style="float:right" name="logout"><a href="${pageContext.request.contextPath}/servlet/Login" id="navbarlink">Logout</a></li>
    <c:catch var="exception">${cart.totalCount}</c:catch>
    <li style="float:right" name="checkout"><a href="${pageContext.request.contextPath}/servlet/Checkout?checkout=true" id="navbarlink">Cart (${cart.totalCount})</a></li>
	<li style="float:right" name="advancedsearch"><a href="${pageContext.request.contextPath}/advancedSearch.jsp" id="navbarlink">Advanced Search</a></li>
    <li style="float:right">
      <form action="/Project2/servlet/Result" method="get">
        <input id="navbarsearchinput" type="text" name="search" list="title-datalist" placeholder="Search..">


        <select id="navbarselect" name="searchoption">
          <option value="title">Title</option>
          <option value="director">Director</option>
          <option value="year">Year</option>
          <option value="star">Star Name</option>
        </select>
        
        <input id="navbarsubmit" type="submit" name="searchsubmit" value="Search">
      </form>        
    </li>
  </ul>

  </form>
</div>