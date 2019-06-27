package com.sucl.springbootwebsocket.websocket.cache;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存管理
 * @author sucl
 * @date 2019/5/9
 */
public class TaskCacheManager {

    private Map<String,SimpleCache> cacheMap = new ConcurrentHashMap<>();
    private List<TimeRecord> timeRecords = Collections.synchronizedList(new ArrayList<>());
    private long survival = 10*60;//存活时间(s)
    private int limit = 50;//最大数量
    private int interval = 10;//检查间隔(s)

    private static class CacheManagerHolder{
        private static TaskCacheManager taskCacheManager = new TaskCacheManager();
    }

    public static TaskCacheManager getInstance(){
        return CacheManagerHolder.taskCacheManager;
    }

    private TaskCacheManager(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
            if(cacheMap.size() >= limit){
                intervalClean();
            }
            }
        },0,interval*1000);
    }

    private void intervalClean() {
        for(TimeRecord tr : timeRecords){
            if(System.currentTimeMillis()-tr.getDate().getTime()>=survival*1000){
                cacheMap.remove(tr.getId());
            }
        }
    }


    public void put(String key,Object data){
        SimpleCache cache = new SimpleCache(data);
        cacheMap.put(key,cache);
        timeRecords.add(new TimeRecord(key,cache.getDate()));
    }

    public Object get(String key){
        SimpleCache cache = cacheMap.get(key);
        Object data = cache.getData();
        if(data instanceof ErrorData){
            //缓存异常
        }
        return data;
    }

    public void clean(){
        cacheMap.clear();
    }

    public void remove(String key){
        cacheMap.remove(key);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    class TimeRecord {

        private String id;
        private Date date;

    }

    @Data
    class ErrorData {

        private String info;

    }
}
