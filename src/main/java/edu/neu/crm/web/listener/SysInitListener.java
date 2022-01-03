package edu.neu.crm.web.listener;

import edu.neu.crm.settings.service.DicService;
import edu.neu.crm.settings.service.impl.DicServiceImpl;
import edu.neu.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {

    /**
     * 在服务器启动后将数据库中的数据字典取出来保存到ServletContext对象中，方便在后续开发中的使用
     * 数据字典：
     *      前端页面需要用户选择的一些单选框、下拉框之类的数据对象都用两张数据表保存起来
     *      一张是数据类型，一张是数据值，统称为数据字典
     *      方便灵活使用，以及各种维护
     *
     *  数据字典的保存：
     *      一定要按照类型分别保存起来，才能在后续开发中正常使用
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        System.out.println("全局作用域对象创建好了");
        //1.初始化数据字典
        ServletContext servletContext = servletContextEvent.getServletContext();
        DicService dicService = (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List> map = dicService.getAll();
        Set<String> keySet = map.keySet();
        for(String key: keySet){
            servletContext.setAttribute(key,map.get(key));
        }

        //2.解析存放交易阶段和可能性的properties文件
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = resourceBundle.getKeys();
        Map<String,String> possibilityMap = new HashMap<>();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = resourceBundle.getString(key);
            possibilityMap.put(key,value);
        }
        servletContext.setAttribute("possibilityMap",possibilityMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
