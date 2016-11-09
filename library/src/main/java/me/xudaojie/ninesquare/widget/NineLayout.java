package me.xudaojie.ninesquare.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xdj on 2016/11/7.
 */

public class NineLayout extends ViewGroup {

    private static final String TAG = NineLayout.class.getSimpleName();

    public NineLayout(Context context) {
        super(context);
    }

    public NineLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NineLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NineLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    /**
//     * 为子ViewGroup指定LayoutParams
//     */
//    @Override
//    protected LayoutParams generateLayoutParams(LayoutParams p) {
//        return new MarginLayoutParams(p);
//    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int height = 0;

        // 计算child尺寸
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int childCount = getChildCount();
        if (childCount > 6) {
            height = width;
        } else if (childCount > 3 && childCount <= 6) {
            height = width / 3 * 2;
        } else {
            height = width / 3;
        }

        setMeasuredDimension(widthSize, height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        Log.d(TAG, "measureChildBefore");
        int parentWidthMode = MeasureSpec.getMode(parentWidthMeasureSpec);
        int parentWidthSize = MeasureSpec.getSize(parentWidthMeasureSpec);
        int parentHeightMode = MeasureSpec.getMode(parentHeightMeasureSpec);
        int parentHeightSize = MeasureSpec.getSize(parentHeightMeasureSpec);

        LayoutParams mlp = child.getLayoutParams();
        mlp.width = parentWidthSize / 3;
        mlp.height = parentWidthSize / 3;
        child.setLayoutParams(mlp);

        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        Log.d(TAG, "measureChildAfter");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        int offsetX = 0;
        int offsetY = 0;

        for (int i = 0; i < childCount; i++) {
            int cWidth = 0;
            int cHeight = 0;
            View child = getChildAt(i);
            Log.d(TAG, "getMeasuredWidth");
            cWidth = child.getMeasuredWidth();
            Log.d(TAG, "getMeasuredHeight");
            cHeight = child.getMeasuredHeight();
            MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
            int cl = 0, ct = 0, cr = 0, cb = 0;

            switch (i) {
                case 0:
                    cl = mlp.leftMargin;
                    ct = mlp.topMargin;
                    break;
                case 1:
                    cl = getChildAt(0).getWidth();
                    ct = mlp.topMargin;
                    break;
                case 2:
                    cl = getChildAt(0).getWidth() * 2;
                    ct = mlp.topMargin;
                    break;
                case 3:
                    cl = 0;
                    ct = mlp.topMargin + getChildAt(0).getHeight();
                    break;
                case 4:
                    cl = getChildAt(0).getWidth();
                    ct = mlp.topMargin + getChildAt(0).getHeight();
                    break;
                case 5:
                    cl = getChildAt(0).getWidth() * 2;
                    ct = mlp.topMargin + getChildAt(0).getHeight();
                    break;
                case 6:
                    cl = 0;
                    ct = mlp.topMargin + getChildAt(0).getHeight() * 2;
                    break;
                case 7:
                    cl = getChildAt(0).getWidth();
                    ct = mlp.topMargin + getChildAt(0).getHeight() * 2;
                    break;
                case 8:
                    cl = getChildAt(0).getWidth() * 2;
                    ct = mlp.topMargin + getChildAt(0).getHeight() * 2;
                    break;
            }
            cr = cl + cWidth;
            cb = ct + cHeight;
            child.layout(cl, ct, cr, cb);
        }
    }

}
