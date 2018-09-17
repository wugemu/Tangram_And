package com.tmall.wireless.tangram.example.data;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tmall.wireless.tangram.example.util.TangramUtil;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;
import com.tmall.wireless.tangram.util.ImageUtils;
import com.tmall.wireless.tangram.example.R;
/**
 * Created by Bill56 on 2018-1-5.
 */

public class SubjectProductView extends LinearLayout implements ITangramViewLifeCycle {

    LinearLayout ll_product;
    ImageView ivProductImg;

    public SubjectProductView(Context context) {
        super(context);
        init();
    }

    public SubjectProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SubjectProductView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.item_home_special_product,this);
        ll_product=(LinearLayout)findViewById(R.id.ll_product);
        ivProductImg = (ImageView) findViewById(R.id.iv_product_img);
    }

    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
    }

    @Override
    public void postBindView(BaseCell cell) {
        String imgUrl = cell.optStringParam("imgUrl");
        Double widthRatio = cell.optDoubleParam("widthRatio");
        ImageUtils.doLoadImageUrl(ivProductImg, imgUrl);
        LinearLayout.LayoutParams p2= (LinearLayout.LayoutParams) ivProductImg.getLayoutParams();
        if (!Double.isNaN(widthRatio)) {
            int width=(int)(TangramUtil.getDisplayWidth((Activity)getContext())/widthRatio);
            p2.width =width;
            p2.height=width;
        }else {
            p2.width = 80;
            p2.height = 80;
        }
        ivProductImg.setLayoutParams(p2);
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
