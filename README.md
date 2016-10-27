# UNDONE NineSquare
[![](https://jitpack.io/v/XuDaojie/NineSquare.svg)](https://jitpack.io/#XuDaojie/NineSquare)
<br>
查看大图乞丐版封装<br>
![screenshot](https://github.com/XuDaojie/NineSquare/blob/master/art/NineSquare.gif)

## Use
目前支持Glide、Picasso、Fresco
<br>
1. Gilde、Picasso显示大图使用的是[PhotoView](https://github.com/chrisbanes/PhotoView)
<br>
2. Fresco 显示大图使用的是**Fresco** [sample](https://github.com/facebook/fresco/tree/master/samples/zoomableapp) 中的方案

``` java
// mThumbnails 为缩略图目前还没卵用
ZoomActivity.startActivity(mContext, ImageLoader.PICASSO, mImages, mThumbnails, mCurrentImgPosition);
```

## How to
To get a Git project into your build:
### Step1. Add the JitPack repository to your build file
``` gradle
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```
### Step2. Add the dependency
``` gradle
dependencies {
    compile 'com.github.XuDaojie:NineSquare:v0.3.0'
    // 以下按需引入
    // 使用Glide、Picasso
    compile 'com.github.chrisbanes:PhotoView:v1.2.4'
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
    
    // 使用Fresco
    compile('com.facebook.fresco:fresco:0.14.1', {
        exclude group: 'com.android.support'
    })
    // 提取自Fresco sample源码
    compile 'com.github.XuDaojie:FrescoZoomable:v0.1.0'
}
```

## Thanks 
[得到ImageView中drawable显示的区域的计算方法](http://www.cnblogs.com/tianzhijiexian/p/4104836.html)
<br>[android-testing](https://github.com/googlesamples/android-testing)