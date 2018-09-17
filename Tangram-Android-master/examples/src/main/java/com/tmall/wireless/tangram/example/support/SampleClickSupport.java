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

package com.tmall.wireless.tangram.example.support;

import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.support.SimpleClickSupport;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

/**
 * Created by longerian on 17/3/7.
 */

public class SampleClickSupport extends SimpleClickSupport {

    private Handler handler;
    public SampleClickSupport() {
        setOptimizedMode(true);
    }
    public SampleClickSupport(Handler handler) {
        setOptimizedMode(true);
        this.handler=handler;
    }

    @Override
    public void defaultClick(View targetView, BaseCell cell, int eventType) {
        super.defaultClick(targetView, cell, eventType);
        Toast.makeText(targetView.getContext(), cell.optStringParam("action"), Toast.LENGTH_SHORT).show();
        if("刷新".equals(cell.optStringParam("action"))){
            if(handler!=null){
                handler.sendEmptyMessage(0);
            }
        }
    }
}
