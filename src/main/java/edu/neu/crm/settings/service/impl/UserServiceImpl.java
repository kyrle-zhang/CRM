package edu.neu.crm.settings.service.impl;

import edu.neu.crm.settings.dao.UserDAO;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.utils.SqlSessionUtil;

public class UserServiceImpl implements UserService {

    //获取DAO层的对象，并将其作为成员变量
    private UserDAO userDAO = SqlSessionUtil.getSqlSession().getMapper(UserDAO.class);
}
