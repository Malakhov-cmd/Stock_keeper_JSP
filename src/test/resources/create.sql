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
    id       SERIAL8   not null,
    cost     float8 not null,
    date     timestamp,
    stock_id int8,
    primary key (id)
);
create table stock_purpose
(
    id       SERIAL8   not null,
    cost     float8 not null,
    date     timestamp,
    stock_id int8,
    primary key (id)
);
create table usr
(
    id       SERIAL8 not null,
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
