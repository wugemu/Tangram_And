package in.srain.cube.views.ptr;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class PtrClassicFrameLayout extends PtrFrameLayout {

    //    private PtrClassicDefaultHeader mPtrClassicHeader;
    private boolean disallowInterceptTouchEvent = false;
    private PtrClassHeader mPtrClassicHeader;

    public PtrClassicFrameLayout(Context context) {
        super(context);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews();
    }

    public PtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initViews();
    }

    private void initViews() {
//        mPtrClassicHeader = new PtrClassicDefaultHeader(getContext());
        mPtrClassicHeader = new PtrClassHeader(getContext());
        setHeaderView(mPtrClassicHeader);
        addPtrUIHandler(mPtrClassicHeader);
    }

    //    public PtrClassicDefaultHeader getHeader() {
//        return mPtrClassicHeader;
//    }
    public PtrClassHeader getHeader() {
        return mPtrClassicHeader;
    }

    /**
     * Specify the last update time by this key string
     *
     * @param key
     */
    public void setLastUpdateTimeKey(String key) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeKey(key);
        }
    }

    /**
     * Using an object to specify the last update time.
     *
     * @param object
     */
    public void setLastUpdateTimeRelateObject(Object object) {
        if (mPtrClassicHeader != null) {
            mPtrClassicHeader.setLastUpdateTimeRelateObject(object);
        }
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;
        Log.i("lhp", "PtrClassicFrameLayout+requestDisallowInterceptTouchEvent:" + disallowIntercept);
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        Log.i("lhp", "PtrClassicFrameLayout+dispatchTouchEvent:");
        return super.dispatchTouchEvent(e);
    }


}
