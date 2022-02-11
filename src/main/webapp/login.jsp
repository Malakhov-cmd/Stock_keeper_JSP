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
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"
          crossorigin="anonymous">

    <link href="static/style/loginStyle.css" rel="stylesheet">
</head>
<body id="loginBodyId">
<div id="loginMainContentId">

    <div class="card">
        <form action="/StockKeeper_war_exploded/stock" method="post" class="form-registration">
            <div class="mb-3 row">
                <label for="inputName" class="col-sm-2 col-form-label">Name</label>
                <div class="col-sm-10">
                    <input class="form-control" id="inputName" name="name">
                </div>
            </div>
            <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-2 col-form-label">Password</label>
                <div class="col-sm-10">
                    <input type="password" class="form-control" id="inputPassword" name="password">
                </div>
            </div>
            <small class="text-muted">If you haven't registered yet, we will immediately create an account for
                you</small>
            <br>${message}
            <br><br>
            <button type="submit" class="btn btn-primary">Login/Registration</button>
        </form>
    </div>
</div>
<script type="text/javascript">

    $(document).ready(function () {
        $("#loginForm").validate({
            name: {
                required: true,
                minLength: 1
            },
            password: {
                required: true,
                minLength: 1
            },
            messages: {
                name: {
                    required: "Please enter name",
                    minLength: "Please enter a valid name"
                },
                password: {
                    required: "Please enter name",
                    minLength: "Please enter a valid name"
                }
            }
        })
    });
</script>
<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"
        integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13"
        crossorigin="anonymous"></script>
</body>
</html>
