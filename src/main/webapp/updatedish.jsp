<%--
  Created by IntelliJ IDEA.
  User: Andreea-PC
  Date: 3/20/2017
  Time: 10:04 PM
  To change this template use File | Settings | File Templates.
  onload="document.getElementById('meals').submit()"
  <c:forEach items="${mealEntriesSelect}" var="meal">
            <option value="${mealEntry.getId()}">${mealEntry.getName()}</option>
        </c:forEach>
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <title>Title</title>
</head>
<body>
<div>
    <h2>Update Dish</h2>
</div>
<div>
    <form action="dishModify" method="post">
        <input type="hidden" name="id" value="${dishId}"/>
        Name:<br />
        <input type="text" name="name" placeholder="Name of the dish." value="${dishName}"/><br />
        Date:<br />
        <input type="text" name="date" placeholder="(e.g 2017-02-19)" value="${dishDate}"/><br />
        Meal:<br />
        <select name="mealid" value="${dishMealId}">
            <c:forEach items="${meals}" var="meal">
                <option value="${meal.getId()}" ${dishMealId == meal.getId()  ? "selected" : ""}>${meal.getName()}</option>
            </c:forEach>
        </select><br />

        <input type="submit" value="Submit"/>&nbsp;
    </form>
</div>
</body>
</html>
