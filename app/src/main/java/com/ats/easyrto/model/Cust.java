package com.ats.easyrto.model;

public class Cust {

    private int custId;
    private String custName;
    private String custMobile;
    private String custPassword;
    private String custDob;
    private String custEmail;
    private String lastUpdateTime;
    private int isUsed;
    private String exStr1;
    private String exStr2;
    private int exInt1;
    private int exInt2;
    private String date1;
    private String date2;
    private String addPincode;
    private String sutTime;
    private String resp;


    public Cust(int custId, String custName, String custMobile, String custPassword, String custDob, String custEmail, String lastUpdateTime, int isUsed, String addPincode, String sutTime) {
        this.custId = custId;
        this.custName = custName;
        this.custMobile = custMobile;
        this.custPassword = custPassword;
        this.custDob = custDob;
        this.custEmail = custEmail;
        this.lastUpdateTime = lastUpdateTime;
        this.isUsed = isUsed;
        this.addPincode = addPincode;
        this.sutTime = sutTime;
    }

    public Cust(int custId, String custName, String custMobile, String custDob, String custEmail, String addPincode, String sutTime) {
        this.custId = custId;
        this.custName = custName;
        this.custMobile = custMobile;
        this.custDob = custDob;
        this.custEmail = custEmail;
        this.addPincode = addPincode;
        this.sutTime = sutTime;
    }

    public Cust(int custId, String custName, String custMobile, String custPassword, String custDob, String custEmail, String lastUpdateTime, int isUsed, String exStr1, String exStr2, int exInt1, int exInt2, String date1, String date2, String addPincode, String sutTime, String resp) {
        this.custId = custId;
        this.custName = custName;
        this.custMobile = custMobile;
        this.custPassword = custPassword;
        this.custDob = custDob;
        this.custEmail = custEmail;
        this.lastUpdateTime = lastUpdateTime;
        this.isUsed = isUsed;
        this.exStr1 = exStr1;
        this.exStr2 = exStr2;
        this.exInt1 = exInt1;
        this.exInt2 = exInt2;
        this.date1 = date1;
        this.date2 = date2;
        this.addPincode = addPincode;
        this.sutTime = sutTime;
        this.resp = resp;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getCustPassword() {
        return custPassword;
    }

    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }

    public String getCustDob() {
        return custDob;
    }

    public void setCustDob(String custDob) {
        this.custDob = custDob;
    }

    public String getCustEmail() {
        return custEmail;
    }

    public void setCustEmail(String custEmail) {
        this.custEmail = custEmail;
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

    public String getAddPincode() {
        return addPincode;
    }

    public void setAddPincode(String addPincode) {
        this.addPincode = addPincode;
    }

    public String getSutTime() {
        return sutTime;
    }

    public void setSutTime(String sutTime) {
        this.sutTime = sutTime;
    }

    public String getResp() {
        return resp;
    }

    public void setResp(String resp) {
        this.resp = resp;
    }

    @Override
    public String toString() {
        return "Cust{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custMobile='" + custMobile + '\'' +
                ", custPassword='" + custPassword + '\'' +
                ", custDob='" + custDob + '\'' +
                ", custEmail='" + custEmail + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", isUsed=" + isUsed +
                ", exStr1='" + exStr1 + '\'' +
                ", exStr2='" + exStr2 + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", date1='" + date1 + '\'' +
                ", date2='" + date2 + '\'' +
                ", addPincode='" + addPincode + '\'' +
                ", sutTime='" + sutTime + '\'' +
                ", resp='" + resp + '\'' +
                '}';
    }
}
