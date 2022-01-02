package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.ActivityRemark;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityRemarkDAO {

    Integer getByActivityId(String[] ids);

    Integer deleteByActivityId(String[] ids);

    Integer saveRemark(ActivityRemark activityRemark);

    List<ActivityRemark> getRemark(String activityId);

    Integer deleteById(String id);

    Integer updateRemark(ActivityRemark activityRemark);
}
