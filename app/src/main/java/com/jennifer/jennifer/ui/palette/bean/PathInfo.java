package com.jennifer.jennifer.ui.palette.bean;

import java.util.List;

/**
 * Created by wutongtech_shengmao on 2017/6/9 09:11.
 * 作用：一条笔迹
 */
public class PathInfo {

    private List<LinePath> lineData;
    private int lineColor;
    private String sendTime;

    private int lineWidth;

//    private int type = 0;//默认为0，正常笔记 1橡皮擦


    public PathInfo() {
    }

    public PathInfo(List<LinePath> lineData, int lineColor, String sendTime, int lineWidth) {
        this.lineData = lineData;
        this.lineColor = lineColor;
        this.sendTime = sendTime;
        this.lineWidth = lineWidth;
    }
//    public PathInfo(List<LinePath> lineData, int lineColor, String sendTime) {
//        this.lineData = lineData;
//        this.lineColor = lineColor;
//        this.sendTime = sendTime;
//    }


//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public List<LinePath> getLineData() {
        return lineData;
    }

    public void setLineData(List<LinePath> lineData) {
        this.lineData = lineData;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }
}
