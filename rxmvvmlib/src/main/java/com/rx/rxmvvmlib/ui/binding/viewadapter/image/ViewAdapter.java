package com.rx.rxmvvmlib.ui.binding.viewadapter.image;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public final class ViewAdapter {
    @BindingAdapter(value = {"url", "placeholderRes", "strategy"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, Drawable placeholderRes, DiskCacheStrategy strategy) {
        //使用Glide框架加载图片
        RequestBuilder<Drawable> load = Glide.with(imageView.getContext())
                .load(url);
        if (placeholderRes != null) {
            load = load.apply(new RequestOptions().placeholder(placeholderRes));
        }
        if (strategy != null) {
            load = load.diskCacheStrategy(strategy);
        }
        load.into(imageView);
    }

    @BindingAdapter(value = {"url", "placeholderRes", "strategy"}, requireAll = false)
    public static void setImageUri(ImageView imageView, int resId, Drawable placeholderRes, DiskCacheStrategy strategy) {
        //使用Glide框架加载图片
        RequestBuilder<Drawable> load = Glide.with(imageView.getContext())
                .load(resId);
        if (placeholderRes != null) {
            load = load.apply(new RequestOptions().placeholder(placeholderRes));
        }
        if (strategy != null) {
            load = load.diskCacheStrategy(strategy);
        }
        load.into(imageView);
    }

    @BindingAdapter(value = {"url", "placeholderRes", "strategy"}, requireAll = false)
    public static void setImageUri(ImageView imageView, Drawable drawable, Drawable placeholderRes, DiskCacheStrategy strategy) {
        //使用Glide框架加载图片
        RequestBuilder<Drawable> load = Glide.with(imageView.getContext())
                .load(drawable);
        if (placeholderRes != null) {
            load = load.apply(new RequestOptions().placeholder(placeholderRes));
        }
        if (strategy != null) {
            load = load.diskCacheStrategy(strategy);
        }
        load.into(imageView);
    }
}

