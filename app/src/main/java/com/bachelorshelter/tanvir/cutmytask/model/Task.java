package com.bachelorshelter.tanvir.cutmytask.model;

/**
 * Created by Tanvir on 2017-05-31.
 */

public class Task {
    private int id;
    private int action;
    private String date;
    private String taskDesc;
    private String backColor;

    public Task() {
    }

    public Task(String date, String taskDesc, String backColor,int action) {
        this.date = date;
        this.taskDesc = taskDesc;
        this.backColor = backColor;
        this.action = action;
    }

    public Task(int id, String date, String taskDesc, String backColor,int action) {
        this.id = id;
        this.date = date;
        this.taskDesc = taskDesc;
        this.backColor = backColor;
        this.action = action;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
