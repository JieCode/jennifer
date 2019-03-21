package com.jennifer.jennifer.ui.palette.bean;

/**
 * Created by wutongtech_shengmao on 2017/6/9 09:01.
 * 作用：线条的点
 */
public class LinePath {

    private int x;
    private int y;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LinePath{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
