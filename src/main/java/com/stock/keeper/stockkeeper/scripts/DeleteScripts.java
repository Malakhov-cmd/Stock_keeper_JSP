package com.stock.keeper.stockkeeper.scripts;

import lombok.Getter;

public enum DeleteScripts {
    deleteStockByUser("delete from stock where id = ?"),
    deletePricesByUser("delete from stock_price where stock_id = ?"),
    deletePurposesStockByUser("delete from stock_purpose where stock_id = ?");

    @Getter
    private final String state;

    DeleteScripts(String state) {
        this.state = state;
    }
}
