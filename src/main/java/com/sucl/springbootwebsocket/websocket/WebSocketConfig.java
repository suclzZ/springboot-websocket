package com.sucl.springbootwebsocket.websocket;

import com.sucl.springbootwebsocket.websocket.cache.TaskCacheManager;
import com.sucl.springbootwebsocket.websocket.handler.TaskProcessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author sucl
 * @date 2019/5/9
 */
@Configuration
@ConditionalOnProperty(value = "websocket.enable",havingValue = "true",matchIfMissing = true)
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter ();
    }

    @Bean
    public WsServerEndpoint wsServerEndpoint(){
        return new WsServerEndpoint();
    }


    @Bean
    public TaskProcessHandler taskProcessHandler(){
        TaskProcessHandler taskProcessHandler = new TaskProcessHandler();
        taskProcessHandler.setWsServerEndpoint(wsServerEndpoint());
        taskProcessHandler.setTaskCacheManager(TaskCacheManager.getInstance());
        return taskProcessHandler;
    }
}
