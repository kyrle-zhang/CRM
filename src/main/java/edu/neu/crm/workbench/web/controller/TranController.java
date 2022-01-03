package edu.neu.crm.workbench.web.controller;

import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.settings.service.impl.UserServiceImpl;
import edu.neu.crm.utils.PrintJson;
import edu.neu.crm.utils.ServiceFactory;
import edu.neu.crm.workbench.service.CustomerService;
import edu.neu.crm.workbench.service.impl.CustomerServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易控制器");
        String path = request.getServletPath();
        if("/workbench/transaction/save.do".equals(path)){
           save(request, response);
        } else if("/workbench/transaction/getCustomerName.do".equals(path)){
           getCustomerName(request, response);
        }

    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("客户名称自动补全");
        String name = request.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> nameList = customerService.getCustomerName(name);
        PrintJson.printJsonObj(response,nameList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到创建交易页面");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
