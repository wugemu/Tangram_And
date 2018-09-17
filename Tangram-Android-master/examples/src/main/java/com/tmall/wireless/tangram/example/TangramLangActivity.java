package com.tmall.wireless.tangram.example;
/*
 * MIT License
 *
 * Copyright (c) 2017 Alibaba Group
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.libra.Utils;
import com.squareup.picasso.Picasso;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.example.data.SimpleImgView;
import com.tmall.wireless.tangram.example.data.SingleFixView;
import com.tmall.wireless.tangram.example.data.SubjectProductView;
import com.tmall.wireless.tangram.example.data.TestView;
import com.tmall.wireless.tangram.example.support.SampleClickSupport;
import com.tmall.wireless.tangram.example.util.TangramUtil;
import com.tmall.wireless.tangram.example.util.http.HttpCallback;
import com.tmall.wireless.tangram.example.util.http.HttpU;
import com.tmall.wireless.tangram.example.widget.MyRecyclerView;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.async.AsyncLoader;
import com.tmall.wireless.tangram.support.async.AsyncPageLoader;
import com.tmall.wireless.tangram.support.async.CardLoadSupport;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import com.tmall.wireless.vaf.framework.VafContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/**
 * Created by villadora on 15/8/18.
 */
public class TangramLangActivity extends Activity {

    private static final String TAG = TangramLangActivity.class.getSimpleName();

    private Handler mMainHandler;
    TangramEngine engine;
    TangramBuilder.InnerBuilder builder;
    private MyRecyclerView recyclerView;
    private PtrClassicFrameLayout refresh_header;
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Step 10: get tangram data and pass it to engine
        initTangram();
        getLayoutData(false);
//        loadLocJson();
    }
    public void loadLocJson(){
        String json = new String(TangramUtil.getAssertsFile(this, "data.json"));
        JSONArray data = null;
        try {
            data = new JSONArray(json);
            engine.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void initTangram(){
        try {
            Picasso.setSingletonInstance(new Picasso.Builder(this).loggingEnabled(true).build());
        } catch (Exception e) {

        }
        setContentView(R.layout.tangram_lang_activity);
        refresh_header=(PtrClassicFrameLayout)findViewById(R.id.refresh_header);
        refresh_header.setLastUpdateTimeRelateObject(this);
        recyclerView = (MyRecyclerView) findViewById(R.id.main_view);
        recyclerView.setNestParent(refresh_header);
        refresh_header.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getLayoutData(true);
            }
        });

        //Step 1: init tangram
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {

                if (url.toLowerCase().contains(".gif")) {
                    Glide.with(getApplicationContext()).load(url).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(view);
                } else {
                    Picasso.with(TangramLangActivity.this).load(url).into(view);
                }
            }
        }, ImageView.class);

        //Tangram.switchLog(true);
        mMainHandler = new Handler(getMainLooper());

        //Step 2: register build=in cells and cards
        builder = TangramBuilder.newInnerBuilder(this);

        //Step 3: register business cells and cards
        builder.registerCell(1, SimpleImgView.class);
        builder.registerCell(2, TestView.class);
        builder.registerCell(3, SingleFixView.class);
        builder.registerCell(4, SubjectProductView.class);
        //Step 4: new engine
        engine = builder.build();
        Utils.setUedScreenWidth(720);
        VafContext.loadImageLoader(getApplicationContext());

        //Step 5: add card load support if you have card that loading cells async
        engine.setPreLoadNumber(5);
        engine.addCardLoadSupport(new CardLoadSupport(
                new AsyncLoader() {
                    @Override
                    public void loadData(Card card, @NonNull final LoadedCallback callback) {
                        Log.w("Load Card", card.load);

                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // do loading
                                JSONArray cells = new JSONArray();

                                for (int i = 0; i < 10; i++) {
                                    try {
                                        JSONObject obj = new JSONObject();
                                        obj.put("type", 1);
                                        obj.put("msg", "async loaded");
                                        JSONObject style = new JSONObject();
                                        style.put("bgColor", "#FF1111");
                                        obj.put("style", style.toString());
                                        cells.put(obj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                // callback.fail(false);
                                callback.finish(engine.parseComponent(cells));
                            }
                        }, 200);
                    }
                },

                new AsyncPageLoader() {
                    @Override
                    public void loadData(final int page, @NonNull final Card card, @NonNull final LoadedCallback callback) {
                        mMainHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("0.0", card.load + " page " + page);
                                getMoreData(page,card,callback);
                            }
                        }, 400);
                    }
                }));

        engine.addSimpleClickSupport(new SampleClickSupport(handler));

        //Step 6: enable auto load more if your page's data is lazy loaded
        engine.enableAutoLoadMore(true);

        //Step 7: bind recyclerView to engine
        engine.bindView(recyclerView);

        //Step 8: listener recyclerView onScroll event to trigger auto load more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                engine.onScrolled();
            }
        });

        //Step 9: set an offset to fix card
        engine.getLayoutManager().setFixOffset(0, 0, 0, 0);
    }

    private void getLayoutData(final boolean isRefresh) {

        HttpU.getInstance().post(TangramLangActivity.this, "http://10.42.0.1:8080/scb/doTest/main.do", null, this, new HttpCallback() {

            @Override
            public void onResponse(String response) {
                try {
                    response=response.replace("\\","");
                    response=response.replace("u0026", "&");
                    response=response.replace("u003d", "=");
                    response=response.substring(1,response.length()-1);
                    JSONObject jsonObj = new JSONObject(response);
                    String data= jsonObj.getString("data");
                    JSONObject dataObj = new JSONObject(data);
                    String cards= dataObj.getString("cards");
                    JSONArray cardsObj = null;
                    try {
                        cardsObj = new JSONArray(cards);
                        engine.setData(cardsObj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onAfter() {
                super.onAfter();
                if(isRefresh) {
                    refresh_header.refreshComplete();
                }
            }
        });

    }
    private void getMoreData(final int page, @NonNull final Card card, @NonNull final AsyncPageLoader.LoadedCallback callback) {

        HttpU.getInstance().post(TangramLangActivity.this, "http://10.42.0.1:8080/scb/doTest/loadMoreTangram.do", null, this, new HttpCallback() {

            @Override
            public void onResponse(String response) {
                try {
                    response=response.replace("\\","");
                    response=response.replace("u0026", "&");
                    response=response.replace("u003d", "=");
                    response=response.substring(1,response.length()-1);
                    JSONArray cells = new JSONArray(response);
                    List<BaseCell> cs = engine.parseComponent(cells);
                    //mock load 6 pages
                    callback.finish(cs,true);//true加载更多  false不再加载更多
                    card.notifyDataChange();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onAfter() {
                super.onAfter();
            }
        });

    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    //刷新
                    getLayoutData(false);
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
