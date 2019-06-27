package com.sucl.springbootwebsocket.websocket;

import org.apache.tomcat.websocket.WsSession;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sucl
 * @date 2019/5/9
 */

@ServerEndpoint(value = "/websocket")
public class WsServerEndpoint {
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    private static CopyOnWriteArraySet<WsServerEndpoint> webSocketSet = new CopyOnWriteArraySet<WsServerEndpoint>();

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
    }

    @OnMessage
    public void onMessage(String message, Session session) {

    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message,String wsId){
        for(WsServerEndpoint wse : webSocketSet){
            WsSession wsSession = (WsSession)wse.session;
            if(wsSession.getHttpSessionId().equals(wsId)){
                try {
                    wse.session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        try {
//            this.session.getBasicRemote().sendText(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static int getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        WsServerEndpoint.onlineCount.incrementAndGet();
    }

    public static void subOnlineCount() {
        WsServerEndpoint.onlineCount.decrementAndGet();
    }
}
