package me.xdj.ninesquare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xdj on 16/7/5.
 */
public class NineSquareFragment extends DialogFragment {

    private Activity mActivity;
    private View mRoot;

    @BindView(R.id.view_pager)
    HackyViewPager mViewPager;
    @BindView(R.id.pager_number_tv)
    TextView mPagerNumberTv;

    private Animator mCurrentAnimator;
    private RecyclerView mItemRecyclerView;
    private RecyclerView mImgRecyclerView;

//    private int mCurrentItemPosition;
    private int mCurrentImgPosition;
    private ITarget mTarget;
    private ImageFragment.PhotoAdapter mPhotoAdapter;

    public static NineSquareFragment newIntance(int currentImgPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt("img_position", currentImgPosition);

        NineSquareFragment fragment = new NineSquareFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);


        Bundle bundle = getArguments();
        if (bundle == null) return;

        mCurrentImgPosition = bundle.getInt("img_position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mRoot = LayoutInflater.from(mActivity).inflate(R.layout.square_fragment, container, false);
        ButterKnife.bind(this, mRoot);

        mViewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), null, null, null));
        mViewPager.setCurrentItem(mCurrentImgPosition);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("onPageSelected", position + "");
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

        // TODO: 16/6/30 应点击delta区域缩小 目前点击无效?
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
                // TODO: 16/7/5 收起DialogFragment
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

        Rect startBounds;
        Rect finalBounds;
        Point globalOffset;

        public PagerAdapter(FragmentManager fm, Rect startBounds, Rect finalBounds, Point globalOffset) {
            super(fm);
            this.startBounds = startBounds;
            this.finalBounds = finalBounds;
            this.globalOffset = globalOffset;
        }

        @Override
        public Fragment getItem(final int position) {
            ImageFragment fragment = ImageFragment.newInstance(position, startBounds, finalBounds, globalOffset);
            fragment.setAdapter(mPhotoAdapter);
            fragment.setOnDrawableClickListener(new MyPhotoViewAttacher.OnDrawableClickListener() {
                @Override
                public void onDeltaClick(View v) {
//                    MainActivity.NineSquareViewHolder viewHolder = (MainActivity.NineSquareViewHolder) mItemRecyclerView.findViewHolderForAdapterPosition(mCurrentItemPosition);
//                    View targetView = viewHolder
//                            .mPhotoRv
//                            .findViewHolderForAdapterPosition(position)
//                            .itemView;
                    View targetView = mTarget.getTargetView(position);
                    playZoomOutAnimator(targetView);
                }

                @Override
                public void onDrawableClick(View v) {
//                    MainActivity.NineSquareViewHolder viewHolder = (MainActivity.NineSquareViewHolder) mItemRecyclerView.findViewHolderForAdapterPosition(mCurrentItemPosition);
//                    View targetView = viewHolder
//                            .mPhotoRv
//                            .findViewHolderForAdapterPosition(position)
//                            .itemView;
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

    interface ITarget {
        /**
         * 获得缩小目标的View
         * @param position 当前图片position
         * @return
         */
        View getTargetView(int position);
    }

}
