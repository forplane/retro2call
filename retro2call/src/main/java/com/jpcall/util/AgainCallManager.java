package com.jpcall.util;

import com.jpcall.dao.AgainLoginLoadListener;
import com.jpcall.dao.ECallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jp on 2016/12/13.
 * 重复/异地登陆管理操作类
 */

public class AgainCallManager {
    private static AgainCallManager manager=new AgainCallManager();
    private List<AgainLoginLoadListener> backList = new ArrayList<>();
    private AgainCallManager(){

    }

    public static void putCall(AgainLoginLoadListener call) {
        manager.backList.add(call);
    }

    public static void sendCall(){
        manager.send();
    }
    private void send(){
        for (AgainLoginLoadListener call : backList) {
            call.againLoading();
        }
        backList.clear();
    }

}
