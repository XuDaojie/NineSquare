package me.xudaojie.ninesquare;

import android.animation.Animator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import me.xudaojie.frecozoomable.ZoomableDraweeView;
import me.xudaojie.ninesquare.photoview.MyPhotoView;
import me.xudaojie.ninesquare.photoview.MyPhotoViewAttacher;

/**
 * Created by xdj on 16/6/30.
 */

public class ImageFragment extends Fragment {

    public static final String POSITION = "position";

    private ImageView mZoomIv;

    private View mRootView;

    private int mImageLoader;
    private int mPosition;

    private Animator mCurrentAnimator;
    private MyPhotoViewAttacher.OnDrawableClickListener mOnDrawableClickListener;
    private PhotoAdapter mAdapter;

    private GestureDetector mDeltaGestureDetector;

    public static ImageFragment newInstance(int imageLoader, int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_LOADER, imageLoader);
        bundle.putInt(POSITION, position);

        ImageFragment fragment = new ImageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = LayoutInflater.from(getActivity()).inflate(R.layout.ns_image_frag, container, false);

            Bundle bundle = getArguments();
            mImageLoader = bundle.getInt(Constants.IMAGE_LOADER);
            mPosition = bundle.getInt(POSITION);

            if (mImageLoader == ImageLoader.FRESCO) {
//                mZoomIv = new MyFrescoPhotoView(getActivity());
                mZoomIv = new ZoomableDraweeView(getActivity());
            } else {
                mZoomIv = new MyPhotoView(getActivity());
            }

            ((ViewGroup) mRootView).addView(mZoomIv, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            if (mAdapter != null) {
                mAdapter.loadPhoto(mPosition, mZoomIv);
            }
        }

        return mRootView;
    }

    public PhotoAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(PhotoAdapter adapter) {
        mAdapter = adapter;
    }

    public interface PhotoAdapter {
        /**
         * 当前图片position
         * @param position
         */
        void loadPhoto(int position, ImageView imageView);
    }
}
