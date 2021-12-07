package edu.neu.crm.settings;

import edu.neu.crm.utils.DateTimeUtil;
import org.junit.Test;


public class SettingsTest {

    @Test
    public void test01() {
        String expireTime = "2022-10-08 14:52:25";
        String currentTime = DateTimeUtil.getSysTime();
        System.out.println(currentTime.compareTo(expireTime));
    }

}
