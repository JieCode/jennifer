package com.jennifer.jennifer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jingjie
 * @date :2020/10/19 16:12
 * TODO:
 */
public class HoverItemEntity {
    private String firstSpell;
    private List<TermsEntity> contentList = new ArrayList<>();

    public String getFirstSpell() {
        return firstSpell;
    }

    public void setFirstSpell(String firstSpell) {
        this.firstSpell = firstSpell;
    }

    public List<TermsEntity> getContentList() {
        return contentList;
    }

    public void setContentList(List<TermsEntity> contentList) {
        this.contentList = contentList;
    }
}
