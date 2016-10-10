package com.jpcall.dao;

/**
 * Created by jon on 2016/9/27.<br/>
 */

import android.content.Context;

/**
 * 供Call操作的状态，比如完成，没网；然后回调给Operation的类
 */
public interface OnOpeListener {
    Context getOpeContext();
    void setOnLoadListener(OnLoadListener loadListener);

    //请求成功，然后dismiss掉
    void dismiss();

    //通知view请求出错
    void showError();

    //通知view显示loading
    void showLoading();

    //通知view请求结果没有数据
    void showNoData();
}
