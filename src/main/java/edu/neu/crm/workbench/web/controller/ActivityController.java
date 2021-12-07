package edu.neu.crm.workbench.web.controller;

import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.settings.service.impl.UserServiceImpl;
import edu.neu.crm.utils.*;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.service.ActivityService;
import edu.neu.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("access in activity controller。。。");

        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/activity/saveActivity.do".equals(path)){
            saveActivity(request,response);
        }
    }

    private void saveActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("保存市场活动信息");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.saveActivity(activity);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得用户信息列表");

        //调用业务层的方法处理请求
        //由于查询的是用户数据表，所以这里应该调用用户的service层方法
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        //将查询出的结果转换为JSON格式并发送给前端
        if(userList != null){
            PrintJson.printJsonObj(response,userList);
        }

    }

}
