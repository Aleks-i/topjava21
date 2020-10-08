<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<p></p>
<a href="meals?action=create">Save</a>
<p></p>
<table border="1" cellspacing="0" cellpadding="2">
    <tr>
        <td>Дата</td>
        <td>Еда</td>
        <td>Калории</td>
        <td>Удалить</td>
        <td>Редактировать</td>
    </tr>
    <c:forEach var="mealTo" items="${mealsTo}">
        <jsp:useBean id="mealTo" class="ru.javawebinar.topjava.model.MealTo"/>
    <tr style="color: ${mealTo.excess == true ? 'red' : 'green'}">
        <td>
                <fmt:parseDate value="${mealTo.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }" />
        <td>${mealTo.description}</td>
        <td>${mealTo.calories}</td>
        <td style="color: #000000"><a href="meals?action=delete&id=${mealTo.id}">Delete</a></td>
        <td style="color: #000000"><a href="meals?action=update&id=${mealTo.id}">Update</a></td>
    </tr>
    </c:forEach>
</table>
</body>
</html>