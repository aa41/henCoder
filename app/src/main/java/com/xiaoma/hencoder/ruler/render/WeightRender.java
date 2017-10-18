package com.xiaoma.hencoder.ruler.render;

/**
 * author: mxc
 * date: 2017/10/18.
 */

public class WeightRender extends BaseRender {
    @Override
    public boolean isRulerLongIndex(float index) {
        return index % 10 == 0;
    }

    @Override
    public float addEveryIndex() {
        return 2f;
    }
}
