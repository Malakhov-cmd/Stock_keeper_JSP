package com.stock.keeper.stockkeeper.servlet;

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
import java.sql.Date;

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
            Long deleteStockOnwerId = Long.valueOf(request.getParameter("deleteStockOwnerId"));

            DeleteStockService deleteStockService = new DeleteStockService();
            deleteStockService.deleteStock(Long.valueOf(deleteStockId), deleteStockOnwerId);

            RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
            dispatcher.forward(request, response);

        } else {
            if (refreshStockId != null) {
                StockDataAPIService apiService = new StockDataAPIService();
                apiService.refreshPriceStock(Long.valueOf(refreshStockId));

                RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
                dispatcher.forward(request, response);
            } else {
                if (purposeCostEntered != null) {
                    Long userId = Long.valueOf(request.getParameter("userPurposeId"));
                    Long stockId = Long.valueOf(request.getParameter("stockPurposeId"));
                    Double purposeCost = Double.valueOf(purposeCostEntered);
                    String purposeDate = request.getParameter("purposeDate");

                    PurposeService purposeService = new PurposeService();
                    purposeService.insertStock(userId, purposeDate, stockId, purposeCost);

                    session.setAttribute("currentStock", dataRepo.selectStockById(stockId));

                    RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
                    dispatcher.forward(request, response);
                } else {
                    if (newCurrent != null) {
                        Long newCurrentId = Long.valueOf(newCurrent);

                        session.setAttribute("currentStock", dataRepo.selectStockById(newCurrentId));

                        RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
                        dispatcher.forward(request, response);
                    } else {
                        if (index != null) {
                            Long userId = Long.valueOf(request.getParameter("userId"));

                            StockDataAPIService apiService = new StockDataAPIService();
                            apiService.getResponceByAPI(index.toUpperCase(), userId);

                            User user = dataRepo.selectUserById(userId);

                            session.setAttribute("user", user);

                            RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
                            dispatcher.forward(request, response);
                        } else {

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
                    }
                }
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("home.jsp");
        requestDispatcher.forward(req, resp);
    }
}
