package edu.neu.crm.workbench.dao;

public interface ActivityRemarkDAO {

    Integer getByActivityId(String[] ids);

    Integer deleteByActivityId(String[] ids);
}
