package cn.top.util;



import org.apache.commons.beanutils.BeanUtils;

import java.util.Map;

public class Map2Bean {

    public static void transMap2Bean2(Map<String, Object> map, Object obj) {
        if (map == null || obj == null) {
            return;
        }
        try {
            BeanUtils.populate(obj, map);
        } catch (Exception e) {
            System.out.println("transMap2Bean2 Error " + e);
        }
    }
}
