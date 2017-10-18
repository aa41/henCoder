package com.xiaoma.hencoder.ruler.render;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.xiaoma.hencoder.ruler.OnScrollChangeListener;
import com.xiaoma.hencoder.ruler.RulerView;

import java.util.HashMap;
import java.util.Map;

/**
 * author: mxc
 * date: 2017/10/16.
 */

public abstract class BaseRender implements IRender {
    protected int width;
    protected int height;
    protected HashMap<Float, RectF> indexMap = new HashMap<>();
    protected RulerView rulerView;


    protected Paint rulerPaint;
    protected Paint rulerBorderPaint;
    protected Paint trianglePaint;
    protected Paint rulerTextPaint;
    private Path trianglePath;
    private float lastX;
    private float nowX;
    private float offsetX;
    protected HashMap<Float, RectF> rectFs = new HashMap<>();
    private float animatorValue=0;
    protected boolean isNeedScrollToDefault = true;
    private Rect bounds = new Rect();
    private float oldIndex = 0, newIndex = 0;
    private OnScrollChangeListener listener;

    @Override
    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void bindRuler(RulerView view) {
        this.rulerView = view;
        initPaint(rulerView.getContext());
    }

    public void initPaint(Context context) {

        rulerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rulerPaint.setColor(rulerView.rulerColor);
        rulerPaint.setStyle(Paint.Style.FILL);


        rulerBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rulerBorderPaint.setColor(rulerView.rulerColor);
        rulerBorderPaint.setStyle(Paint.Style.FILL);


        trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trianglePaint.setColor(rulerView.triangleColor);
        trianglePaint.setStyle(Paint.Style.FILL);

        rulerTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rulerTextPaint.setColor(rulerView.rulerTextColor);
        rulerTextPaint.setTextAlign(Paint.Align.CENTER);
        rulerTextPaint.setTextSize(sp2px(context, 12));


    }

    @Override
    public void onMeasure(RulerView rulerView, int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = rulerView.getMeasuredWidth();
        } else {
            width = rulerView.getSuperSuggestedMinimumWidth();
        }
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = rulerView.getMeasuredHeight();
        } else {
            height = rulerView.getSuperSuggestedMinimumHeight();
        }
        rulerView.superSetMeasuredDimension(width, height);
    }


    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(rulerView.rulerBackground);
        if (isNeedScrollToDefault) {
            if (rulerView.defaultShow < rulerView.start) {
                rulerView.defaultShow = rulerView.start;
            }
            offsetX = -((rulerView.defaultShow - rulerView.start) * rulerView.gap - (width / 2 - rulerView.rulerBorderWidth / 2));
            newIndex=rulerView.defaultShow;
            if (listener != null) {
                listener.onChange(newIndex);
            }
            isNeedScrollToDefault = !isNeedScrollToDefault;
        }
        canvas.save();
        canvas.translate(offsetX - animatorValue, 0);
        drawRulers(canvas);
        canvas.restore();
        drawStaff(canvas);
    }

    private void drawStaff(Canvas canvas) {
        trianglePath = new Path();
        trianglePath.addRoundRect(new RectF(width / 2 - rulerView.rulerBorderWidth / 2, 0, width / 2 + rulerView.rulerBorderWidth / 2, height / 2), new float[]{0, 0, 0, 0, rulerView.rulerBorderWidth / 2, rulerView.rulerBorderWidth / 2, rulerView.rulerBorderWidth / 2, rulerView.rulerBorderWidth / 2}, Path.Direction.CW);
        canvas.drawPath(trianglePath, trianglePaint);
    }

    protected void drawRulers(Canvas canvas) {
        for (float i = rulerView.start; i <= rulerView.end; i+=addEveryIndex()) {
            float index = i - rulerView.start;
            String text = String.valueOf((int) i);
            rulerTextPaint.getTextBounds(text, 0, text.length(), bounds);
            if (isRulerLongIndex(i)) {
                canvas.drawRect(index * rulerView.gap, 0, rulerView.rulerBorderWidth + index * rulerView.gap, rulerView.rulerBorderHeight, rulerBorderPaint);
                canvas.drawText(text, index * rulerView.gap, rulerView.rulerBorderHeight + bounds.height() * 2, rulerTextPaint);
            } else {
                canvas.drawRect(index * rulerView.gap, 0, rulerView.rulerNormalWidth + index * rulerView.gap, rulerView.rulerNormalHeight, rulerPaint);
            }
            rectFs.put(i, new RectF(index * rulerView.gap, 0, (index + 1) * rulerView.gap, rulerView.rulerBorderHeight));
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                nowX = x;
                offsetX += nowX - lastX;
                if (offsetX > width / 2 - rulerView.rulerBorderWidth / 2) {
                    offsetX = width / 2 - rulerView.rulerBorderWidth / 2;
                } else if (offsetX < -((rulerView.end - rulerView.start) * rulerView.gap - (width / 2 - rulerView.rulerBorderWidth / 2))) {
                    offsetX = -((rulerView.end - rulerView.start) * rulerView.gap - (width / 2 - rulerView.rulerBorderWidth / 2));
                }
                Log.e("move", "offsetX:" + offsetX);
                lastX = x;
                for (Map.Entry<Float, RectF> entry : rectFs.entrySet()) {
                    Float key = entry.getKey();
                    RectF rectF = entry.getValue();
                    float abs = Math.abs(offsetX - (width / 2 - rulerView.rulerBorderWidth / 2));

                    if (rectF.contains(abs, 0)) {
                        newIndex = key;
                        if (oldIndex != newIndex) {
                            if (listener != null) {
                                listener.onChange(newIndex);
                            }
                            oldIndex = newIndex;
                        }

                    }
                }
                rulerView.invalidate();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                RectF rectF = rectFs.get(newIndex);
                float abs = Math.abs(offsetX - (width / 2 - rulerView.rulerBorderWidth / 2));
                if (rectF.contains(abs, 0)) {
                    float left = rectF.left - abs;
                    float right = rectF.right - abs;
                    Log.e("up:", "left:" + left);
                    Log.e("up:", "right:" + right);
                    scrollerAnimator(Math.abs(left) >= Math.abs(right) ? right : left).start();
                    newIndex = Math.abs(left) >= Math.abs(right) ? newIndex + 1 : newIndex ;
                    if (oldIndex != newIndex) {
                        oldIndex = newIndex;
                        if (listener != null) {
                            listener.onChange(newIndex);
                        }
                    }
                }
                rulerView.invalidate();

                break;
        }


        return true;
    }

    private ValueAnimator scrollerAnimator(final float offset) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, offset);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatorValue = (float) valueAnimator.getAnimatedValue();
                rulerView.postInvalidate();
            }
        });
        animator.setInterpolator(new BounceInterpolator());
        return animator;
    }


    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


}
