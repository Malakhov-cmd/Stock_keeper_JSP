package com.stock.keeper.stockkeeper.servlet;

import com.stock.keeper.stockkeeper.domain.Stock;
import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.service.DeleteStockService;
import com.stock.keeper.stockkeeper.service.LoginService;
import com.stock.keeper.stockkeeper.service.PurposeService;
import com.stock.keeper.stockkeeper.service.StockDataAPIService;
import lombok.NoArgsConstructor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@NoArgsConstructor
@WebServlet(name = "main", value = "/stock")
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*
         * From registration
         * */
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        /*
         * From stock add panel
         * */
        String index = request.getParameter("index");

        /*
         * From stock show button
         * */
        String newCurrent = request.getParameter("newCurrentStockId");

        /*
         * From add purpose
         * */
        String purposeCostEntered = request.getParameter("purposeCost");


        /*
         * From refresh stock
         * */
        String refreshStockId = request.getParameter("stockRefreshId");

        /*
         * From delete stock
         * */
        String deleteStockId = request.getParameter("deleteStockId");

        HttpSession session = request.getSession();
        DataRepo dataRepo = new DataRepo();

        if (deleteStockId != null) {
            deleteStock(request, response, deleteStockId, session, dataRepo);
        } else {
            if (refreshStockId != null) {
                refreshStockCost(request, response, session, refreshStockId);
            } else {
                if (purposeCostEntered != null) {
                    addPurpose(request, response, purposeCostEntered, session, dataRepo);
                } else {
                    if (newCurrent != null) {
                        changeCurrentStock(request, response, newCurrent, session, dataRepo);
                    } else {
                        if (index != null) {
                            addStock(request, response, index, session, dataRepo);
                        } else {
                            loginOperatopn(request, response, name, password, session);
                        }
                    }
                }
            }
        }
    }

    private void loginOperatopn(HttpServletRequest request, HttpServletResponse response, String name, String password, HttpSession session) throws ServletException, IOException {
        LoginService loginService = new LoginService();

        User user = loginService.checkLogin(name, password);
        String destPage = "login.jsp";

        if (user.getPassword() != null && user.getUsr_name() != null) {
            session.setAttribute("userId", user.getId());

            session.setAttribute("user", user);
            destPage = "home.jsp";
        } else {
            String message = "Invalid name/password";
            request.setAttribute("message", message);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

    private void addStock(HttpServletRequest request, HttpServletResponse response, String index, HttpSession session, DataRepo dataRepo) throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("userId"));

        StockDataAPIService apiService = new StockDataAPIService();
        Stock newCurrent = apiService.getResponceByAPI(index.toUpperCase(), userId);

        if (newCurrent != null) {
            session.setAttribute("currentStock", dataRepo.selectStockById(newCurrent.getId()));
        }
        User user = dataRepo.selectUserById(userId);

        session.setAttribute("user", user);

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void changeCurrentStock(HttpServletRequest request, HttpServletResponse response, String newCurrent, HttpSession session, DataRepo dataRepo) throws ServletException, IOException {
        Long newCurrentId = Long.valueOf(newCurrent);

        session.setAttribute("currentStock", dataRepo.selectStockById(newCurrentId));

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void addPurpose(HttpServletRequest request, HttpServletResponse response, String purposeCostEntered, HttpSession session, DataRepo dataRepo) throws ServletException, IOException {
        Long stockId = Long.valueOf(request.getParameter("stockPurposeId"));
        Double purposeCost = Double.valueOf(purposeCostEntered);
        String purposeDate = request.getParameter("purposeDate");

        PurposeService purposeService = new PurposeService();
        purposeService.insertStock(purposeDate, stockId, purposeCost);

        session.setAttribute("currentStock", dataRepo.selectStockById(stockId));

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void refreshStockCost(HttpServletRequest request, HttpServletResponse response, HttpSession session, String refreshStockId) throws ServletException, IOException {
        StockDataAPIService apiService = new StockDataAPIService();
        Stock refreshedStock = apiService.refreshPriceStock(Long.valueOf(refreshStockId));

        session.setAttribute("currentStock", refreshedStock);

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    private void deleteStock(HttpServletRequest request, HttpServletResponse response, String deleteStockId, HttpSession session, DataRepo dataRepo) throws ServletException, IOException {
        DeleteStockService deleteStockService = new DeleteStockService();
        deleteStockService.deleteStock(Long.valueOf(deleteStockId));

        Stock currentStock = (Stock) session.getAttribute("currentStock");

        if (currentStock.getId().equals(Long.valueOf(deleteStockId)))
            session.setAttribute("currentStock", null);

        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("home.jsp");
        requestDispatcher.forward(req, resp);
    }
}
