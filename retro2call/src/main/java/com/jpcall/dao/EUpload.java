package com.jpcall.dao;

import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;

/**
 * Created by jp on 2016/12/26.
 * EUploadCallBack 简写成EUploadCB
 * 上传头像
 */

public class EUpload extends EStringCallBack {

    /**
     * 主要就是讲HashMap转化成上传类型的
     * @param h             相关字段
     * @param fileKey       图片的key
     * @param file          图片的文件
     * @return
     */
    public static Map<String, RequestBody> initBodys(HashMap<String,String> h,String fileKey, File file){
        RequestBody photo = RequestBody.create(MediaType.parse("image/png"), file);
        Map<String, RequestBody> hash = new HashMap<>();
        if (file.exists()) {
            hash.put(fileKey + "\"; filename=\"icon.png", photo);
//            hash.put("elifeImg\"; filename=\"icon.png", photo);
        }
        Set<String> strings = h.keySet();
        for (String key : strings) {
            String s = h.get(key);
            hash.put(key, RequestBody.create(null, s));
        }
        return hash;
    }

    private Object obj;

    public EUpload(Object object) {
        super(object);
        this.obj = object;
    }

    /**
     * @param call
     * @param back 如果不需要自己请求完接口后操作的话，可以传入null，默认完成后会Toast 上传成功
     */
    public void enqueue(Call call, ECallBack back) {
        if (back != null) {
            call.enqueue(back);
        } else {
            call.enqueue(this);
        }
    }

    @Override
    public void typeString(JSONObject ydInfo) throws JSONException {
        Toast.makeText(mContext, "上传成功", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void eFailure(String failure) {
        super.eFailure(failure);
    }
}
