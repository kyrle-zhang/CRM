package edu.neu.crm.settings.dao;

import edu.neu.crm.settings.domain.User;

import java.util.Map;

public interface UserDAO {
    User login(Map<String, String> map);
}
