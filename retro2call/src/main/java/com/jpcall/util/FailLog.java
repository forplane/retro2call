package com.jpcall.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jon on 2016/10/20.<br/>
 * 请求网络出未知错误的情况，把信息写在本地种，后期会上传到服务器
 */

public class FailLog {
    public static FailLog instance = new FailLog();
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Context appContext;

    private FailLog() {

    }

    public void writeFailMsg(Throwable t, Context ctx) {
        appContext = ctx.getApplicationContext();

        String msg = t.getMessage() == null ? t.toString() : t.getMessage();
        LogThread logThread = new LogThread(msg);
        executor.execute(logThread);
    }

    private class LogThread extends Thread {

        String msg;

        LogThread(String msg) {
            this.msg = msg;
        }

        @Override
        public void run() {
            String s = fileName();
            readFromFile(s, msg);
        }
    }

    private String fileName() {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return f.format(date);
    }


    private String currentTime() {
        SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return f.format(date);
    }


    private void readFromFile(String fileName, String msg) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            String name = appContext.getApplicationInfo().packageName;
            String foldername = Environment.getExternalStorageDirectory().getPath() + "/" + name;
            File folder = new File(foldername);
            if (folder == null || !folder.exists()) {
                folder.mkdir();
            }

            File targetFile = new File(foldername + "/" + fileName + ".txt");
            OutputStreamWriter osw;
            msg=currentTime()+"\n"+msg;
            try{
                if(!targetFile.exists()){
                    targetFile.createNewFile();
                    osw = new OutputStreamWriter(new FileOutputStream(targetFile),"utf-8");
                    osw.write(msg);
                    osw.close();
                }else{
                    osw = new OutputStreamWriter(new FileOutputStream(targetFile,true),"utf-8");
                    osw.write("\n\n\n"+msg);
                    osw.flush();
                    osw.close();
                }
            } catch (Exception e) {
            }
    }

}
}
