package com.stock.keeper.stockkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Purpose {
    private Long id;

    private double cost;
    private Date date;

    private Long stock_id;
}
