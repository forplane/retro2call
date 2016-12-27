package com.jpcall.dao;

/**
 * Created by planes on 2016/8/29.
 *
 */

public abstract class EIBeanCallBack<T> extends ECallBack<T> implements ECallTypeListener<T>{

    protected Class<T> beanCls;

    public EIBeanCallBack(Object object,Class<T> t) {
        super(object);
        this.beanCls=t;
    }
}
