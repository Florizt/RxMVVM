package com.rx.rxmvvmlib.binding.viewadapter.textview;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rx.rxmvvmlib.util.UIUtils;

import java.math.BigDecimal;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.databinding.BindingAdapter;

/**
 * Created by wuwei
 * 2019/12/6
 * 佛祖保佑       永无BUG
 */
public final class ViewAdapter {
    @BindingAdapter("text_color")
    public static void setTextColor(TextView textView, int colorId) {
        textView.setTextColor(colorId);
    }

    @BindingAdapter("movementMethod")
    public static void setMovementMethod(TextView textView, boolean movementMethod) {
        if (movementMethod) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    @BindingAdapter("flag")
    public static void setFlag(TextView textView, int flag) {
        textView.getPaint().setFlags(flag | Paint.ANTI_ALIAS_FLAG); //中划线
    }

    @BindingAdapter("coupon_text")
    public static void setCouponText(TextView textView, String couponText) {
        if (!TextUtils.isEmpty(couponText)) {
            try {
                SpannableString span = new SpannableString(couponText);
                if (couponText.contains("¥")) {
                    span.setSpan(new AbsoluteSizeSpan(12, true), couponText.indexOf("¥"),
                            couponText.indexOf("¥") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(span);
                } else if (couponText.contains("折")) {
                    span.setSpan(new AbsoluteSizeSpan(12, true), couponText.indexOf("折"),
                            couponText.indexOf("折") + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    textView.setText(span);
                } else {
                    textView.setText(couponText);
                }
            } catch (Exception e) {
                textView.setText(couponText);
            }
        } else {
            textView.setText(couponText);
        }
    }

    @BindingAdapter(value = {"text", "autoSize", "autoSizeMin", "autoSizeMax"}, requireAll = false)
    public static void setText(AppCompatTextView textView, String text, boolean autoSize, int autoSizeMin, int autoSizeMax) {
        textView.setText(text);
        if (autoSize) {
            setAutoSize(textView, autoSizeMin, autoSizeMax);
        }
    }

    @BindingAdapter(value = {"textSize"}, requireAll = false)
    public static void setTextSize(AppCompatTextView textView, int size) {
        textView.setTextSize(size);
    }

    @BindingAdapter(value = {"textStyle"}, requireAll = false)
    public static void setTextStyle(AppCompatTextView textView, int style) {
        if (style == 0) {
            textView.setTypeface(Typeface.DEFAULT);
        } else if (style == 1) {
            textView.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            textView.setTypeface(Typeface.DEFAULT);
        }
    }

    public static void setAutoSize(final AppCompatTextView textView, final int autoSizeMin, final int autoSizeMax) {
        textView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup parent = (ViewGroup) textView.getParent();
                if (parent != null && parent instanceof LinearLayout) {
                    int maxWidth = parent.getWidth();

                    int width = 0;
                    for (int i = 0; i < parent.getChildCount(); i++) {
                        View childView = parent.getChildAt(i);
                        if (childView.getId() == textView.getId()) {//需要此功能的view一定要设置id
                            continue;
                        }
                        if (childView instanceof TextView) {
                            TextPaint paint = ((TextView) childView).getPaint();
                            width = width + (int) Layout.getDesiredWidth(((TextView) childView).getText().toString(), 0,
                                    ((TextView) childView).getText().length(), paint);
                        }
                    }

                    textView.setMaxLines(1);//如果想一行显示，不能设置singleLine，不然setAutoSizeTextTypeUniformWithConfiguration会无效

                    TextPaint paint = ((TextView) textView).getPaint();
                    paint.setTextSize(UIUtils.sp2px(autoSizeMax));//计算原始大小时，需要将字体大小恢复至原始值
                    int selfWidth = (int) Layout.getDesiredWidth(((TextView) textView).getText().toString(), 0,
                            ((TextView) textView).getText().length(), paint);//计算原始大小

                    // 一定要加上0.5f，不然有空白间距
                    BigDecimal bigDecimal = new BigDecimal((1f * (maxWidth - width) / selfWidth) * autoSizeMax + 0.5f);
                    BigDecimal scale = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_DOWN);

                    textView.setWidth(Math.min(selfWidth, maxWidth - width));
                    textView.setTextSize(Math.min(scale.intValue(), autoSizeMax));
                }
            }
        });
    }
}

