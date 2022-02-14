package com.stock.keeper.stockkeeper.servlet;

import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.repo.DataRepo;
import com.stock.keeper.stockkeeper.service.LoginService;
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

        HttpSession session = request.getSession();

        if (purposeCostEntered != null) {
            Long userId = Long.valueOf(request.getParameter("userId"));
            Double purposeCost = Double.valueOf(purposeCostEntered);


        } else {
            if (newCurrent != null) {
                Long newCurrentId = Long.valueOf(newCurrent);

                DataRepo dataRepo = new DataRepo();

                session.setAttribute("currentStock", dataRepo.selectStockById(newCurrentId));
                RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
                dispatcher.forward(request, response);
            } else {

                if (index != null) {
                    Long userId = Long.valueOf(request.getParameter("userId"));

                    StockDataAPIService apiService = new StockDataAPIService();
                    apiService.getResponceByAPI(index, userId);

                    DataRepo dataRepo = new DataRepo();
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("home.jsp");
        requestDispatcher.forward(req, resp);
    }
}
