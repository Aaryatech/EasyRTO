package com.ats.easyrto.model;

import java.util.List;

public class WorkHeader {

    private int workId;
    private int workTypeTd;
    private int custId;
    private String vehicalNo;
    private String vehicalName;
    private String insurance;
    private String insurance1;
    private String rcbook;
    private String rcbook1;
    private String puc;
    private String puc1;
    private String lastUpdateTime;
    private int isUsed;
    private String bankDocument;
    private String bankDocument1;
    private String orignalLicence;
    private String orignalLicence1;
    private String adharCard;
    private String adharCard1;
    private String photo;
    private String photo1;
    private String addProof;
    private String addProof1;
    private int status;
    private float workCost;
    private String exStr1;
    private String exStr2;
    private int exInt1;
    private int exInt2;
    private String date1;
    private String date2;
    private String custMobile;
    private String custName;
    private String workTypeName;
    List<WorkDetail> workDetailList;

    public WorkHeader(int workTypeTd, int custId, String vehicalNo, String vehicalName, String insurance, String insurance1, String rcbook, String puc, int isUsed, String bankDocument, String orignalLicence, String adharCard, String photo, String photo1, String addProof, int status, float workCost, List<WorkDetail> workDetailList) {
        this.workTypeTd = workTypeTd;
        this.custId = custId;
        this.vehicalNo = vehicalNo;
        this.vehicalName = vehicalName;
        this.insurance = insurance;
        this.insurance1 = insurance1;
        this.rcbook = rcbook;
        this.puc = puc;
        this.isUsed = isUsed;
        this.bankDocument = bankDocument;
        this.orignalLicence = orignalLicence;
        this.adharCard = adharCard;
        this.photo = photo;
        this.photo1 = photo1;
        this.addProof = addProof;
        this.status = status;
        this.workCost = workCost;
        this.workDetailList = workDetailList;
    }

    public WorkHeader(int workTypeTd, int custId, String vehicalNo, String vehicalName, String insurance, String insurance1, String rcbook, String puc, String lastUpdateTime, int isUsed, String bankDocument, String bankDocument1, String orignalLicence, String adharCard, String photo, String photo1, String addProof, int status, float workCost, String exStr2,String date1, List<WorkDetail> workDetailList) {
        this.workTypeTd = workTypeTd;
        this.custId = custId;
        this.vehicalNo = vehicalNo;
        this.vehicalName = vehicalName;
        this.insurance = insurance;
        this.insurance1 = insurance1;
        this.rcbook = rcbook;
        this.puc = puc;
        this.lastUpdateTime = lastUpdateTime;
        this.isUsed = isUsed;
        this.bankDocument = bankDocument;
        this.bankDocument1 = bankDocument1;
        this.orignalLicence = orignalLicence;
        this.adharCard = adharCard;
        this.photo = photo;
        this.photo1 = photo1;
        this.addProof = addProof;
        this.status = status;
        this.workCost = workCost;
        this.exStr2 = exStr2;
        this.date1 = date1;
        this.workDetailList = workDetailList;
    }



    public int getWorkId() {
        return workId;
    }

    public void setWorkId(int workId) {
        this.workId = workId;
    }

    public int getWorkTypeTd() {
        return workTypeTd;
    }

