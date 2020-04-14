# RxMVVM
一款基于mvvm+databinding+Google AAC+rxlifecycle+rxbinding+retrofit2+rxjava2的架构

## 使用
#### android studio
> Add it in your root build.gradle at the end of repositories:
```java
 allprojects {
		repositories {
 			...
			maven { url 'https://jitpack.io' }
 		}
 	}
```
> Add the dependency:
```java
dependencies {
	 implementation 'com.github.Florizt:RxMVVM:v1.0.0'
	}
```

## 用法
### 初始化
- 基础用法（已经可以满足日常使用），需要在application的onCreate()中调用。

```java
RxMVVMInitializer.init(this);//默认设计底稿为1080p

RxMVVMInitializer.init(this,360,640);//可以自己传入设计底稿的尺寸，单位为dp
```


## 已实现沉浸式状态栏
### [具体用法参考](https://github.com/Florizt/ImmersionBar)
### 项目已封装几个方法，继承BaseActivity即可：
 ```java
immersionBarEnabled(); //是否开启沉浸式，默认true
isFullScreen(); //是否全屏，默认false
statusBarDarkFont(); //状态栏字体是否深色，默认true
 ```


## 已实现下拉刷新
### [具体用法参考](https://github.com/scwang90/SmartRefreshLayout)

## 已实现软键盘监听
### 项目已封装几个方法，继承BaseActivity即可：
```java
onKeyboardChange(boolean isPopup, int keyboardHeight); //软键盘弹起/隐藏
```

## 已实现返回按键监听
### 项目已封装几个方法，继承BaseActivity即可：
```java
isExit(); //按返回键是否只是返回，默认true
doSthIsExit(); //按返回键仅仅只是返回上个界面时要做的操作
```

## 已实现6.0权限请求
### [具体用法参考](https://github.com/tbruyelle/RxPermissions)
### 项目已封装几个方法，继承BaseActivity即可：
```java
requestPermission(final int requestCode, final boolean showDialog, String... permissions); //请求权限
showPermissionDialog(int requestCode); //权限申请失败，如果需要弹窗，实现这个
permissionDenied(int requestCode); //权限申请失败，不弹窗
permissionGranted(int requestCode); //权限申请成功
permissionGrantedOrDenineCanDo(int requestCode); //权限申请成功或者失败都要执行
```

## 已实现美团多渠道打包
### 如果需要拿到渠道号上传到统计平台，实现方法如下,必须先在application的onCreate()中调用RxMVVMInitializer.init(this)：
```java
RxMVVMInitializer.getChannel();
```

## 已实现事件总线EventBus
### 用法如下：
```java
EventBus.getDefault().post(new MessageEvent(int type,Object src)); //发送
onMessageEvent(MessageEvent event); //需要继承BaseViewModel,重写此方法即可
```

## 已实现BackgroundLibrary，通过标签直接生成shape，无需再写shape.xml
### [具体用法参考](https://github.com/JavaNoober/BackgroundLibrary)

## 已实现BaseEntity的toString方法，继承BaseEntity，通过打印toString，数据一览无遗

## 已集成Glide，通过实现BindingAdapter,再也不用写Glide.with(this);直接在xml里写：
```java
app:url="@{viewModel.userHeadimg}"
```

## 已封装retrofit2+rxjava2+okhttp,具体用法稍后更新

## 已实现bindingcollectionadapter，从此告别setAdapter();,具体用法稍后更新


## 联系我 ##
- QQ群 1036651333（问题交流）














