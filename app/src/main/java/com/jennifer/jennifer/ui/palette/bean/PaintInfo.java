package com.jennifer.jennifer.ui.palette.bean;

/**
 * Created by wutongtech_shengmao on 2017/6/9 09:21.
 * 作用：一个PaintInfo 相当于一笔。
 *
 */
public class PaintInfo {

    private String paint;
    private int paint_id;
    private String createtime;

    public PaintInfo() {
    }

    public PaintInfo(String paint, int paint_id) {
        this.paint = paint;
        this.paint_id = paint_id;
    }

    public String getPaint() {
        return paint;
    }

    public void setPaint(String paint) {
        this.paint = paint;
    }

    public int getPaint_id() {
        return paint_id;
    }

    public void setPaint_id(int paint_id) {
        this.paint_id = paint_id;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }
}