    public void setWorkTypeTd(int workTypeTd) {
        this.workTypeTd = workTypeTd;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public String getVehicalNo() {
        return vehicalNo;
    }

    public void setVehicalNo(String vehicalNo) {
        this.vehicalNo = vehicalNo;
    }

    public String getVehicalName() {
        return vehicalName;
    }

    public void setVehicalName(String vehicalName) {
        this.vehicalName = vehicalName;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getInsurance1() {
        return insurance1;
    }

    public void setInsurance1(String insurance1) {
        this.insurance1 = insurance1;
    }

    public String getRcbook() {
        return rcbook;
    }

    public void setRcbook(String rcbook) {
        this.rcbook = rcbook;
    }

    public String getRcbook1() {
        return rcbook1;
    }

    public void setRcbook1(String rcbook1) {
        this.rcbook1 = rcbook1;
    }

    public String getPuc() {
        return puc;
    }

    public void setPuc(String puc) {
        this.puc = puc;
    }

    public String getPuc1() {
        return puc1;
    }

    public void setPuc1(String puc1) {
        this.puc1 = puc1;
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

    public String getBankDocument() {
        return bankDocument;
    }

    public void setBankDocument(String bankDocument) {
        this.bankDocument = bankDocument;
    }

    public String getBankDocument1() {
        return bankDocument1;
    }

    public void setBankDocument1(String bankDocument1) {
        this.bankDocument1 = bankDocument1;
    }

    public String getOrignalLicence() {
        return orignalLicence;
    }

    public void setOrignalLicence(String orignalLicence) {
        this.orignalLicence = orignalLicence;
    }

    public String getOrignalLicence1() {
        return orignalLicence1;
    }

    public void setOrignalLicence1(String orignalLicence1) {
        this.orignalLicence1 = orignalLicence1;
    }

    public String getAdharCard() {
        return adharCard;
    }

    public void setAdharCard(String adharCard) {
        this.adharCard = adharCard;
    }

    public String getAdharCard1() {
        return adharCard1;
    }

    public void setAdharCard1(String adharCard1) {
        this.adharCard1 = adharCard1;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getAddProof() {
        return addProof;
    }

    public void setAddProof(String addProof) {
        this.addProof = addProof;
    }

    public String getAddProof1() {
        return addProof1;
    }

    public void setAddProof1(String addProof1) {
        this.addProof1 = addProof1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getWorkCost() {
        return workCost;
    }

    public void setWorkCost(float workCost) {
        this.workCost = workCost;
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

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getWorkTypeName() {
        return workTypeName;
    }

    public void setWorkTypeName(String workTypeName) {
        this.workTypeName = workTypeName;
    }

    public List<WorkDetail> getWorkDetailList() {
        return workDetailList;
    }

    public void setWorkDetailList(List<WorkDetail> workDetailList) {
        this.workDetailList = workDetailList;
    }

    @Override
    public String toString() {
        return "WorkHeader{" +
                "workId=" + workId +
                ", workTypeTd=" + workTypeTd +
                ", custId=" + custId +
                ", vehicalNo='" + vehicalNo + '\'' +
                ", vehicalName='" + vehicalName + '\'' +
                ", insurance='" + insurance + '\'' +
                ", insurance1='" + insurance1 + '\'' +
                ", rcbook='" + rcbook + '\'' +
                ", rcbook1='" + rcbook1 + '\'' +
                ", puc='" + puc + '\'' +
                ", puc1='" + puc1 + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", isUsed=" + isUsed +
                ", bankDocument='" + bankDocument + '\'' +
                ", bankDocument1='" + bankDocument1 + '\'' +
                ", orignalLicence='" + orignalLicence + '\'' +
                ", orignalLicence1='" + orignalLicence1 + '\'' +
                ", adharCard='" + adharCard + '\'' +
                ", adharCard1='" + adharCard1 + '\'' +
                ", photo='" + photo + '\'' +
                ", photo1='" + photo1 + '\'' +
                ", addProof='" + addProof + '\'' +
                ", addProof1='" + addProof1 + '\'' +
                ", status=" + status +
                ", workCost=" + workCost +
                ", exStr1='" + exStr1 + '\'' +
                ", exStr2='" + exStr2 + '\'' +
                ", exInt1=" + exInt1 +
                ", exInt2=" + exInt2 +
                ", date1='" + date1 + '\'' +
                ", date2='" + date2 + '\'' +
                ", custMobile='" + custMobile + '\'' +
                ", custName='" + custName + '\'' +
                ", workTypeName='" + workTypeName + '\'' +
                ", workDetailList=" + workDetailList +
                '}';
    }
}
