package com.application.sharedlinkapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;



public class MainActivity extends Activity {

	//shared preferences variable
    public static final String PREFS_NAME = "UserCredentialFile";

	//they MUST BE EQUALS TO THE ONES IN THE PHP file !!!!
	private static final int USERS_DB = 98;
	private static final int LINKS_DB = 99;

	private static final String EMPTY_USERNAME="";
	private static final String EMPTY_PASSWORD="";
	private static final int EMPTY_USERID=-1;
	// to store the result of MySQL query after decoding JSON
	String returnString;   

	//create intent to get URI from browser
	String urlString=null;

	
	
	public boolean checkUserLoggedIn=false;
	
	//get all button from layout
	Button cancelButton;
	Button loginButton;
	Button fetchDataFromDb;
	TextView fetchResultText;

	


	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

		Log.v("onCreateTAG", "onCreate it's called");
        //create and set all buttons/inputField actions
		createLayoutLOGIN();

		
		
		// Restore userLOGIN credentials even if user kill the app
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

		/***
		 *	getString(key,defValue)
		 *	key	The name of the preference to retrieve. 
		 * 	defValue	Value to return if this preference does not exist.
		 */
		String usernameStored = settings.getString("usernameStored", EMPTY_USERNAME);
   		String passwordStored = settings.getString("passwordStored", EMPTY_PASSWORD);
   		int userIdStored=settings.getInt("userIdStored", EMPTY_USERID);
   		
	    try
	    {
	    	toastMessageWrapper(usernameStored+passwordStored);
	    	
	    	if(usernameStored!=EMPTY_USERNAME && passwordStored!=EMPTY_PASSWORD)
	    	{
	    		ApplicationCheckUserLoggedIn.newUserObjWrapper(userIdStored,usernameStored,passwordStored,true);

	    		createLayoutUserLoggedIn();

	    	}
	    }
	    catch(Exception e)
	    {
	    	Log.v("ON_STOP","error - " + e);
	    }

   		
     
        
		
		try
		{
			
			Intent urlIntent = getIntent();
		    if (urlIntent.getAction().equals(Intent.ACTION_SEND)) 
		    {
		    	urlString=IntentURLFromBrowser.getURLFromIntentBrowser(urlIntent);
//		    	IntentURLFromBrowser.setUrlString(urlString);
//				urlString=getURLFromIntentBrowser();
		    	
		    	//check if user is loggedin
		    	if(ApplicationCheckUserLoggedIn.getUserLoggedIn()==true)
		    	{
			    	toastMessageWrapper(urlString);
			    	
		    		ApplicationCheckUserLoggedIn.insertUrlEntryOnDb(LINKS_DB, urlString);
		    	}
		    	else
		    		toastMessageWrapper("u're not loggedin in - plez login");

		    }
			

		}
		catch(Exception e)
		{
			toastMessageWrapper("no URL availble - something has gone wrong");
		}	

			
    }

	public void onResume()
	{
		super.onResume();

		boolean usrStatusTmp=ApplicationCheckUserLoggedIn.getUserLoggedIn();
				
		
		//check if user is logged in 
		if(usrStatusTmp)
		{
			//clean up all the main activity layout
			this.cleanUpMainActivity();
			//create new layout
			this.createLayoutUserLoggedIn();
		}
		Log.v("onResumeTAG", "onResume it's called");
		
	}
	
	 @Override
	 protected void onStop(){
		 super.onStop();
	     String usernameStored=EMPTY_USERNAME;
	     String passwordStored=EMPTY_PASSWORD; 
	     int userIdStored=EMPTY_USERID;

	     
	     if(ApplicationCheckUserLoggedIn.getUserLoggedIn()==true)
	     {
	    	 usernameStored=ApplicationCheckUserLoggedIn.getLoggedInUsername();
	    	 passwordStored=ApplicationCheckUserLoggedIn.getLoggedInPassword();
	    	 userIdStored=ApplicationCheckUserLoggedIn.getLoggedInUserId();

	     }
	     // We need an Editor object to make preference changes.
	     // All objects are from android.context.Context
	     SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	     SharedPreferences.Editor editor = settings.edit();

	     editor.putString("usernameStored", usernameStored);
	     editor.putString("passwordStored", passwordStored);
	     editor.putInt("userIdStored", userIdStored);

	     editor.clear();
	     // Commit the edits!
	     editor.commit();

	     
	 }	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    

    
    
    public void createLayoutLOGIN()
    {

    	
    	
        //clean up all the main view
        ScrollView layout=(ScrollView)findViewById(R.id.mainActivityScrollViewId);
    	View child = getLayoutInflater().inflate( R.layout.login_layout,null);
    	layout.addView(child);
    
    	//get all button from layout
    	cancelButton=(Button) findViewById(R.id.cancelButtonId);
    	loginButton=(Button) findViewById(R.id.loginButtonId);
//    	Button testDbButton=(Button) findViewById(R.id.testDbButtonId);
    	//EditText byear = (EditText) findViewById(R.id.editText1);

    	
    	
    	//get all EditText from layout
    	final EditText usernameText=(EditText)findViewById(R.id.usernameEditTextId);
    	final EditText passwordText=(EditText)findViewById(R.id.passwordEditTextId);
    	
    	
    	//set actions to buttons
    	cancelButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
              toastMessageWrapper("cancel all data inserted");
              
              //clean the fields
              usernameText.setText("");
              passwordText.setText("");
            }
    	});
    	
    	loginButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
//              toastMessageWrapper("Hey u're trying to LOGIN");

