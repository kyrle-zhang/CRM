package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.Tran;
import java.util.List;
import java.util.Map;

public interface TranDao {

    Integer save(Tran tran);

    Integer getTotalNum(Map<String, Object> map);

    List<Tran> getTran(Map<String, Object> map);

    Tran detail(String id);

    Integer changeTranStage(Tran tran);

    List<Map<String,Integer>> getChartData();
}
