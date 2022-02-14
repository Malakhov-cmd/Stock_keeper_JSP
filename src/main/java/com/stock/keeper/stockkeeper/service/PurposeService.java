package com.stock.keeper.stockkeeper.service;

import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.repo.DataRepository;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@NoArgsConstructor
public class PurposeService {
    private final DataRepository dataRepo = new DataRepo();

    public void insertStock(Long userId, Long stockId, Double purposeCost) {
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();

        dataRepo.insertPurpose(
                purposeCost,
                java.sql.Date.valueOf(formater.format(date.getTime())),
                stockId
        );
    }
}
