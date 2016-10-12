package com.jpcall.dao;

import android.content.Context;

import com.jpcall.util.LoadOperate;

/**
 * Created by planes on 2016/8/29.
 */

public abstract class EIBeanCallBack<T> extends ECallBack<T> implements ECallTypeListener<T>{

    protected Class<T> beanCls;

    public EIBeanCallBack(Object object,Class<T> t) {
        super(object);
        this.beanCls=t;
    }

    //2.0
//    @Deprecated
    /**
     *统一使用OnOpeListenerg构造的
     */
//    public EIBeanCallBack(LoadOperate load,Class<T> t) {
//        super(load);
//        this.beanCls =t;
//    }
//    public EIBeanCallBack(Context mContext, Class<T> t) {
//        super(mContext);
//        this.beanCls =t;
//    }
//
//    public EIBeanCallBack(OnOpeListener opeListener, Class<T> t){
//        super(opeListener);
//        this.beanCls =t;
//    }
}
