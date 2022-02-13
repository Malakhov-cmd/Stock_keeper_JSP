package com.stock.keeper.stockkeeper.scripts;

import lombok.Getter;

public enum SelectScripts {
    selectUserByNameAndPassword("select * from usr where password = ? and usr_name = ?"),
    selectUserById("select * from usr where id=?"),
    selectStockById("select * from stock where id=?"),
    selectStockByUserId("select * from stock where usr_id = ?"),
    selectPriceByStockId("select * from stock_price where  stock_id = ?"),
    selectPurposeByStockId("select * from stock_purpose where  stock_id = ?"),
    selectNextVal("select nextval('public.hibernate_sequence');");

    @Getter
    private final String state;

    SelectScripts(String state) {
        this.state = state;
    }
}
