package com.application.sharedlinkapp;

import android.content.Intent;
import android.util.Log;

public class IntentURLFromBrowser {

	public  static String urlString;
	
	public IntentURLFromBrowser(String urlString)
	{
		this.urlString=urlString;
	}

	public static void setUrlString(String value)
	{
		urlString=value;
	}	
	
    public static String getURLFromIntentBrowser(Intent urlIntent)
    {
		String urlString="";
		
		try
    	{
			
//	    	Uri data = this.getIntent().getData();	    	
//	    	url = new URL(data.getScheme(), data.getHost(), data.getPath());
//	    	url=new URL(data.getScheme(),"bla","bla");
	        urlString = urlIntent.getStringExtra(Intent.EXTRA_TEXT);
    	}
    	catch(Exception e)
    	{
    		Log.e("URL_TAG","error - " + e);
    	}
		
    	return urlString;
    }

	
	
	
	
}
