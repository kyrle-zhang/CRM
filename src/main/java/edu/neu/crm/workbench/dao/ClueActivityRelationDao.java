package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.Activity;
import edu.neu.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    Integer save(ClueActivityRelation clueActivityRelationList);

    List<Activity> getActivityListByClueId(String clueId);

    Integer deleteBundleById(String id);

    List<ClueActivityRelation> getByClueId(String clueId);

    Integer deleteBundleByClueId(String clueId);
}
