package com.jennifer.jennifer.entity;

import java.io.Serializable;

/**
 * @author jingjie
 * @date :2020/10/19 15:48
 * TODO:
 */
public class TermEntity implements Serializable {

    /**
     * gradeId : 2106
     * id : 2222
     * score : 0.0
     * terms : 资源
     */

    private int gradeId;
    private int id;
    private double score;
    private String terms;

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }
}
