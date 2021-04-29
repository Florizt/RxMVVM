# RxMVVM
一项基于mvvm + databinding + Google AAC + rxlifecycle + rxbinding + retrofit2 + rxjava2 + bindingcollectionadapter + aspectj 架构的快速开发框架

兼容AndroidX,适配Android Q，自动适配，沉浸式状态栏，下拉刷新，返回键监听，6.0权限请求，美团多渠道打包，网络层契合RESTful风格，从此告别setText()、setOnClickListener()、Glide.with()、recyclerview.setAdapter()、recyclerview.setLayoutManager()等，框架会逐渐丰富，使用方式也会逐渐更新

## 依赖
### **android studio**
- Add it in your root build.gradle at the end of repositories:
```java
allprojects {
	repositories {
 		...
		maven { url 'https://jitpack.io' }
 	}
}
```
- Add the dependency:
```java
dependencies {
    implementation 'com.github.Florizt:RxMVVM:v2.0.1'
}
```

## 用法
### **初始化**
1，java代码方式：
在Application的onCreate()方法中添加：
```java
RxMVVMInit.getInstance().init(this);
```
此处建议使用两个参数的构造方法：
```
RxMVVMInit.getInstance().init(this, new ICfgsAdapter() {

            @Override
            public boolean debugEnable() {
                return false;
            }

            @Override
            public int designWidthInDp() {
                return 0;
            }

            @Override
            public int designHeightInDp() {
                return 0;
            }

            @Override
            public ICrashHandler crashHandler() {
                return null;
            }

            @Override
            public IActivityLifecycleCallbacks activityLifecycleCallbacks() {
                return null;
            }

            @Override
            public String floderName() {
                return null;
            }

            @Override
            public String httpBaseUrl() {
                return null;
            }

            @Override
            public String httpSuccessCode() {
                return null;
            }

            @Override
            public List<Interceptor> interceptors() {
                return null;
            }

            @Override
            public ICustomHttpCodeFilter customHttpCodeFilter() {
                return null;
            }
        });
```

2，注解方式：
在Application的onCreate()方法上使用：

```
@RxMVVMInitz(clazz = DemoCfgsAdapter.class)
@Override
public void onCreate() {
    super.onCreate();
}
```
需要自定义一个类实现ICfgsAdapter，并且传入注解。


### **ICfgsAdapter属性介绍：**
| 方法      | 描述 |
| --------- | -----:|
| debugEnable|是否是debug环境，release环境日志不输出|
| designWidthInDp|设计底稿的宽度，单位为dp（必填）|
| designHeightInDp|设计底稿的高度，单位为dp（必填）|
| crashHandler|框架自动捕获全局异常，如需上报错误日志，需实现ICrashHandler接口|
| activityLifecycleCallbacks|如需控制生命周期和管理堆栈，需继承IActivityLifecycleCallbacks,AppManager自动管理栈|
| floderName      |  文件系统，适配AndroidQ，最外层文件夹名称|
| httpBaseUrl      |    网络请求的baseUrl（必填） |
| httpSuccessCode      |    http成功码 |
| interceptors      |    http拦截器 |
| customHttpCodeFilter|http自定义code拦截，需实现ICustomHttpCodeFilter接口|


