package com.jpcall.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jon on 2016/9/9.
 * php返回的参数配置
 */

public class YdInfoConfig {

    private static List<String> listCodes = new ArrayList<>();


    public static String code_100000 = "100000";
    public static String code_100001 = "100001";
    public static String code_100004 = "100004";//没有相关数据的情况
    public static String code_100101 = "100101";//e生活重新登录的（另一台设备登录）


    /**
     *
     * @param _code_100000 成功有数据的情况
     * @param _code_100001
     * @param _code_100004 没有相关数据的情况
     * @param _code_100101 另一台设备登录
     */
    public static void setCode(String _code_100000, String _code_100001, String _code_100004, String _code_100101) {
        if (_code_100000 != null) {
            code_100000 = _code_100000;
        }
        if (_code_100001 != null) {
            code_100001 = _code_100001;
        }
        if (_code_100004 != null) {
            code_100004 = _code_100004;
        }
        if (_code_100101 != null) {
            code_100101 = _code_100101;
        }



    }


    public static void addCode(String code) {
        listCodes.add(code);//listCodes的作用就是用来处理如果突然增加一些特别的返回值处理的时候起作用
    }
}
