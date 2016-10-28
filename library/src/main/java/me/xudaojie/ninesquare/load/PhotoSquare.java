package me.xudaojie.ninesquare.load;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

import me.xudaojie.ninesquare.Constants;
import me.xudaojie.ninesquare.ZoomActivity;

/**
 * Created by xdj on 2016/10/28.
 * todo 未完成
 */
public class PhotoSquare {

    private RequestOptions mRequestOptions;

    private int mCurrentPosition;

    private ArrayList<String> mUrls;
    private ArrayList<File> mFiles;
    private ArrayList<Integer> mResourceIds;

    private PhotoSquare() {
    }

    public static PhotoSquare newInstance() {
        return new PhotoSquare();
    }

    public RequestOptions loadUrl(ArrayList<String> urls, int currentPosition) {
        mUrls = urls;
        mCurrentPosition = currentPosition;
        return mRequestOptions = new RequestOptions();
    }

//    public RequestOptions loadFile(ArrayList<File> files) {
//        mFiles = files;
//        return mRequestOptions = new RequestOptions();
//    }

//    public RequestOptions loadResource(ArrayList<Integer> resourceId) {
//        mResourceIds = resourceId;
//        return mRequestOptions = new RequestOptions();
//    }

    public void show(Context context) {
        Intent i = new Intent(context, ZoomActivity.class);
        if (mUrls != null) {
            i.putExtra(Constants.IMAGE, mUrls);
        } else if (mFiles != null) {

        } else if (mResourceIds != null) {

        }

        i.putExtra(Constants.CURRENT_POSITION, mCurrentPosition);
        i.putExtra(Constants.IMAGE_LOADER, mRequestOptions.getImageLoader());

        if (mRequestOptions.getErrorId() != Constants.NONE) {
            i.putExtra(Constants.ERROR, mRequestOptions.getErrorId());
        }
        if (mRequestOptions.getPlaceholderId() != Constants.NONE) {
            i.putExtra(Constants.PLACEHOLDER, mRequestOptions.getErrorId());
        }
        context.startActivity(i);
    }
}
