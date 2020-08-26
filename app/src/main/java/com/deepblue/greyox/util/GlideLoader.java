package com.deepblue.greyox.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mdx.framework.Frame;

/**
 * Created by DELL on 2019/10/16.
 */

public class GlideLoader {
    public static void loadImage(String url, ImageView img, int i) {
        Glide.with(Frame.CONTEXT).load(url).error(i) //异常时候显示的图片
                .placeholder(i) //加载成功前显示的图片
                .skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.ALL).fallback(i).into(img);
    }

    public static void loadImage_error(String url, ImageView img) {
        Glide.with(Frame.CONTEXT).load(url).error(img.getDrawable()) //异常时候显示的图片
                .placeholder(img.getDrawable()).into(img);
    }

//    public static void loadImageRotated(String url, ImageView img, float rotateRotationAngle) {
//        Glide.with(Frame.CONTEXT)
//                .load(url)
//                .transform(new RotateTransformation(Frame.CONTEXT, rotateRotationAngle))
//                .into(img);
//    }
//
//    public static void loadImageCrop(String url, ImageView img) {
//        Glide.with(Frame.CONTEXT)
//                .load(url)
//                .transform(new CropTransformation(Frame.CONTEXT))
//                .into(img);
//    }
}
