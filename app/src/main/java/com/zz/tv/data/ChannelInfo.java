package com.zz.tv.data;

/**
 * Created by zhangxiaohui on 2017/6/2.
 */

public class ChannelInfo {
    private int index; //频道的顺序索引，用于表示频道在频道列表中的顺序，从0开始，排在后面的频道依次增1
    private int number;     //频道号 例如CCTV1的频道号为1
    private String name;    //频道名称
    private String[] urls;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
