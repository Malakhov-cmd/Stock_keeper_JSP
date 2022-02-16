package com.stock.keeper.stockkeeper.service;

import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.repo.DataRepository;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DeleteStockService {
    private final DataRepository dataRepo = new DataRepo();

    public void deleteStock(Long deleteStockId){
        dataRepo.deleteStockById(deleteStockId);
    }
}
