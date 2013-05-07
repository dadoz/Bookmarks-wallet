package com.application.sharedlinkapp;

public class User {
	
	private int userId;
	private String username;
	private String password;
	private boolean userLoggedIn;
	
	public User(int userId,String username,String password,boolean userLoggedIn)
	{
		this.userId=userId;
		this.username=username;
		this.password=password;
		this.userLoggedIn=userLoggedIn;
	}
	
	public int getUserId()
	{
		return this.userId;
	}
	
	public String getUsername()
	{
		return this.username;
	}
	public String getPassword()
	{
		return this.password;
	}
	
	public boolean getUserLoggedIn()
	{
		return this.userLoggedIn;	
	}
	public void setUserLoggedIn(boolean value)
	{
		this.userLoggedIn=value;
	}
	
	
	
	
}
