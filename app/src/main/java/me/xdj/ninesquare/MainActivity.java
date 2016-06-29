package me.xdj.ninesquare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private FrameLayout mContainer;
    private RecyclerView mRecyclerView;
    private ImageView mZoomIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = (FrameLayout) findViewById(R.id.container);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mZoomIv = (ImageView) findViewById(R.id.zoom_iv);

        GridLayoutManager glm = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(glm);
        mRecyclerView.setAdapter(new NineSquareAdapter());


        ItemClickSupport.addTo(mRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                playAnimator(v);
            }
        });
    }

    class NineSquareAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.img_item, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.mImageView.setImageResource(R.drawable.img);

        }

        @Override
        public int getItemCount() {
            return 9;
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    private void playAnimator(View targetView) {
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
        set
                .play(ObjectAnimator.ofFloat(mZoomIv, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(mZoomIv, View.SCALE_Y, startScale, 1f));

        set.setDuration(500);
        set.start();
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
