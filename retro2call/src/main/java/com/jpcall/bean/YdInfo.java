package com.jpcall.bean;

import com.google.gson.JsonObject;


/**
 * Created by john on 16-7-19.
 */

public class YdInfo<T> {
    private String ydCode;
    private String ydMsg;
    private JsonObject ydBody;

    public String getYdCode() {
        return ydCode;
    }

    public void setYdCode(String ydCode) {
        this.ydCode = ydCode;
    }

    public String getYdMsg() {
        return ydMsg;
    }

    public void setYdMsg(String ydMsg) {
        this.ydMsg = ydMsg;
    }

    public JsonObject getYdBody() {
        return ydBody;
    }

    public void setYdBody(JsonObject ydBody) {
        this.ydBody = ydBody;
    }
}

