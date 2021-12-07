package edu.neu.crm.settings.service;

import edu.neu.crm.exception.LoginException;
import edu.neu.crm.settings.domain.User;

import java.util.List;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