## View层
### **BaseActivity**
| 方法      | 描述 |
| --------- | -----:|
| initParam  | 页面接受的参数方法，在setContentView之前 |
| initView(savedInstanceState)     |   初始化界面 |
| initData      |    页面数据初始化方法 |
| initViewObservable      |    页面事件监听的方法，一般用于ViewModel层转到View层的事件注册 |
| initLayoutId      |    初始化根布局 |
| initVariableId      |    初始化ViewModel的id |
| initLoadingLayoutId      |    设置loading弹窗布局，有默认弹窗 |
| loadingCancelable      |    loading弹窗是否取消，默认false |
| immersionBarEnabled| 是否开启沉浸式，默认true|
| isFullScreen|沉浸式下是否全屏，默认false|
| fitsSystemWindows|沉浸式非全屏下是否自动处理安全区
| statusBarDarkFont|沉浸式非全屏下状态栏字体是否深色，默认true，vivo手机由于状态栏字体颜色无法修改，所以默认0.2f的透明度|
| statusBarColor|沉浸式非全屏下状态栏背景颜色，默认白色|
| navigationBarColor|沉浸式非全屏下底部导航栏背景颜色，默认黑色|
| isExit      |    按返回键是否只是返回，默认true |
| doSthIsExit      |    按返回键仅仅只是返回上个界面时要做的操作，默认finish() |
| loadRootFragment      |    添加fragment |
| showFragment      |    显示fragment |
| hideFragment      |    隐藏fragment |
| restartAutoSize      |    重新开启适配 |
| stopAutoSize      |    停止适配 |
| hideSoftKeyBoard      |    隐藏软键盘 |
| showLoading      |    显示loading弹窗 |
| dismissLoading      |    隐藏loading弹窗 |

###    **BaseDialogActivity**
继承BaseActivity，dialog形式的Activity。
| 方法      | 描述 |
| --------- | -----:|
| setGravity  | 弹窗的位置，默认中间 |
| setWindowAnimations     |   弹窗的动画，默认系统自带 |
| setDimAmount      |    弹窗的dim值，默认0.6f |
| setCanceledOnTouchOutside      |点击弹窗外部，弹窗是否消失，默认true|

###    **BaseFragment**
| 方法      | 描述 |
| --------- | -----:|
| initParam  | 页面接受的参数方法，在setContentView之前 |
| initView(savedInstanceState)     |   初始化界面 |
| initData      |    页面数据初始化方法 |
| initViewObservable      |    页面事件监听的方法，一般用于ViewModel层转到View层的事件注册 |
| initLayoutId      |    初始化根布局 |
| initVariableId      |    初始化ViewModel的id |
| initLoadingLayoutId      |    设置loading弹窗布局，有默认弹窗 |
| loadingCancelable      |    loading弹窗是否取消，默认false |
| loadRootFragment      |    添加fragment |
| showFragment      |    显示fragment |
| hideFragment      |    隐藏fragment |
| hideSoftKeyBoard      |    隐藏软键盘 |
| showLoading      |    显示loading弹窗 |
| dismissLoading      |    隐藏loading弹窗 |

###    **BaseDialogFragment**
| 方法      | 描述 |
| --------- | -----:|
| initParam  | 页面接受的参数方法，在setContentView之前 |
| initView(savedInstanceState)     |   初始化界面 |
| initData      |    页面数据初始化方法 |
| initViewObservable      |    页面事件监听的方法，一般用于ViewModel层转到View层的事件注册 |
| initLayoutId      |    初始化根布局 |
| initVariableId      |    初始化ViewModel的id |
| initLoadingLayoutId      |    设置loading弹窗布局，有默认弹窗 |
| loadingCancelable      |    loading弹窗是否取消，默认false |
| setCanceledOnTouchOutside| 点击外部是否消失，默认true|
| setGravity|弹窗的位置，默认中间|
| setWindowAnimations|弹窗的动画，默认没有|
| showLoading      |    显示loading弹窗 |
| dismissLoading      |    隐藏loading弹窗 |

###    **IActivityLifecycleCallbacks和AppManager**
像友盟的一些需要放在BaseActivity里初始化的方法就可以放在IActivityLifecycleCallbacks的实现类里进行。
AppManager自动管理了Activity栈。

