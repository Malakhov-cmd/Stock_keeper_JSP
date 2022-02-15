package com.stock.keeper.stockkeeper.repo;

import com.stock.keeper.stockkeeper.domain.Price;
import com.stock.keeper.stockkeeper.domain.Purpose;
import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.domain.User;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface DataRepository {
    void createDataBase();

    void deleteDataBase();

    List<User> selectUser(String name, String password);

    Stock selectStockById(Long stockId);

    List<Stock> selectStocksByUsrId(Long userId);

    void selectPrice();

    List<Purpose> selectPurposeBySrockId(Long stockId);

    User insertUser(String name, String password);

    Stock insertStock(
            LocalDateTime dateInit, String description,
            String imgLink, String index,
            String name, Long usrId
    );

    Price insertPrice(Double cost, Date date, Long stock_id);

    Purpose insertPurpose(Double cost, Date date, Date purposeDate, Long stock_id);

    void deleteStockById(Long stockId);

    void deletePricesByStockId(Long stockId);

    void deletePurposesByStockId(Long stockId);
}
