<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="ru">
<head>
    <title>MealForm</title>
</head>
<body>
<section>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${param.action == 'create' ? 'Create meal' : 'Update meal'}</h2>
    <jsp:useBean id="meal" class="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form method="post" action="meals">
        <input type="hidden" name="id" value="${meal.id}">
        <dl>
            <dt>DateTime:</dt>
            <dd><input type="datetime-local" name="dateTime" value="${meal.dateTime}" required></dd>
        </dl>
        <dl>
            <dt>Description:</dt>
            <dd><input type="text" name="description" value="${meal.description}" size=40 required></dd>
        </dl>
        <dl>
            <dt>Calories:</dt>
            <dd><input type="number" name="calories" value="${meal.calories}" required></dd>
        </dl>
        <button type="submit">Save</button>
        <button onclick="window.history.back()" type="button">Cancel</button>
    </form>
</section>
</body>
</html>