# RxMVVM
一款基于mvvm+databinding+Google AAC+rxlifecycle+rxbinding+retrofit2+rxjava2+bindingcollectionadapter架构的快速开发框架

兼容AndroidX,适配Android Q,集成了各个好用的框架，自动适配，沉浸式状态栏，下拉刷新，返回键监听，6.0权限请求，美团多渠道打包，网络层契合RESTful风格，从此告别setText()、setOnClickListener()、Glide.with()、recyclerview.setAdapter()、recyclerview.setLayoutManager()等，框架会逐渐丰富，使用方式也会逐渐更新

## 依赖
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
	 implementation 'com.github.Florizt:RxMVVM:v1.0.3'
	}
```

## 用法
#### 初始化
- 基础用法（已经可以满足日常使用），需要在application的onCreate()中调用,已实现自动适配。

```java
RxMVVMInitializer.getInstance().init(this, appConfig);
```
#### AppConfig
| 方法      | 描述 |
| --------- | -----:|
| setCrashHandler  | 上报错误信息接口，仅支持BuildConfig.RELEASE环境 |
| setDesignWidthInDp     |   设计底稿的宽度，单位为dp（必填） |
| setDesignHeightInDp      |    设计底稿的高度，单位为dp（必填） |
| setHttpHostName      |    主机名，验证用 |
| setHttpDebugUrl      |    测试服url（必填） |
| setHttpReleaseUrl      |    正式服url（必填） |
| setCookieNetworkTime      |    有网络http缓存时长 |
| setCookieNoNetworkTime      |    无网络http缓存时长 |
| setHttpSuccessCode      |    http成功码 |
| setHeader      |    http请求头 |

#### BaseActivity
| 方法      | 描述 |
| --------- | -----:|
| initParam  | 页面接受的参数方法，在setContentView之前 |
| initView(savedInstanceState)     |   初始化界面 |
| initData      |    页面数据初始化方法 |
| initViewObservable      |    页面事件监听的方法，一般用于ViewModel层转到View层的事件注册 |
| initLayoutId      |    初始化根布局 |
| initVariableId      |    初始化ViewModel的id |
| isExit      |    按返回键是否只是返回 |
| doSthIsExit      |    按返回键仅仅只是返回上个界面时要做的操作 |
| requestPermission      |    请求权限 |
| showPermissionDialog      |    权限申请失败，如果需要弹窗，自己实现 |
| permissionDenied      |    权限申请失败，不弹窗 |
| permissionGranted      |    权限申请成功 |
| permissionGrantedOrDenineCanDo      |    权限申请成功或者失败都要执行 |
| loadRootFragment      |    添加fragment |
| showFragment      |    显示fragment |
| hideFragment      |    隐藏fragment |
| restartAutoSize      |    重新开启适配 |
| stopAutoSize      |    停止适配 |
| hideSoftKeyBoard      |    隐藏软键盘 |

#### loading弹窗
> 继承BaseActivity/BaseFragment/BaseDialogFragment：
 ```java
showLoading()
dismissLoading()
 ```
 > 继承BaseViewModel：
  ```java
getUC().getShowDialogEvent().call();
getUC().getDismissDialogEvent().call();
  ```

#### 沉浸式状态栏，继承BaseActivity即可，[具体用法](https://github.com/Florizt/ImmersionBar)
 ```java
immersionBarEnabled(); //是否开启沉浸式，默认true
isFullScreen(); //沉浸式下是否全屏，默认false
statusBarDarkFont(); //沉浸式非全屏下状态栏字体是否深色，默认true，vivo手机由于状态栏字体颜色无法修改，所以默认0.2f的透明度
statusBarColor(); //沉浸式非全屏下状态栏背景颜色，默认白色
navigationBarColor(); //沉浸式非全屏下底部导航栏背景颜色，默认黑色
 ```

#### 下拉刷新，[具体用法](https://github.com/scwang90/SmartRefreshLayout)

#### 美团多渠道打包，如果需要拿到渠道号上传到统计平台，实现方法如下,必须先在application的onCreate()中调用RxMVVMInitializer.init(this)：
```java
RxMVVMInitializer.getInstance().getChannel();
```

#### 事件总线EventBus
```java
EventBus.getDefault().post(new MessageEvent(int type, Object src, Map<String, Object> extra)); //发送
onMessageEvent(MessageEvent event); //需要继承BaseViewModel,重写此方法即可
```

#### BackgroundLibrary，通过标签直接生成shape，无需再写shape.xml，[具体用法](https://github.com/JavaNoober/BackgroundLibrary)

#### 已实现BaseEntity的toString方法，继承BaseEntity，直接toString

#### 已集成Glide，通过实现BindingAdapter,再也不用写Glide.with(this);直接在xml里写：
```java
app:url="@{viewModel.userHeadimg}"
```

## 已封装retrofit2+rxjava2+okhttp,具体用法稍后更新

## 已实现bindingcollectionadapter，从此告别setAdapter();,具体用法稍后更新


## 联系我 ##
- QQ群 1036651333（问题交流）














