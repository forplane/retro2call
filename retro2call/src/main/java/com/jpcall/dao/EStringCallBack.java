package com.jpcall.dao;


import android.widget.Toast;

import com.jpcall.bean.YdInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by john on 16-7-20.
 */

public abstract class EStringCallBack extends ECallBack<YdInfo> implements ECallTypeListener {
    public EStringCallBack(Object object) {
        super(object);
    }


//    public EStringCallBack(LoadOperate load) {
//        super(load);
//    }
//
//    public EStringCallBack(Context mContext) {
//        super(mContext);
//    }
//
//    public EStringCallBack(OnOpeListener opeListener) {
//        super(opeListener);
//    }

    @Override
    protected void typeSomeThing(String bodyString) {
        try {
            JSONObject jsonObject = new JSONObject(bodyString);
            typeString(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
            eFailure(e.getMessage());
        }
    }


    @Override
    public abstract void typeString(JSONObject ydInfo) throws JSONException;

    @Override
    public void typeBean(Object t) {

    }

    @Override
    public void typeListBean(List list) {

    }

    @Override
    public void typeManyList(HashMap hash) {

    }

    @Override
    public void dealNoData(boolean first) {
        if (first) {
            //显示没有数据图片
            super.dealNoData(first);
        }
        Toast.makeText(mContext, ydInfo.getYdMsg(), Toast.LENGTH_SHORT).show();
    }
}
