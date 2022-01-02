package edu.neu.crm.settings.service.impl;

import edu.neu.crm.settings.dao.DicTypeDAO;
import edu.neu.crm.settings.dao.DicValueDAO;
import edu.neu.crm.settings.domain.DicValue;
import edu.neu.crm.settings.service.DicService;
import edu.neu.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService{

    private DicTypeDAO dicTypeDAO = SqlSessionUtil.getSqlSession().getMapper(DicTypeDAO.class);
    private DicValueDAO dicValueDAO = SqlSessionUtil.getSqlSession().getMapper(DicValueDAO.class);


    @Override
    public Map<String, List> getAll() {

        //1.先查询数据字典所有的type
        List<String> typeList = dicTypeDAO.getAllType();

        //2.分别查询每个类型下的所有数据
        Map<String, List> map = new HashMap<>();
        for(String type:typeList){
            List<DicValue> dicList = dicValueDAO.getByTypeCode(type);
            map.put(type,dicList);
        }
        return map;
    }
}
