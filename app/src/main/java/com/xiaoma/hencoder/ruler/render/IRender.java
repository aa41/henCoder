package com.xiaoma.hencoder.ruler.render;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.xiaoma.hencoder.ruler.OnScrollChangeListener;
import com.xiaoma.hencoder.ruler.RulerView;

/**
 * author: mxc
 * date: 2017/10/16.
 */

public interface IRender {

    void bindRuler(RulerView view);

    void setOnScrollChangeListener(OnScrollChangeListener listener);

    void onMeasure(RulerView rulerView,int widthMeasureSpec, int heightMeasureSpec);

    void onDraw(Canvas canvas);

    boolean onTouchEvent(MotionEvent event);

    boolean isRulerLongIndex(float index);

    float addEveryIndex();

}