###    **登录检测、登录完成后自动执行上一次的方法**
步骤1：在项目根目录新建aspectj-app.gradle文件
```
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'org.aspectj:aspectjtools:1.8.9'
    }
}

dependencies {
    implementation 'org.aspectj:aspectjrt:1.8.9'
}

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main

android.applicationVariants.all { variant ->
    JavaCompile javaCompile = variant.javaCompile
    javaCompile.doLast {
        String[] args = ["-showWeaveInfo",
                         "-1.5",
                         "-inpath", javaCompile.destinationDir.toString(),
                         "-aspectpath", javaCompile.classpath.asPath,
                         "-d", javaCompile.destinationDir.toString(),
                         "-classpath", javaCompile.classpath.asPath,
                         "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
        ]

        MessageHandler handler = new MessageHandler(true);
        new Main().run(args, handler)

        def log = project.logger
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    log.error message.message, message.thrown
                    break;
                case IMessage.WARNING:
                case IMessage.INFO:
                    log.info message.message, message.thrown
                    break;
                case IMessage.DEBUG:
                    log.debug message.message, message.thrown
                    break;
            }
        }
    }
}
```
步骤2：在app目录的build.gradle里引用
```
apply from: '../aspectj-app.gradle'
```
步骤3：在需要检测登录的方法上添加注解：@LoginCheck
reExecute：表示登录完成是否继续执行上一次操作，默认false
clazz：需要实现ILoginCheck接口，有两个方法：
--->isLogin()：是否已经登录
--->toLogin()：去登录的操作，如开启登录弹窗
```
binding.test.setOnClickListener(new View.OnClickListener() {
    @LoginCheck(reExecute = true,clazz = LoginCheckImpl.class)
    @Override
    public void onClick(View v) {

    }
});
```
如果登录完成需要继续执行上一次操作，请继续使用步骤4。
步骤4：在登录完成的方法上使用注解：@Retention
框架会自动执行上一次执行过的方法。
```
@LoginReExecute
private void loginSuccess() {
    LogUtil.i("登录成功");
}
```


### **权限检测**
步骤1：同”登录检测“的步骤1
步骤2：同”登录检测“的步骤2
步骤3：在需要权限检测的方法上添加注解：@PermissionCheck
permissions：需要检测的权限，是个数组
clazz：需要实现IPermissionCheckDenine接口，有一个方法：
--->permissionCheckDenine()：权限申请失败，用户自定义操作
```
binding.test.setOnClickListener(new View.OnClickListener() {
   @PermissionCheck(permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    clazz = PermissionCheckDenineImpl.class)
    @Override
    public void onClick(View v) {

    }
});
```

### **下拉刷新**，[具体用法](https://github.com/scwang90/SmartRefreshLayout)

### **BackgroundLibrary，通过标签直接生成shape，无需再写shape.xml**，[具体用法](https://github.com/JavaNoober/BackgroundLibrary)

### 已集成Glide，通过实现BindingAdapter,再也不用写Glide.with(this);直接在xml里写：
```java
app:url="@{viewModel.userHeadimg}"
```

### **美团多渠道打包，如果需要拿到渠道号上传到统计平台**：
```java
WalleChannelReader.getChannel(getApplicationContext(), "default"),
```

## ViewModel层
### **BaseViewModel**
拥有View的生命周期，网络请求等业务操作放在BaseViewModel里进行。
| 方法      | 描述 |
| --------- | -----:|
| getUC().getShowDialogEvent().call()  | 显示loading弹窗 |
| getUC().getDismissDialogEvent().call()     |   隐藏loading弹窗 |
| getUC().getHideSoftKeyBoardEvent().call()      |    隐藏软键盘 |
| getUC().getBackEvent().call()      |返回键|

### **ItemViewModel**
持有BaseViewModel对象的vm

### **MultiItemViewModel**
ItemViewModel的多type类


### **网络请求**

步骤1：
在你的http返回实体类中（例如：HttpResult），使用注解：
1.@HttpCode，代表内部定义的http错误码
2.@HttpMsg，代表内部定义的http错误信息
3.@HttpData，代表内部定义的http返回数据
```
public class HttpResult<D> {
    @HttpCode
    private String httpCode;
    @HttpMsg
    private String httpMsg;
    @HttpData
    private D content;

    public String getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(String httpCode) {
        this.httpCode = httpCode;
    }

    public String getHttpMsg() {
        return httpMsg;
    }

    public void setHttpMsg(String httpMsg) {
        this.httpMsg = httpMsg;
    }

    public D getContent() {
        return content;
    }

    public void setContent(D content) {
        this.content = content;
    }
}

```

