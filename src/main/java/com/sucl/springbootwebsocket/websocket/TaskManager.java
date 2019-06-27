package com.sucl.springbootwebsocket.websocket;

import java.util.Vector;
import java.util.concurrent.*;

/**
 * 任务执行
 * @author sucl
 * @date 2019/5/9
 */
public class TaskManager {
    private ExecutorService service = new ThreadPoolExecutor(
            10, 10,0,TimeUnit.MILLISECONDS,new LinkedBlockingQueue<Runnable>());//Executors.newFixedThreadPool(10);

    public void execute(Runnable task){
        service.execute(task);
    }

    public Future<?> submit(Runnable task){
        return service.submit(task);
    }

    public void shutdown(boolean now , Callable call){
        if(now){
            service.shutdownNow();
        }else{
            service.shutdown();
        }
        if(call!=null){
            try {
                service.awaitTermination(30,TimeUnit.SECONDS);
                call.call();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException{

        TaskManager taskManager = new TaskManager();
        Vector<Future> vector = new Vector();
        for(int i=0;i<15 ;i++){
            Future<?> f = taskManager.submit(new Runnable() {
                @Override
                public void run() {
                    long v = ((long) (Math.random() * 100000)) % 2;
                    if (v == 0) {
                        throw new RuntimeException("error:" + v);
                    } else {
                        taskManager.println(v);
                    }
                }
            });
            try {
                if(f.get()==null){

                }
            } catch (ExecutionException e) {
//                e.printStackTrace();
                System.err.println(e.getCause().getMessage());
            }
        }

        taskManager.service.shutdown();
        taskManager.service.awaitTermination(10, TimeUnit.SECONDS);

//        for(Future future : vector){
//            System.out.println(future.get());
//        }

        if (taskManager.service.isShutdown()){
            taskManager.println("shutdown");
        }
        if (taskManager.service.isTerminated()){
            taskManager.println("over");
        }

    }

    public synchronized void println(Object info){
        System.out.println(info);
    }

}
