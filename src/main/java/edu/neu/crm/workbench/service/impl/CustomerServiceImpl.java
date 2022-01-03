package edu.neu.crm.workbench.service.impl;

import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.workbench.dao.CustomerDao;
import edu.neu.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    @Override
    public List<String> getCustomerName(String name) {

        List<String> nameList = customerDao.getCustomerName(name);
        return nameList;
    }
}
