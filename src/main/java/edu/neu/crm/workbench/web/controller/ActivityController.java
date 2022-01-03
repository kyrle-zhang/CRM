package edu.neu.crm.workbench.web.controller;

import edu.neu.crm.exception.DeleteActivityException;
import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.settings.service.impl.UserServiceImpl;
import edu.neu.crm.utils.*;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.ActivityRemark;
import edu.neu.crm.workbench.service.ActivityService;
import edu.neu.crm.workbench.service.impl.ActivityServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        }else if("/workbench/activity/getActivity.do".equals(path)){
            getActivity(request,response);
        }else if("/workbench/activity/deleteActivity.do".equals(path)){
            deleteActivity(request,response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){
            getUserListAndActivity(request,response);
        }else if("/workbench/activity/update.do".equals(path)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/activity/saveRemark.do".equals(path)){
            saveRemark(request,response);
        }else if("/workbench/activity/getRemark.do".equals(path)){
            getRemark(request,response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)){
            deleteRemark(request,response);
        }else if("/workbench/activity/updateRemark.do".equals(path)){
            updateRemark(request,response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("修改市场活动备注信息");
        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        String editFlag = "1";

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(editTime);
        activityRemark.setEditBy(editBy);
        activityRemark.setEditFlag(editFlag);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.updateRemark(activityRemark);
        PrintJson.printJsonFlag(response,flag);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除市场活动备注");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动备注列表");
        String activityId = request.getParameter("activityId");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> activityRemarkList = activityService.getRemark(activityId);
        PrintJson.printJsonObj(response,activityRemarkList);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("添加市场活动备注信息");
        String id = UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        String editFlag = request.getParameter("editFlag");
        String activityId = request.getParameter("activityId");

        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(createTime);
        activityRemark.setCreateBy(createBy);
        activityRemark.setEditFlag(editFlag);
        activityRemark.setActivityId(activityId);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.saveRemark(activityRemark);
        PrintJson.printJsonFlag(response,flag);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("市场活动详细信息查询");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity = activityService.detail(id);
        request.getSession().setAttribute("activity",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("更新市场活动");
        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        String editTime = DateTimeUtil.getSysTime();
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();

        Activity activity = new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = activityService.update(activity);

        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("修改市场活动");
        String id = request.getParameter("id");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String,Object> map = activityService.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除市场活动");
        //首先获得用户提交的参数
        String [] ids = request.getParameterValues("id");
        //调用业务层的方法处理业务
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag = false;
        try{
            flag = activityService.deleteActivity(ids);
        }catch (DeleteActivityException e){
            e.printStackTrace();
        }finally {
            PrintJson.printJsonFlag(response,flag);
        }

    }

    private void getActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询市场活动");
        //1.获取前端传递的查询参数
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        //计算数据库分页查询的起始查询条数
        Integer skipNo = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);
        //创一个Map集合来封装这些要传递给业务层的数据
        Map<String,Object> searchParameters = new HashMap();
        searchParameters.put("skipNo",skipNo);
        searchParameters.put("pageSize",Integer.valueOf(pageSize));
        searchParameters.put("name",name);
        searchParameters.put("owner",owner);
        searchParameters.put("startDate",startDate);
        searchParameters.put("endDate",endDate);
        //2.获取业务层的代理对象
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        /*
            业务层要返回的数据：
            数据库记录总条数：分页组件需要
            市场活动列表：前端展示数据需要
            由于分页查询功能的使用非常普遍，所以可以专门定义一个VO类来对查询出来的的数据进行封装
         */
        PaginationVO<Activity> activityPaginationVO = activityService.getActivity(searchParameters);

        PrintJson.printJsonObj(response,activityPaginationVO);

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
