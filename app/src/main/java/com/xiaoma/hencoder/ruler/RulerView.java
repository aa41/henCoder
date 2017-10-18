package com.xiaoma.hencoder.ruler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.xiaoma.hencoder.R;
import com.xiaoma.hencoder.ruler.render.BaseRender;
import com.xiaoma.hencoder.ruler.render.HeightRender;
import com.xiaoma.hencoder.ruler.render.IRender;

/**
 * author: mxc
 * date: 2017/10/16.
 */

public class RulerView extends View {
    public static final float START = 100;
    public static final float END = 230;
    private IRender render;
    private Context mContext;

    public int gap;
    public float defaultShow;
    public float start = START, end = END;
    public int rulerColor = Color.WHITE;
    public int rulerBackground;
    public int rulerTextColor;
    public int triangleColor;
    public int rulerBorderWidth;
    public int rulerBorderHeight;
    public int rulerNormalWidth;
    public int rulerNormalHeight;

    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RulerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttr(context,attrs,defStyleAttr);
    }

    public void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RulerView, defStyleAttr, 0);
        rulerColor = ta.getColor(R.styleable.RulerView_ruler_color, Color.WHITE);
        rulerBackground = ta.getColor(R.styleable.RulerView_ruler_backgroundColor, Color.YELLOW);
        rulerTextColor = ta.getColor(R.styleable.RulerView_ruler_textColor, Color.WHITE);
        triangleColor = ta.getColor(R.styleable.RulerView_ruler_triangle_color, Color.WHITE);
        rulerBorderWidth = ta.getDimensionPixelOffset(R.styleable.RulerView_ruler_border_width, dp2px(context, 2));
        rulerBorderHeight = ta.getDimensionPixelOffset(R.styleable.RulerView_ruler_border_height, dp2px(context, 50));
        rulerNormalWidth = ta.getDimensionPixelOffset(R.styleable.RulerView_ruler_normal_width, dp2px(context, 1));
        rulerNormalHeight = ta.getDimensionPixelOffset(R.styleable.RulerView_ruler_normal_height, dp2px(context, 25));
        start = ta.getFloat(R.styleable.RulerView_ruler_start, START);
        end = ta.getFloat(R.styleable.RulerView_ruler_end, END);
        gap = ta.getDimensionPixelOffset(R.styleable.RulerView_ruler_scale_gap, dp2px(context, 10));
        defaultShow = ta.getFloat(R.styleable.RulerView_ruler_default_show, 160);
        ta.recycle();





    }

    public void setRender(BaseRender render) {
        this.render = render;
        render.bindRuler(this);
        requestLayout();
        invalidate();
    }

    public int getSuperSuggestedMinimumHeight() {
        return getSuggestedMinimumHeight();
    }

    public int getSuperSuggestedMinimumWidth() {
        return getSuggestedMinimumWidth();
    }

    public void superSetMeasuredDimension(int measureWidth, int measureHeight) {
        setMeasuredDimension(measureWidth, measureHeight);
    }

    public boolean superOnTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        checkRender();
        render.onMeasure(this, widthMeasureSpec, heightMeasureSpec);
    }

    private void checkRender(){
        if(render==null){
            render=new HeightRender();
            render.bindRuler(this);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        checkRender();
        render.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        checkRender();
        return render.onTouchEvent(event);
    }
    public  int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
