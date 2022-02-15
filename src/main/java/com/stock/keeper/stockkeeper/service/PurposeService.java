package com.stock.keeper.stockkeeper.service;

import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.repo.DataRepository;
import lombok.NoArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

@NoArgsConstructor
public class PurposeService {
    private final DataRepository dataRepo = new DataRepo();

    public void insertStock(Long userId, String purposeDate, Long stockId, Double purposeCost) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");

        DateTimeFormatter formatterDatePicker = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                .toFormatter();

        java.util.Date date = new Date();

        dataRepo.insertPurpose(
                purposeCost,
                java.sql.Date.valueOf(String.valueOf(LocalDate.parse(purposeDate, formatterDatePicker))),
                java.sql.Date.valueOf(formater.format(date.getTime())),
                stockId
        );
    }
}
