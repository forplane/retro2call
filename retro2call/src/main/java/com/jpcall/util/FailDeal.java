package com.jpcall.util;

import com.jpcall.dao.ECallBack;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jon on 2016/10/14.<br/>
 */

public class FailDeal {

    private FailDeal() {
    }

    private DealHandler build(ECallBack callBack, String msg) {
        DealHandler jsonHandler = new JsonHandler(callBack, msg);
        return jsonHandler;
    }

    public static DealHandler obj(ECallBack callBack, String msg) {
        return new FailDeal().build(callBack, msg);
    }


    public abstract class DealHandler {
        protected String msg;
        protected ECallBack callBack;

        public DealHandler(ECallBack callBack, String msg) {
            this.callBack = callBack;
            this.msg = msg;
        }

        //下一个需要处理的对象
        protected DealHandler next;

        //是否已经处理了这次失败结果
        public abstract boolean Deal();
    }

    class JsonHandler extends DealHandler {


        public JsonHandler(ECallBack callBack, String msg) {
            super(callBack, msg);
        }

        @Override
        public boolean Deal() {
            //表示gson解析出现问题的
            Pattern pattern = Pattern.compile(".*at line.*column.*");
            Matcher matcher = pattern.matcher(msg);
            boolean deal = matcher.matches();
            if (deal) {
                callBack.getYdInfo().setYdMsg("暂无数据，解析出错");
                callBack.dealNoData(true);
                return true;
            } else {
                //只有当需要的时候，此时才new，而不是预先new好
                next = new NoNetHandler(callBack, msg);
                return next.Deal();
            }
        }
    }

    class NoNetHandler extends DealHandler {

        public NoNetHandler(ECallBack callBack, String msg) {
            super(callBack, msg);
        }

        @Override
        public boolean Deal() {
            callBack.showError();//仁佳决定无论是哪一方面的问题，都显示这个网络出现错误的图片，但是Toast不一样
            boolean networkAvailable = LoadOperate.isNetworkAvailable(callBack.getmContext());
            //没有连接wifi
            if (!networkAvailable) {
                String msg = "请连接网络";
                callBack.eFailure(msg);
                return true;
            } else {
                //有链接wifi，但是服务器有问题，或者服务器没启动，php，nginx等一起服务器相关的问题
                if (msg.contains("failed to connect to")
                        || msg.contains("No address associated with hostname")) {
                    String msg = "网络异常，请检查网络";
                    callBack.eFailure(msg);
                    return true;
                }
                return false;
            }
        }
    }


}