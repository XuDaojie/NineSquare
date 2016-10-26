# UNDONE NineSquare
[![](https://jitpack.io/v/XuDaojie/NineSquare.svg)](https://jitpack.io/#XuDaojie/NineSquare)
<br>
查看大图乞丐版封装
## Use
使用Glide加载图片

``` java
ZoomActivity.startActivity(mContext, mImages, mThumbnails, mCurrentImgPosition);
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
``` gralde
dependencies {
    compile 'com.github.XuDaojie:NineSquare:9efa688515'
}
```

[得到ImageView中drawable显示的区域的计算方法](http://www.cnblogs.com/tianzhijiexian/p/4104836.html)
[android-testing](https://github.com/googlesamples/android-testing)