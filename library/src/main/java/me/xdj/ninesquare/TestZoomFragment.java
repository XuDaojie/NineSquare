package me.xdj.ninesquare;

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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.xdj.ninesquare.photoview.HackyViewPager;

/**
 * Created by xdj on 2016/10/26.
 */

public class TestZoomFragment extends Fragment {

    private Activity mActivity;
    private View mRoot;

    HackyViewPager mViewPager;
    TextView mPagerNumberTv;

    private int mCurrentPosition;
    private ArrayList<String> mImages;
    private ArrayList<String> mThumbnails;
    private ImageFragment.PhotoAdapter mPhotoAdapter;

    public static TestZoomFragment newInstance(@NonNull ArrayList<String> images,
                                                 ArrayList<String> thumbnails, int currentPosition) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.IMAGE, images);
        bundle.putStringArrayList(Constants.THUMBNAIL, thumbnails);
        bundle.putInt(Constants.CURRENT_POSITION, currentPosition);

        TestZoomFragment fragment = new TestZoomFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();

        Bundle bundle = getArguments();
        if (bundle == null) return;

        mCurrentPosition = bundle.getInt(Constants.CURRENT_POSITION);
        mImages = bundle.getStringArrayList(Constants.IMAGE);
        mThumbnails = bundle.getStringArrayList(Constants.THUMBNAIL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.ns_square_frag, container, false);
        mViewPager = (HackyViewPager) mRoot.findViewById(R.id.view_pager);
        mPagerNumberTv = (TextView) mRoot.findViewById(R.id.pager_number_tv);

        mRoot.setBackgroundResource(android.R.color.black);
        mPagerNumberTv.setText(mCurrentPosition + 1 + "/" + mImages.size());

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
                mPagerNumberTv.setText(position + 1 + "/" + mImages.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return mRoot;
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
                    Glide
                            .with(mActivity)
                            .load(mImages.get(position))
                            .dontAnimate()
                            .dontTransform()
                            .into(imageView);
                }
            });
            return fragment;
        }

        @Override
        public int getCount() {
            return mImages.size();
        }
    }
}
