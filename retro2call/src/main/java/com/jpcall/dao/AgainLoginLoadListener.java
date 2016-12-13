package com.jpcall.dao;

/**
 * Created by jp on 2016/12/13.</br>
 */

public interface AgainLoginLoadListener {
    /**
     * 所有接口的请求是重复登录返回的，之后跳转到登录界面，此时登录成功后</br>
     * 返回到上一个界面，此时是需要重新请求一遍数据
     */
    void againLoading();
}
