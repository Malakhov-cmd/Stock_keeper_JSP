package com.stock.keeper.stockkeeper.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Price {
    private String id;

    private double cost;
    private LocalDateTime date;

    private Stock stock;
}
