package com.stock.keeper.stockkeeper.repo;

import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.scripts.CreateDeleteDBscripts;
import com.stock.keeper.stockkeeper.scripts.InsertScripts;
import com.stock.keeper.stockkeeper.scripts.SelectScripts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataRepo implements DataRepository{
    private final static String DB_URL = "jdbc:postgresql://localhost:5432/Testing";

    private final static String USER = "postgres";
    private final static String PASS = "Orel-5287";

    public DataRepo() {
        try {
            DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Установка соединения");
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public void createDataBase() {
        System.out.println("Create db");
        try (Connection connection = DriverManager
                .getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     String.valueOf(CreateDeleteDBscripts.valueOf("createStatement").getState())
             )
        ) {
            statement.execute();
        } catch (SQLException e) {
            System.err.println("Ошибка исполнения запроса");
        }
    }

    @Override
    public void deleteDataBase() {

    }

    @Override
    public List<User> selectUser(String name, String password) {
        System.out.println("select user");

        List<User> selectedUsers = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectUserByNameAndPassword").getState())
             )
        ) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, name);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getString("id"));
                user.setPassword(resultSet.getString("password"));
                user.setUsr_name(resultSet.getString("usr_name"));

                selectedUsers.add(user);
            }
            return selectedUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            //System.err.println("Ошибка исполнения запроса");
            return selectedUsers;
        }
    }

    @Override
    public void selectStock() {

    }

    @Override
    public void selectPrice() {

    }

    @Override
    public void selectPurpose() {

    }

    @Override
    public User insertUser(String name, String password) {
        System.out.println("insert user");

        User user = new User();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(InsertScripts.valueOf("insertUser").getState())
             )
        ) {
            String generatedId =  generateId();

            preparedStatement.setString(1, generatedId);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);

            if(preparedStatement.execute()){
                user.setId(generatedId);
                user.setUsr_name(name);
                user.setPassword(password);
            }
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return user;
        }
    }

    @Override
    public void insertStock() {

    }

    @Override
    public void insertPrice() {

    }

    @Override
    public void insertPurpose() {

    }

    @Override
    public void updateUser() {

    }

    @Override
    public void updateStock() {

    }

    @Override
    public void updatePrice() {

    }

    @Override
    public void updatePurpose() {

    }

    @Override
    public void deleteUser() {

    }

    @Override
    public void deleteStock() {

    }

    @Override
    public void deletePrice() {

    }

    @Override
    public void deletePurpose() {

    }
}
