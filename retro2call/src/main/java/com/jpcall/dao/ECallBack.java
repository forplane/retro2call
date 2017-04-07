package com.jpcall.dao;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.jpcall.bean.YdInfo;
import com.jpcall.util.AgainCallManager;
import com.jpcall.util.FailDeal;
import com.jpcall.util.FailLog;
import com.jpcall.util.YdInfoConfig;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by john on 16-7-19.
 *
 */

public abstract class ECallBack<T> implements Callback<YdInfo>, OnLoadListener, AgainLoginLoadListener {
    /**
     * 存放post请求的头部以及请求参数
     */
    protected HashMap<String, String> params = new HashMap<>();
    protected YdInfo ydInfo;

    protected Context mContext;
    private Call<YdInfo> call;
    private OnOpeListener opeListener;

    public YdInfo getYdInfo() {
        return ydInfo;
    }

    public Context getmContext() {
        return mContext;
    }

    public ECallBack(Object object) {
        if (object instanceof Context) {
            this.mContext = (Context) object;
        } else if (object instanceof OnOpeListener) {
            this.opeListener = (OnOpeListener) object;
            this.opeListener.setOnLoadListener(this);
            this.mContext = opeListener.getOpeContext();
        } else {
            throw new RuntimeException("非法操作，只能传入Context对象或者实现了OnOpeListener的对象");
        }

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
        dismiss();
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
                AgainCallManager.putCall(this);
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
        ydInfo = new YdInfo();//因为只要是走这里的失败，ydInfo必定为null，在这里弄一个
        //出来的目的就是供处理的来设置数据
        boolean deal = FailDeal.obj(this, t).Deal();
        //没有处理，统一Toast
        if (!deal) {
            eFailure("网络请求超时，请重试");
            showError();
            FailLog.instance.writeFailMsg(t, mContext);
        }
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
     * 具体处理没有数据的情况,是否首次请求，默认回调过去都是true
     * 如果自己需要处理没有数据的情况，重写后，然后选择是否super
     */
    public void dealNoData(boolean first) {
        if (first && opeListener != null) {
            opeListener.showNoData();
        }
    }

    /**
     * 是否要Toast没有数据，默认为true
     */
    public boolean isToastNoData() {
        return true;
    }

    //主要是处理返回您已在另外台设备登录的情况
    protected void initDoLogin() {
        //这里过渡到3.0版本，需要删掉第一个if
        if (mContext.getApplicationContext() instanceof AgainLoginListener) {
            AgainLoginListener listener = (AgainLoginListener) mContext.getApplicationContext();
            listener.doLogin(mContext);
        } else if (mContext.getApplicationContext() instanceof com.jpcall.dao.AgainLoginListener) {
            com.jpcall.dao.AgainLoginListener listener = (com.jpcall.dao.AgainLoginListener) mContext.getApplicationContext();
            listener.doLogin(mContext);
        }
    }

    @Override
    public void dismiss() {
        if (opeListener != null) {
            opeListener.dismiss();
        }
    }

    @Override
    public void showError() {
        /*
        * 1：请求超时：failed to connect to /192.168.0.196 (port 80) after 15000ms
        * 2：nginx关闭 Failed to connect to /192.168.0.196:80
        * */
        if (opeListener != null) {
            opeListener.showError();
        }
    }


    @Override
    public void loading() {
        if (isNetworkAvailable(mContext)) {
            if (opeListener != null) {
                opeListener.showLoading();
            }
            call.enqueue(this);
        } else {
            Toast.makeText(mContext, "联网了吗?", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void noData() {

    }

    @Override
    public void noNet() {


    }

    @Override
    public void againLoading() {
        this.call = call.clone();
        loading();
    }


    /**
     * 检查当前网络是否可用
     */
    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
