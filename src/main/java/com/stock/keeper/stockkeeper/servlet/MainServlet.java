package com.stock.keeper.stockkeeper.servlet;

import com.stock.keeper.stockkeeper.domain.Stock;
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
import java.util.List;

@NoArgsConstructor
@WebServlet(name = "main", value = "/stock")
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        String index = request.getParameter("index");

        if (index != null) {
            Long userId = Long.valueOf(request.getParameter("userId"));

            StockDataAPIService apiService = new StockDataAPIService();
            apiService.getResponceByAPI(index, userId);

            DataRepo dataRepo = new DataRepo();
            //List<Stock> stocks = dataRepo.selectStocksByUsrId(userId);
            User user = dataRepo.selectUserById(userId);

            HttpSession session = request.getSession();
            //session.setAttribute("stocks", stocks);
            session.setAttribute("user", user);

            RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
            dispatcher.forward(request, response);
        } else {

            LoginService loginService = new LoginService();

            User user = loginService.checkLogin(name, password);
            String destPage = "login.jsp";

            if (user.getPassword() != null && user.getUsr_name() != null) {
                HttpSession session = request.getSession();

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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("home.jsp");
        requestDispatcher.forward(req, resp);
    }
}
