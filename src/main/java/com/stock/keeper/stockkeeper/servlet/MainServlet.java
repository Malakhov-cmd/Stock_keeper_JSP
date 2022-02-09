package com.stock.keeper.stockkeeper.servlet;

import com.stock.keeper.stockkeeper.domain.User;
import com.stock.keeper.stockkeeper.service.LoginService;
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
@WebServlet(name="main", value="/stock")
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String password = request.getParameter("password");

        LoginService loginService = new LoginService();

        User user = loginService.checkLogin(name, password);
        String destPage = "login.jsp";

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            destPage = "home.jsp";
        } else {
            String message = "Invalid name/password";
            request.setAttribute("message", message);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher(destPage);
        dispatcher.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("home.jsp");
        requestDispatcher.forward(req, resp);
    }
}
