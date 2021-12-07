package edu.neu.crm.settings.service.impl;

import edu.neu.crm.exception.LoginException;
import edu.neu.crm.settings.dao.UserDAO;
import edu.neu.crm.settings.domain.User;
import edu.neu.crm.settings.service.UserService;
import edu.neu.crm.utils.DateTimeUtil;
import edu.neu.crm.utils.SqlSessionUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    //获取DAO层的对象，并将其作为成员变量
    private UserDAO userDAO = SqlSessionUtil.getSqlSession().getMapper(UserDAO.class);

    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);

        //调用DAO层的login方法进行从数据库中验证用户的账号密码
        User user = userDAO.login(map);

        //如果从数据库中未查询到任何信息，则抛出账号密码异常
        if(user == null){

            throw new LoginException("账号密码不正确");
        }

        //如果程序执行到此处，说明账号密码正确
        //继续验证其它信息
        //1.验证账号是否失效
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(currentTime.compareTo(expireTime) > 0){
            throw new LoginException("your account is already expired");
        }

        //2.验证账号状态是否被锁定
        if("0".equals(user.getLockState())){
            throw new LoginException("your account is already locked");
        }

        //3.验证ip是否受限
        if(!user.getAllowIps().contains(ip)){
            throw new LoginException("your IP is not allowed");
        }

        return user;
    }

    @Override
    public List<User> getUserList() {

        List<User> userList = userDAO.getUserList();

        return userList;
    }
}
