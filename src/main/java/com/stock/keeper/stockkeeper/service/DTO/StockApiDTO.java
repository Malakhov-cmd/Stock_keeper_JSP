package com.stock.keeper.stockkeeper.service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockApiDTO {
    private String status;
    private Date from;
    private String symbol;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
    private Double afterHours;
    private Double preMarket;

    public Double AveragePerDay() {
        return (high + low) / 2;
    }
}
