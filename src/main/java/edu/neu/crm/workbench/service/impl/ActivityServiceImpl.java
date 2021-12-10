package edu.neu.crm.workbench.service.impl;


import edu.neu.crm.exception.DeleteActivityException;
import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.dao.ActivityDAO;
import edu.neu.crm.workbench.dao.ActivityRemarkDAO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.service.ActivityService;

import java.util.List;
import java.util.Map;


public class ActivityServiceImpl implements ActivityService{

    private ActivityDAO activityDAO = SqlSessionUtil.getSqlSession().getMapper(ActivityDAO.class);
    private ActivityRemarkDAO activityRemarkDAO = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDAO.class);


    @Override
    public Boolean saveActivity(Activity activity) {

        Boolean flag = true;

        Integer count = activityDAO.saveActivity(activity);

        if(count!=1){
            flag = false;
        }

        return flag;
    }

    @Override
    public PaginationVO<Activity> getActivity(Map<String, Object> searchParameters) {

        //1.获取查询总条数
        Integer total = activityDAO.getTotalByCondition(searchParameters);

        //2.获取数据记录
        List<Activity> activityList = activityDAO.getActivityListByCondition(searchParameters);

        //3.将查询总条数和查询数据封装到VO中
        PaginationVO<Activity> activityPaginationVO = new PaginationVO<>();
        activityPaginationVO.setTotal(total);
        activityPaginationVO.setDataList(activityList);

        return activityPaginationVO;
    }

    @Override
    public Boolean deleteActivity(String[] ids) throws DeleteActivityException {

        Boolean flag = true;
        //首先应该删除市场活动关联的所有备注信息
        Integer countInDataR = activityRemarkDAO.getByActivityId(ids);
        Integer countByDeleteR = activityRemarkDAO.deleteByActivityId(ids);
        //如果应该删除的记录条数和实际删除的记录条数不相同，就表示删除失败，回滚事务
        if(countInDataR != countByDeleteR){
            flag = false;
            throw new DeleteActivityException("删除市场活动备注信息失败");
        }else {
            //再删除市场活动表中的记录
            Integer countInDataA = activityDAO.getById(ids);
            Integer countByDeleteA = activityDAO.deleteById(ids);
            if(countInDataA != countByDeleteA){
                flag = false;
                throw new DeleteActivityException("删除市场活动失败");
            }
        }

        return flag;
    }
}
