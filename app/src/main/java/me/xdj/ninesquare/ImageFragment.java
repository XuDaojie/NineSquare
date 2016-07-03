package me.xdj.ninesquare;

import android.animation.Animator;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xdj on 16/6/30.
 */

public class ImageFragment extends Fragment {
    @BindView(R.id.zoom_iv)
    ImageView mZoomIv;

    private View mRoot;

    private Rect mStartRect;
    private Rect mFinalRect;
    private Point mGlobalOffset;
    private int mPosition;

    private Animator mCurrentAnimator;
    private OnDeltaClickListener mOnDeltaClickListener;

    public static ImageFragment newInstance(int position, Rect startBounds, Rect finalBounds, Point globalOffset) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("startBounds", startBounds);
        bundle.putParcelable("finalBounds", finalBounds);
        bundle.putParcelable("globalOffset", globalOffset);
        bundle.putInt("position", position);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = LayoutInflater.from(getActivity()).inflate(R.layout.image_fragment, container, false);
        ButterKnife.bind(this, mRoot);

        Bundle bundle = getArguments();

        mStartRect = bundle.getParcelable("startBounds");
        mFinalRect = bundle.getParcelable("finalBounds");
        mGlobalOffset = bundle.getParcelable("globalOffset");
        mPosition = bundle.getInt("position");

        // 通过另一个请求加载缩略图
        DrawableRequestBuilder<String> thumbRequest = Glide
                .with(getActivity())
                .load(MainActivity.mImgUrl[mPosition]);

        Glide
                .with(getActivity())
                .load(MainActivity.mBigImgUrl[mPosition])
//                .load("http://www.bz55.com/uploads/allimg/150616/139-150616101938.jpg")
                .dontAnimate()
                .dontTransform()
                .placeholder(R.drawable.img_place)
                .error(R.drawable.img_error)
                .thumbnail(thumbRequest)
                .into(mZoomIv);

        // 得到`ImageView`中的矩阵，准备得到drawable的拉伸比率
        Matrix m = mZoomIv.getImageMatrix();
        float[] values = new float[10];
        m.getValues(values);

        mRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 得到`ImageView`中的矩阵，准备得到drawable的拉伸比率
                Matrix m = mZoomIv.getImageMatrix();
                float[] values = new float[10];
                m.getValues(values);

                // ImageView当前显示空间
                Rect rect = new Rect();
                mZoomIv.getGlobalVisibleRect(rect);

                // 获得图片实际宽高 (资源文件中的图片获得的宽高未必是原图宽高,与屏幕分辨率有关)
                Drawable drawable = mZoomIv.getDrawable();
                if (drawable != null) {
                    int drawableOriginalWidth = mZoomIv.getDrawable().getIntrinsicWidth();
                    int drawableOriginalHeight = mZoomIv.getDrawable().getIntrinsicHeight();

                    // 获得实际显示的宽高
                    int drawableWidth = (int) (drawableOriginalWidth * values[0]);
                    int drawableHeight = (int) (drawableOriginalHeight * values[4]);

                    // TODO: 16/7/1 计算当前点是否在delta区域
                    int deltaHeight = (1920 - drawableHeight) / 2;

                    if (event.getY() < deltaHeight
                            || event.getY() > deltaHeight + drawableHeight) {
                        if (mOnDeltaClickListener != null) {
                            mOnDeltaClickListener.onClick(v);
                        }
                    }
                } else if (mOnDeltaClickListener != null) {
                    mOnDeltaClickListener.onClick(v);
                }

                return false;
            }
        });

        return mRoot;
    }

    public void setOnDeltaClickListener(OnDeltaClickListener onDeltaClickListener) {
        mOnDeltaClickListener = onDeltaClickListener;
    }

    /**
     * 图片上下空白区域点击事件
     */
    public interface OnDeltaClickListener {
        void onClick(View v);
    }

}
