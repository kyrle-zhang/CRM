package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.Clue;
import java.util.List;
import java.util.Map;

public interface ClueDao {

    Integer save(Clue clue);

    Integer getTotalNum();

    List<Clue> getClueListByPage(Map<String, Object> searchParameters);

    Clue getById(String id);
}
