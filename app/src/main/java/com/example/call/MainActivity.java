package com.example.call;

import android.app.Activity;
import android.os.Bundle;

import com.example.call.bean.UserBean;
import com.jpcall.dao.EBeanCallBack;
import com.jpcall.util.LoadOperate;

import org.json.JSONObject;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new EBeanCallBack<UserBean>(new UserBean(),UserBean.class){
            @Override
            public void typeBean(UserBean userBean) {

            }
        };
    }
}
