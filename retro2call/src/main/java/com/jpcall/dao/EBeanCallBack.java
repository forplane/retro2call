package com.jpcall.dao;

import android.content.Context;

import com.google.gson.Gson;
import com.jpcall.util.LoadOperate;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


/**
 * Created by john on 16-7-25.<br/>
 */

public abstract class EBeanCallBack<T> extends EIBeanCallBack<T>  {

    public EBeanCallBack(LoadOperate load, Class<T> t) {
        super(load,t);
    }
    public EBeanCallBack(Context mContext, Class<T> t) {
        super(mContext,t);
    }

    public EBeanCallBack(OnOpeListener opeListener, Class<T> t) {
        super(opeListener, t);
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




}
