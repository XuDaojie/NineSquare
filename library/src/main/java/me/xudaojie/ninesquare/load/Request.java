package me.xudaojie.ninesquare.load;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.ArrayList;

import me.xudaojie.ninesquare.Constants;
import me.xudaojie.ninesquare.ZoomActivity;

/**
 * Created by xdj on 2016/11/4.
 */

public class Request {
    private Context mContext;

    private RequestOptions mRequestOptions;

    private int mCurrentPosition;
    private ArrayList<String> mUris;
    private ArrayList<File> mFiles;
    private ArrayList<Integer> mResourceIds;

    public Request imageLoader(int imageLoader) {
        mRequestOptions.imageLoader(imageLoader);
        return this;
    }

    public Request(Context context, ArrayList<String> uris, int currentPosition) {
        mContext = context;
        mUris = uris;
        mCurrentPosition = currentPosition;
        mRequestOptions = new RequestOptions();
    }

    public Request placeholder(int resourceId) {
        mRequestOptions.placeholder(resourceId);
        return this;
    }

    public Request error(int resourceId) {
        mRequestOptions.error(resourceId);
        return this;
    }

    public void show() {
        Intent i = new Intent(mContext, ZoomActivity.class);
        if (mUris != null) {
            i.putExtra(Constants.IMAGE, mUris);
        } else if (mFiles != null) {

        } else if (mResourceIds != null) {
            //...
        }

        i.putExtra(Constants.CURRENT_POSITION, mCurrentPosition);
        i.putExtra(Constants.IMAGE_LOADER, mRequestOptions.getImageLoader());

        if (mRequestOptions.getErrorId() != Constants.NONE) {
            i.putExtra(Constants.ERROR, mRequestOptions.getErrorId());
        }
        if (mRequestOptions.getPlaceholderId() != Constants.NONE) {
            i.putExtra(Constants.PLACEHOLDER, mRequestOptions.getErrorId());
        }
        mContext.startActivity(i);
    }
}
