package me.xdj.ninesquare;

import android.animation.Animator;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.xdj.ninesquare.photoview.MyPhotoView;
import me.xdj.ninesquare.photoview.MyPhotoViewAttacher;

/**
 * Created by xdj on 16/6/30.
 */

public class ImageFragment extends Fragment {

    public static final String POSITION = "position";

    private MyPhotoView mZoomIv;

    private View mRoot;

    private int mPosition;

    private Animator mCurrentAnimator;
    private MyPhotoViewAttacher.OnDrawableClickListener mOnDrawableClickListener;
    private PhotoAdapter mAdapter;

    private GestureDetector mDeltaGestureDetector;

    public static ImageFragment newInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, position);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = LayoutInflater.from(getActivity()).inflate(R.layout.ns_image_fragment, container, false);

        mZoomIv = (MyPhotoView) mRoot.findViewById(R.id.zoom_iv);

        Bundle bundle = getArguments();
        mPosition = bundle.getInt(POSITION);

        if (mAdapter != null) {
            mAdapter.loadPhoto(mPosition, mZoomIv);
        }

        //
        mZoomIv.setOnDrawableClickListener(mOnDrawableClickListener);

        // 得到`ImageView`中的矩阵，准备得到drawable的拉伸比率
        Matrix m = mZoomIv.getImageMatrix();
        float[] values = new float[10];
        m.getValues(values);

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
