package com.jpcall.dao;

import android.content.Context;

/**
 * Created by jon on 2016/9/27.<br/>
 */

/**
 * 没有登陆的接口，需要再次登录，作用于当接口返回102（代表重复登陆）的时候需要执行的回调接口
 */
public interface AgainLoginListener {
    /**
     * 由ECallBack中调用，当判断是102的时候，就会执行这个方法
     */
    void doLogin(Context mContext);

    /**
     * 当登陆成功后，通过回调执行此方法
     */
    void doLoginCall();
}
