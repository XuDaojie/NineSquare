package me.xdj.ninesquare;

import android.animation.Animator;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xdj on 16/6/30.
 */

public class ImageFragment extends Fragment {

    @BindView(R.id.zoom_iv)
    MyPhotoView mZoomIv;

    private View mRoot;

    private Rect mStartRect;
    private Rect mFinalRect;
    private Point mGlobalOffset;
    private int mPosition;

    private Animator mCurrentAnimator;
    private MyPhotoViewAttacher.OnDrawableClickListener mOnDrawableClickListener;
    private PhotoAdapter mAdapter;

    private GestureDetector mDeltaGestureDetector;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        mDeltaGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
//
//            @Override
//            public boolean onSingleTapConfirmed(MotionEvent event) {
////                return super.onSingleTapConfirmed(e);
//                // 得到`ImageView`中的矩阵，准备得到drawable的拉伸比率
//                Matrix m = mZoomIv.getImageMatrix();
//                float[] values = new float[10];
//                m.getValues(values);
//
//                // ImageView当前显示空间
//                Rect rect = new Rect();
//                mZoomIv.getGlobalVisibleRect(rect);
//
//                // 获得图片实际宽高 (资源文件中的图片获得的宽高未必是原图宽高,与屏幕分辨率有关)
//                Drawable drawable = mZoomIv.getDrawable();
//                if (drawable != null) {
//                    int drawableOriginalWidth = mZoomIv.getDrawable().getIntrinsicWidth();
//                    int drawableOriginalHeight = mZoomIv.getDrawable().getIntrinsicHeight();
//
//                    // 获得实际显示的宽高
//                    int drawableWidth = (int) (drawableOriginalWidth * values[0]);
//                    int drawableHeight = (int) (drawableOriginalHeight * values[4]);
//
//                    // TODO: 16/7/1 计算当前点是否在delta区域
//                    int deltaHeight = (1920 - drawableHeight) / 2;
//
//                    if (event.getY() < deltaHeight
//                            || event.getY() > deltaHeight + drawableHeight) {
//                        if (mOnDeltaClickListener != null) {
//                            mOnDeltaClickListener.onClick(mZoomIv);
//                        }
//                    }
//                } else if (mOnDeltaClickListener != null) {
//                    mOnDeltaClickListener.onClick(mZoomIv);
//                }
//
//                return false;
//            }
//
//        });
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

        if (mAdapter != null) {
            mAdapter.loadPhoto(mPosition, mZoomIv);
        }

//        // 通过另一个请求加载缩略图
//        DrawableRequestBuilder<String> thumbRequest = Glide
//                .with(getActivity())
//                .load(MainActivity.mImgUrl[mPosition]);
//
//        Glide
//                .with(getActivity())
//                .load(MainActivity.mBigImgUrl[mPosition])
////                .load("http://www.bz55.com/uploads/allimg/150616/139-150616101938.jpg")
//                .dontAnimate()
//                .dontTransform()
//                .placeholder(R.drawable.img_place)
//                .error(R.drawable.img_error)
//                .thumbnail(thumbRequest)
//                .into(mZoomIv);

        //
        mZoomIv.setOnDrawableClickListener(mOnDrawableClickListener);

        // 得到`ImageView`中的矩阵，准备得到drawable的拉伸比率
        Matrix m = mZoomIv.getImageMatrix();
        float[] values = new float[10];
        m.getValues(values);

        // ImageView 已消费了`onTouch`事件,所以父布局的`onTouch事件无法执行`
//        mRoot.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                mDeltaGestureDetector.onTouchEvent(event);
//                return true;
//            }
//        });

        return mRoot;
    }

    public PhotoAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(PhotoAdapter adapter) {
        mAdapter = adapter;
    }

    public void setOnDrawableClickListener(MyPhotoViewAttacher.OnDrawableClickListener onDrawableClickListener) {
        mOnDrawableClickListener = onDrawableClickListener;
    }

    public interface PhotoAdapter {
        /**
         * 当前图片position
         * @param position
         */
        void loadPhoto(int position, ImageView imageView);
    }
}
