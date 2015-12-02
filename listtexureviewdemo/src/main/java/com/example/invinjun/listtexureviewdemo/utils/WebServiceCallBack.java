package com.example.invinjun.listtexureviewdemo.utils;

public abstract interface WebServiceCallBack
{
  public abstract void onStart();

  public abstract void onSuccess(String paramString);

  public abstract void onEmptyResponse();

  public abstract void onException(Exception paramException);

  public abstract void onDisconnectedNetWork();
}