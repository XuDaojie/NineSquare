package me.xdj.ninesquare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
//    @BindView(R.id.zoom_iv)
//    ImageView mZoomIv;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.background_view)
    View mBackgroundView;

    private Animator mCurrentAnimator;
    private View mCurrentView; // 当前选中View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(new NineSquareAdapter());
    }

    private void playZoomInAnimator(final View targetView) {

        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        Rect startBounds = new Rect();
        Rect finalBounds = new Rect();
        Point globalOffset = new Point();
        // TODO: 16/6/30 测试
        mBackgroundView.setVisibility(View.VISIBLE);
//        mViewPager.setVisibility(View.VISIBLE);
//        mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), startBounds, finalBounds, globalOffset));

        targetView.getGlobalVisibleRect(startBounds);
        mContainer.getGlobalVisibleRect(finalBounds, globalOffset);

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

        set.setDuration(300);
        set.start();
        mCurrentAnimator = set;
        mCurrentView = targetView;

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
        mContainer.getGlobalVisibleRect(finalBounds, globalOffset);

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
                mViewPager.setVisibility(View.INVISIBLE);
                mBackgroundView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mViewPager.setVisibility(View.INVISIBLE);
                mBackgroundView.setVisibility(View.INVISIBLE);
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

    @Override
    public void onBackPressed() {
        if (mViewPager.getVisibility() == View.VISIBLE) {
//            mViewPager.setVisibility(View.INVISIBLE);
//            mBackgroundView.setVisibility(View.INVISIBLE);
            playZoomOutAnimator(mCurrentView);
        } else {
            super.onBackPressed();
        }
    }

    class NineSquareAdapter extends RecyclerView.Adapter<NineSquareViewHolder> {

        @Override
        public NineSquareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.nine_square_item, parent, false);
            NineSquareViewHolder holder = new NineSquareViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(NineSquareViewHolder holder, int position) {
            GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 3);
            holder.mPhotoRv.setLayoutManager(glm);
            holder.mPhotoRv.setAdapter(new ImageAdapter());
            ItemClickSupport.addTo(holder.mPhotoRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                    mBackgroundView.setVisibility(View.VISIBLE);
                    mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), null, null, null));
                    mViewPager.setVisibility(View.VISIBLE);
                    playZoomInAnimator(v);
                }
            });
        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }

    class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.img_item, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }

    class NineSquareViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatar_iv)
        ImageView mAvatarIv;
        @BindView(R.id.nickname_tv)
        TextView mNicknameTv;
        @BindView(R.id.content_tv)
        TextView mContentTv;
        @BindView(R.id.photo_rv)
        RecyclerView mPhotoRv;

        NineSquareViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
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
        public Fragment getItem(int position) {
            ImageFragment fragment = ImageFragment.newInstance(startBounds, finalBounds, globalOffset);
            fragment.setOnDeltaClickListener(new ImageFragment.OnDeltaClickListener() {
                @Override
                public void onClick(View v) {
                    playZoomOutAnimator(mCurrentView);
                }
            });
            return fragment;
        }

        @Override
        public int getCount() {
            return 9;
        }
    }
}
