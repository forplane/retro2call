package com.jpcall.dao;

import android.content.Context;
import android.widget.Toast;

import com.jpcall.bean.CommonJson4List;
import com.jpcall.util.LoadOperate;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


/**
 * Created by john on 16-7-20.<br/>
 */

public abstract class EListCallBack<T> extends EIBeanCallBack<T> {
    private String beanKey;

    public EListCallBack(Object object, Class<T> t, String key) {
        super(object, t);
        this.beanKey = key;
    }


//    public EListCallBack(LoadOperate load, Class<T> t, String key) {
//        super(load, t);
//        this.beanKey = key;
//    }
//
//    public EListCallBack(Context mContext, Class<T> t, String key) {
//        super(mContext, t);
//        this.beanKey = key;
//    }
//
//    public EListCallBack(OnOpeListener opeListener, Class<T> t, String beanKey) {
//        super(opeListener, t);
//        this.beanKey = beanKey;
//    }

    @Override
    protected void typeSomeThing(String bodyString) {
        if (beanKey != null) {
            List<T> data = null;
            try {
                JSONObject jsonObject = new JSONObject(bodyString);
                String keyValue = jsonObject.getString(beanKey);
                CommonJson4List<?> jsonList = CommonJson4List.fromJson(keyValue, beanCls);
                data = (List<T>) jsonList.getData();
            } catch (Exception e) {
                e.printStackTrace();
                eFailure(e.getMessage());
            }
            if (data != null)
                typeListBean(data);
        }
    }


    @Override
    public void typeString(JSONObject ydInfo) {

    }

    @Override
    public void typeBean(T t) {

    }

    @Override
    public void typeManyList(HashMap<String, List> hash) {

    }


    @Override
    public void dealNoData(boolean first) {
        String page = this.params.get("page");
        if (page.equals("0")) {
            super.dealNoData(first);
        }else{
            Toast.makeText(mContext, this.ydInfo.getYdMsg(), Toast.LENGTH_SHORT).show();
        }
    }
}
