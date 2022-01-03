package edu.neu.crm.workbench.service.impl;

import edu.neu.crm.exception.SaveClueException;
import edu.neu.crm.utils.DateTimeUtil;
import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.utils.UUIDUtil;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.dao.*;
import edu.neu.crm.workbench.domain.*;
import edu.neu.crm.workbench.service.ClueService;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

    //线索相关表
    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //交易相关表
    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    //客户相关表
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //联系人相关表
    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    @Override
    public Boolean save(Clue clue) throws SaveClueException {

        Boolean flag = false;
        Integer count = clueDao.save(clue);
        if (count == 1) {
            flag = true;
        } else {
            throw new SaveClueException("保存线索失败");
        }

        return flag;
    }

    @Override
    public PaginationVO<Clue> get(Map<String, Object> searchParameters) {

        Integer total = clueDao.getTotalNum();
        List<Clue> clueList = clueDao.getClueListByPage(searchParameters);
        PaginationVO<Clue> paginationVO = new PaginationVO<>();
        paginationVO.setTotal(total);
        paginationVO.setDataList(clueList);
        return paginationVO;
    }

    @Override
    public Clue getById(String id) {

        Clue clue = clueDao.getById(id);
        return clue;
    }

    @Override
    public Boolean saveClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList) {

        Boolean flag = false;
        Integer count = 0;
        Integer count1 = 0;
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            count1 = clueActivityRelationDao.save(clueActivityRelation);
            if (count1 == 1) {
                count += 1;
            }
        }
        if (count == clueActivityRelationList.size()) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> activityList = clueActivityRelationDao.getActivityListByClueId(clueId);
        return activityList;
    }

    @Override
    public Boolean deleteBundleById(String id) {

        Boolean flag = false;
        Integer count = clueActivityRelationDao.deleteBundleById(id);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean convert(String clueId, Tran tran, String createBy) {

        Boolean flag = true;
        //1.根据线索Id查询线索信息
        Clue clue = clueDao.getOwnerIdByClueId(clueId);

        //2.判断客户表中是否已经存该公司信息
        Customer customer = customerDao.getByName(clue.getCompany());
        if (customer == null) {
            //保存客户信息
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setContactSummary(clue.getContactSummary());
            customer.setDescription(clue.getDescription());
            customer.setCreateBy(createBy);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setOwner(clue.getOwner());
            customer.setPhone(clue.getPhone());
            customer.setWebsite(clue.getWebsite());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(clue.getCompany());
            Integer count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }
        //3.保存联系人信息
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setAddress(clue.getAddress());
        contacts.setAppellation(clue.getAppellation());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getSysTime());
        contacts.setDescription(clue.getDescription());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setEmail(clue.getEmail());
        contacts.setSource(clue.getSource());
        contacts.setOwner(clue.getOwner());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setCustomerId(customer.getId());
        Integer count2 = contactsDao.save(contacts);
        if (count2 != 1) {
            flag = false;
        }

        //4.将线索备注转换到客户备注及联系人备注表中
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkList(clueId);
        ContactsRemark contactsRemark = new ContactsRemark();
        CustomerRemark customerRemark = new CustomerRemark();
        for(ClueRemark clueRemark:clueRemarkList){
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getSysTime());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            Integer count3 = contactsRemarkDao.save(contactsRemark);
            if (count3 != 1) {
                flag = false;
            }
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(DateTimeUtil.getSysTime());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            Integer count4 = customerRemarkDao.save(customerRemark);
            if (count4 != 1) {
                flag = false;
            }
        }

        //5.将线索市场活动关系保存到联系人市场活动中
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getByClueId(clueId);
        ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
        for(ClueActivityRelation clueActivityRelation: clueActivityRelationList){
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            Integer count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1) {
                flag = false;
            }
        }


        //6.创建交易
        if (tran != null) {
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            Integer countTran = tranDao.save(tran);
            if (countTran != 1) {
                flag = false;
            }
            //7.创建交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getSysTime());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setStage(tran.getStage());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            Integer count6 = tranHistoryDao.save(tranHistory);
            if (count6 != 1) {
                flag = false;
            }
        }
        //8.删除线索备注
        Integer count7 = clueRemarkDao.deleteRemarkByClueId(clueId);
        if(clueRemarkList.size() != count7){
            flag = false;
        }
        //9.删除线索和市场活动的关系
        Integer count8 = clueActivityRelationDao.deleteBundleByClueId(clueId);
        if(clueActivityRelationList.size() != count8){
            flag = false;
        }
        //10.删除线索
        Integer count9 = clueDao.delete(clueId);
        if (count9 != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Boolean saveRemark(ClueRemark clueRemark) {

        Boolean flag = false;
        Integer count = clueRemarkDao.save(clueRemark);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

    @Override
    public List<ClueRemark> getRemarkList(String clueId) {

        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkList(clueId);
        return clueRemarkList;
    }

    @Override
    public Boolean deleteRemark(String id)  {

        Boolean flag = false;
        Integer count = clueRemarkDao.deleteRemark(id);
        if (count == 1) {
            flag = true;
        }
        return flag;
    }

}
