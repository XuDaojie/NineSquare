# UNDONE NineSquare
[![](https://jitpack.io/v/XuDaojie/NineSquare.svg)](https://jitpack.io/#XuDaojie/NineSquare)
<br>
查看大图乞丐版封装
## Use
目前支持Glide、Picasso加载图片

``` java
// mThumbnails 为缩略图目前还没卵用
ZoomActivity.startActivity(MainActivity.this, ImageLoader.PICASSO, mImages, mThumbnails, mCurrentImgPosition);
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
    compile 'com.github.XuDaojie:NineSquare:v0.2.0'
    // 按需引入
    compile 'com.github.bumptech.glide:glide:3.7.0'
    compile 'com.squareup.picasso:picasso:2.5.2'
}
```

## Thanks 
[得到ImageView中drawable显示的区域的计算方法](http://www.cnblogs.com/tianzhijiexian/p/4104836.html)
<br>[android-testing](https://github.com/googlesamples/android-testing)