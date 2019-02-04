package com.ats.easyrto.model;

public class LoginResponse {

    private boolean error;
    private String msg;
    private Cust cust;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Cust getCust() {
        return cust;
    }

    public void setCust(Cust cust) {
        this.cust = cust;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "error=" + error +
                ", msg='" + msg + '\'' +
                ", cust=" + cust +
                '}';
    }
}
