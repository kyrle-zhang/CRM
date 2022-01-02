package edu.neu.crm.settings.dao;

import edu.neu.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDAO {
    List<DicValue> getByTypeCode(String type);
}
