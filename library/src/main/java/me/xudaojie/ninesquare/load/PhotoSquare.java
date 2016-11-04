package me.xudaojie.ninesquare.load;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by xdj on 2016/10/28.
 * todo 未完成
 */
public class PhotoSquare {

    private Context mContext;
    private Request mRequest;

    private PhotoSquare(Context context) {
        mContext = context;
    }

    public static PhotoSquare with(Context context) {
        return new PhotoSquare(context);
    }

    public Request loadUrl(ArrayList<String> urls, int currentPosition) {
        return mRequest = new Request(mContext, urls, currentPosition);
    }

//    public RequestOptions loadFile(ArrayList<File> files) {
//        mFiles = files;
//        return mRequestOptions = new RequestOptions();
//    }

//    public RequestOptions loadResource(ArrayList<Integer> resourceId) {
//        mResourceIds = resourceId;
//        return mRequestOptions = new RequestOptions();
//    }

}
