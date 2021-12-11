package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.Activity;
import java.util.List;
import java.util.Map;


public interface ActivityDAO {

    Integer saveActivity(Activity activity);

    Integer getTotalByCondition(Map<String, Object> searchParameters);

    List<Activity> getActivityListByCondition(Map<String, Object> searchParameters);

    Integer getByIds(String[] ids);

    Integer deleteById(String[] ids);

    Activity getById(String id);

    Integer update(Activity activity);
}
