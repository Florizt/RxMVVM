```
架构模式 + AOP思想
View-------------------↓
    databinding        ↓
    lifecycle          ↓
    rxbinding          ↓
    Arouter            ↓
ViewModel--------------↓
    AndroidViewModel   ↓
Repository-------------↓
                       ↓
                       ↓
                       ↓
Model------------------↓
     ↓
     ↓
     ↓
     ↓→ → → → → → → Remote
     ↓                  Retrofit
     ↓                  Okhttp
     ↓                  Rxjava
     ↓→ → → → → → → Local
                        SharedPreferences
                        Greendao
```
--------------------------------------------------------------------------------------------------------
```
module-app：
aop
  --anno
    --切面注解
  Aspect切面类
core
  --audio 音频核心类
    AudioRecord + AudioTrack 音频录制类（开始、暂停、停止、播放）
    音频合并
    MediaCodec + MediaFormat 音频压缩转码
  --camera
    系统相机（打开相册、拍照、录像）
    MediaRecorder + Camera + Mp4Parser 自定义相机（拍照、录像）,录像支持暂停+分片+合并
repository
  --config app配置类
  --datasource
    --local 本地服务接口
    --remote 远程服务接口
  --entity 数据模型类
  --greendao db管理类
  --impl repository接口实现类
  repository接口
ui
  --activity 用户窗口
  --widge 组件
videmodel
  --item 列表item viewmodel类
  viewmodel类
```