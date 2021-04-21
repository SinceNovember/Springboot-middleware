package com.simple.dto;

/**
 *  Binlog数据传输对象
 */
public class BinlogDto {

    private String event;

    private Object value;

    public BinlogDto() {
    }

    public BinlogDto(String event, Object value) {
        this.event = event;
        this.value = value;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
