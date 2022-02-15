<%@ page import="com.stock.keeper.stockkeeper.service.LoginService" %>
<%@ page import="com.stock.keeper.stockkeeper.service.StockDataAPIService" %>
<%@ page import="com.stock.keeper.stockkeeper.domain.Stock" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.*, java.text.*" %>

<%@ page import="com.google.gson.Gson" %>
<%@ page import="com.google.gson.JsonObject" %>

<%@ page import="java.util.List, java.text.*" %>
<%@ page import="com.stock.keeper.stockkeeper.repo.DataRepo" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.stream.Stream" %>
<%@ page import="com.stock.keeper.stockkeeper.domain.Purpose" %>
<%@ page import="java.util.stream.Collectors" %>
<%--
  Created by IntelliJ IDEA.
  User: Георгий Малахов
  Date: 09.02.2022
  Time: 0:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Stock currentStock = new Stock();

    Object sessionAtr = session.getAttribute("currentStock");

    Long userId = (Long) session.getAttribute("userId");

    DataRepo dataRepo = new DataRepo();
    List<Stock> stocks = dataRepo.selectStocksByUsrId(userId);

    if (sessionAtr != null) {
        currentStock = (Stock) sessionAtr;
    } else {
        if (stocks.size() != 0) {
            currentStock = stocks.get(0);
        }
    }
%>

<%
    Gson gsonObj = new Gson();
    List<Map<Object, Object>> list = new ArrayList<>();
    if (currentStock.getId() != null) {
        currentStock.getPriceList().stream().forEach(item -> {
            Map<Object, Object> map = new HashMap<>();
            map.put("label", item.getDate());
            map.put("y", item.getCost());
            list.add(map);
        });
    }

    String dataPoints = gsonObj.toJson(list);
%>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="generator" content="Hugo 0.88.1">
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
    <link rel="stylesheet" href="https://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>

    <!-- Custom styles for this template -->
    <link href="static/style/mainStyle.css" rel="stylesheet">


    <script type="text/javascript">
        window.onload = function () {

            var chart = new CanvasJS.Chart("chartContainer", {
                theme: "light2",
                title: {
                    text: "Prices of <%
                    if (currentStock.getId() != null){
                        out.print(currentStock.getName());
                    } else {
                        out.print("null. There is no stock picked");
                    }
                    %>"
                },
                axisX: {
                    title: "Dates"
                },
                axisY: {
                    title: "Price in dollars",
                    includeZero: true
                },
                data: [{
                    type: "line",
                    yValueFormatString: "#,##0$",
                    dataPoints: <%out.print(dataPoints);%>
                }]
            });
            chart.render();

        }
    </script>

</head>
<body>

<header class="navbar navbar-dark sticky-top bg-dark flex-md-nowrap p-0 shadow">
    <p class="navbar-brand col-md-3 col-lg-2 me-0 px-3">Stock Keeper</p>

    <div class="navbar-nav">
        <div class="nav-item text-nowrap">
            <a class="nav-link px-3" href="/StockKeeper_war_exploded">Sign out</a>
        </div>
    </div>
</header>

