package me.xdj.ninesquare;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.zoom_iv)
    ImageView mZoomIv;
    @BindView(R.id.container)
    FrameLayout mContainer;

    private Animator mCurrentAnimator;

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
            View view  = LayoutInflater.from(MainActivity.this).inflate(R.layout.img_item, parent, false);
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

    private void playZoomInAnimator(final View targetView) {
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

        mZoomIv.setPivotX(0);
        mZoomIv.setPivotY(0);
        mZoomIv.setImageResource(R.drawable.img);
        mZoomIv.setVisibility(View.VISIBLE);

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
                .play(ObjectAnimator.ofFloat(mZoomIv, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.SCALE_Y, startScale, 1f));

        set.setDuration(300);
        set.start();
        mCurrentAnimator = set;

        mZoomIv.setOnClickListener(new View.OnClickListener() {
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

        mZoomIv.setPivotX(0);
        mZoomIv.setPivotY(0);
        mZoomIv.setImageResource(R.drawable.img);
        AnimatorSet set = new AnimatorSet();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mZoomIv.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                mZoomIv.setVisibility(View.INVISIBLE);
            }
        });
        set
                .play(ObjectAnimator.ofFloat(mZoomIv, View.X, startBounds.left))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.Y, startBounds.top))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.SCALE_X, startScale))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.SCALE_Y, startScale));

        set.setDuration(300);
        set.start();
        mCurrentAnimator = set;
    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 99;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.img_item, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mImageView.setImageResource(R.drawable.img);
            return convertView;
        }
    }

    class ViewHolder {

        ImageView mImageView;

        public ViewHolder(View itemView) {
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
