package com.tmall.wireless.tangram.example.util.http;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 1 on 2016/1/18.
 */
public class HttpU {

    private static HttpU mInstance;
    private ExecutorService executorService = Executors.newFixedThreadPool(6); // 固定五个线程来执行任务
    private final Handler handler;
    private OkHttpClient mOkHttpClient;

    private HttpU() {
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newBuilder().dispatcher(new Dispatcher(executorService)).readTimeout(30, TimeUnit.SECONDS)// 设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS)// 设置写的超时时间
                .connectTimeout(30, TimeUnit.SECONDS);// 设置连接超时时间  ;
        handler = new Handler(Looper.getMainLooper());
    }


    public static HttpU getInstance() {
        if (mInstance == null) {
            synchronized (HttpU.class) {
                if (mInstance == null) {
                    mInstance = new HttpU();
                }
            }
        }
        return mInstance;
    }


    /**
     * 网络请求方法
     *
     * @param context
     * @param url
     * @param params
     * @param tag
     * @param callback
     */
    public void post(final Context context, final String url, Map<String, Object> params, Object tag, final HttpCallback callback) {
        if (context==null){
            callback.onAfter();
            return;
        }
        Log.i("0.0","url:"+url);
        String reqmsg = "";
        //token校验参数
        if (params == null) {
            params = new HashMap<String, Object>();
        }

        FormBody.Builder formBuilder = new FormBody.Builder();
        FormBody formBody = formBuilder.build();
        Request.Builder requestBuilder = new Request.Builder();
        final Request request = requestBuilder.url(url).post(formBody).tag(tag).build();
        callback.onBefore(request);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(request, e, context);
                        Log.i("nyso_bbc","onError"+e);
                        callback.onAfter();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String result = response.body().string();

                    Log.d("0.0", "返回报文body：" + result);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(result);
                            callback.onAfter();
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(request, e, context);
                            callback.onAfter();
                        }
                    });
                }
            }
        });
    }


    /**
     * 网络请求方法
     *
     * @param context
     * @param url
     * @param callback
     *
     */
    public void get(final Context context, final String url, final HttpCallback callback) {


        Request.Builder requestBuilder = new Request.Builder();

        final Request request = requestBuilder.url(url).build();



        callback.onBefore(request);

        Call call = mOkHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError(request, e, context);
                        callback.onAfter();
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                try {
                    final String result = response.body().string();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onResponse(result);
                            callback.onAfter();
                        }
                    });

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError(request, e, context);
                            callback.onAfter();
                        }
                    });
                }
            }
        });
    }

}
