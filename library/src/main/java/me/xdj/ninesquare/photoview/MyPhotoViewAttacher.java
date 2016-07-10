package me.xdj.ninesquare.photoview;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by xdj on 16/7/4.
 */
public class MyPhotoViewAttacher extends PhotoViewAttacher {

    private GestureDetector mDeltaGestureDetector;
    private OnDrawableClickListener mOnDrawableClickListener;

    public MyPhotoViewAttacher(ImageView imageView) {
        super(imageView);
        init();
    }

    public MyPhotoViewAttacher(ImageView imageView, boolean zoomable) {
        super(imageView, zoomable);
        init();
    }

    private void init() {

        mDeltaGestureDetector = new GestureDetector(getImageView().getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
//                return super.onSingleTapConfirmed(e);
                // 得到`ImageView`中的矩阵，准备得到drawable的拉伸比率
                ImageView imageView = getImageView();

                if (imageView == null) return false;
                Matrix m = imageView.getImageMatrix();
                float[] values = new float[10];
                m.getValues(values);

                // ImageView当前显示空间
                Rect rect = new Rect();
                imageView.getGlobalVisibleRect(rect);

                // 获得图片实际宽高 (资源文件中的图片获得的宽高未必是原图宽高,与屏幕分辨率有关)
                Drawable drawable = imageView.getDrawable();
                if (drawable != null) {
                    int drawableOriginalWidth = imageView.getDrawable().getIntrinsicWidth();
                    int drawableOriginalHeight = imageView.getDrawable().getIntrinsicHeight();

                    // 获得实际显示的宽高
                    int drawableWidth = (int) (drawableOriginalWidth * values[0]);
                    int drawableHeight = (int) (drawableOriginalHeight * values[4]);

                    // TODO: 16/7/1 计算当前点是否在delta区域
                    int deltaHeight = (1920 - drawableHeight) / 2;

                    if (event.getY() < deltaHeight
                            || event.getY() > deltaHeight + drawableHeight) {
                        if (mOnDrawableClickListener != null) {
                            mOnDrawableClickListener.onDeltaClick(imageView);
                        }
                    } else {
                        if (mOnDrawableClickListener != null) {
                            // 点击区域为图片显示区域
                            mOnDrawableClickListener.onDrawableClick(imageView);
                        }
                    }
                    return true;
                } else if (mOnDrawableClickListener != null) {
                    mOnDrawableClickListener.onDeltaClick(imageView);
                    return true;
                }

                return false;
            }

        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        if (mOnDrawableClickListener != null
                && mDeltaGestureDetector.onTouchEvent(ev)) {
            return true;
        }
        return super.onTouch(v, ev);
    }

    public void setOnDrawableClickListener(OnDrawableClickListener onDrawableClickListener) {
        mOnDrawableClickListener = onDrawableClickListener;
    }

    public interface OnDrawableClickListener {
        /**
         * 图片上下空白区域点击事件
         * @param v
         */
        void onDeltaClick(View v);

        /**
         * 图片内容区域点击事件
         * @param v
         */
        void onDrawableClick(View v);
    }
}
