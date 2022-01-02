package edu.neu.crm.workbench.service;


import edu.neu.crm.exception.DeleteActivityException;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    Boolean saveActivity(Activity activity);

    PaginationVO<Activity> getActivity(Map<String, Object> searchParameters);

    Boolean deleteActivity(String[] ids) throws DeleteActivityException;

    Map<String, Object> getUserListAndActivity(String id);

    Boolean update(Activity activity);

    Activity detail(String id);

    Boolean saveRemark(ActivityRemark activityRemark);

    List<ActivityRemark> getRemark(String activityId);

    Boolean deleteRemark(String id);

    Boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityList(Map<String, Object> map);

    List<Activity> getActivityListByName(String name);
}
