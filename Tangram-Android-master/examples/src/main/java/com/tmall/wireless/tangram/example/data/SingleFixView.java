package com.tmall.wireless.tangram.example.data;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tmall.wireless.tangram.example.R;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

/**
 * Created by lang on 18-1-2.
 */
public class SingleFixView extends FrameLayout implements ITangramViewLifeCycle {
    private TextView textView;
    private BaseCell cell;

    public SingleFixView(Context context) {
        super(context);
        init();
    }

    public SingleFixView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SingleFixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.singlefixview, this);
        textView = (TextView) findViewById(R.id.title);
    }
    @Override
    public void cellInited(BaseCell cell) {
        setOnClickListener(cell);
        this.cell = cell;
    }

    @Override
    public void postBindView(BaseCell cell) {
        String text = cell.optStringParam("text");
        textView.setText(text);
    }

    @Override
    public void postUnBindView(BaseCell cell) {

    }
}
