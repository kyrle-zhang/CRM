package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    Integer save(ClueRemark clueRemark);

    List<ClueRemark> getRemarkList(String clueId);

    Integer deleteRemark(String id);

    Integer deleteRemarkByClueId(String clueId);
}
