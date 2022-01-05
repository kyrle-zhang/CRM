package edu.neu.crm.workbench.service;

<<<<<<< HEAD
import edu.neu.crm.vo.PaginationVO;
import edu.neu.crm.workbench.domain.Tran;
import edu.neu.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    Boolean save(Tran tran, String customerName);

    PaginationVO<Tran> getTran(Map<String, Object> map);

    Tran detail(String id);

    List<TranHistory> getTranHistory(String tranId);

    Boolean changeTranStage(Tran tran);

    Map<String, List<Object>> getChartData();
=======
public interface TranService {
>>>>>>> CRM/master
}
