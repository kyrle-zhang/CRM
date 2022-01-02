package edu.neu.crm.workbench.service.impl;

import edu.neu.crm.exception.SaveClueException;
import edu.neu.crm.utils.SqlSessionUtil;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.dao.ClueActivityRelationDao;
import edu.neu.crm.workbench.dao.ClueDao;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.Clue;
import edu.neu.crm.workbench.domain.ClueActivityRelation;
import edu.neu.crm.workbench.service.ClueService;

import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService{

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    @Override
    public Boolean save(Clue clue) throws SaveClueException {

        Boolean flag = false;
        Integer count = clueDao.save(clue);
        if (count == 1){
            flag = true;
        }else {
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
        for(ClueActivityRelation clueActivityRelation : clueActivityRelationList){
            count1 = clueActivityRelationDao.save(clueActivityRelation);
            if(count1 == 1){
                count += 1;
            }
        }
        if(count == clueActivityRelationList.size()){
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
        if(count == 1){
            flag = true;
        }
        return flag;
    }

}
