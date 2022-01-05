package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    Integer save(TranHistory tranHistory);

    List<TranHistory> getTranHistory(String tranId);

}
