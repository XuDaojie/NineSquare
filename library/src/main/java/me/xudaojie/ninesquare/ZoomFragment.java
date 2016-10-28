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

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;

import me.xudaojie.frecozoomable.ZoomableDraweeView;
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
    private int mPlaceholder = Constants.NONE;
    private int mError = Constants.NONE;

    static ZoomFragment newInstance(int imageLoader, @NonNull ArrayList<String> images,
                                    ArrayList<String> thumbnails, int currentPosition) {
        return newInstance(imageLoader, images, thumbnails,
                Constants.NONE, Constants.NONE, currentPosition);
    }

    static ZoomFragment newInstance(int imageLoader,
                                    @NonNull ArrayList<String> images, ArrayList<String> thumbnails,
                                    int placeholder, int error,
                                    int currentPosition) {
        Bundle args = new Bundle();
        args.putInt(Constants.IMAGE_LOADER, imageLoader);
        args.putStringArrayList(Constants.IMAGE, images);
        args.putStringArrayList(Constants.THUMBNAIL, thumbnails);
        args.putInt(Constants.PLACEHOLDER, placeholder);
        args.putInt(Constants.ERROR, error);
        args.putInt(Constants.CURRENT_POSITION, currentPosition);

        ZoomFragment fragment = new ZoomFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        Bundle args = getArguments();
        if (args == null) return;

        mImageLoader = args.getInt(Constants.IMAGE_LOADER);
        mCurrentPosition = args.getInt(Constants.CURRENT_POSITION);
        mImages = args.getStringArrayList(Constants.IMAGE);
        mThumbnails = args.getStringArrayList(Constants.THUMBNAIL);
        mPlaceholder = args.getInt(Constants.PLACEHOLDER, Constants.NONE);
        mError = args.getInt(Constants.ERROR, Constants.NONE);
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
            ImageFragment fragment = ImageFragment.newInstance(mImageLoader, position);
            fragment.setAdapter(new ImageFragment.PhotoAdapter() {
                @Override
                public void loadPhoto(int position, ImageView imageView) {
                    String imageUri = mImages.get(position);

                    if (mImageLoader == ImageLoader.GLIDE) {
                        DrawableRequestBuilder requestBuilder = Glide
                                .with(mActivity)
                                .load(imageUri)
                                .dontAnimate()
                                .dontTransform();
                        if (mPlaceholder != Constants.NONE) {
                            requestBuilder = requestBuilder.placeholder(mPlaceholder);
                        }
                        if (mError != Constants.NONE) {
                            requestBuilder = requestBuilder.error(mPlaceholder);
                        }
                        requestBuilder.into(imageView);

                    } else if (mImageLoader == ImageLoader.PICASSO) {
                        RequestCreator creator = Picasso
                                .with(mActivity)
                                .load(imageUri);
                        if (mPlaceholder != Constants.NONE) {
                            creator = creator.placeholder(mPlaceholder);
                        }
                        if (mError != Constants.NONE) {
                            creator = creator.error(mPlaceholder);
                        }
                        creator.into(imageView);
                    } else if (mImageLoader == ImageLoader.FRESCO) {
                        GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                                .setPlaceholderImage(mPlaceholder)
                                .build();

                        ZoomableDraweeView draweeView = (ZoomableDraweeView) imageView;
                        draweeView.setAllowTouchInterceptionWhileZoomed(true);
                        DraweeController controller = Fresco.newDraweeControllerBuilder()
                                .setUri(imageUri)
                                .build();
                        draweeView.setController(controller);
                        draweeView.setHierarchy(hierarchy);
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
