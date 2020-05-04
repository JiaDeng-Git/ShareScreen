# ShareScreen App

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