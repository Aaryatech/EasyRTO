package com.ats.easyrto.model;

public class WorkTypeModel {

    private int workTypeId;
    private String workTypeName;
    private String workImage1;
    private String workImage2;
    private int wType;
    private String lastUpdateTime;
    private int isUsed;
    private String exStr1;
    private String exStr2;
    private int exInt1;
    private int exInt2;
    private String date1;
    private String date2;

    public int getWorkTypeId() {
        return workTypeId;
    }

    public void setWorkTypeId(int workTypeId) {
        this.workTypeId = workTypeId;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public String getWorkImage1() {
        return workImage1;
    }

    public void setWorkImage1(String workImage1) {
        this.workImage1 = workImage1;
    }

    public String getWorkImage2() {
        return workImage2;
    }

    public void setWorkImage2(String workImage2) {
        this.workImage2 = workImage2;
    }

    public int getwType() {
        return wType;
    }

    public void setwType(int wType) {
        this.wType = wType;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public String getExStr1() {
        return exStr1;
    }

    public void setExStr1(String exStr1) {
        this.exStr1 = exStr1;
    }

    public String getExStr2() {
        return exStr2;
    }

    public void setExStr2(String exStr2) {
        this.exStr2 = exStr2;
    }

    public int getExInt1() {
        return exInt1;
    }

    public void setExInt1(int exInt1) {
        this.exInt1 = exInt1;
    }

    public int getExInt2() {
        return exInt2;
    }

    public void setExInt2(int exInt2) {
        this.exInt2 = exInt2;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    @Override
    public String toString() {
        return "WorkTypeModel{" +
                "workTypeId=" + workTypeId +
                ", workTypeName='" + workTypeName + '\'' +
                ", workImage1='" + workImage1 + '\'' +
                ", workImage2='" + workImage2 + '\'' +
                ", wType=" + wType +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", isUsed=" + isUsed +
                ", exStr1='" + exStr1 + '\'' +
                ", exStr2='" + exStr2 + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", date1='" + date1 + '\'' +
                ", date2='" + date2 + '\'' +
                '}';
    }
}
