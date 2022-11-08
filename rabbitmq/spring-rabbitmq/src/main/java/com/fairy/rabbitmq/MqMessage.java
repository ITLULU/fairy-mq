package com.fairy.rabbitmq;

import java.io.Serializable;

/**
 * @author 鹿少年
 * @date 2022/11/8 19:04
 */
public class MqMessage implements Serializable {

    private String id;
    private String message;
    private String time;

    public MqMessage(String id, String message, String time) {
        this.id = id;
        this.message = message;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
