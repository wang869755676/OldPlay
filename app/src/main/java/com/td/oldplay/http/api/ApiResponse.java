package com.td.oldplay.http.api;

public class ApiResponse<T> {
    private String errcode;
    private String errdesc;
    private T data;

    public String getErrcode() {
        if(errcode==null)
            errcode="0";
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getErrdesc() {
        return errdesc;
    }

    public void setErrdesc(String errdesc) {
        this.errdesc = errdesc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "errcode='" + errcode + '\'' +
                ", errdesc='" + errdesc + '\'' +
                ", data=" + data +
                '}';
    }
}