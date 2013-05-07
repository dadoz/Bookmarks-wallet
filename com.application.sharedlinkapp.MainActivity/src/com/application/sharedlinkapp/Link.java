package com.application.sharedlinkapp;

import android.util.Log;

public class Link {
	
	int linkId;
	String linkIconPath;
	String linkUrl;
	int userId;
	String linkName;	
	String delIcon;
	boolean deletedLinkFlag;

	private static final int EMPTY_LINKID=-1;

	public Link(int linkId,String linkIconPath,String linkName,String linkUrl,int userId,String delIcon,boolean deletedLinkFlag)
	{
		this.linkId=linkId;
		this.linkIconPath=linkIconPath;
		this.delIcon=delIcon;
		this.linkName=linkName;
		this.userId=userId;
		this.deletedLinkFlag=deletedLinkFlag;
	}
	
	public int getUserId()
	{
		return this.userId;
	}
	
	public String getIconPath()
	{
		return this.linkIconPath;
	}

	public String getLinkUrl()
	{
		return this.linkUrl;
	}
	
	public String getLinkName()
	{
		return this.linkName;
	}
	
	public void setLinkUrl(String value)
	{
		this.linkUrl=value;
	}

	public void setLinkName(String value)
	{
		this.linkName=value;
	}
	
	public void setDeletedLink(boolean value)
	{
		this.deletedLinkFlag=value;
	}
	public boolean getDeletedLink()
	{
		return this.deletedLinkFlag;
	}
	public boolean findLinkNameBool(String value)
	{
		if(this.linkName==value)
			return true;
		else
			return false;
	}
	
	public int getLinkId(String value)
	{
		Log.v("linkID_TAG",""+this.linkName+" "+value);
//		Log.v("getLinkId_TAG",""+this.linkId);
		//compare name of link to the value
		if((this.linkName).compareTo(value)==0)
			return this.linkId;
		else
			return EMPTY_LINKID;
	}

	
}
