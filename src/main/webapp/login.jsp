<%--
  Created by IntelliJ IDEA.
  User: Георгий Малахов
  Date: 09.02.2022
  Time: 0:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Stock Keeper</title>

    <script
            src="https://code.jquery.com/jquery-3.4.1.min.js"
            integrity="sha256-CSXorXvZcTkaix6Yvo6HppcZGetbYMGWSFlBw8HfCJo="
            crossorigin="anonymous"></script>
    <script type="text/javascript"
            src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.0/dist/jquery.validate.min.js"></script>
</head>
<body>
<div style="text-align: center">
    <h1>Admin Login</h1>
    <form action="/StockKeeper_war_exploded/stock" method="post">
        <label for="name">Name:</label>
        <input name="name" size="30" minlength="1"/>
        <br><br>
        <label for="password">Password:</label>
        <input type="password" name="password" size="30" minlength="1"/>
        <br>${message}
        <br><br>
        <button type="submit">Login</button>
    </form>
</div>
<script type="text/javascript">

    $(document).ready(function() {
        $("#loginForm").validate({
            rules: {
                name: {
                    required: true,
                    name: true
                },

                password: "required",
            },

            messages: {
                name: {
                    required: "Please enter email",
                    name: "Please enter a valid email address"
                },

                password: "Please enter password"
            }
        });

    });
</script>
</body>
</html>
