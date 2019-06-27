package com.sucl.springbootwebsocket.websocket.cache;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 缓存的实际数据
 * @author sucl
 * @date 2019/5/9
 */
@Data
public class SimpleCache implements Serializable {
    private static final long serialVersionUID = -2698474772251528333L;

    private String id;
    private Object data;
    private Date date = new Date();

    public SimpleCache(Object data) {
        this.data = data;
    }
}
