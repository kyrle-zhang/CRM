package edu.neu.crm.workbench.web.controller;

import edu.neu.crm.exception.SaveClueException;
import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.settings.service.impl.UserServiceImpl;
import edu.neu.crm.utils.DateTimeUtil;
import edu.neu.crm.utils.PrintJson;
import edu.neu.crm.utils.ServiceFactory;
import edu.neu.crm.utils.UUIDUtil;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.Clue;
import edu.neu.crm.workbench.domain.ClueActivityRelation;
import edu.neu.crm.workbench.service.ActivityService;
import edu.neu.crm.workbench.service.ClueService;
import edu.neu.crm.workbench.service.impl.ActivityServiceImpl;
import edu.neu.crm.workbench.service.impl.ClueServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入线索模块控制器");
        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)) {
            getUserList(request, response);
        } else if ("/workbench/clue/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/clue/get.do".equals(path)) {
            get(request, response);
        } else if ("/workbench/clue/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/clue/getActivityList.do".equals(path)) {
            getActivityList(request, response);
        } else if ("/workbench/clue/saveClueActivityRelation.do".equals(path)) {
            saveClueActivityRelation(request, response);
        } else if ("/workbench/clue/getActivityListByClueId.do".equals(path)) {
            getActivityListByClueId(request, response);
        } else if ("/workbench/clue/deleteBundleById.do".equals(path)) {
            deleteBundleById(request, response);
        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {
            getActivityListByName(request, response);
        }
    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("模糊查询市场活动列表");
        String name = request.getParameter("activityName");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> activityList = activityService.getActivityListByName(name);
        PrintJson.printJsonObj(response,activityList);
    }

    private void deleteBundleById(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("删除线索市场活动关系");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = clueService.deleteBundleById(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取关联的市场活动列表");
        String clueId = request.getParameter("clueId");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        List<Activity> activityList = clueService.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,activityList);
    }

    private void saveClueActivityRelation(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("保存线索和市场活动关系");
        String clueId = request.getParameter("clueId");
        String[] activityIds = request.getParameterValues("activityId");
        List<ClueActivityRelation> clueActivityRelationList = new ArrayList<>();
        for (int i = 0; i < activityIds.length; i++) {
            clueActivityRelationList.add(new ClueActivityRelation());
            String id = UUIDUtil.getUUID();
            clueActivityRelationList.get(i).setId(id);
            clueActivityRelationList.get(i).setClueId(clueId);
            clueActivityRelationList.get(i).setActivityId(activityIds[i]);
        }
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = clueService.saveClueActivityRelation(clueActivityRelationList);
        PrintJson.printJsonFlag(response, flag);
    }

    private void getActivityList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取市场活动列表");
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        String clueId = request.getParameter("clueId");
        String activityName = request.getParameter("activityName");
        Map<String, Object> map = new HashMap<>();
        map.put("clueId", clueId);
        map.put("activityName", activityName);
        List<Activity> activityList = activityService.getActivityList(map);
        PrintJson.printJsonObj(response, activityList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到线索详细信息页");
        String id = request.getParameter("id");
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue = clueService.getById(id);
        request.setAttribute("clue", clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);
    }

    private void get(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("查询线索信息");
        //1.获取前端传递的查询参数
        String pageNo = request.getParameter("pageNo");
        String pageSize = request.getParameter("pageSize");
        //计算数据库分页查询的起始查询条数
        Integer skipNo = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);
        //创一个Map集合来封装这些要传递给业务层的数据
        Map<String, Object> searchParameters = new HashMap();
        searchParameters.put("skipNo", skipNo);
        searchParameters.put("pageSize", Integer.valueOf(pageSize));
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        PaginationVO<Clue> paginationVO = clueService.get(searchParameters);
        PrintJson.printJsonObj(response, paginationVO);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("保存线索信息");
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");
        String id = UUIDUtil.getUUID();
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        String createTime = DateTimeUtil.getSysTime();
        System.out.println(owner);
        Clue clue = new Clue();
        clue.setAddress(address);
        clue.setAppellation(appellation);
        clue.setCompany(company);
        clue.setContactSummary(contactSummary);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setEmail(email);
        clue.setFullname(fullname);
        clue.setWebsite(website);
        clue.setState(state);
        clue.setSource(source);
        clue.setPhone(phone);
        clue.setOwner(owner);
        clue.setMphone(mphone);
        clue.setJob(job);
        clue.setId(id);
        clue.setNextContactTime(nextContactTime);

        System.out.println(clue.getOwner());
        ClueService clueService = (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag = false;
        try {
            flag = clueService.save(clue);
        } catch (SaveClueException e) {
            e.printStackTrace();
        }
        PrintJson.printJsonFlag(response, flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取用户列表");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        PrintJson.printJsonObj(response, userList);
    }
}
