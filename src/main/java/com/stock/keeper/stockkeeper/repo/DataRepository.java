package com.stock.keeper.stockkeeper.repo;

import com.stock.keeper.stockkeeper.domain.User;

import java.util.List;

public interface DataRepository {
    public void createDataBase();

    public void deleteDataBase();

    public List<User> selectUser(String name, String password);

    public void selectStock();

    public void selectPrice();

    public void selectPurpose();

    public User insertUser(String name, String password);

    public void insertStock();

    public void insertPrice();

    public void insertPurpose();

    public void updateUser();

    public void updateStock();

    public void updatePrice();

    public void updatePurpose();

    public void deleteUser();

    public void deleteStock();

    public void deletePrice();

    public void deletePurpose();
}
