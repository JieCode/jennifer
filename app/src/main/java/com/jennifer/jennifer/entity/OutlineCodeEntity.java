package com.jennifer.jennifer.entity;

import java.io.Serializable;

/**
 * @author jingjie
 * @date :2020/10/19 16:43
 * TODO:
 */
public class OutlineCodeEntity implements Serializable {
    /**
     * category : 2
     * id : 2106
     * name : 六年级
     * parentId : 10
     * slug : grade6
     * wordsCount : 426
     */

    private int category;
    private int id;
    private String name;
    private int parentId;
    private String slug;
    private int wordsCount;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public void setWordsCount(int wordsCount) {
        this.wordsCount = wordsCount;
    }
}
