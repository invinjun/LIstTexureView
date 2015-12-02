package com.example.invinjun.listtexureviewdemo.utils;

import android.os.Environment;

public class Const
{
  public static final String ADDR = "addr_config";
  public static final String ADDR_KEY = "addr_name";
  public static final int APPID = 2;
  public static final String HOST = "http://xinyyg.vicp.cc:84/";
  public static final String APPPATH = Environment.getExternalStorageDirectory() + "/KoteiWisdomAgriculture";
  public static final String DatabaseName = "Tour.db";
  public static final String PICTORIAL_DIR_PATH = APPPATH + 
    "/Images/pictorial";
  public static final String PICWALL = "/PhotoWallFalls/";
  public static final String IMAGE_SUFFIX = "kotei";
  public static final String ACTION_REFRESH_LOCATOR = "kotei.action.navicn.broadcast.refreshlocator";
  public static final int PAGE_SIZE = 10;
  public static final int MSG_START_MOVE_HandMAP = 70001;
  public static final int MSG_AFTER_REFRESH_HandMAP = 70008;
  public static final String SETTING_AUTOVOICE = "autovoice";
  public static final String SEPARATOR = " SEPARATOR ";
  public static final String SEPARATORSPLIT = "SEPARATOR";
  public static final int DELTE_TOPIC = 456;
  public static final int PUBLISH_TOPIC_SUCCESS = 457;
  public static final int COMMENT_SUCCESS = 458;
  public static final int SUCCESS = 0;
  public static final int EMPTY = 1;
  public static final int EXCEPTION = 2;
  public static final int DISNETWORK = 3;
  public static final String BOX_ITEM_POSITION = "index";
  public static final String BOX_ITEM_KEY = "boxitem";
  public static final boolean ENABLE_DEBUG = true;
}