package com.jpcall.dao;

import android.content.Context;

import com.jpcall.bean.CommonJson4List;
import com.jpcall.bean.YdInfo;
import com.jpcall.util.LoadOperate;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by john on 16-7-20.<br/>
 */

public abstract class EListsCallBack extends ECallBack<YdInfo> implements ECallTypeListener {

    private HashMap<String, Class> hashMap;

    public EListsCallBack(LoadOperate load, HashMap<String, Class> hashMap) {
        super(load);
        this.hashMap = hashMap;
    }

    public EListsCallBack(Context mContext, HashMap<String, Class> hashMap) {
        super(mContext);
        this.hashMap = hashMap;
    }

    public EListsCallBack(OnOpeListener opeListener, HashMap<String, Class> hashMap) {
        super(opeListener);
        this.hashMap = hashMap;
    }

    @Override
    protected void typeSomeThing(String bodyString) {
        if (hashMap != null) {
            HashMap<String, List> hashValues = new HashMap<>();
            try {
                JSONObject jsonObject = new JSONObject(bodyString);
                Set<String> strings = hashMap.keySet();
                for (String beanKey : strings) {
                    Class aClass = hashMap.get(beanKey);
                    String keyValue = jsonObject.getString(beanKey);
                    CommonJson4List<?> jsonList = CommonJson4List.fromJson(keyValue, aClass);
                    List data = jsonList.getData();
                    hashValues.put(beanKey, data);
                }
            } catch (Exception e) {
                e.printStackTrace();
                eFailure(e.getMessage());
            }
            if (hashValues.size() > 0)
                typeManyList(hashValues);
        }
    }


    @Override
    public void typeString(JSONObject ydInfo) {

    }

    @Override
    public void typeBean(Object o) {

    }

    @Override
    public void typeListBean(List list) {

    }

}
