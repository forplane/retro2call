package com.jpcall.dao;

import android.widget.Toast;

import com.jpcall.bean.CommonJson4List;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


/**
 * Created by john on 16-7-20.<br/>
 * 单个List
 */

public abstract class EListCallBack<T> extends EIBeanCallBack<T> {
    private String beanKey;

    public EListCallBack(Object object, Class<T> t, String key) {
        super(object, t);
        this.beanKey = key;
    }

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


    /**
     * 自动处理请求List列表数据，如果是第一页(first)请求，那么直接会显示没有数据的背景图<br/>
     * 如果不是，那么就自动从{@link #params}这里获取page（请求网络的时候参数必须为这个）<br/>
     * 以此page来判断后续操作
     */
    @Override
    public void dealNoData(boolean first) {
        if (!first) {
            //不是第一次的，所以直接Toast出来即可
            Toast.makeText(mContext, this.ydInfo.getYdMsg(), Toast.LENGTH_SHORT).show();
        } else {
            String page = this.params.get("request_page");
            if (page == null || page.equals("1")) {
                super.dealNoData(first);
            } else {
                Toast.makeText(mContext, this.ydInfo.getYdMsg(), Toast.LENGTH_SHORT).show();
            }
        }



    }
}
