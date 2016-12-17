package me.xudaojie.ninesquare.widget;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import me.xudaojie.ninesquare.R;

/**
 * Created by xdj on 2016/11/7.
 *
 */

public class BaseGridLayout extends ViewGroup {

    private static final String TAG = BaseGridLayout.class.getSimpleName();

    protected int mHorizontalSpacing;
    protected int mVerticalSpacing;
    protected int mRowCount = 3;
    protected int mColumnCount = 3;

    public BaseGridLayout(Context context) {
        super(context);
        init(context, null);
    }

    public BaseGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BaseGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseGridLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) return;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseGridLayout);
        mHorizontalSpacing = (int) typedArray.getDimension(R.styleable.BaseGridLayout_horizontalSpacing, 0);
        mVerticalSpacing = (int) typedArray.getDimension(R.styleable.BaseGridLayout_verticalSpacing, 0);
        mRowCount = typedArray.getInt(R.styleable.BaseGridLayout_rowCount, 3);
        mColumnCount = typedArray.getInt(R.styleable.BaseGridLayout_columnCount, 3);


        typedArray.recycle();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = widthSize;
        int height = 0;

        ProgressDialog.Builder builder = new ProgressDialog.Builder(getContext());

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
        mlp.width = (parentWidthSize - mVerticalSpacing * (mColumnCount - 1)) / mColumnCount;
        mlp.height = mlp.width;
        child.setLayoutParams(mlp);

        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec);
        Log.d(TAG, "measureChildAfter");
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        View[][] childs = new View[mColumnCount][mRowCount];
        for (int i = 0; i < mColumnCount; i++) {
            View[] childGroup = childs[i];
            childs[i] = childGroup;
            for (int j = 0; j < mRowCount; j++) {
                int position = i * mColumnCount + j;
                View child = getChildAt(position);
                if (child != null) {
                    childGroup[j] = child;
                } else {
                    break;
                }

                int cWidth = 0;
                int cHeight = 0;
                cWidth = child.getMeasuredWidth();
                cHeight = child.getMeasuredHeight();
                MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
                int cl = 0, ct = 0, cr = 0, cb = 0;
                ct = i * (mVerticalSpacing + cHeight);
                cl = j * (mHorizontalSpacing + cWidth);
                cr = cl + cWidth;
                cb = ct + cHeight;
                child.layout(cl, ct, cr, cb);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClickListener(View view, int position);
    }
}
