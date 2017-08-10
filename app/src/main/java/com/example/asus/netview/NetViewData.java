package com.example.asus.netview;

/**
 * Created by asus on 2017/8/10.
 * 每一个数据的实体类
 */

public class NetViewData {

    private String title;//标题
    private float value;//值

    public NetViewData(String title, float value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
