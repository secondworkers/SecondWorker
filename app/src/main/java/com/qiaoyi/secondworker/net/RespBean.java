package com.qiaoyi.secondworker.net;

/**
 * Created by daixun on 17-3-23.
 */

public class RespBean<T> {

  private String code;
  private String message;
  private T rspData;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public T getRspData() {
    return rspData;
  }

  public void setRspData(T rspData) {
    this.rspData = rspData;
  }
}
