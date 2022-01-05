package edu.neu.crm.workbench.service.impl;

import edu.neu.crm.utils.DateTimeUtil;
import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.utils.UUIDUtil;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.dao.CustomerDao;
import edu.neu.crm.workbench.dao.TranDao;
import edu.neu.crm.workbench.dao.TranHistoryDao;
import edu.neu.crm.workbench.domain.Customer;
import edu.neu.crm.workbench.domain.Tran;
import edu.neu.crm.workbench.domain.TranHistory;
import edu.neu.crm.workbench.service.TranService;

import java.util.*;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public Boolean save(Tran tran, String customerName) {
        /**
         * 添加交易信息的步骤
         *  1.首先判断用户提交的交易表单中的客户是否已经存在，如果存在，就将其Id取出，保存到交易表中
         *    如果不存在，就在客户表中保存这个客户的记录，并将其Id保存到交易表中
         *  2.保存交易表
         *  3.保存交易记录表
         */
        Boolean flag = true;
        Customer customer = customerDao.getByName(customerName);
        if (customer == null) {
            //没有该客户的记录
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(tran.getCreateBy());
            Integer count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }
        tran.setCustomerId(customer.getId());
        Integer count2 = tranDao.save(tran);
        if (count2 != 1) {
            flag = false;
        }
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        Integer count3 = tranHistoryDao.save(tranHistory);
        if (count3 != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Tran> getTran(Map<String, Object> map) {

        PaginationVO<Tran> paginationVO = new PaginationVO<>();
        Integer total = tranDao.getTotalNum(map);
        List<Tran> tranList = tranDao.getTran(map);
        paginationVO.setTotal(total);
        paginationVO.setDataList(tranList);
        return paginationVO;
    }

    @Override
    public Tran detail(String id) {

        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getTranHistory(String tranId) {

        List<TranHistory> tranHistoryList = tranHistoryDao.getTranHistory(tranId);
        return tranHistoryList;
    }

    @Override
    public Boolean changeTranStage(Tran tran) {

        Boolean flag = true;
        //1.先改变交易表记录的阶段
        Integer count1 = tranDao.changeTranStage(tran);
        if (count1 != 1) {
            flag = false;
        }
        //2.增加一条交易历史记录
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        Integer count2 = tranHistoryDao.save(tranHistory);
        if(count2 != 1){
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, List<Object>> getChartData() {
        Map<String,List<Object>> result = new HashMap<>();
        List<Object> stageList = new ArrayList<>();
        List<Object> numList = new ArrayList<>();

        List<Map<String, Integer>> mapList = tranDao.getChartData();
        for(Map map:mapList){
            stageList.add(map.get("stage"));
            numList.add(map.get("num"));
        }
        result.put("stageList",stageList);
        result.put("numList",numList);
        return result;
    }
}
