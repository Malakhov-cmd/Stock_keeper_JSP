package com.stock.keeper.stockkeeper.repo;

import com.stock.keeper.stockkeeper.domain.Price;
import com.stock.keeper.stockkeeper.domain.Purpose;
import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.scripts.CreateDeleteDBscripts;
import com.stock.keeper.stockkeeper.scripts.DeleteScripts;
import com.stock.keeper.stockkeeper.scripts.InsertScripts;
import com.stock.keeper.stockkeeper.scripts.SelectScripts;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class DataRepo implements DataRepository {
    private final static String DB_URL = "jdbc:postgresql://localhost:5432/STOCKDATA";

    private final static String USER = "postgres";
    private final static String PASS = "Orel-5287";

    public DataRepo() {
        try {
            DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            log.info("Establishing a connection");
        }
    }

    @Override
    public void createDataBase() {
        log.info("Create db");
        try (Connection connection = DriverManager
                .getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     String.valueOf(CreateDeleteDBscripts.valueOf("createStatement").getState())
             )
        ) {
            statement.execute();
        } catch (SQLException e) {
            log.error("Request execution error - Create db: SQLException");
        }
    }

    @Override
    public void deleteDataBase() {
        log.info("Delete db");
        try (Connection connection = DriverManager
                .getConnection(DB_URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(
                     String.valueOf(CreateDeleteDBscripts.valueOf("deleteDataBase").getState())
             )
        ) {
            statement.execute();
        } catch (SQLException e) {
            log.error("Request execution error - Delete db: SQLException");
        }
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
            log.error("Request execution error - Select next val: SQLException");
            return -1L;
        }
    }

    @Override
    public List<User> selectUser(String name, String password) {
        log.info("select user");

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
                selectedUsers.add(new User()
                        .setId(resultSet.getLong("id"))
                        .setPassword(resultSet.getString("password"))
                        .setUsr_name(resultSet.getString("usr_name")));
            }
            return selectedUsers;
        } catch (SQLException e) {
            log.error("Request execution error - Select user: SQLException");
            return selectedUsers;
        }
    }

    public User selectUserById(Long id) {
        log.info("select user by id");

        User findedUser = new User();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectUserById").getState())
             )
        ) {
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                findedUser.setId(resultSet.getLong("id"))
                        .setPassword(resultSet.getString("password"))
                        .setUsr_name(resultSet.getString("usr_name"));
            }
            return findedUser;
        } catch (SQLException e) {
            log.error("Request execution error - Select user by id: SQLException");
            return findedUser;
        }
    }

    @Override
    public Stock selectStockById(Long stockId) {
        log.info("select stock by id");

        Stock findedStock = new Stock();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectStockById").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                findedStock.setId(resultSet.getLong("id"))
                        .setDate_init(resultSet.getTimestamp("date_init").toLocalDateTime())
                        .setDescription(resultSet.getString("description"))
                        .setImg_link(resultSet.getString("Img_link"))
                        .setIndex(resultSet.getString("index"))
                        .setName(resultSet.getString("name"))

                        .setOwner(selectUserById(resultSet.getLong("usr_id")))
                        .setPriceList(selectPriceByStockId(stockId))
                        .setPurposeList(selectPurposeBySrockId(stockId));
            }
            return findedStock;
        } catch (SQLException e) {
            log.error("Request execution error - Select stock by id: SQLException");
            return findedStock;
        }
    }

    @Override
    public List<Stock> selectStocksByUsrId(Long userId) {
        log.info("select stocks by userId");

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

                userStock.setId(resultSet.getLong("id"))
                        .setDate_init(resultSet.getTimestamp("date_init").toLocalDateTime())
                        .setDescription(resultSet.getString("description"))
                        .setImg_link(resultSet.getString("Img_link"))
                        .setIndex(resultSet.getString("index"))
                        .setName(resultSet.getString("name"))

                        .setOwner(selectUserById(resultSet.getLong("usr_id")))
                        .setPriceList(selectPriceByStockId(userStock.getId()))
                        .setPurposeList(selectPurposeBySrockId(userStock.getId()));

                findedStocks.add(userStock);
            }
            return findedStocks;
        } catch (SQLException e) {
            log.error("Request execution error - Select stocks by userId: SQLException");
            return findedStocks;
        }
    }

    public List<Price> selectPriceByStockId(Long stockId) {
        log.info("select costs by stock_id");

        List<Price> priceList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectPriceByStockId").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                priceList.add(new Price()
                        .setId(resultSet.getLong("id"))
                        .setCost(resultSet.getDouble("cost"))
                        .setDate(resultSet.getDate("date"))
                        .setStock_id(resultSet.getLong("stock_id")));
            }
            return priceList;
        } catch (SQLException e) {
            log.error("Request execution error - Select costs by stock_id: SQLException");
            return priceList;
        }
    }

    @Override
    public List<Purpose> selectPurposeBySrockId(Long stockId) {
        log.info("select purpose by stock_id");

        List<Purpose> purposesList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(SelectScripts.valueOf("selectPurposeByStockId").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                purposesList.add(new Purpose()
                        .setId(resultSet.getLong("id"))
                        .setCost(resultSet.getDouble("cost"))
                        .setDate(resultSet.getDate("date"))
                        .setPurposeDate(resultSet.getDate("purpose_date"))
                        .setStock_id(resultSet.getLong("stock_id")));
            }
            return purposesList;
        } catch (SQLException e) {
            log.error("Request execution error - Select purpose by stock_id: SQLException");
            return purposesList;
        }
    }

    @Override
    public User insertUser(String name, String password) {
        log.info("insert user");

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

            preparedStatement.execute();

            user.setId(id)
                    .setUsr_name(name)
                    .setPassword(password);

            return user;
        } catch (SQLException e) {
            log.error("Request execution error - Insert user: SQLException");
            return user;
        }
    }

    @Override
    public Stock insertStock(
            LocalDateTime dateInit, String description,
            String imgLink, String index,
            String name, Long usrId
    ) {
        log.info("insert stock");

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

            stock.setId(id)
                    .setOwner(selectUserById(usrId))
                    .setPriceList(new ArrayList<>())
                    .setPurposeList(new ArrayList<>())
                    .setDate_init(dateInit)
                    .setIndex(index)
                    .setName(name)
                    .setImg_link(imgLink)
                    .setDescription(description);

            return stock;
        } catch (SQLException e) {
            log.error("Request execution error - Insert stock: SQLException");
            return stock;
        }
    }

    @Override
    public Price insertPrice(Double cost, Date date, Long stock_id) {
        log.info("insert price");

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

            price.setId(geteratedId)
                    .setDate(date)
                    .setCost(cost)
                    .setStock_id(stock_id);

            return price;
        } catch (SQLException e) {
            log.error("Request execution error - Insert price: SQLException");
            return price;
        }
    }

    @Override
    public Purpose insertPurpose(Double cost, Date date, Date purposeDate, Long stock_id) {
        log.info("insert purpose");

        Purpose purpose = new Purpose();

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(InsertScripts.valueOf("insertPurpose").getState())
             )
        ) {
            Long geteratedId = selectNextVal();

            preparedStatement.setDouble(1, cost);
            preparedStatement.setDate(2, date);
            preparedStatement.setDate(3, purposeDate);
            preparedStatement.setLong(4, stock_id);
            preparedStatement.setLong(5, geteratedId);

            preparedStatement.execute();

            purpose.setId(geteratedId)
                    .setDate(date)
                    .setCost(cost)
                    .setStock_id(stock_id);

            return purpose;
        } catch (SQLException e) {
            log.error("Request execution error - Insert purpose: SQLException");
            return purpose;
        }
    }

    @Override
    public void deleteStockById(Long stockId) {
        log.info("delete stock by id");

        deletePricesByStockId(stockId);
        deletePurposesByStockId(stockId);

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(DeleteScripts.valueOf("deleteStockByUser").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Request execution error - Delete stock by id: SQLException");
        }
    }

    @Override
    public void deletePricesByStockId(Long stockId) {
        log.info("delete prices by stock Id");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(DeleteScripts.valueOf("deletePricesByUser").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Request execution error - Delete prices by stock Id: SQLException");
        }
    }

    @Override
    public void deletePurposesByStockId(Long stockId) {
        log.info("delete purposes by stock Id");

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = connection.prepareStatement(
                     String.valueOf(DeleteScripts.valueOf("deletePurposesStockByUser").getState())
             )
        ) {
            preparedStatement.setLong(1, stockId);

            preparedStatement.execute();
        } catch (SQLException e) {
            log.error("Request execution error - Delete purposes by stock Id: SQLException");
        }
    }
}
