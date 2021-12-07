package edu.neu.crm.workbench.service.impl;


import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.workbench.dao.ActivityDAO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.service.ActivityService;



public class ActivityServiceImpl implements ActivityService{

    private ActivityDAO activityDAO = SqlSessionUtil.getSqlSession().getMapper(ActivityDAO.class);


    @Override
    public Boolean saveActivity(Activity activity) {

        Boolean flag = true;

        Integer count = activityDAO.saveActivity(activity);

        if(count!=1){
            flag = false;
        }

        return flag;
    }
}
