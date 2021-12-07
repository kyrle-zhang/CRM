package edu.neu.crm.settings.service;

import edu.neu.crm.exception.LoginException;
import edu.neu.crm.settings.domain.User;

public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;
}