步骤2：
自定义一接口，并使用步骤1中的实体类：
```java
public interface TestApi {
    @POST("test/add")
    Observable<HttpResult<String>> add();
}
```

步骤3：调用
```java
RetrofitFactory.apiService(TestApi.class).add()
          .map(new TFunc<HttpResult, String>())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new TObserver<String>() {
              @Override
              protected void onRequestStart() {

              }

              @Override
              protected void onRequestEnd() {

              }

              @Override
              protected void onSuccees(String s) {

              }

              @Override
              protected void onFailure(String message) {

              }
         });
```

### 事件总线EventBus
```java
EventBus.getDefault().post(new MessageEvent(int type, Object src, Map<String, Object> extra)); //发送
onMessageEvent(MessageEvent event); //需要继承BaseViewModel,重写此方法即可
```


## Model层
### **SingleLiveEvent**
继承LiveData，一个可被观察的数据。通过databinding，可实现view与viewModel绑定；通过LiveData，可实现view与mode解耦。


##  databinding + rxbinding使用
### **View**
| 方法      | 描述 |
| --------- | -----:|
|onClickCommand、isThrottleFirst | 点击事件、点击事件是否开启防止过快点击，默认开启。false--开启，true--不开启 |
| onLongClickCommand     |   长按事件 |
|currentView     |    回调控件本身 |
| requestFocus  |是否需要获取焦点|
|onFocusChangeCommand|焦点发生变化的事件|
|isVisible|view的显示隐藏|
|layout_width|设置view的宽度|
|layout_height|设置view的高度|
|selected|设置view是否选中|
|enable|设置view是否可用|
|margin_top|设置view的上外边距|
|margin_bottom|设置view的下外边距|
|margin_left|设置view的左外边距|
|margin_right|设置view的右外边距|

示例：
```
 <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        app:onClickCommand="@{viewModel.itemClick}">

 </LinearLayout>
```

```
public BindingCommand itemClick = new BindingCommand(new BindingAction() {
    @Override
    public void call() {

    }
});

public BindingCommand itemClick2 = new BindingCommand(new BindingConsumer<String>(){
    @Override
    public void call(String s) {

    }
});
```

### **TextView**
| 方法      | 描述 |
| --------- | -----:|
|text_color | 字体颜色|
| textSize  |字体大小|
|textStyle|字体粗细|
| movementMethod     |   文本可滚动时需要设置为true |
|flag     |    如需设置中划线等 |


### **EditText**
| 方法      | 描述 |
| --------- | -----:|
|requestFocus | 获取焦点|
| textChanged  |文字改变的监听|

### **ImageView**
| 方法      | 描述 |
| --------- | -----:|
|url | 设置图片，可以是url、资源id或者Drawable|
| placeholderRes  |占位图|


### **RecyclerView**
| 方法      | 描述 |
| --------- | -----:|
|adapter | 适配器|
| itemBinding  |item的布局|
| items  |数据|
| layoutManager  |可设置是否垂直滚动（canScrollVertically），默认true|

示例：
```
<data>

    <import type="me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter" />

    <import type="com.ymx.passenger.binding.viewadapter.recyclerview.LayoutManagers" />

    <import type="com.ymx.passenger.binding.viewadapter.recyclerview.LineManagers" />

    <variable
        name="adapter"
        type="BindingRecyclerViewAdapter" />

</data>

 <androidx.recyclerview.widget.RecyclerView
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:overScrollMode="never"
    android:scrollbars="none"
    android:splitMotionEvents="false"
    app:adapter="@{adapter}"
    app:itemBinding="@{viewModel.itembinding}"
    app:items="@{viewModel.list}"
    app:layoutManager="@{LayoutManagers.linear()}" />
```

## **联系我** ##
- QQ群 1036651333（问题交流）














