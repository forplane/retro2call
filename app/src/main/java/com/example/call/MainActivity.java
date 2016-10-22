package com.example.call;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.jpcall.bean.YdInfo;
import com.jpcall.util.FailLog;
import com.jpcall.util.LoadOperate;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

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
        load = new LoadOperate.Builder(this, mView).build();
        load.showLoading();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("", "");
        testCallType(BSuper.class);
    }


    //一下是测试服务器404 没有响应，死机的情况

    private final static String CALLTAG = "CALLTAG";

    public <T> void testCallType(final Class<T> t) {
        List<Call<YdInfo>> list = new ArrayList<>();
//        第一种，状态正常，输出ok(status=200) echo '';
//        http://192.168.0.196/test.php
        Call<YdInfo> test_php = TestApiConfig.createRetrofit("http://120.24.176.41/").create(TestApi.class).test_php();
        Call<YdInfo> test_php1 = TestApiConfig.createRetrofit("http://120.24.176.41/").create(TestApi.class).test_php();
        Call<YdInfo> test_php2 = TestApiConfig.createRetrofit("http://120.24.176.41/").create(TestApi.class).test_php();
//
////        第二种情况，php文件不存在，或已被删除(status=404)
////        http://192.168.0.196/test1.php
//        Call<ResponseBody> test1_php = TestApiConfig.createRetrofit("http://192.168.0.196/").create(TestApi.class).test1_php();
//
////        第三种情况：所访问的网页无内容，即为空(status=200)
////        http://192.168.0.196/test2.php
//        Call<ResponseBody> test2_php = TestApiConfig.createRetrofit("http://192.168.0.196/").create(TestApi.class).test2_php();
//
////        第四种情况，nginx服务停止(Failed to connect to /192.168.0.197:80)
////        http://192.168.0.197/test3.php
//        Call<ResponseBody> test3_php = TestApiConfig.createRetrofit("http://192.168.0.197/").create(TestApi.class).test3_php();
//
////        第五种情况，服务器死机(EHOSTUNREACH (No route to host) 跟服务器没网返回一样)
////        http://192.168.0.198/test4.php
//        Call<ResponseBody> test4_php = TestApiConfig.createRetrofit("http://192.168.0.198/").create(TestApi.class).test4_php();
//
////        第六种情况，php服务停止，nginx服务正常的(status=502)
////        http://192.168.0.196/test5.php
//        Call<ResponseBody> test5_php = TestApiConfig.createRetrofit("http://192.168.0.196/").create(TestApi.class).test5_php();
//
////        第七种情况，访问超时
////        http://192.168.0.196/test6.php
//        Call<ResponseBody> test6_php = TestApiConfig.createRetrofit("http://192.168.0.10/").create(TestApi.class).test6_php();
//

        //1：服务器开小差（php nginx）
        //2：NOT Found
        //3：平台维护


        list.add(test_php);
        list.add(test_php1);
        list.add(test_php2);
//        list.add(test1_php);
//        list.add(test2_php);
//        list.add(test3_php);
//        list.add(test4_php);
//        list.add(test5_php);
//        list.add(test6_php);

        for (int i = 0; i < list.size(); i++) {
            Call<YdInfo> call = list.get(i);
            final int num = i;
            call.enqueue(new Callback<YdInfo>() {
                @Override
                public void onResponse(Call<YdInfo> call, retrofit2.Response<YdInfo> response) {
                    List<T> mList = new ArrayList<T>();
                    try {
                        int code = response.code();
                        YdInfo body = response.body();
                        if (body != null) {
//                            byte[] bytes = body.bytes();
//                            String s = new String(bytes);
                            Log.i(CALLTAG, "onResponse" + num + "succes\t s=" + body.getYdBody());
                        } else {
                            ResponseBody errorBody = response.errorBody();
                            byte[] bytes = errorBody.bytes();
                            String s = new String(bytes);
                            Log.i(CALLTAG, "onResponse" + num + "succes\t s=" + s);
                        }


                        Gson gson = new Gson();
                        String mm = "[{\"name\": \"类型1\",\"type\": 1,\"one\": \"这个是类型1的内容\"},{\"name\": \"类型2\",\"type\": 2,\"two\": \"这个是类型2的内容\"}]";
                        JSONArray ar = new JSONArray(mm);
                        for (int i = 0; i < ar.length(); i++) {
                            String string = ar.getString(i);
                            //得到泛型T后，然后反射注解获取type类型
                            T bSuper = gson.fromJson(string, t);
                            int type = 0;//这里反射得到注解为类型的type值
                            Class[] classes = {OneB.class, TwoB.class};//这里反射注解得到关联的字bean.class
                            Class aClass = classes[type];//得到具体的子类class
                            T o = (T) gson.fromJson(string, aClass);
                            mList.add(o);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.i(CALLTAG, "onResponse" + num + "success");

                }

                @Override
                public void onFailure(Call<YdInfo> call, Throwable t) {
                    boolean networkAvailable = LoadOperate.isNetworkAvailable(MainActivity.this);

                    String message = t.getMessage();
                    //表示gson解析出现问题的
                    Pattern pattern = Pattern.compile(".*at line.*column.*");
                    Matcher matcher = pattern.matcher(message);
                    boolean b = matcher.matches();

                    if (message.contains("Failed to connect")) {

                    }
                    if (message == null) {

                    }
                    Log.i(CALLTAG, "onFailure" + num + message);
                    FailLog.instance.writeFailMsg(t, MainActivity.this);

                }
            });

//            gson解析不通过的情况
//            java.io.EOFException: End of input at line 1 column 49
//            Expected ':' at line 1 column 34 path $.
//            com.google.gson.stream.MalformedJsonException: Expected ':' at line 1 column 11 path $.ydCode


//            手机本身没有连接wifi(关掉wifi)
//            Failed to connect to /120.24.176.41:80

//            手机改了网关
//            java.net.SocketException: failed to connect to /120.24.176.41 (port 80) after 15000ms: isConnected failed: EHOSTUNREACH (No route to host)


//                    java.net.SocketException: socket failed: EACCES (Permission denied) 没有权限
//                    <uses-permission android:name="android.permission.INTERNET" />
        }


    }


    class BSuper {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


    }

    class OneB extends BSuper {
        String one;

        public String getOne() {
            return one;
        }

        public void setOne(String one) {
            this.one = one;
        }

    }

    class TwoB extends BSuper {
        String two;

        public String getTwo() {
            return two;
        }

        public void setTwo(String two) {
            this.two = two;
        }


    }


    private interface TestApi {
        @GET("retro.php")
        Call<YdInfo> test_php();

        @GET("test1.php")
        Call<ResponseBody> test1_php();

        @GET("test2.php")
        Call<ResponseBody> test2_php();

        @GET("test3.php")
        Call<ResponseBody> test3_php();

        @GET("test4.php")
        Call<ResponseBody> test4_php();


        @GET("test5.php")
        Call<ResponseBody> test5_php();

        @GET("test6.php")
        Call<ResponseBody> test6_php();
    }

    private static class TestApiConfig {
        public static Retrofit createRetrofit(String baseURL) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseURL)

                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
            return retrofit;
        }
    }
}
