package com.stock.keeper.stockkeeper.scripts;

import lombok.Getter;

public enum InsertScripts {
    insertUser("insert into usr values (?, ?, ?)");

    @Getter
    private final String state;

    InsertScripts(String state) {
        this.state = state;
    }
}
