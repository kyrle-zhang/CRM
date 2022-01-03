package edu.neu.crm.workbench.service;

import edu.neu.crm.exception.SaveClueException;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.*;

import java.util.List;
import java.util.Map;

public interface ClueService {

    Boolean save(Clue clue) throws SaveClueException;

    PaginationVO<Clue> get(Map<String, Object> searchParameters);

    Clue getById(String id);

    Boolean saveClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList);

    List<Activity> getActivityListByClueId(String clueId);

    Boolean deleteBundleById(String id);

    Boolean convert(String clueId, Tran tran, String createBy);

    Boolean saveRemark(ClueRemark clueRemark);

    List<ClueRemark> getRemarkList(String clueId);

    Boolean deleteRemark(String id);
}
