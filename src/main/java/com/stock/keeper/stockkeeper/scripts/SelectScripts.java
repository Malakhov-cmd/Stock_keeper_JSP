package com.stock.keeper.stockkeeper.scripts;

import lombok.Getter;

public enum SelectScripts {
    selectUserByNameAndPassword("select * from usr where password = ? and usr_name = ?");

    @Getter
    private final String state;

    SelectScripts(String state) {
        this.state = state;
    }
}
