package edu.neu.crm.settings.web.controller;

import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.settings.service.impl.UserServiceImpl;
import edu.neu.crm.utils.MD5Util;
import edu.neu.crm.utils.PrintJson;
import edu.neu.crm.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("access in user controller。。。");

        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)){
            login(request,response);
        }else if("/settings/user/xxx.do".equals(path)){
            //xxx(request,response);
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("access in login...");
        String loginAct = request.getParameter("loginAct");
        String loginPwd = request.getParameter("loginPwd");
        loginPwd = MD5Util.getMD5(loginPwd);
        String ip = request.getRemoteAddr();

        //创建业务层的代理对象,未来业务层的开发统一使用代理类形态的接口对象
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());

        try{
            //调用业务层的代理类的login方法进行登陆验证的业务处理
            //注意这里走的是TransactionInvocationHandler中的invoke方法
            User user = userService.login(loginAct,loginPwd,ip);

            //如果用户登录成功，就把用户信息存入到session对象中，以便后期使用
            HttpSession session = request.getSession();
            session.setAttribute("user",user);

            //如果登陆成功,给前端发送消息
            PrintJson.printJsonFlag(response,true);

        }catch (Exception e){
            e.printStackTrace();
            //用户登陆失败,给前端发送失败信息
            String msg = e.getMessage();
            Map<String,Object> map = new HashMap<>();
            map.put("flag",false);
            map.put("msg",msg);
            PrintJson.printJsonObj(response,map);
        }
    }
}
