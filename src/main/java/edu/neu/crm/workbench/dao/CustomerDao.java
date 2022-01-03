package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getByName(String company);

    Integer save(Customer customer);

    List<String> getCustomerName(String name);
}
