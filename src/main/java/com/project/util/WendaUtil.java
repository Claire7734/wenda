package com.project.util;

import com.alibaba.fastjson.JSONObject;
import com.project.aspect.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Claire on 12/4/17.
 */
public class WendaUtil {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    public static int ANONYMOUS_USERID = 1;
    public static int SYSTEM_USERID = 2;

    public static String INIT_HEAD = "/images/head.jpg";

    public static String getJSONString(int code) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        return json.toJSONString();
    }
}
