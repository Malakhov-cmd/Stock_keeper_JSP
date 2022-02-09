package com.stock.keeper.stockkeeper.scripts;

import lombok.Getter;

public enum CreateDeleteDBscripts {
    createStatement("create table stock\n" +
            "(\n" +
            "    id          varchar(255) not null,\n" +
            "    date_init   varchar(255),\n" +
            "    description varchar(255),\n" +
            "    img_link    varchar(255),\n" +
            "    index       varchar(255),\n" +
            "    name        varchar(255),\n" +
            "    usr_id      varchar(255),\n" +
            "    primary key (id)\n" +
            ");\n" +
            "create table stock_price\n" +
            "(\n" +
            "    id       varchar(255)   not null,\n" +
            "    cost     float8 not null,\n" +
            "    date     timestamp,\n" +
            "    stock_id varchar(255),\n" +
            "    primary key (id)\n" +
            ");\n" +
            "create table stock_purpose\n" +
            "(\n" +
            "    id       varchar(255)   not null,\n" +
            "    cost     float8 not null,\n" +
            "    date     timestamp,\n" +
            "    stock_id varchar(255),\n" +
            "    primary key (id)\n" +
            ");\n" +
            "create table usr\n" +
            "(\n" +
            "    id       varchar(255) not null,\n" +
            "    password varchar(255),\n" +
            "    usr_name varchar(255),\n" +
            "    primary key (id)\n" +
            ");\n" +
            "alter table stock\n" +
            "    add constraint FK_Stock_To_Usr foreign key (usr_id) references usr;\n" +
            "alter table stock_price\n" +
            "    add constraint FK_Stock_Price_To_Stock foreign key (stock_id) references stock;\n" +
            "alter table stock_purpose\n" +
            "    add constraint FK_Stock_Purpose_To_Stock foreign key (stock_id) references stock;");

    @Getter
    private final String state;

    CreateDeleteDBscripts(String state) {
        this.state = state;
    }
}
