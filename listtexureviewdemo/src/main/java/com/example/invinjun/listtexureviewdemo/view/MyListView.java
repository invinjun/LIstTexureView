package com.example.invinjun.listtexureviewdemo.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.invinjun.listtexureviewdemo.cacheplayer.service.ServiceParams;
import com.example.invinjun.listtexureviewdemo.cacheplayer.service.WebServiceHelper;
import com.example.invinjun.listtexureviewdemo.configs.CONSTANTS;
import com.example.invinjun.listtexureviewdemo.utils.NetWorkUtil;


public class MyListView extends XListView
{
  private int firstVisibleIndex;
  private int totalcount;
  private int vHeight;
  private int vWidth;
  private static Context context;
  private PlayIngListener mlistener;
  private SharedPreferences share;
  private int playtime;
  private static boolean allow=true;

  public MyListView(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    		this.context = context;
    	    WebServiceHelper.newInstance().getResultString(context);
    	    share = context.getSharedPreferences(ServiceParams.NAME_SP,
    				Context.MODE_PRIVATE);
    	    getWidthAndHeight();
    	    this.playtime = Integer.valueOf(share.getString(ServiceParams.PLAY_TIME, "0"));
  }
  private static void getWidthAndHeight(){


				allow = true;

  }
  public void setPlayIngListener(PlayIngListener listener) {
	  this.mlistener = listener;
  }

  public void setScreenInfo(int height,int width) {
    this.vHeight = height;
    this.vWidth=width;
  }

  @Override
public void onScrollStateChanged(AbsListView view, int scrollState) {
	View firstItem = null;
	View firstItem1 = null;
		if (scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
			mlistener.scrolle();
		}
	  if (allow) {
		  firstItem = getChildAt(0);
		    //问题小计：当有下拉刷新控件的时候listview的第一个child是下拉刷新的head，所以第0个位置不作处理，这是我们看的的第一个位置实际上是第二个位置，第一个应该播放但是却播放的是第二个所以
		   //可以在第一个index为零的时候我们看第二个和第三个child 这样就对了。。呼呼 后续做处理 今天打包
		    firstItem1 = getChildAt(1);
		    if (this.firstVisibleIndex == 0){
		    	firstItem = getChildAt(1);
		        firstItem1 = getChildAt(2);
		    }
		    else if (this.firstVisibleIndex == this.totalcount - 2) {
		      firstItem1 = null;
		    }
		}else {
			Toast.makeText(context, CONSTANTS.ERROR+CONSTANTS.ERRORNUM, Toast.LENGTH_SHORT).show();
		}
   
		if (scrollState == 0) {
			if (firstItem != null) {
				int[] location = new int[2];
				firstItem.getLocationOnScreen(location);
				int x = location[0];
				int y = location[1];
				if (isItemViewLittleVisible(y)) {
					mlistener.playTop(firstItem);
					SharedPreferences.Editor editor = share.edit();
					playtime++;
					editor.putString(ServiceParams.PLAY_TIME, playtime + "");
					editor.commit();
				} else {
					mlistener.stopTop(firstItem);
				}
			}

      if ((firstItem1 != null) && (this.firstVisibleIndex < totalcount - 2)) {
        int[] location = new int[2];
        firstItem1.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (isItem1ViewLittleVisible(y)) {
          mlistener.playBottom(firstItem1);
          SharedPreferences.Editor editor = share.edit();
          playtime ++;
          editor.putString(ServiceParams.PLAY_TIME, playtime+"");
          editor.commit();
        } else {
         mlistener.stopBottom(firstItem1);
        }
      }
    }

    super.onScrollStateChanged(view, scrollState);
  }

  @Override
public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
  {
    firstVisibleIndex = firstVisibleItem;
    totalcount = totalItemCount;
    super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
  }
  private boolean isItemViewLittleVisible(int itemY) {
    return (Math.abs(itemY) < vHeight * 1 / 5);
  }

  private boolean isItem1ViewLittleVisible(int itemY) {
    if (itemY >= vHeight) {
      return false;
    }
    return  itemY < vWidth ;
  }

  public static abstract interface PlayIngListener
  {
    public abstract void playTop(View paramView);
    public abstract void playBottom(View paramView);
    public abstract void stopTop(View paramView);
    public abstract void stopBottom(View paramView);
    public abstract void scrolle();
  }
}