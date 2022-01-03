package edu.neu.crm.workbench.service.impl;

import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.workbench.dao.TranDao;
import edu.neu.crm.workbench.dao.TranHistoryDao;
import edu.neu.crm.workbench.service.TranService;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);



}
