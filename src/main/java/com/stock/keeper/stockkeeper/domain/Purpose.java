package com.stock.keeper.stockkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Purpose {
    private Long id;

    private double cost;
    private Date date;
    private Date purposeDate;

    private Long stock_id;
}
