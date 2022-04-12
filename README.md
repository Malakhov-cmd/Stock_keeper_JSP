# Stock Keeper

___
**Main info**

Automated information system for keeping records of securities (stock) based on JavaEE technologies.

To obtain information about current stock prices, use the service https://polygon.io/

**Technical task**

> The purpose of the work is to develop an automated information system for keeping records of securities based on JavaEE technologies.

**System Capabilities**

The system provides the ability to generate its own list of securities and add to it any share that is in free trade on the securities exchange, generate a graph of changes in its price for the last year, receive the current price for the last day, keep records of the estimated share price by a certain date . All work with the system is performed through the Web interface, presented in the form of JSP pages.

The system is implemented by IntelliJ IDEA development and debugging tools in the Java programming language. The system has been debugged and tested for operability.


**Documentation**

Logical data model of the system.

![Logical data model](https://i.ibb.co/D7qR9YZ/logic.jpg)

Physical data model.

![Physical data model](https://i.ibb.co/9nzYGHr/logicalscheme.jpg)

Three-tier architecture components diagram.

![architecture components](https://i.ibb.co/1G3TY4P/threestart.jpg)

Use case diagram.

![Use case diagram](https://i.ibb.co/Jtrx8h3/usercase.jpg)

Class diagram.

![Class diagram](https://i.ibb.co/rxBTvzS/diahramclass.jpg)

**Implementation**

When you launch the application, the start page opens. On the initial page, the user can proceed to the registration or authorization stage

![img of type login](https://i.ibb.co/wdjj8zw/welcomepg.jpg)

![img of google login example](https://i.ibb.co/pr1tVT2/logform.jpg)

Clicking on the "Login/Registration" link opens the main page of the application. On the main page of the application, you can add a new promotion. To do this, click on the "Add new stock" button and enter the index of the stock we are interested in. After completing the data entry, click on the "Add" button. The system will automatically check whether the field is filled in correctly, download the necessary information about the dynamics of stock prices over the past year, add the corresponding record to the database and display a chart of price changes.

![img of filling card info](https://i.ibb.co/WW6CM42/emptymainpg.jpg)

![img of created card](https://i.ibb.co/Smn8FPC/appletest.jpg)

On the main page, you can delete the added promotion. To do this, click on the "Delete" button in the "STOCK LIST" section.

Also on the main page, you can add an assumption about the change in the stock price by a certain date.

![img of test how make operation](https://i.ibb.co/YWCfw5j/dateenter.jpg)

![img of test graphic of operations](https://i.ibb.co/qsVPGB0/recordofprediction.jpg)


**Sql script**

```postgresql
create sequence hibernate_sequence start 1 increment 1;
create table stock
(
    id          SERIAL8 not null,
    date_init   timestamp(255),
    description varchar(4096),
    img_link    varchar(512),
    index       varchar(128),
    name        varchar(255),
    usr_id      int8,
    primary key (id)
);
create table stock_price
(
    id       SERIAL8 not null,
    cost     float8  not null,
    date     timestamp,
    stock_id int8,
    primary key (id)
);
create table stock_purpose
(
    id           SERIAL8   not null,
    cost         float8    not null,
    date         timestamp,
    purpose_date timestamp not null,
    stock_id     int8,
    primary key (id)
);
create table usr
(
    id       SERIAL8      not null,
    password varchar(255) not null,
    usr_name varchar(255) not null,
    primary key (id)
);
alter table stock
    add constraint FK_Stock_To_Usr foreign key (usr_id) references usr;
alter table stock_price
    add constraint FK_Stock_Price_To_Stock foreign key (stock_id) references stock;
alter table stock_purpose
    add constraint FK_Stock_Purpose_To_Stock foreign key (stock_id) references stock;


```

**Application properties**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">

    <servlet>
        <servlet-name>login</servlet-name>
        <servlet-class>com.stock.keeper.stockkeeper.servlet.UserLoginServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>login</servlet-name>
        <url-pattern>/login</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>logout</servlet-name>
        <servlet-class>com.stock.keeper.stockkeeper.servlet.UserLogoutServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>logout</servlet-name>
        <url-pattern>/logout</url-pattern>
    </servlet-mapping>

    <servlet>
        <servlet-name>main</servlet-name>
        <servlet-class>com.stock.keeper.stockkeeper.servlet.MainServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>main</servlet-name>
        <url-pattern>/stock</url-pattern>
    </servlet-mapping>
</web-app>
```