package me.xdj.ninesquare;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by xdj on 2016/10/26.
 */

public class TestZoomActivity extends AppCompatActivity {

    private ArrayList<String> mImages;
    private ArrayList<String> mThumbnails;
    private int mCurrentPosition;

    private Toolbar mToolbar;

    public static void startActivity(@NonNull Context context, @NonNull ArrayList<String> images,
                                     ArrayList<String> thumbnails, int currentPosition) {
        Intent i = new Intent(context, TestZoomActivity.class);
        i.putExtra(Constants.IMAGE, images);
        i.putExtra(Constants.THUMBNAIL, thumbnails);
        i.putExtra(Constants.CURRENT_POSITION, currentPosition);
        context.startActivity(i);
    }

    public static void startActivity(@NonNull Context context, @NonNull String[] images,
                                     String[] thumbnails, int currentPosition) {
        ArrayList<String> imageArrayList = new ArrayList<>();
        ArrayList<String> thumbnailArrayList = null;
        for (String image : images) {
            imageArrayList.add(image);
        }
        if (thumbnails != null) {
            thumbnailArrayList = new ArrayList<>();
            for (String thumbnail : thumbnails) {
                thumbnailArrayList.add(thumbnail);
            }
        }

        Intent i = new Intent(context, TestZoomActivity.class);
        i.putExtra(Constants.IMAGE, imageArrayList);
        i.putExtra(Constants.THUMBNAIL, thumbnailArrayList);
        i.putExtra(Constants.CURRENT_POSITION, currentPosition);
        context.startActivity(i);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ns_square_act);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        mImages = getIntent().getStringArrayListExtra("image");
        mThumbnails = getIntent().getStringArrayListExtra("thumbnail");
        mCurrentPosition = getIntent().getIntExtra("current_position", 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setTitle(mCurrentPosition + 1 + " / " + mImages.size());
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        TestZoomFragment fragment = (TestZoomFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (fragment == null) {
            fragment = TestZoomFragment.newInstance(mImages, mThumbnails, mCurrentPosition);
            fragment.setOnPageChangeListener(new TestZoomFragment.OnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    getSupportActionBar().setTitle(position + 1 + " / " + mImages.size());
                }
            });

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.zoom_menu, menu);
//        return true;
    }
}
