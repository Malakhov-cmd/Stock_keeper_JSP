package com.stock.keeper.stockkeeper.repo;

import com.stock.keeper.stockkeeper.domain.Price;
import com.stock.keeper.stockkeeper.domain.Purpose;
import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.scripts.CreateDeleteDBscripts;
import com.stock.keeper.stockkeeper.scripts.InsertScripts;
import com.stock.keeper.stockkeeper.scripts.SelectScripts;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataRepo implements DataRepository {
    private final static String DB_URL = "jdbc:postgresql://localhost:5432/Testing";

    private final static String USER = "postgres";
    private final static String PASS = "Orel-5287";

    public DataRepo() {
        try {
            DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            System.out.println("Установка соединения");
            //createDataBase();
        }
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

    private Long selectNextVal() {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectNextVal").getState())
             )
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return -1L;
        }
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

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setPassword(resultSet.getString("password"));
                user.setUsr_name(resultSet.getString("usr_name"));

                selectedUsers.add(user);
            }
            return selectedUsers;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return selectedUsers;
        }
    }

    public User selectUserById(Long id) {
        System.out.println("select user by id");

        User findedUser = new User();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectUserById").getState())
             )
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                findedUser.setId(resultSet.getLong("id"));
                findedUser.setPassword(resultSet.getString("password"));
                findedUser.setUsr_name(resultSet.getString("usr_name"));
            }
            return findedUser;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return findedUser;
        }
    }

    @Override
    public Stock selectStockById(Long stockId) {
        System.out.println("select stock by id");

        Stock findedStock = new Stock();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectStockById").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                findedStock.setId(resultSet.getLong("id"));
                findedStock.setDate_init(resultSet.getTimestamp("date_init").toLocalDateTime());
                findedStock.setDescription(resultSet.getString("description"));
                findedStock.setImg_link(resultSet.getString("Img_link"));
                findedStock.setIndex(resultSet.getString("index"));
                findedStock.setName(resultSet.getString("name"));

                findedStock.setOwner(selectUserById(resultSet.getLong("usr_id")));
                findedStock.setPriceList(selectPriceByStockId(stockId));
                findedStock.setPurposeList(selectPurposeBySrockId(stockId));
            }
            return findedStock;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return findedStock;
        }
    }

    @Override
    public List<Stock> selectStocksByUsrId(Long userId) {
        System.out.println("select stocks by userId");

        List<Stock> findedStocks = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectStockByUserId").getState())
             )
        ) {
            preparedStatement.setLong(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Stock userStock = new Stock();

                userStock.setId(resultSet.getLong("id"));
                userStock.setDate_init(resultSet.getTimestamp("date_init").toLocalDateTime());
                userStock.setDescription(resultSet.getString("description"));
                userStock.setImg_link(resultSet.getString("Img_link"));
                userStock.setIndex(resultSet.getString("index"));
                userStock.setName(resultSet.getString("name"));

                userStock.setOwner(selectUserById(resultSet.getLong("usr_id")));
                userStock.setPriceList(selectPriceByStockId(userStock.getId()));
                userStock.setPurposeList(selectPurposeBySrockId(userStock.getId()));

                findedStocks.add(userStock);
            }
            return findedStocks;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return findedStocks;
        }
    }

    @Override
    public void selectPrice() {
    }

    public List<Price> selectPriceByStockId(Long stockId) {
        System.out.println("select costs by stock_id");

        List<Price> priceList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectPriceByStockId").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Price price = new Price();

                price.setId(resultSet.getLong("id"));
                price.setCost(resultSet.getDouble("cost"));
                price.setDate(resultSet.getDate("date"));
                price.setStock_id(resultSet.getLong("stock_id"));

                priceList.add(price);
            }
            return priceList;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return priceList;
        }
    }

    @Override
    public List<Purpose> selectPurposeBySrockId(Long stockId) {
        System.out.println("select purpose by stock_id");

        List<Purpose> purposesList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectPurposeByStockId").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Purpose purpose = new Purpose();

                purpose.setId(resultSet.getLong("id"));
                purpose.setCost(resultSet.getDouble("cost"));
                purpose.setDate(resultSet.getDate("date"));
                purpose.setStock_id(resultSet.getLong("stock_id"));

                purposesList.add(purpose);
            }
            return purposesList;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return purposesList;
        }
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
            Long id = selectNextVal();

            preparedStatement.setLong(1, id);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, name);

            //if (preparedStatement.execute()) {
            preparedStatement.execute();

            user.setId(id);
            user.setUsr_name(name);
            user.setPassword(password);
            //}
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return user;
        }
    }

    @Override
    public Stock insertStock(
            LocalDateTime dateInit, String description,
            String imgLink, String index,
            String name, Long usrId
    ) {
        System.out.println("insert stock");

        Stock stock = new Stock();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(InsertScripts.valueOf("insertStock").getState())
             )
        ) {
            Long id = selectNextVal();

            preparedStatement.setTimestamp(1, Timestamp.valueOf(dateInit));
            preparedStatement.setString(2, description);
            preparedStatement.setString(3, imgLink);
            preparedStatement.setString(4, index);
            preparedStatement.setString(5, name);
            preparedStatement.setLong(6, usrId);
            preparedStatement.setLong(7, id);

            preparedStatement.execute();

            stock.setId(id);
            stock.setOwner(selectUserById(usrId));
            stock.setPriceList(new ArrayList<>());
            stock.setPurposeList(new ArrayList<>());
            stock.setDate_init(dateInit);
            stock.setIndex(index);
            stock.setName(name);
            stock.setImg_link(imgLink);
            stock.setDescription(description);

            return stock;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return stock;
        }
    }

    @Override
    public Price insertPrice(Double cost, Date date, Long stock_id) {
        System.out.println("insert price");

        Price price = new Price();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(InsertScripts.valueOf("insertCost").getState())
             )
        ) {
            Long geteratedId = selectNextVal();

            preparedStatement.setDouble(1, cost);
            preparedStatement.setDate(2, date);
            preparedStatement.setLong(3, stock_id);
            preparedStatement.setLong(4, geteratedId);

            preparedStatement.execute();

            price.setId(geteratedId);
            price.setDate(date);
            price.setCost(cost);
            price.setStock_id(stock_id);

            return price;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return price;
        }
    }

    @Override
    public Purpose insertPurpose(Double cost, Date date, Long stock_id) {
        System.out.println("insert purpose");

        Purpose purpose = new Purpose();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(InsertScripts.valueOf("insertPurpose").getState())
             )
        ) {
            Long geteratedId = selectNextVal();

            preparedStatement.setDouble(1, cost);
            preparedStatement.setDate(2, date);
            preparedStatement.setLong(3, stock_id);
            preparedStatement.setLong(4, geteratedId);

            preparedStatement.execute();

            purpose.setId(geteratedId);
            purpose.setDate(date);
            purpose.setCost(cost);
            purpose.setStock_id(stock_id);

            return purpose;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка исполнения запроса");
            return purpose;
        }
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
