package com.ats.easyrto.model;

public class WorkDetail {

    private int workDetailId;
    private int workId;
    private String workDesc;
    private int userId;
    private int innerTaskId;
    private String date;
    private String dateTime;
    private int isUsed;
    private String exStr1;
    private String exStr2;
    private int exInt1;
    private int exInt2;
    private String date1;
    private String date2;

    public WorkDetail(int workId, String workDesc, int userId, int innerTaskId, String date, String dateTime, int isUsed) {
        this.workId = workId;
        this.workDesc = workDesc;
        this.userId = userId;
        this.innerTaskId = innerTaskId;
        this.date = date;
        this.dateTime = dateTime;
        this.isUsed = isUsed;
    }

    public int getWorkDetailId() {
        return workDetailId;
    }

    public void setWorkDetailId(int workDetailId) {
        this.workDetailId = workDetailId;
    }

    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getInnerTaskId() {
        return innerTaskId;
    }

    public void setInnerTaskId(int innerTaskId) {
        this.innerTaskId = innerTaskId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
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
        return "WorkDetail{" +
                "workDetailId=" + workDetailId +
                ", workId=" + workId +
                ", workDesc='" + workDesc + '\'' +
                ", userId=" + userId +
                ", innerTaskId=" + innerTaskId +
                ", date='" + date + '\'' +
                ", dateTime='" + dateTime + '\'' +
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
