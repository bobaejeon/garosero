package com.foo.garosero.myUtil;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageHelper {
    static public boolean showImage(Context context, String url_string, ImageView imageView){
        try{
            Glide.with(context).load(url_string).into(imageView);
            return true;
        } catch (Exception e){
            Log.w("ImageHelper", "해당 경로에 파일이 존재하지 않습니다.");
            return false;
        }
    }
}
