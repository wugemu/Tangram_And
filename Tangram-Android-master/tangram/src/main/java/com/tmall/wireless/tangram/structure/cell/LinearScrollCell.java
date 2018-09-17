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

package com.tmall.wireless.tangram.structure.cell;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.tmall.wireless.tangram.core.adapter.BinderViewHolder;
import com.tmall.wireless.tangram.core.adapter.GroupBasicAdapter;
import com.tmall.wireless.tangram.dataparser.concrete.Style;
import com.tmall.wireless.tangram.structure.BaseCell;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kunlun on 9/17/16.
 */
public class LinearScrollCell extends BaseCell {
    /**
     * width of each item
     */
    public static final String KEY_PAGE_WIDTH = "pageWidth";
    /**
     * height of each item
     */
    public static final String KEY_PAGE_HEIGHT = "pageHeight";
    /**
     * width of each item
     */
    public static final String KEY_WIDTH_RATIO = "widthRatio";
    /**
     * background of indicator
     */
    public static final String KEY_DEFAULT_INDICATOR_COLOR = "defaultIndicatorColor";
    /**
     * foreground of indicator
     */
    public static final String KEY_INDICATOR_COLOR = "indicatorColor";
    public static final String KEY_HAS_INDICATOR = "hasIndicator";
    public static final String KEY_FOOTER_TYPE = "footerType";
    public static final String KEY_RETAIN_SCROLL_STATE = "retainScrollState";

    public static final int DEFAULT_DEFAULT_INDICATOR_COLOR = Color.parseColor("#80ffffff");
    public static final int DEFAULT_INDICATOR_COLOR = Color.parseColor("#ffffff");

    public List<BaseCell> cells = new ArrayList<BaseCell>();

    public double pageWidth = Double.NaN;
    public double pageHeight = Double.NaN;
    public double widthRatio = Double.NaN;
    public int defaultIndicatorColor = DEFAULT_DEFAULT_INDICATOR_COLOR;
    public int indicatorColor = DEFAULT_INDICATOR_COLOR;
    public boolean hasIndicator = true;
    public String footerType;
    public Adapter adapter;
    public int screenWidth;
    public int bgColor = Color.TRANSPARENT;

    // current distance that responding recycler view has scrolled.
    public int currentDistance = 0;

    public boolean retainScrollState = true;

    @Override
    protected void onAdded() {
        super.onAdded();
        adapter = new Adapter(getAdapter());
    }

    public void setCells(List<BaseCell> cells) {
        this.cells.clear();
        if (cells != null && cells.size() > 0) {
            this.cells.addAll(cells);
        }
        adapter.notifyDataSetChanged();
    }

    public GroupBasicAdapter getAdapter() {
        if (serviceManager != null) {
            return serviceManager.getService(GroupBasicAdapter.class);
        }
        return null;
    }

    public RecyclerView.RecycledViewPool getRecycledViewPool() {
        if (serviceManager != null) {
            return serviceManager.getService(RecyclerView.RecycledViewPool.class);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public class Adapter extends RecyclerView.Adapter<BinderViewHolder> {
        private GroupBasicAdapter adapter;

        public Adapter(GroupBasicAdapter adapter) {
            this.adapter = adapter;
        }

        @Override
        public BinderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(BinderViewHolder binderViewHolder, int position) {
            binderViewHolder.bind(cells.get(position));
            BaseCell cell = cells.get(position);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(binderViewHolder.itemView.getLayoutParams());
            if (!Double.isNaN(pageWidth)) {
                lp.width = (int) (pageWidth + 0.5);
            }else if (!Double.isNaN(cell.extras.optDouble("pageWidth"))) {
                lp.width = Style.dp2px(cell.extras.optDouble("pageWidth"));
            }
            if (!Double.isNaN(pageHeight)) {
                lp.height = (int) (pageHeight + 0.5);
            }
            int[] margins = {0, 0, 0, 0};
            if (cell.style != null) {
                margins = cell.style.margin;
            }
            lp.setMargins(margins[3], margins[0], margins[1], margins[2]);
            //chils cell's pageWidth override pageWidth in style
            binderViewHolder.itemView.setLayoutParams(lp);
        }

        @Override
        public int getItemViewType(int position) {
            return adapter.getItemType(cells.get(position));
        }

        @Override
        public int getItemCount() {
            return cells == null ? 0 : cells.size();
        }

        @Override
        public void onViewRecycled(BinderViewHolder holder) {
            // to call cell.unbind method.
            adapter.onViewRecycled(holder);
        }
    }
}
