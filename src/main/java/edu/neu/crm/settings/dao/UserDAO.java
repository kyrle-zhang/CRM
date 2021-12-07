package edu.neu.crm.settings.dao;

import edu.neu.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDAO {
    User login(Map<String, String> map);

    List<User> getUserList();
}
