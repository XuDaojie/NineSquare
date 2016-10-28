package me.xdj.ninesquare.sample;

import android.animation.Animator;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.xudaojie.ninesquare.ImageLoader;
import me.xudaojie.ninesquare.ZoomActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.container)
    FrameLayout mContainer;
    private Animator mCurrentAnimator;
    private int mCurrentItemPosition = -1;
    private int mCurrentImgPosition = -1;
    //    private View mCurrentView; // 当前选中View
    private int mStateBarColor;

    public static final String[] mImgUrl = new String[]{
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

    public static final String[] mBigImgUrl = new String[]{
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
        public void onBindViewHolder(final NineSquareViewHolder holder, final int position) {
            GridLayoutManager glm = new GridLayoutManager(MainActivity.this, 3);
            holder.mPhotoRv.setLayoutManager(glm);
            holder.mPhotoRv.setAdapter(new ImageAdapter());

            ItemClickSupport.addTo(holder.mPhotoRv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                @Override
                public void onItemClicked(final RecyclerView recyclerView, final int imgPosition, View v) {
                    mCurrentItemPosition = position;
                    mCurrentImgPosition = imgPosition;

                    ZoomActivity.startActivity(MainActivity.this, ImageLoader.FRESCO,
                            mBigImgUrl, mImgUrl,
                            R.drawable.img_place, R.drawable.img_error,
                            mCurrentImgPosition);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBigImgUrl.length;
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
}
