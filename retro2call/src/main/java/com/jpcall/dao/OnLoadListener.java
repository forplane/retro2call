package com.jpcall.dao;

/**
 * Created by jon on 2016/9/27.
 *
 */

/**
 * 供view状态需要操作的事件，然后回调给Call
 */
public interface OnLoadListener {
    void dismiss();
    void loading();
    void showError();

    void noData();

    void noNet();

}
