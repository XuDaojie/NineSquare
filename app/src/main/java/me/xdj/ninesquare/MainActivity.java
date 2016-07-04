package me.xdj.ninesquare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    @BindView(R.id.background_view)
    View mBackgroundView;
    @BindView(R.id.pager_number_tv)
    TextView mPagerNumberTv;
    @BindView(R.id.container)
    FrameLayout mContainer;

    private Animator mCurrentAnimator;
    private int mCurrentItemPosition = -1;
    private int mCurrentImgPosition = -1;
//    private View mCurrentView; // 当前选中View
    private int mStateBarColor;

    public static final String[] mImgUrl = new String[] {
            "http://ww2.sinaimg.cn/thumb180/8f1fe6aajw1f5gtp60m71j216o1kwq7p.jpg",
            "http://ww4.sinaimg.cn/orj480/005SiNxygw1f5gsgcibauj30ku334qdx.jpg",
            "http://ww2.sinaimg.cn/thumb180/bfc243a3gw1f5gk1acbo7j20xc0xcmxw.jpg",
            "http://ww1.sinaimg.cn/thumb180/bfc243a3gw1f5gk1aqxsrj20xc0xcmxz.jpg",
            "http://ww1.sinaimg.cn/thumb180/795bf814gw1f5eddc8y95j20rs0ie108.jpg",
            "http://ww1.sinaimg.cn/thumb180/53899d01jw1f5ev8meqlxj20ku0kwdhk.jpg",
            "http://ww1.sinaimg.cn/thumb180/a316360dgw1f5ftuqyeikj20ku0fmgmw.jpg",
            "http://ww2.sinaimg.cn/thumb180/692c7ed6gw1f5fq6ocpfhj20qo0zkn7k.jpg",
            "http://ww2.sinaimg.cn/thumb180/67dd74e0gw1f5fq5c66xhj20j60wbjvc.jpg"
    };

    public static final String[] mBigImgUrl = new String[] {
            "http://ww2.sinaimg.cn/mw690/8f1fe6aajw1f5gtp60m71j216o1kwq7p.jpg",
            "http://ww4.sinaimg.cn/mw690/005SiNxygw1f5gsgcibauj30ku334qdx.jpg",
            "http://ww2.sinaimg.cn/mw690/bfc243a3gw1f5gk1acbo7j20xc0xcmxw.jpg",
            "http://ww1.sinaimg.cn/mw690/bfc243a3gw1f5gk1aqxsrj20xc0xcmxz.jpg",
            "http://ww1.sinaimg.cn/mw690/795bf814gw1f5eddc8y95j20rs0ie108.jpg",
            "http://ww1.sinaimg.cn/mw690/53899d01jw1f5ev8meqlxj20ku0kwdhk.jpg",
            "http://ww2.sinaimg.cn/mw690/a316360dgw1f5ftuqyeikj20ku0fmgmw.jpg",
            "http://ww2.sinaimg.cn/mw690/692c7ed6gw1f5fq6ocpfhj20qo0zkn7k.jpg",
            "http://ww2.sinaimg.cn/mw690/67dd74e0gw1f5fq5c66xhj20j60wbjvc.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setAdapter(new NineSquareAdapter());

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
        mPagerNumberTv.setVisibility(View.VISIBLE);

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

        set.setDuration(300 * 1);
        set.start();
        mCurrentAnimator = set;
//        mCurrentView = targetView;

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
                mPagerNumberTv.setVisibility(View.INVISIBLE);
                mBackgroundView.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(mStateBarColor);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mViewPager.setVisibility(View.INVISIBLE);
                mPagerNumberTv.setVisibility(View.INVISIBLE);
                mBackgroundView.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(mStateBarColor);
                }
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
            if (mCurrentImgPosition != -1) {
                View targetView = mRecyclerView
                        .findViewHolderForAdapterPosition(mCurrentImgPosition)
                        .itemView;
                playZoomOutAnimator(targetView);
            }
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
        public void onBindViewHolder(NineSquareViewHolder holder, final int position) {
            GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 3);
            holder.mPhotoRv.setLayoutManager(glm);
            holder.mPhotoRv.setAdapter(new ImageAdapter());

            ItemClickSupport.addTo(holder.mPhotoRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(RecyclerView recyclerView, int imgPosition, View v) {
                    // TODO: 16/7/1 修改状态栏颜色
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mStateBarColor == 0) {
                            mStateBarColor = getWindow().getStatusBarColor();
                        }
                        getWindow().setStatusBarColor(Color.BLACK);
                    }

                    mCurrentItemPosition = position;
                    mCurrentImgPosition = imgPosition;

                    mBackgroundView.setVisibility(View.VISIBLE);
                    mPagerNumberTv.setText(imgPosition + 1 + "/" + 9);
                    mViewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(), null, null, null));
                    mViewPager.setCurrentItem(imgPosition);
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
            holder.mImageView.setImageDrawable(null);
            Glide
                    .with(MainActivity.this)
                    .load(mImgUrl[position])
                    .placeholder(R.drawable.img_place)
                    .error(R.drawable.img_error)
                    .dontTransform()
                    .dontAnimate()
                    .centerCrop()
                    .into(holder.mImageView);
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
        public Fragment getItem(final int position) {
            ImageFragment fragment = ImageFragment.newInstance(position, startBounds, finalBounds, globalOffset);
            fragment.setOnDrawableClickListener(new MyPhotoViewAttacher.OnDrawableClickListener() {
                @Override
                public void onDeltaClick(View v) {
                    View targetView = mRecyclerView
                            .findViewHolderForAdapterPosition(position)
                            .itemView;
                    playZoomOutAnimator(targetView);
                }

                @Override
                public void onDrawableClick(View v) {
                    NineSquareViewHolder viewHolder = (NineSquareViewHolder) mRecyclerView.findViewHolderForAdapterPosition(mCurrentItemPosition);
                    View targetView = viewHolder
                            .mPhotoRv
                            .findViewHolderForAdapterPosition(position)
                            .itemView;
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
}
