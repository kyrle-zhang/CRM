package edu.neu.crm.workbench.web.controller;

import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.settings.service.impl.UserServiceImpl;
import edu.neu.crm.utils.DateTimeUtil;
import edu.neu.crm.utils.PrintJson;
import edu.neu.crm.utils.ServiceFactory;
import edu.neu.crm.utils.UUIDUtil;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.Tran;
import edu.neu.crm.workbench.domain.TranHistory;
import edu.neu.crm.workbench.service.ActivityService;
import edu.neu.crm.workbench.service.CustomerService;
import edu.neu.crm.workbench.service.TranService;
import edu.neu.crm.workbench.service.impl.ActivityServiceImpl;
import edu.neu.crm.workbench.service.impl.CustomerServiceImpl;
import edu.neu.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入交易控制器");
        String path = request.getServletPath();
        if ("/workbench/transaction/save.do".equals(path)) {
            save(request, response);
        } else if ("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request, response);
        } else if ("/workbench/transaction/getAllActivity.do".equals(path)) {
            getAllActivity(request, response);
        } else if ("/workbench/transaction/saveTran.do".equals(path)) {
            saveTran(request, response);
        } else if ("/workbench/transaction/getTran.do".equals(path)) {
            getTran(request, response);
        } else if ("/workbench/transaction/detail.do".equals(path)) {
            detail(request, response);
        } else if ("/workbench/transaction/getTranHistory.do".equals(path)) {
            getTranHistory(request, response);
        } else if ("/workbench/transaction/changeTranStage.do".equals(path)) {
            changeTranStage(request, response);
        } else if ("/workbench/transaction/getChartData.do".equals(path)) {
            getChartData(request, response);
        }
    }

    private void getChartData(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取图表数据");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,List<Object>> map = tranService.getChartData();
        PrintJson.printJsonObj(response,map);
    }

    private void changeTranStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("改变交易阶段");
        String tranId = request.getParameter("tranId");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        User user = (User) request.getSession().getAttribute("user");
        String editBy = user.getName();
        String editTime = DateTimeUtil.getSysTime();
        String createBy = user.getName();
        Map<String,String> possibilityMap = (Map<String, String>) this.getServletContext().getAttribute("possibilityMap");
        String possibility = possibilityMap.get(stage);
        Tran tran = new Tran();
        tran.setId(tranId);
        tran.setStage(stage);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        tran.setPossibility(possibility);
        tran.setCreateBy(createBy);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);
        Map<String,Object> map = new HashMap<>();
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag = tranService.changeTranStage(tran);
        map.put("success",flag);
        map.put("tran",tran);
        PrintJson.printJsonObj(response,map);
    }

    private void getTranHistory(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取交易历史列表");
        String tranId = request.getParameter("tranId");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> tranHistoryList = tranService.getTranHistory(tranId);
        Map<String, String> map = (Map<String, String>) this.getServletContext().getAttribute("possibilityMap");
        for (TranHistory tranHistory : tranHistoryList) {
            String stage = tranHistory.getStage();
            String possibility = map.get(stage);
            tranHistory.setPossibility(possibility);
        }
        PrintJson.printJsonObj(response, tranHistoryList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("交易详细信息页");
        String id = request.getParameter("id");
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran = tranService.detail(id);
        //获取交易成功的可能性
        String stage = tran.getStage();
        Map<String, String> map = (Map<String, String>) this.getServletContext().getAttribute("possibilityMap");
        String possibility = map.get(stage);
        request.setAttribute("tran", tran);
        request.setAttribute("possibility", possibility);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);
    }

    private void getTran(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取交易列表");
        String pageSize = request.getParameter("pageSize");
        String pageNo = request.getParameter("pageNo");
        Integer skipNo = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("skipNo", skipNo);
        map.put("pageSize", Integer.valueOf(pageSize));
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        PaginationVO<Tran> paginationVO = tranService.getTran(map);
        PrintJson.printJsonObj(response, paginationVO);
    }

    private void saveTran(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("保存交易信息");
        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        User user = (User) request.getSession().getAttribute("user");
        String createBy = user.getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        Tran tran = new Tran();
        tran.setOwner(owner);
        tran.setContactsId(contactsId);
        tran.setName(name);
        tran.setMoney(money);
        tran.setStage(stage);
        tran.setExpectedDate(expectedDate);
        tran.setCreateTime(createTime);
        tran.setCreateBy(createBy);
        tran.setId(id);
        tran.setContactSummary(contactSummary);
        tran.setType(type);
        tran.setSource(source);
        tran.setNextContactTime(nextContactTime);
        tran.setDescription(description);
        tran.setActivityId(activityId);
        TranService tranService = (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag = tranService.save(tran, customerName);
        if (flag) {
            //使用重定向跳转到交易初始页
            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");
        }
    }

    private void getAllActivity(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取市场活动分页列表");
        String pageSize = request.getParameter("pageSize");
        String pageNo = request.getParameter("pageNo");
        String name = request.getParameter("name");
        Integer skipNo = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);
        ActivityService activityService = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Map<String, Object> map = new HashMap<>();
        map.put("skipNo", skipNo);
        map.put("pageSize", Integer.valueOf(pageSize));
        map.put("name", name);
        PaginationVO<Activity> activity = activityService.getActivity(map);
        PrintJson.printJsonObj(response, activity);
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("客户名称自动补全");
        String name = request.getParameter("name");
        CustomerService customerService = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> nameList = customerService.getCustomerName(name);

        PrintJson.printJsonObj(response, nameList);
        PrintJson.printJsonObj(response,nameList);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("跳转到创建交易页面");
        UserService userService = (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList = userService.getUserList();
        request.setAttribute("userList", userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);
        request.setAttribute("userList",userList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
