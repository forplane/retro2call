package com.example.call;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.jpcall.util.LoadOperate;

public class MainActivity extends Activity {

    private ViewGroup mView;
    private LoadOperate load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mView = (ViewGroup) findViewById(R.id.layout);
        findViewById(R.id.txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("", "");
            }
        });
        load=new LoadOperate.Builder(this,mView).build();
        load.showLoading();
//        new EBeanCallBack<UserBean>(new UserBean(),UserBean.class){
//            @Override
//            public void typeBean(UserBean userBean) {
//
//            }
//        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("", "");
    }
}
