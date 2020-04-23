package com.dengjia.lib_share_core.EventRouter;

import android.util.Log;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// 已知Bug
//
// 存在Bug，取消订阅的话，除了收不到信令交互过程，还收不到socket过程。

/**
 * 修复Bug
 *
 * 已修复订阅者重加Bug
 *
 */

public class EventRouter {

    private static final String TAG = "EventRouter";

    public static EventRouter mInstance;
    public static EventRouter getInstance(){
        synchronized (EventRouter.class){
            if (mInstance == null){
                mInstance = new EventRouter();
            }
        }
        return mInstance;
    }

    private EventRouter() {
        init();
    }

    private static void init(){
        addEventType("AsrEvent");
        addEventType("RtcEvent");
    }

    // 事件订阅者HashMap
    private static HashMap<String, List<Object>> orders = new HashMap<>();

    // 添加事件类型
    public static void addEventType(String eventName){
        if (orders != null) {
            orders.put(eventName, new ArrayList<Object>());
        }else {
            Log.e(TAG, "订阅者列表丢失！！！");
        }
    }

    // 移除事件类型
    public static void removeEventType(String eventName){
        if (orders != null) {
            orders.remove(eventName);
        }else {
            Log.e(TAG, "订阅者列表丢失！！！");
        }
    }

    // 订阅事件，默认订阅所有事件
    public static void orderEvent(Object order){
        if (orders != null) {
            Iterator<Map.Entry<String, List<Object>>> iterator = orders.entrySet().iterator();
            while (iterator.hasNext()){
                Map.Entry<String, List<Object>> entry = iterator.next();
                entry.getValue().add(order);
            }
        }else {
            Log.e(TAG, "订阅者列表丢失！！！");
        }
    }

    // 订阅事件，订阅指定类型事件
    public static void orderEvent(Object order, String eventType){
        if (orders != null) {
            orders.get(eventType).add(order);
        }else {
            Log.e(TAG, "订阅者列表丢失！！！");
        }
    }

    // 取消订阅事件
    public static void cancleOrderEvent(Object order){
        // TODO 添加取消订阅逻辑
//        if (ordersList != null) {
//            if (ordersList.remove(order)){
//                Log.i(TAG, "已取消订阅");
//            }else{
//                Log.e(TAG, "取消订阅失败！！！");
//            }
//        }else {
//            Log.e(TAG, "订阅者列表丢失！！！");
//        }
    }

    // 清除所有事件的所有订阅者
    public static void clearOrderList(){
        if (orders != null) {
            orders.clear();
        }else {
            Log.e(TAG, "订阅者列表丢失！！！");
        }
    }

    // 事件传入
    public static void putEvent(String eventType, String event, Object data){
        sendEvent(eventType, event, data);
    }

    // 路由事件
    public static void sendEvent(String eventType, String event, Object data){

        if (orders != null) {
            List<Object> order = orders.get(eventType);

            try {
                Method method = order.getClass().getMethod(event, Object.class);
                method.invoke(order, data);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }else {
            Log.e(TAG, "订阅者列表丢失！！！");
        }
    }

}
