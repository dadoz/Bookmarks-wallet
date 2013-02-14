package com.application.sharedlinkapp;

import java.net.URL;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
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

	//they MUST BE EQUALS TO THE ONES IN THE PHP file !!!!
	private static final int USERS_DB = 98;
	private static final int LINKS_DB = 99;

	
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
        		userLoggedIn=ApplicationCheckUserLoggedIn.usersParserJSONData(username, password, result);

        		
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
    	Button logoutButton=(Button)findViewById(R.id.logoutButtonId);
    	LinearLayout getYourLinks=(LinearLayout)findViewById(R.id.getYourLinksLayoutId);
    	
    	
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
