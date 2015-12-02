package com.example.invinjun.listtexureviewdemo.view;

import java.util.List;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.invinjun.listtexureviewdemo.dao.Weibo;
import com.example.invinjun.listtexureviewdemo.utils.FileUtil;
import com.squareup.picasso.Picasso;

public class MyAdapter extends BaseAdapter {
private Context context;
private LayoutInflater inflater;

private int vHeight;
private int vWidth;
private videoPlayListener listener;
public void setVideoListener(videoPlayListener listener){
	this.listener=listener;
}
public interface videoPlayListener{
	public void OnClicked(View v);
}
	public MyAdapter(Context context,List<Weibo> list,int Width) {
		this.vWidth=Width;
		this.context=context;
		this.weibolist=list;

	}

	private List<Weibo> weibolist;
	public void setList(List<Weibo> list){
		this.weibolist=list;
		Log.e("适配器中 集合大小", weibolist.size()+"");
	}

	@Override
	public int getCount() {
		return weibolist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return weibolist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	/**
	 * 向列表头添加数据
	 * 
	 * @param data
	 */

	/**
	 * 向列表尾部添加数据
	 * 
	 * @param data
	 */
	public  void addDataToFooter(List<Weibo> data){
		setList(data);
	};
	/**
	 * 图片加载第一次显示监听器
	 * @author Administrator
	 *
	 */

	@Override
	public View getView(final int position,  View convertView, ViewGroup parent) {
		inflater=LayoutInflater.from(context);

		final ViewHolder holder;
		if (convertView==null) {
			convertView =inflater.inflate(FileUtil.getLayoutResIDByName(context, "main_listview_item"), null);
			holder=new ViewHolder();
			holder.rl=(RelativeLayout) convertView.findViewById(FileUtil.getIdResIDByName(context, "video_layout"));
			holder.video=(ImageView) convertView.findViewById(FileUtil.getIdResIDByName(context, "video_cover_default"));
			holder.textureView=(VideoPlayTextureView) convertView.findViewById(FileUtil.getIdResIDByName(context, "videoview"));
			holder.videoPlay=(ImageView) convertView.findViewById(FileUtil.getIdResIDByName(context, "play_controller"));
			holder.loading=(ProgressWheel) convertView.findViewById(FileUtil.getIdResIDByName(context, "loading"));
			holder.LongVideobt=(LinearLayout) convertView.findViewById(FileUtil.getIdResIDByName(context, "longVideo"));
			convertView.setTag(holder);
		}else {
			 holder = (ViewHolder) convertView.getTag();
			 holder.video.setImageDrawable(null);
			 holder.textureView.reset();
			 holder.loading.setVisibility(View.GONE);
			 holder.videoPlay.setVisibility(View.GONE);
		}
		final Weibo weibo=weibolist.get(position);
		Picasso.with(context).load(weibolist.get(position).getPicUrl()).into(holder.video);

		if (position==2) {
			holder.LongVideobt.setVisibility(View.VISIBLE);
			holder.LongVideobt.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intet=new Intent(context,VideoCachePlayer.class);
					intet.putExtra("videourl", "http://eduopen.ctbri.com.cn/eduVideo/video/long1.mp4");
					context.startActivity(intet);
				}
			});
		}else {
			holder.LongVideobt.setVisibility(View.GONE);
		}
		holder.textureView.setVideoPath(weibolist.get(position).getVideoSourceId());
		holder.videoPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.OnClicked(holder.rl);
			}
		});
		 LinearLayout.LayoutParams relaParams =(LinearLayout.LayoutParams) holder.rl.getLayoutParams();
		 relaParams.height=vWidth;
		 relaParams.width=vWidth;
		 //动态设置视频播放窗口宽高
		holder.rl.setLayoutParams(relaParams);
		
		if(holder.loading.getVisibility() != View.INVISIBLE){
			holder.loading.resetCount(0);
			holder.loading.setVisibility(View.INVISIBLE);
		}
		
		
		return convertView;
	}
	
	static class ViewHolder{
		ImageView video ;
		ImageView videoPlay ;
		ProgressWheel loading;
		TextView title;
		TextView description;
		VideoPlayTextureView textureView; 
		RelativeLayout rl;
		LinearLayout LongVideobt;
		ImageView btn_favorit;
		ImageView btn_retweeted;
		ImageView btn_comment;
		ImageView btn_pm; 
		ImageView btn_more;
		View layout_user;
		/** 收藏 */
		TextView txt_favorit;
		/** 评论 */
		TextView txt_comment;
		/** 转发 */
		TextView txt_retweeted;
		/** 来源 */
		TextView txt_source;
		TextView txt_userName;
		TextView txt_time;
		TextView txt_schoolInfo;
		ImageView img_roleLogo;
		int position;
	}
	
}



