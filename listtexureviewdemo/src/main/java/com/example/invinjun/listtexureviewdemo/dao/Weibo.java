package com.example.invinjun.listtexureviewdemo.dao;

import java.io.Serializable;


/**
 * 
 * 
 * <p>
 * Title:微博信息类
 * </p>
 * 
 * <p>
 * Description:描述
 * </p>
 * 
 * <p>
 * Copyright: Copyright 2012 FSTI All right reserved.
 * </p>
 * 
 * <p>
 * Company:fsti
 * </p>
 * 
 * @author hphua
 * 
 * @version 1.0
 * 
 * @createtime 2012-7-17-上午09:37:57
 * 
 * @histroy 修改历史
 * 
 *          <li>版本号 修改日期 修改人 修改说明
 * 
 *          <li>2012.7.24 guoew 根据接口类修改
 * 
 *          <li>
 */
public class Weibo implements Serializable {
	/** 微博ID */
	private String id = "";
	/** 创建时间 */
	private String createTime = "";
	/** 微博信息内容 */
	private String content = "";
	/** 是否已收藏,true 已收藏,false未收藏 */
	private boolean favorited = false;
	/** 微博来源 */
	private String weiboDatasourceName = "";
	/** 缩略图URL */
	private String smallPicUrl = "";
	/** 原始图URL */
	private String picUrl = "";
	/** 视频截图URL */
	private String videoImg = "";
	/** 视频源ID */
	private String videoSourceId = "";
	/** 被已收藏的次数 */
	private long favNum = 0;
	/** 被转发的次数 */
	private long forwardNum = 0;
	/** 回复次数 */
	private long commentNum = 0;
	/** 转发的微博，如果此微博不是转发类型，则没有此字段 */
	private Weibo sourceWeibo = null;
	/** 点击数 */
	private int accessNum = 0;

	/** 视频时长，单位：秒 */
	private int videoDuration = 0;

	/** 是否可转发 0是 1否 */
	private int transmitable = 0;
	
	/**视频点播数*/
	private int videoPlayNum = 0;
private int videoFileSize =0;
	public int getVideoFileSize() {
	return videoFileSize;
}

public void setVideoFileSize(int videoFileSize) {
	this.videoFileSize = videoFileSize;
}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public void setFavNum(long favNum) {
		this.favNum = favNum;
	}

	public long getFavNum() {
		return favNum;
	}

	public void setForwardNum(long forwardNum) {
		this.forwardNum = forwardNum;
	}

	public long getForwardNum() {
		return forwardNum;
	}

	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}

	public long getReplyNumber() {
		return commentNum;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getWeiboDatasourceName() {
		return weiboDatasourceName;
	}

	public void setWeiboDatasourceName(String weiboDatasourceName) {
		this.weiboDatasourceName = weiboDatasourceName;
	}

	public String getSmallPicUrl() {
		return smallPicUrl;
	}

	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getVideoImg() {
		return videoImg;
	}

	public void setVideoImg(String videoImg) {
		this.videoImg = videoImg;
	}

	public String getVideoSourceId() {
		return videoSourceId;
	}

	public void setVideoSourceId(String videoSourceId) {
		this.videoSourceId = videoSourceId;
	}

	public Weibo getSourceWeibo() {
		return sourceWeibo;
	}

	public void setSourceWeibo(Weibo sourceWeibo) {
		this.sourceWeibo = sourceWeibo;
	}

	public void setAccessNum(int accessNum) {
		this.accessNum = accessNum;
	}

	public int getAccessNum() {
		return accessNum;
	}

	public void setVideoDuration(int videoDuration) {
		this.videoDuration = videoDuration;
	}

	public int getVideoDuration() {
		return videoDuration;
	}

	/** 是否可转发 0是 1否 */
	public int getTransmitable() {
		return transmitable;
	}

	/** 是否可转发 0是 1否 */
	public void setTransmitable(int transmitable) {
		this.transmitable = transmitable;
	}

	public void setVideoPlayNum(int videoPlayNum) {
		this.videoPlayNum = videoPlayNum;
	}

	public int getVideoPlayNum() {
		return videoPlayNum;
	}
}
