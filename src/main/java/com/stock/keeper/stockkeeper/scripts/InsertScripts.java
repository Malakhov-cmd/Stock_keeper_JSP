package com.stock.keeper.stockkeeper.scripts;

import lombok.Getter;

public enum InsertScripts {
    insertUser("insert into usr values (?, ?, ?)"),
    insertStock("insert into stock (date_init, description, img_link, index, name, usr_id, id) values (?, ?, ?, ?, ?, ?, ?)"),
    insertCost("insert into stock_price (cost, date, stock_id, id) values (?, ?, ?, ?)"),
    insertPurpose("insert into stock_purpose (cost, date, stock_id, id) values (?, ?, ?, ?)");

    @Getter
    private final String state;

    InsertScripts(String state) {
        this.state = state;
    }
}
