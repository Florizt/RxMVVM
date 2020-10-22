# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\android_sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:EnclosingMethod
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#---------------------------------基本指令区----------------------------------
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-useuniqueclassmembernames
-dontpreverify
-dontoptimize
-ignorewarnings
-verbose
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keepattributes EnclosingMethod
#
##start混淆后报错打印报错行数
##将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
##end
#----------------------------------------------------------------------------

#---------------------------------实体类-----------------------------------
-dontwarn com.rx.mvvm.**
-keep class com.rx.mvvm.** { *; }
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.google.vending.licensing.ILicensingService

-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#support.v4/v7包不混淆
-keep class android.support.** { *; }
-keep class android.support.v4.** { *; }
-keep public class * extends android.support.v4.**
-keep interface android.support.v4.app.** { *; }
-keep class android.support.v7.** { *; }
-keep public class * extends android.support.v7.**
-keep interface android.support.v7.app.** { *; }
-keep public class * extends android.support.annotation.**
-dontwarn android.support.**
#AndroidX
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

-keep class android.support.v8.renderscript.** { *; }
-keep class androidx.renderscript.** { *; }

#保持注解继承类不混淆
-keep class * extends java.lang.annotation.Annotation {*;}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保留 Serializable序列化的类不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# 保留本地native方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#----------------------------------------------------------------------------

#---------------------------------WebView----------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
#----------------------------------------------------------------------------
#---------------------------------第三方jar包----------------------------------

#MaterialDialog
-dontwarn com.afollestad.materialdialogs.**
-keep class com.afollestad.materialdialogs.** { *;}

#fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn jp.wasabeef.glide.transformations.**
-keep class jp.wasabeef.glide.transformations.** {*;}
-dontwarn com.bumptech.glide.**
-keep class com.bumptech.glide.** { *;}

#logx
-dontwarn dgrlucky.log.**
-keep class dgrlucky.log.** { *; }

#google
-dontwarn com.google.**
-keep class com.google.** { *; }

# Gson
-keepattributes Signature
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# 使用Gson时需要配置Gson的解析对象及变量都不混淆。不然Gson会找不到变量。
# 将下面替换成自己的实体类

#immersionbar
-dontwarn com.gyf.barlibrary.**
-keep class com.gyf.barlibrary.** { *; }

#rxbinding2
-dontwarn com.jakewharton.rxbinding2.**
-keep class com.jakewharton.rxbinding2.** { *; }

#美团打包
-dontwarn com.meituan.android.walle.**
-keep class com.meituan.android.walle.** { *; }

#backgroundLib
-dontwarn com.noober.background.**
-keep class com.noober.background.** { *; }

#smartrefresh
-dontwarn com.scwang.smartrefresh.layout.**
-keep class com.scwang.smartrefresh.layout.** { *; }

#okhttp3
-dontwarn okhttp3.**
-keep class okhttp3.** { *;}

-dontwarn okio.**
-keep class okio.**{*;}

# Okio
-dontwarn com.squareup.**
-dontwarn okio.**
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }

# Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# RxJava RxAndroid
-dontwarn io.reactivex.**
-keep class io.reactivex.** { *; }
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#rxpermissions
-dontwarn com.tbruyelle.**
-keep class com.tbruyelle.**{*;}

#rxlifecycle2
-dontwarn com.trello.rxlifecycle2.**
-keep class com.trello.rxlifecycle2.**{*;}

#autosize
-dontwarn me.jessyan.autosize.**
-keep class me.jessyan.autosize.** { *; }

#bindingcollectionadapter2
-dontwarn me.tatarka.bindingcollectionadapter2.**
-keep class me.tatarka.bindingcollectionadapter2.** { *; }

#materialprogressbar
-dontwarn me.zhanghai.android.materialprogressbar.**
-keep class me.zhanghai.android.materialprogressbar.** { *; }

#commons
-dontwarn org.apache.commons.**
-keep class org.apache.commons.** { *; }

#org
-dontwarn org.**
-keep class org.** {*;}

#evenbus
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

#reactive-streams
-dontwarn org.reactivestreams.**
-keep class org.reactivestreams.** {*;}


#databinding
-dontwarn android.databinding.**
-keep class android.databinding.** { *; }

#lambda
-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*
#----------------------------------------------------------------------------