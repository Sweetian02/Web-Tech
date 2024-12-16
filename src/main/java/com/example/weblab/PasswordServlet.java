package com.example.weblab;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/PasswordServlet")
public class PasswordServlet extends HttpServlet {

    //密码
    private static final String PASSWORD="123456";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String password = request.getParameter("password");
        if(password.equals(PASSWORD)){
            String contextPath=request.getContextPath();
            response.sendRedirect(contextPath+"/update.html");
        }

        else {
            // 获取输出流
            // 使用 response.getOutputStream() 避免与 sendRedirect 冲突
            response.getWriter().write("<h1 align=\"center\";>Password wrong!Please try again.</h1>");
        }

    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
}
