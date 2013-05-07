package com.application.sharedlinkapp;

public class Link {
	
	int linkId;
	String iconPath;
	String linkUrl;
	int userId;
	String linkName;
	
	public Link(int linkId,String iconPath,String linkUrl,int userId,String linkName)
	{
		this.linkId=linkId;
		this.iconPath=iconPath;
		this.linkUrl=linkUrl;
		this.userId=userId;
		this.linkName=linkName;
	}
	
	public int getUserId()
	{
		return this.userId;
	}
	
	public String getIconPath()
	{
		return this.iconPath;
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
	
	
	public boolean findLinkNameBool(String value)
	{
		if(this.linkName==value)
			return true;
		else
			return false;
	}

	
}
