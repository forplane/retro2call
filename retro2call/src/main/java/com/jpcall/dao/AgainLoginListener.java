package com.jpcall.dao;

import android.content.Context;

/**
 * Created by jon on 2016/9/27.<br/>
 */

/**
 * 没有登陆的接口，需要再次登录
 */
public interface AgainLoginListener {
    void doLogin(Context mContext);
    void doLoginCall();
}
