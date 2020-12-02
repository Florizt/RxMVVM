package com.rx.rxmvvmlib.binding.viewadapter.image;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rx.rxmvvmlib.R;

import androidx.databinding.BindingAdapter;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes) {
        if (!TextUtils.isEmpty(url)) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(url)
                    .apply(new RequestOptions().placeholder(placeholderRes != 0 ? placeholderRes : R.drawable.placeholder_bg))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, int resId, int placeholderRes) {
        if (resId != 0) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(resId)
                    .apply(new RequestOptions().placeholder(placeholderRes != 0 ? placeholderRes : R.drawable.placeholder_bg))
                    .into(imageView);
        }
    }

    @BindingAdapter(value = {"url", "placeholderRes"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Drawable drawable, int placeholderRes) {
        if (drawable != null) {
            //使用Glide框架加载图片
            Glide.with(imageView.getContext())
                    .load(drawable)
                    .apply(new RequestOptions().placeholder(placeholderRes != 0 ? placeholderRes : R.drawable.placeholder_bg))
                    .into(imageView);
        }
    }
}

