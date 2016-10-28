package me.xudaojie.ninesquare.load;

import me.xudaojie.ninesquare.Constants;

/**
 * Created by xdj on 2016/10/28.
 */

public class RequestOptions {

    private int mPlaceholderId = Constants.NONE;
//    private Drawable mPlaceholderDrawable;
    private int mErrorId = Constants.NONE;
//    private Drawable mErrorPlaceholder;
    private int mImageLoader = Constants.NONE;

    public RequestOptions placeholder(int resourceId) {
        mPlaceholderId = resourceId;
        return this;
    }

//    public RequestOptions placeholder(@NonNull Drawable drawable) {
//        mPlaceholderDrawable = drawable;
//        return this;
//    }

    public RequestOptions imageLoader(int imageLoader) {
        mImageLoader = imageLoader;
        return this;
    }

    public RequestOptions error(int resourceId) {
        mErrorId = resourceId;
        return this;
    }

//    public RequestOptions error(@NonNull Drawable drawable) {
//        mErrorPlaceholder = drawable;
//        return this;
//    }


    public int getPlaceholderId() {
        return mPlaceholderId;
    }

    public int getErrorId() {
        return mErrorId;
    }

    public int getImageLoader() {
        return mImageLoader;
    }
}
