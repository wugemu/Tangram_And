package com.tmall.wireless.tangram.example;

import com.alibaba.android.vlayout.Range;
import com.libra.Utils;
import com.squareup.picasso.Picasso;
import com.tmall.wireless.tangram.TangramBuilder;
import com.tmall.wireless.tangram.TangramEngine;
import com.tmall.wireless.tangram.core.adapter.GroupBasicAdapter;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.example.data.RatioTextView;
import com.tmall.wireless.tangram.example.data.SimpleImgView;
import com.tmall.wireless.tangram.example.data.SingleImageView;
import com.tmall.wireless.tangram.example.data.TestView;
import com.tmall.wireless.tangram.example.data.TestViewHolder;
import com.tmall.wireless.tangram.example.data.TestViewHolderCell;
import com.tmall.wireless.tangram.example.data.VVTEST;
import com.tmall.wireless.tangram.example.dataparser.SampleDataParser;
import com.tmall.wireless.tangram.example.support.SampleClickSupport;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.card.BannerCard;
import com.tmall.wireless.tangram.structure.card.FourColumnCard;
import com.tmall.wireless.tangram.structure.viewcreator.ViewHolderCreator;
import com.tmall.wireless.tangram.support.async.AsyncLoader;
import com.tmall.wireless.tangram.support.async.AsyncPageLoader;
import com.tmall.wireless.tangram.support.async.CardLoadSupport;
import com.tmall.wireless.tangram.util.IInnerImageSetter;
import com.tmall.wireless.vaf.framework.VafContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by liupeng on 13/12/2017.
 */

public class TangramDataParserActivity extends Activity {

    private static final String TAG = TangramActivity.class.getSimpleName();

    private Handler mMainHandler;
    TangramEngine engine;
    TangramBuilder.InnerBuilder builder;
    RecyclerView recyclerView;

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Picasso.setSingletonInstance(new Picasso.Builder(this).loggingEnabled(true).build());
        } catch (Exception e) {

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        recyclerView = (RecyclerView) findViewById(R.id.main_view);

        //Step 1: init tangram
        TangramBuilder.init(this, new IInnerImageSetter() {
            @Override
            public <IMAGE extends ImageView> void doLoadImageUrl(@NonNull IMAGE view,
                                                                 @Nullable String url) {
                Picasso.with(TangramDataParserActivity.this).load(url).into(view);
            }
        }, ImageView.class);

        //Tangram.switchLog(true);
        mMainHandler = new Handler(getMainLooper());

        //Step 2: register build=in cells and cards
        builder = TangramBuilder.newInnerBuilder(this);

        //add: custom dataParser
        builder.setDataParser(new SampleDataParser());

        //Step 3: register business cells and cards
        builder.registerCard(1000, BannerCard.class);
        builder.registerCard(2000, FourColumnCard.class);
        builder.registerCell(1, TestView.class);
        builder.registerCell(10, SimpleImgView.class);
        builder.registerCell(2, SimpleImgView.class);
        builder.registerCell(4, RatioTextView.class);
        builder.registerCell(110,
                TestViewHolderCell.class,
                new ViewHolderCreator<>(R.layout.item_holder, TestViewHolder.class, TextView.class));
        builder.registerCell(199,SingleImageView.class);
        builder.registerVirtualView("vvtest");
        //Step 4: new engine
        engine = builder.build();
        engine.setVirtualViewTemplate(VVTEST.BIN);
        Utils.setUedScreenWidth(720);
        VafContext.loadImageLoader(getApplicationContext());

        //Step 5: add card load support if you have card that loading cells async
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
                                Log.w("Load page", card.load + " page " + page);
                                JSONArray cells = new JSONArray();
                                for (int i = 0; i < 9; i++) {
                                    try {
                                        JSONObject obj = new JSONObject();
                                        obj.put("type", 1);
                                        obj.put("msg", "async page loaded, params: " + card.getParams().toString());
                                        cells.put(obj);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                List<BaseCell> cs = engine.parseComponent(cells);

                                if (card.page == 1) {
                                    GroupBasicAdapter<Card, ?> adapter = engine.getGroupBasicAdapter();

                                    card.setCells(cs);
                                    adapter.refreshWithoutNotify();
                                    Range<Integer> range = adapter.getCardRange(card);

                                    adapter.notifyItemRemoved(range.getLower());
                                    adapter.notifyItemRangeInserted(range.getLower(), cs.size());

                                } else
                                    card.addCells(cs);

                                //mock load 6 pages
                                callback.finish(card.page != 6);
                                card.notifyDataChange();
                            }
                        }, 400);
                    }
                }));
        engine.addSimpleClickSupport(new SampleClickSupport());

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
        engine.getLayoutManager().setFixOffset(0, 40, 0, 0);

        //Step 10: get tangram data and pass it to engine
        String json = new String(getAssertsFile(this, "data_third.json"));
        JSONArray data = null;
        try {
            data = new JSONArray(json);
            engine.setData(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static byte[] getAssertsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();
        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {

            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (Exception e) {

                    }
                }
            }

            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}