<div class="container-fluid">
    <div class="row">
        <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block bg-light sidebar collapse">
            <div class="position-sticky pt-3">
                <ul class="nav flex-column">
                    <li class="nav-item add-stock-li">
                        <p>
                            <button class="btn" type="button" data-bs-toggle="collapse"
                                    data-bs-target="#collapseAddStockByIndex" aria-expanded="false"
                                    aria-controls="collapseAddStockByIndex">
                                Add new stock
                            </button>
                        </p>
                        <div class="collapse collapse-horizontal" id="collapseAddStockByIndex">
                            <form action="/StockKeeper_war_exploded/stock" method="post" id="inputNewStockId">
                                <div class="form-floating mb-3">
                                    <input class="form-control" type="hidden" value="${user.id}" name="userId">
                                    <input class="form-control" id="floatingIndex" required="required"
                                           minlength="1" name="index">
                                    <label for="floatingIndex">Company Ticker</label>
                                    <button type="submit" class="btn btn-primary stock-add-btn">Add</button>
                                </div>
                            </form>
                        </div>
                    </li>
                    <h4 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
                        <span>Stock list</span>
                    </h4>
                    <%
                        if (currentStock.getId() != null) {
                            for (Stock item :
                                    stocks) {
                                out.println(
                                        "<li class=\"nav-item\">\n" +
                                                "<div class=\"stock-card\">\n" +
                                                "<img src=\"" + item.getImg_link() + "\" class=\"stock-card-img\"/>\n" +
                                                "<div class=\"stock-card-info\">\n" +
                                                "<div class=\"stock-card-info-ticker\">\n" + item.getIndex() + "</div>\n" +
                                                "<div class=\"stock-card-info-name\"><p class=\"stock-card-text-info\">" + item.getName() + "<p></div>\n" +
                                                "</div>\n" +
                                                "<div class=\"stock-card-forms\">" +
                                                "<form action=\"/StockKeeper_war_exploded/stock\" method=\"post\" class=\"stock-card-first-form\">\n" +
                                                "<div class=\"card-show-info-btn\">\n" +
                                                "<input class=\"form-control\" type=\"hidden\" value=\"" + item.getId() + "\" name=\"newCurrentStockId\">\n" +
                                                "<button type=\"submit\" class=\"btn btn-primary\">Show</button>\n" +
                                                "</div>\n" +
                                                "</form>\n" +
                                                "<form action=\"/StockKeeper_war_exploded/stock\" method=\"post\">\n" +
                                                "<div class=\"card-show-info-btn\">\n" +
                                                "<input class=\"form-control\" type=\"hidden\" value=\"" + item.getId() + "\" name=\"deleteStockId\">\n" +
                                                "<input class=\"form-control\" type=\"hidden\" value=\"" + item.getOwner().getId() + "\" name=\"deleteStockOwnerId\">\n" +
                                                "<button type=\"submit\" class=\"btn btn-primary\">Delete</button>\n" +
                                                "</div>\n" +
                                                "</form>\n" +
                                                "</div>\n" +
                                                "</div>\n" +
                                                "</li>\n");
                            }
                        }
                    %>
                </ul>
            </div>
        </nav>

        <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                <h1 class="h2">Stock cost graphic</h1>
            </div>

            <div class="stock-graphic-main">
                <div id="chartContainer" style="height: 670px; width: 100%;"></div>

                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom control-panel-label">
                    <h1 class="h2">Control panel</h1>
                </div>

                <div class="stock-controls-forms">
                    <form action="/StockKeeper_war_exploded/stock" method="post" class="form-stock-refresh">
                        <div class="form-floating mb-3">
                            <input class="form-control" type="hidden" value="<%out.print(currentStock.getId());%>"
                                   name="stockRefreshId">
                        </div>
                        <button type="submit" class="btn btn-primary"
                                data-bs-toggle="tooltip" data-bs-placement="top" title="Get fresh price info">Refresh
                        </button>
                    </form>

                    <form action="/StockKeeper_war_exploded/stock" method="post" class="form-stock-add-purpose">
                        <input class="form-control" type="hidden" value="${user.id}" name="userPurposeId">
                        <input class="form-control" type="hidden" value="<%out.print(currentStock.getId());%>"
                               name="stockPurposeId">

                        <div class="form-floating mb-3 form-add-purpose-componet">
                            <input class="form-control" id="floatingAddpurpose" placeholder="Enter purpose cost"
                                   name="purposeCost">
                            <label for="floatingAddpurpose">Enter purpose price</label>
                        </div>

                        <div class="form-floating mb-3 form-add-purpose-componet">
                            <input class="form-control" name="purposeDate" id="datepicker">
                            <label for="datepicker">Enter date to purpose</label>
                        </div>

                        <button type="submit" class="btn btn-primary"
                                data-bs-toggle="tooltip" data-bs-placement="top" title="Add purpose">Add</button>

                    </form>
                </div>
            </div>


            <h2>Purposes</h2>
            <div class="table-responsive">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <th scope="col">#</th>
                        <th scope="col">Wanted price</th>
                        <th scope="col">Date</th>
                        <th scope="col">Date purpose</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        if (currentStock.getId() != null)
                            currentStock = dataRepo.selectStockById(currentStock.getId());

                        currentStock.setPurposeList(currentStock
                                .getPurposeList()
                                .stream()
                                .sorted(Comparator.comparingLong(Purpose::getId).reversed())
                                .collect(Collectors.toList())
                        );

                            for (int i = 0; i < currentStock.getPurposeList().size(); i++) {
                                out.println("<tr>\n" +
                                        "                        <td>" + i + "</td>\n" +
                                        "                        <td>" + currentStock.getPurposeList().get(i).getCost() + "</td>\n" +
                                        "                        <td>" + currentStock.getPurposeList().get(i).getPurposeDate().toString() + "</td>\n" +
                                        "                        <td>" + currentStock.getPurposeList().get(i).getDate().toString() + "</td>\n" +
                                        "                    </tr>");
                            }
                    %>
                    </tbody>
                </table>
            </div>
        </main>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.10.2/dist/umd/popper.min.js"
        integrity="sha384-7+zCNj/IqJ95wo16oMtfsKbZ9ccEh31eOz1HGyDuCQ6wgnyJNSYdrPa03rtR1zdB"
        crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js"
        integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13"
        crossorigin="anonymous"></script>

<script src="https://canvasjs.com/assets/script/canvasjs.min.js"></script>

<script>
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl)
    })

    $(function () {
        $("#datepicker").datepicker();
    });
</script>
</body>
</html>