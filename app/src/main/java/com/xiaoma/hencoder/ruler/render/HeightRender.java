package com.xiaoma.hencoder.ruler.render;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.icu.math.BigDecimal;

/**
 * author: mxc
 * date: 2017/10/16.
 */

public class HeightRender extends BaseRender {

    @Override
    public boolean isRulerLongIndex(float index) {
        return index % 5 == 0;
    }

    @Override
    public float addEveryIndex() {
        return 1;
    }


}
