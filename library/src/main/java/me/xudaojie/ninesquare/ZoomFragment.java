package me.xudaojie.ninesquare;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import me.xudaojie.ninesquare.photoview.HackyViewPager;

/**
 * Created by xdj on 2016/10/26.
 */

public class ZoomFragment extends Fragment {

    private Activity mActivity;
    private View mRootView;

    private HackyViewPager mViewPager;
    private OnPageChangeListener mOnPageChangeListener;


    private int mCurrentPosition;
    private int mImageLoader;
    private ImageFragment.PhotoAdapter mPhotoAdapter;
    private ArrayList<String> mImages;
    private ArrayList<String> mThumbnails;

    public static ZoomFragment newInstance(int imageLoader, @NonNull ArrayList<String> images,
                                           ArrayList<String> thumbnails, int currentPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.IMAGE_LOADER, imageLoader);
        bundle.putStringArrayList(Constants.IMAGE, images);
        bundle.putStringArrayList(Constants.THUMBNAIL, thumbnails);
        bundle.putInt(Constants.CURRENT_POSITION, currentPosition);

        ZoomFragment fragment = new ZoomFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        Bundle bundle = getArguments();
        if (bundle == null) return;

        mImageLoader = bundle.getInt(Constants.IMAGE_LOADER);
        mCurrentPosition = bundle.getInt(Constants.CURRENT_POSITION);
        mImages = bundle.getStringArrayList(Constants.IMAGE);
        mThumbnails = bundle.getStringArrayList(Constants.THUMBNAIL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRootView = LayoutInflater.from(mActivity).inflate(R.layout.ns_zoom_frag, container, false);
        mViewPager = (HackyViewPager) mRootView.findViewById(R.id.view_pager);

        mRootView.setBackgroundResource(android.R.color.black);

        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(mCurrentPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (BuildConfig.DEBUG) Log.d("onPageSelected", position + "");
                mCurrentPosition = position;
                if (mOnPageChangeListener != null) {
                    mOnPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return mRootView;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            ImageFragment fragment = ImageFragment.newInstance(position);
            fragment.setAdapter(new ImageFragment.PhotoAdapter() {
                @Override
                public void loadPhoto(int position, ImageView imageView) {
                    if (mImageLoader == ImageLoader.GLIDE) {
                        Glide
                                .with(mActivity)
                                .load(mImages.get(position))
                                .dontAnimate()
                                .dontTransform()
                                .into(imageView);
                    } else if (mImageLoader == ImageLoader.PICASSO) {
                        Picasso
                                .with(mActivity)
                                .load(mImages.get(position))
                                .into(imageView);
                    } else {
                        throw new RuntimeException("not support imageLoader = " + mImageLoader);
                    }
                }
            });
            return fragment;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
    }
}
