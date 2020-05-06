# ShareScreen App

## 运行图
![运行图1](https://img-blog.csdnimg.cn/20200507004039423.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RqMjAxNw==,size_16,color_FFFFFF,t_70)
![运行图2](https://img-blog.csdnimg.cn/20200507004059719.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RqMjAxNw==,size_16,color_FFFFFF,t_70)
## 一、概述
公司为了在智能音箱产品系上，避免被掐脖。
所以，集中精力，综合多种开源技术，开发智能音箱App。


## 二、架构

1. 基础组件
   *  语音识别：  lib-share-asr
   *  音视频通信：lib-share-rtc
   *  安卓USB：   lib-share-usb
   *  网络：      lib-share-network

2. 核心组件
   * 调度基础组件：lib-share-core

3. 业务组件
   * 音视频通话：lib-share-avcall
   * 环境数据：lib-share-envir-data
   * 家居控制：lib-share-home-control

4. 项目组件


## 三、采用的开源项目
1. kaldi     语音识别
2. WebRTC    音视频通信
3. coTrun    NAT穿透
4. Socket.io 信令服务
