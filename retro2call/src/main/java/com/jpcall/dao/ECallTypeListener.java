package com.jpcall.dao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by planes on 2016/8/14.<br/>
 * 不同请求Call的接口
 */

public interface ECallTypeListener<T> {
    /**一般字符串*/
    void typeString(JSONObject jo) throws JSONException;
    /**单个 bean*/
    void typeBean(T t);
    /**一个List*/
    void typeListBean(List<T> list);
    /**多个List*/
    void typeManyList(HashMap<String, List> hash);

}
