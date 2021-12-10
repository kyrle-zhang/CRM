package edu.neu.crm.workbench.service;


import edu.neu.crm.exception.DeleteActivityException;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Activity;

import java.util.Map;

public interface ActivityService {

    Boolean saveActivity(Activity activity);

    PaginationVO<Activity> getActivity(Map<String, Object> searchParameters);

    Boolean deleteActivity(String[] ids) throws DeleteActivityException;
}
