package edu.neu.crm.workbench.dao;

import edu.neu.crm.workbench.domain.Tran;
<<<<<<< HEAD
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
=======
>>>>>>> CRM/master

public interface TranDao {

    Integer save(Tran tran);
<<<<<<< HEAD

    Integer getTotalNum(Map<String, Object> map);

    List<Tran> getTran(Map<String, Object> map);

    Tran detail(String id);

    Integer changeTranStage(Tran tran);

    List<Map<String,Integer>> getChartData();
=======
>>>>>>> CRM/master
}
