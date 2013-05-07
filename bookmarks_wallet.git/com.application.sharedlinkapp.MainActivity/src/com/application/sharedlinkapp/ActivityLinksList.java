package com.application.sharedlinkapp;


import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityLinksList extends Activity{

	
	//they MUST BE EQUALS TO THE ONES IN THE PHP file !!!!
	private static final int USERS_DB = 98;
	private static final int LINKS_DB = 99;

	
	
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.links_list_layout);

		
        createLayout();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    

    
    
    
    
    public void createLayout()
    {
    	ArrayList<String> linksUrlArray = null;
    	ArrayList<Link> linksObjList = null;
    	
    	/**get all view I need**/
    	ListView linksListView = (ListView) findViewById(R.id.linksListId);
    	LinearLayout userProfile=(LinearLayout)findViewById(R.id.userProfileLayoutId);
    	
    	
    	/****set get your links layout action***/
    	userProfile.setOnClickListener(new View.OnClickListener(){        
	        public void onClick(View v) {
          	  	//launch new activity
	        	finish();
	        }
        });



    	
    	
    	/**CREATE ListView **/

    	//get data from db
    	
    	try
    	{
			
			//fetch data
			String result=ApplicationCheckUserLoggedIn.fetchDataFromDb(LINKS_DB);
			
//			linksObjList=ApplicationCheckUserLoggedIn.linksParserJSONData(result);
			linksUrlArray=ApplicationCheckUserLoggedIn.linksParserJSONData(result);
			
			
			//get all links name
//			for(int i=0;i<linksObjList.size();i++) {
//				linksUrlArray.add(linksObjList.get(i).getLinkName());
//			}
			
// 			linksUrlArray.add("");
			Log.v("MY_TAG","url link to be shown"+linksUrlArray.toString());
			
			
    	}
    	catch(Exception e)
    	{
    		Log.e("MY_TAG","error - " + e);
    	}
    
    	if(linksUrlArray!=null)
    	{
    		linksListView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_row,linksUrlArray));	
    
    		linksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView parentView, View childView, int position, long id) {
					// TODO Auto-generated method stub
					
	        		//cast View to text view (cos the listView obj is a textview)
	        		TextView urlSelectedTextView=(TextView)childView;
	        		 
	        		
	        		
	        		
	        		try
	        		{
	        			String urlSelected=(String) urlSelectedTextView.getText();
	        			urlSelected=ApplicationCheckUserLoggedIn.findUrlFromLinkName(urlSelected);
		        		//TEST - print out the text of obj selected
		        		toastMessageWrapper("URL selected "+urlSelected);
		        		 
		        		
		        		//check if urlSelected is right parsed :D
		        		;
		        		
		        		if(checkURL(urlSelected)==true)
		        		{
			        		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlSelected));
			        		startActivity(browserIntent);
		        		}
		        		else
		            		toastMessageWrapper("your URL is wrong "+urlSelected);

	        		}
	        		catch(Exception e)
	        		{
	            		Log.e("MY_TAG","error - " + e);
	            		toastMessageWrapper("I cant load your URL "+ e);

	        		}
	        		
	        		        			
				}
				public void onNothingSelected(AdapterView parentView) 
				{
				}
    			
			});
    	}
    	else
    		Log.e("MY_TAG","error - no one url fetched");
    		
    		
    	//STATIC items
    	// Create and populate a List of planet names.  
//        String[] linksArray = new String[] { "url_link1", "url_link2","url_link3","url_link4","url_link5",};    
    	//populate listView
//    	ArrayList<String> itemsArray=new ArrayList<String>();
    	//populate listView
//    	itemsArray.addAll( Arrays.asList(linksArray) );  
    	
    	
//    	itemsArray.add("link1");
//    	itemsArray.add("link2");
//    	itemsArray.add("link3");
//    	itemsArray.add("link4");
    	
    	

//    	linksListView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_row,itemsArray));
    
    
    
    }
    
    
    //TO BE IMPLEMENTED !!!! PLEZ take care of it
    public boolean checkURL(String urlString)
    {
    	//check URL with regex
    	
    	return true;
    }
    
    public void onBackPressed()
    {
    	super.onBackPressed();
    	
    	Log.v("MY_TAG","back_pressed");
    	return;
    	
    }
    
    //toast message wrapper
	private void toastMessageWrapper(String message) 
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

    
}
