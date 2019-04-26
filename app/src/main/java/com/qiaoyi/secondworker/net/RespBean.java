package com.qiaoyi.secondworker.net;

/**
 * Created by daixun on 17-3-23.
 */

public class RespBean<T> {

  private int code;
  private String message;
  private T result;

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getRspData() {
    return result;
  }

  public void setRspData(T result) {
    this.result = result;
  }

  public static class RspInfoBean {

    private String rspType;
    private int rspCode;
    private String rspDesc;

    public String getRspType() {
      return rspType;
    }

    public void setRspType(String rspType) {
      this.rspType = rspType;
    }

    public int getRspCode() {
      return rspCode;
    }

    public void setRspCode(int rspCode) {
      this.rspCode = rspCode;
    }

    public String getRspDesc() {
      return rspDesc;
    }

    public void setRspDesc(String rspDesc) {
      this.rspDesc = rspDesc;
    }
  }
}
