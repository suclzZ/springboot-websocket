package com.sucl.springbootwebsocket.websocket.handler;

import com.sucl.springbootwebsocket.websocket.WsServerEndpoint;
import com.sucl.springbootwebsocket.websocket.cache.TaskCacheManager;

import java.util.UUID;

/**
 *  通过 任务->缓存->通知 实现异步请求处理
 *  如果业务执行时间比较长，可以通过TaskManager去处理，将结果写入缓存，返回结果key，
 *  处理完成后通过NotifyManager告知前台，通过之前的key去查询真实结果
 * @author sucl
 * @since 2019/6/27
 */
public class TaskProcessHandler {
    private WsServerEndpoint wsServerEndpoint;
    private TaskCacheManager taskCacheManager;

    public void setWsServerEndpoint(WsServerEndpoint wsServerEndpoint) {
        this.wsServerEndpoint = wsServerEndpoint;
    }

    public void setTaskCacheManager(TaskCacheManager taskCacheManager) {
        this.taskCacheManager = taskCacheManager;
    }

    public void sendMessage(String id, String message, Object data){
        String key = UUID.randomUUID().toString();
        if(wsServerEndpoint!=null){
            wsServerEndpoint.sendMessage(buildKeyMessage(key,message),id);
        }
        if(taskCacheManager!=null){
            taskCacheManager.put(key,data);
        }
    }

    private String buildKeyMessage(String key, String message) {
        return String.format("消息【%s】,查询【%s】",message,key);
    }

}
