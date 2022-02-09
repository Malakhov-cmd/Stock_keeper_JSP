package com.stock.keeper.stockkeeper.service;

import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.repo.DataRepo;

import java.util.List;

public class LoginService {
    public User checkLogin(String name, String password) {
        DataRepo dataRepo = new DataRepo();

        List<User> selectedUsers = dataRepo.selectUser(name, password);
        if (selectedUsers
                .stream()
                .filter(item -> item.getUsr_name().equals(name))
                .noneMatch(item -> item.getPassword().equals(password))) {
            return dataRepo.insertUser(name, password);
        } else {
            return selectedUsers.get(0);
        }
    }
}