//            get usr and pswd from user
              String username = usernameText.getText().toString();  
              String password = passwordText.getText().toString();  


              //TEST
//              username="username1";
//              password="testPassword";
              
//              toastMessageWrapper(username+password);
              //check usr and pswd user typed in 
//              checkCredentialsOnDb(username,password);
              
              checkUserLoggedIn=checkUserLoggedIn(username,password);

              if(checkUserLoggedIn)
              {
            	  //launch new activity
            	  Intent intent = new Intent(MainActivity.this, ActivityLinksList.class);
                  startActivity(intent);                  	  
              }
              
            }
    	});

    	
    	
    	

      
    }

    
	/**
	 *     
	 * check credential of user - check if user could be logged in or not    
	 * @param username
	 * @param password
	 */
    public boolean checkUserLoggedIn(String username,String password)
    {
    	//check credential typed in
//    	toastMessageWrapper("credential insterted from user\n\n" + username +"\n\n"+password);
    	boolean userLoggedIn=false;

        if(username.length() > 0 && password.length() >0)  
        {

        	//check user password and username to log him in 
        	try
        	{
        		
        		//fetch data
        		String result=ApplicationCheckUserLoggedIn.fetchDataFromDb(USERS_DB);
        		
        		//get if usser is logged in - check
        		boolean checkDataParsered=ApplicationCheckUserLoggedIn.usersParserJSONData(username, password, result);

        		if(checkDataParsered==true)
        			userLoggedIn=ApplicationCheckUserLoggedIn.getUserLoggedIn();
        		
        	}
        	catch(Exception e)
        	{
        		Log.e("MY_TAG","Error - "+ e);
        	}
        	
        	
            if(userLoggedIn==true)  
            {  
            	toastMessageWrapper("u're LOGGED IN :D");
                return true;
            }else{
            	toastMessageWrapper("Invalid username or password - plez reinsert");  
            }  
        }  
        else
            toastMessageWrapper("Username and pswd empty");
    	
        return false;
    }
    


    public void createLayoutUserLoggedIn()
    {
    	
        //clean up all the main view
    	ScrollView layout=(ScrollView)findViewById(R.id.mainActivityScrollViewId);
    	View child = getLayoutInflater().inflate( R.layout.logged_in_user_layout,null);
    	layout.addView(child);

    	//get button from view
    	fetchDataFromDb = (Button) findViewById(R.id.connectAndFetchDbButtonId);
    	fetchResultText = (TextView) findViewById(R.id.resultTextId);
	
    	// Create ArrayAdapter using the planet list.  
    	TextView loggedInUsername=(TextView) findViewById(R.id.loggedInUsernameTextId);
    	LinearLayout logoutButton=(LinearLayout)findViewById(R.id.logoutLayoutId);
    	LinearLayout getYourLinks=(LinearLayout)findViewById(R.id.getYourLinksLayoutId);
    	LinearLayout getYourDB=(LinearLayout)findViewById(R.id.getYourDBLayoutId);
    	
    	
    	/****set username of loggedin user***/
    	String username=ApplicationCheckUserLoggedIn.getLoggedInUsername();
    	loggedInUsername.setText(username);

    	/****set logout button action***/
    	logoutButton.setOnClickListener(new View.OnClickListener(){        
	        public void onClick(View v) {
	        	toastMessageWrapper("Hey u're logging out");

	        	//get string of all database entries
	        	if(ApplicationCheckUserLoggedIn.logout()==true)
	        	{
	    			//clean up all the main activity layout
	    			cleanUpMainActivity();
	    			//create new layout
	    			createLayoutLOGIN();

	        	}	
	        	else
	        		toastMessageWrapper("Logout Failed :(");
	        }
        });


    	/****set get your links layout action***/
    	getYourLinks.setOnClickListener(new View.OnClickListener(){        
	        public void onClick(View v) {
          	  	//launch new activity
	        	Intent intent = new Intent(MainActivity.this, ActivityLinksList.class);
                startActivity(intent);                  	  

	        }
        });
    	
    	/****set get your DB layout action***/
    	getYourDB.setOnClickListener(new View.OnClickListener(){        
	        public void onClick(View v) {
          	  	//launch new activity
	        	Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);                  	  

	        }
        });

    	/****set get your links layout action***/
    	/*    	getYourLinks.setOnClickListener(new View.OnClickListener(){        
	        public void onClick(View v) {
          	  	//launch new activity
	        	Intent intent = new Intent(MainActivity.this, ActivityLinksList.class);
                startActivity(intent);                  	  

	        }
        });
*/
//      fetch all database data            
        fetchDataFromDb.setOnClickListener(new View.OnClickListener(){        
  	        public void onClick(View v) {
  	        	toastMessageWrapper("Hey u're fetching data from db");

  	        	//get string of all database entries
  	        	String userDBEntriesString=ApplicationCheckUserLoggedIn.fetchDataFromDb(USERS_DB);
  	        	
  	        	String linksDBEntriesString=ApplicationCheckUserLoggedIn.fetchDataFromDb(LINKS_DB);

  	        	fetchResultText.setText("USERS_DB JSON\n"+userDBEntriesString+"\n\n\n\nLINKS_DB JSON\n"+linksDBEntriesString);
  	        }
        });

    }
    
    
    

    public void cleanUpMainActivity()
    {
        //clean up all the main view
        ScrollView layout=(ScrollView)findViewById(R.id.mainActivityScrollViewId);
        layout.removeAllViews();

    }

    
    //toast message wrapper
	private void toastMessageWrapper(String message) 
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

    
}
