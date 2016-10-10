package com.jpcall.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.jpcall.bean.YdInfo;
import com.jpcall.util.LoadOperate;
import com.jpcall.util.YdInfoConfig;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by john on 16-7-19.<br/>
 */

public abstract class ECallBack<T> implements Callback<YdInfo>, OnLoadListener {
    protected HashMap<String, String> params = new HashMap<>();
    protected YdInfo ydInfo;

    protected Context mContext;
    protected LoadOperate load;
    private Call<YdInfo> call;

    //v2.0版本的-----

    //提供2.0版本的对应的操作
    private OnOpeListener opeListener;
    //v2.0版本的-----


    //2.0
    @Deprecated
    /**
     *统一使用public ECallBack(OnOpeListener opeListener)
     */
    public ECallBack(LoadOperate load) {
        this.mContext = load.getContext();
        this.load = load;
        this.load.setOnLoadListener(this);
    }

    //2.0
    public ECallBack(OnOpeListener opeListener) {
        this.opeListener = opeListener;
        this.opeListener.setOnLoadListener(this);
        this.mContext = opeListener.getOpeContext();
    }
    //2.0
    public ECallBack(Context mContext) {
        this.mContext = mContext;
    }


    public void eFailure(String failure) {
        if (failure != null) {
            Log.i("ECallBack", failure);
            Toast.makeText(this.mContext, failure, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResponse(Call<YdInfo> call, Response<YdInfo> response) {
        this.call = call.clone();
        //2.0
        dismiss();

        //2.0
        getAllParams(response.raw());
        int code = response.code();
        if (code != 200) {
            eFailure(response.message());//这个是告知错误了，不具体处理，就跟ydMsg中的提示一样
            //这里就要责任链模式处理具体
            return;
        }

        ydInfo = response.body();
        //这里打算用责任链模式
        boolean isAllow = false;
        if (isAllow) {
            return;
        }

        if (ydInfo.getYdCode().equals(YdInfoConfig.code_100101)) {
            if (mContext != null) {
                initDoLogin();
            }
            return;
        }
        if (ydInfo.getYdCode().equals(YdInfoConfig.code_100004)) {
            dealNoData(true);
            return;
        }

        if (!ydInfo.getYdCode().equals(YdInfoConfig.code_100000)) {
            if (mContext != null) {
                eFailure(ydInfo.getYdMsg());
            }
            return;
        }
        typeSomeThing(ydInfo.getYdBody().toString());

    }

    //不能重写
    @Override
    public final void onFailure(Call<YdInfo> call, Throwable t) {
        this.call = call.clone();
        call.cancel();
        showError();
        eFailure(t.getMessage());
//        RequestBody body = call.request().body();

    }

    private void getAllParams(okhttp3.Response response) {

        RequestBody body = response.request().body();
        if (body instanceof FormBody) {
            FormBody formBody = (FormBody) body;
            for (int a = 0; a < formBody.size(); a++) {
                String k = formBody.encodedName(a);
                String v = formBody.encodedValue(a);
                params.put("request_" + k, v);
            }
        }
        Headers headers = response.request().headers();
        for (int a = 0; a < headers.size(); a++) {
            String k = headers.name(a);
            String v = headers.get(k);
            params.put("header" + k, v);

        }
    }

    /**
     * 具体处理数据的抽象方法
     */
    protected abstract void typeSomeThing(String bodyString);

    /**
     * 具体处理没有数据的情况,是否首次请求
     */
    protected void dealNoData(boolean first) {
        if (first && (load != null || opeListener != null)) {
            if (load != null) {
                load.showNoData();
            }else if(opeListener!=null){
                opeListener.showNoData();
            }
        }
        Toast.makeText(mContext, ydInfo.getYdMsg(), Toast.LENGTH_SHORT).show();
    }

    //主要是处理返回您已在另外台设备登录的情况
    private void initDoLogin() {
        //这里过渡到3.0版本，需要删掉第一个if
        if (mContext.getApplicationContext() instanceof AgainLoginListener) {
            AgainLoginListener listener = (AgainLoginListener) mContext.getApplicationContext();
            listener.doLogin(mContext);
        } else if (mContext.getApplicationContext() instanceof com.jpcall.dao.AgainLoginListener) {
            com.jpcall.dao.AgainLoginListener listener = (com.jpcall.dao.AgainLoginListener) mContext.getApplicationContext();
            listener.doLogin(mContext);
        }
    }

    @Deprecated
    /**
     * 已抛弃使用，请使用com.jpcall.dao.AgainLoginListener
     */
    public interface AgainLoginListener {
        void doLogin(Context mContext);
    }


    //2.0

    @Override
    public void dismiss() {
        if (load != null)
            load.dismiss();
        else if (opeListener != null) {
            opeListener.dismiss();
        }
    }

    @Override
    public void showError() {
        /*
        * 1：请求超时：failed to connect to /192.168.0.196 (port 80) after 15000ms
        * 2：nginx关闭 Failed to connect to /192.168.0.196:80
        * */
        if (load != null) {
            load.showError();
        } else if (opeListener != null) {
            opeListener.showError();
        }
    }


    @Override
    public void loading() {

    }


    @Override
    public void noData() {

    }

    @Override
    public void noNet() {
        if (LoadOperate.isNetworkAvailable(mContext)) {
            if (load != null) {
                load.showLoading();
            } else if (opeListener != null) {
                opeListener.showLoading();
            }
            call.enqueue(this);
        } else {
            Toast.makeText(mContext, "联网了吗?", Toast.LENGTH_SHORT).show();
        }

    }
}