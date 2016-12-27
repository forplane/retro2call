package com.jpcall.dao;

import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


/**
 * Created by john on 16-7-25.
 * Bean请求
 */

public abstract class EBeanCallBack<T> extends EIBeanCallBack<T> {


    public EBeanCallBack(Object object, Class<T> t) {
        super(object, t);
    }

    @Override
    protected void typeSomeThing(String bodyString) {
        Gson g = new Gson();
        T bean = g.fromJson(bodyString, beanCls);
        typeBean(bean);
    }

    @Override
    public void typeString(JSONObject ydInfo) {

    }

    @Override
    public void typeListBean(List<T> list) {

    }

    @Override
    public void typeManyList(HashMap<String, List> hash) {

    }

    @Override
    public void dealNoData(boolean first) {
        if (first) {
            //显示没有数据图片
            super.dealNoData(first);
        }
        if (isToastNoData())
            Toast.makeText(mContext, ydInfo.getYdMsg(), Toast.LENGTH_SHORT).show();
    }
}
