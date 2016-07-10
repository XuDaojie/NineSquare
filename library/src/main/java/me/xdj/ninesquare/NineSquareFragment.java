package me.xdj.ninesquare;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.xdj.ninesquare.photoview.HackyViewPager;
import me.xdj.ninesquare.photoview.MyPhotoViewAttacher;

/**
 * Created by xdj on 16/7/5.
 */
public class NineSquareFragment extends DialogFragment {

    public static final String POSITION = "position";

    private Activity mActivity;
    private View mRoot;

    HackyViewPager mViewPager;
    TextView mPagerNumberTv;

    private Animator mCurrentAnimator;

    private int mCurrentImgPosition;
    private ITarget mTarget;
    private ImageFragment.PhotoAdapter mPhotoAdapter;

    public static NineSquareFragment newIntance(int currentImgPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, currentImgPosition);

        NineSquareFragment fragment = new NineSquareFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);

        Bundle bundle = getArguments();
        if (bundle == null) return;

        mCurrentImgPosition = bundle.getInt(POSITION);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //return super.onCreateDialog(savedInstanceState);
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                //super.onBackPressed();
                playZoomOutAnimator(mTarget.getTargetView(mCurrentImgPosition));
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.ns_square_fragment, container, false);
        mViewPager = (HackyViewPager) mRoot.findViewById(R.id.view_pager);
        mPagerNumberTv = (TextView) mRoot.findViewById(R.id.pager_number_tv);

        mRoot.setBackgroundResource(android.R.color.black);

        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager()));
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
        View view = mTarget.getTargetView(mCurrentImgPosition);
        playZoomInAnimator(view);

        return mRoot;
    }

    public void setTarget(ITarget target) {
        mTarget = target;
    }

    public ImageFragment.PhotoAdapter getPhotoAdapter() {
        return mPhotoAdapter;
    }

    public void setPhotoAdapter(ImageFragment.PhotoAdapter photoAdapter) {
        mPhotoAdapter = photoAdapter;
    }

    private void playZoomInAnimator(final View targetView) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        mRoot.setBackgroundResource(android.R.color.black);

        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();
        Point globalOffset = new Point();

        targetView.getGlobalVisibleRect(startBounds);
        mActivity.getWindow().getDecorView().getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;

        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();

            float startWidth = startScale * finalBounds.width(); // 获得缩放后的宽
            float detailWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= detailWidth;
            startBounds.right += detailWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();

            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        mViewPager.setPivotX(0);
        mViewPager.setPivotY(0);
        mViewPager.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mCurrentAnimator = null;

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mCurrentAnimator = null;
            }
        });
        set
                .play(ObjectAnimator.ofFloat(mViewPager, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mViewPager, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mViewPager, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mViewPager, View.SCALE_Y, startScale, 1f));

        set.setDuration(300 * 1);
        set.start();
        mCurrentAnimator = set;

        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playZoomOutAnimator(targetView);
            }
        });
    }

    private void playZoomOutAnimator(final View targetView) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }
        mRoot.setBackgroundResource(android.R.color.transparent);

        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();
        Point globalOffset = new Point();

        targetView.getGlobalVisibleRect(startBounds);
        mActivity.getWindow().getDecorView().getGlobalVisibleRect(finalBounds, globalOffset);

        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        float startScale;

        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();

            float startWidth = startScale * finalBounds.width(); // 获得缩放后的宽
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();

            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        mViewPager.setPivotX(0);
        mViewPager.setPivotY(0);

        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                NineSquareFragment.this.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                NineSquareFragment.this.dismiss();
            }
        });
        set
                .play(ObjectAnimator.ofFloat(mViewPager, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(mViewPager, View.Y, startBounds.top))
                .with(ObjectAnimator.ofFloat(mViewPager, View.SCALE_X, startScale))
                .with(ObjectAnimator.ofFloat(mViewPager, View.SCALE_Y, startScale));

        set.setDuration(300);
        set.start();
        mCurrentAnimator = set;
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
                    View targetView = mTarget.getTargetView(position);
                    playZoomOutAnimator(targetView);
                }

                @Override
                public void onDrawableClick(View v) {
                    View targetView = mTarget.getTargetView(position);
                    playZoomOutAnimator(targetView);
                }
            });
            return fragment;
        }

        @Override
        public int getCount() {
            return 9;
        }
    }

    public interface ITarget {
        /**
         * 获得缩小目标的View
         * @param position 当前图片position
         * @return
         */
        View getTargetView(int position);
    }

    public void dismiss(boolean playAnimator) {
        if (playAnimator) {
            View view = mTarget.getTargetView(mCurrentImgPosition);
            playZoomOutAnimator(view);
        } else {
            dismiss();
        }
    }
}
