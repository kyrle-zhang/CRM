package edu.neu.crm.workbench.service;

import edu.neu.crm.exception.SaveClueException;
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.Clue;
import edu.neu.crm.workbench.domain.ClueActivityRelation;

import java.util.List;
import java.util.Map;

public interface ClueService {

    Boolean save(Clue clue) throws SaveClueException;

    PaginationVO<Clue> get(Map<String, Object> searchParameters);

    Clue getById(String id);

    Boolean saveClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList);

    List<Activity> getActivityListByClueId(String clueId);

    Boolean deleteBundleById(String id);
}
