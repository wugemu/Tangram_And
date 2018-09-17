package in.srain.cube.views.ptr;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by 1 on 2015/12/10.
 */
public class PtrClassHeader extends LinearLayout implements PtrUIHandler {

    private ImageView headerImage;
    private AnimationDrawable animationDrawable;
    public PtrClassHeader(Context context) {
        super(context);
        initView();
    }

    private void initView() {

        View header = LayoutInflater.from(getContext()).inflate(R.layout.cube_ptr_classic__header, this);
        headerImage= (ImageView) header.findViewById(R.id.pull_to_refresh_image);
        buildAnimation();
    }

    private void buildAnimation() {
        animationDrawable=((AnimationDrawable) headerImage.getDrawable());
    }


    @Override
    public void onUIReset(PtrFrameLayout frame) {
        buildAnimation();
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        animationDrawable.start();
        animationDrawable.stop();
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        animationDrawable.stop();
        animationDrawable.start();
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        animationDrawable.stop();
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
    }


    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        setLastUpdateTimeKey(object.getClass().getName());
    }
    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
    }

    private void tryUpdateLastUpdateTime() {

    }


    private String getLastUpdateTime() {

        return "";
    }
}
