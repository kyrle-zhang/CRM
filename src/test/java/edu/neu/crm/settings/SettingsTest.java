package edu.neu.crm.settings;

import edu.neu.crm.utils.DateTimeUtil;
import org.junit.Test;


public class SettingsTest {

    @Test
    public void test01() {
        String currentTime = DateTimeUtil.getSysTime();
        System.out.println(currentTime);
    }

}
