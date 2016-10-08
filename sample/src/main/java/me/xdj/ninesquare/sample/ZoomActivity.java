package me.xdj.ninesquare.sample;


import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.xdj.ninesquare.BuildConfig;
import me.xdj.ninesquare.ImageFragment;
import me.xdj.ninesquare.photoview.HackyViewPager;
import me.xdj.ninesquare.photoview.MyPhotoViewAttacher;

/**
 * Created by xdj on 16/7/5.
 */
public class ZoomActivity extends AppCompatActivity {

    public static final String POSITION = "position";
    public static final String PAGER_COUNT = "pager_count";
    public static final String URL_ARRAY = "url_list";

    private Activity mActivity;

    HackyViewPager mViewPager;
    TextView mPagerNumberTv;

    private Animator mCurrentAnimator;

    private int mCurrentImgPosition;
    private int mPagerCount;
    private String[] mUrlArray;
    private ImageFragment.PhotoAdapter mPhotoAdapter;

    private int mStateBarColor;

    public static void startActivity(Context context, int currentImgPosition, int pagerCount,
                                     String[] urlList) {
        Intent i = new Intent(context, ZoomActivity.class);
        i.putExtra(POSITION, currentImgPosition);
        i.putExtra(PAGER_COUNT, pagerCount);
        i.putExtra(URL_ARRAY, urlList);
        context.startActivity(i);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(me.xdj.ninesquare.R.layout.ns_square_fragment);
        mActivity = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStateBarColor = mActivity.getWindow().getStatusBarColor();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mActivity
                    .getWindow()
                    .setStatusBarColor(ContextCompat.getColor(mActivity, android.R.color.black));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) return;

        mCurrentImgPosition = bundle.getInt(POSITION);
        mPagerCount = bundle.getInt(PAGER_COUNT);
        mUrlArray = bundle.getStringArray(URL_ARRAY);

        mViewPager = (HackyViewPager) findViewById(me.xdj.ninesquare.R.id.view_pager);
        mPagerNumberTv = (TextView) findViewById(me.xdj.ninesquare.R.id.pager_number_tv);

        mViewPager.setBackgroundResource(android.R.color.black);

        initPhotoAdapter();
        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(mCurrentImgPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (BuildConfig.DEBUG) Log.d("onPageSelected", position + "");
                mCurrentImgPosition = position;
                mPagerNumberTv.setText(position + 1 + "/" + 9);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    protected void initPhotoAdapter() {
        mPhotoAdapter = new ImageFragment.PhotoAdapter() {
            @Override
            public void loadPhoto(int position, ImageView imageView) {
                Glide
                        .with(ZoomActivity.this)
                        .load(mUrlArray[position])
                        .dontAnimate()
                        .dontTransform()
                        .placeholder(R.drawable.img_place)
                        .error(R.drawable.img_error)
                        .into(imageView);
            }
        };
    }

    class PagerAdapter extends FragmentPagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            ImageFragment fragment = ImageFragment.newInstance(position);
            fragment.setAdapter(mPhotoAdapter);
            fragment.setOnDrawableClickListener(new MyPhotoViewAttacher.OnDrawableClickListener() {
                @Override
                public void onDeltaClick(View v) {
                    finish();
                }

                @Override
                public void onDrawableClick(View v) {
                    finish();
                }
            });
            return fragment;
        }

        @Override
        public int getCount() {
            return mPagerCount;
        }
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
