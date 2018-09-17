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

package com.tmall.wireless.tangram;

import com.tmall.wireless.tangram.core.service.ServiceManager;
import com.tmall.wireless.tangram.dataparser.concrete.Card;
import com.tmall.wireless.tangram.structure.BaseCell;

import java.util.List;

/**
 * Created by villadora on 15/11/24.
 */
public interface Engine extends ServiceManager {

    /**
     * Notify Tangrm to update data or view.
     * @param layoutUpdate True to update both data and view, false to update data only.
     */
    void refresh(boolean layoutUpdate);

    /**
     * Notify Tangrm to update data and view.
     */
    void refresh();

    /**
     * Replace card with new data, would cause view updated.
     * @param oldCard Old card data to be replaced.
     * @param newCard New card data.
     */
    void replaceCard(Card oldCard, Card newCard);

    void replaceCells(Card parent, List<BaseCell> cells);

    /**
     * Scroll the page to target card position
     * @param card Target card.
     */
    void scrollToPosition(Card card);

    /**
     * Scroll the page to target cell position
     * @param cell Target cell.
     */
    void scrollToPosition(BaseCell cell);


    void topPosition(BaseCell cell);

    void topPosition(Card card);
}
