package com.application.sharedlinkapp;

import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.StrictMode;
import android.util.Log;

@SuppressLint("NewApi")
public class ApplicationCheckUserLoggedIn{

	//they MUST BE EQUALS TO THE ONES IN THE PHP file !!!!
	private static final int USERS_DB = 98;
	private static final int LINKS_DB = 99;

	private static String EMPTY_STRING="EMPTY";

	
	private static final boolean SELECT_ALL_ROW_FROM_DB=true;
	private static final boolean INSERT_URL_ON_DB=true;
	private static final boolean DELETE_URL_FROM_DB=true;

	
	private static final String EMPTY_USERNAME="";
	private static final String EMPTY_PASSWORD="";
	private static final int EMPTY_USERID=-1;
	private static final int EMPTY_LINKID=-1;
		
	private static String databaseResultString="";
	
	private static User userObj=null;
	
	private static ArrayList<Link> linksObjList=null;

	//localhost URL for android emulator dev (AVD)
//	private static String DBUrl="http://10.0.2.2/sharedLinksApp/fetchDataFromDbJSON.php";
//	private static String DBUrl="http://192.168.42.155:8080/sharedLinksApp/fetchDataFromDbJSON.php";
	private static String DBUrl="http://kubiz.altervista.org/sharedLinksApp/fetchDataFromDbJSON.php";
		
	
	//TEST
	private static String infoURL="";
	
	public static boolean getUserLoggedIn()
	{
		if(userObj!=null)
			return userObj.getUserLoggedIn();
		else
			return false;
	}
	
	public static String getDatabaseResultString()
	{
		if(userObj.getUserLoggedIn()==true)	
			return databaseResultString;
		else
		{
			Log.e("MY_TAG","u're not autorized to get this data - u must log in");
			return "EMPTY";
		}
	}

	public static String getLoggedInUsername()
	{
		if(userObj.getUserLoggedIn()==true)	
			return userObj.getUsername();
		else
			return EMPTY_USERNAME;
	}

	public static String getLoggedInPassword()
	{
		if(userObj.getUserLoggedIn()==true)	
			return userObj.getPassword();
		else
			return EMPTY_PASSWORD;
	}
	
	public static int getLoggedInUserId()
	{
		if(userObj.getUserLoggedIn()==true)	
			return userObj.getUserId();
		else
			return EMPTY_USERID;
	}

	
	public static boolean logout()
	{
		if(userObj.getUserLoggedIn()==true)
		{
			//delete the object
			userObj=null;
			return true;
		}
		else
		{
			Log.e("MY_TAG","u're not autorized to get this data - u must log in");
			return false;
		}
		
	}
	
	/**
	 * 
	 * function to fetch data from db and compare 
	 * the usr and pws typed in from user
	 * @param usernameTypedIn
	 * @param passwordTypedIn
	 * @return true or false - return usr and pswd typed in from user check :D
	 */
    public static String fetchDataFromDb(int choicedDB)
    {
    	
      	String response ="";
      	String result="";
      	
      	// call executeHttpPost method passing necessary parameters 
      	try {
      		
//      		String choicedDB="usersDB";

      		//check if db is right
      		if(choicedDB!=LINKS_DB && choicedDB!=USERS_DB)
      			Log.e("MY_TAG", "NO DB FOUND - u must define the right database name");
      		
      		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

      		postParameters.add(new BasicNameValuePair("choicedDB",""+choicedDB));
      		postParameters.add(new BasicNameValuePair("selectAllRowFromDB",""+SELECT_ALL_ROW_FROM_DB));
      		
      		//add new pair of params to set userId
      		if(choicedDB==LINKS_DB)
      		{
      			//get my userId to fetch all liks I stored before
      			int userIdTMP=userObj.getUserId();
      			postParameters.add(new BasicNameValuePair("userId",""+userIdTMP));
      		}	
      		
      		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
      	    StrictMode.setThreadPolicy(policy);
      		
      		//response = CustomHttpClient.executeHttpPost("http://127.0.0.1/sharedLinksApp/fetchDataFromDbJSON.php",postParameters);
      		
      		// from device instead of use 127.0.0.1 u must use 10.0.2.2 
//      		response=CustomHttpClient.executeHttpGet(DBUrl);
    		response = CustomHttpClient.executeHttpPost(DBUrl,postParameters);
      	  
      		// store the result returned by PHP script that runs MySQL query
    	    result = response.toString();  
            
      	}
      	catch (Exception e) {
      		Log.e("log_tag","Error in http connection!!" + e.toString());     
      	}

      	return result;
    }


    public static boolean usersParserJSONData(String usernameTypedIn,String passwordTypedIn,String result)
    {
	    Log.v("JSON_TAG","this is the result" + result);
	    //parse json data
	    try{
	    	//def temp db variable
	    	String usernameDb;
	    	String passwordDb;
	    	int userIdDb;

	    	//set false cos the user should be logged out before trying to log in again
	    	
	    	databaseResultString = "";
	    	JSONArray jArray = new JSONArray(result);
	    	for(int i=0;i<jArray.length();i++){
	    		//getJSONObj 
	    		JSONObject json_data = jArray.getJSONObject(i); 

	    		
                //get usr and pswd from JASON data
                userIdDb=json_data.getInt("user_id");
                usernameDb=json_data.getString("username");
                passwordDb=json_data.getString("password");
                
                //set databaseResultString to print out all database entries
                databaseResultString += "\n" +"USR:" + usernameDb + "  " +"PSWD:" + passwordDb;


                //Log all database entries
                Log.i("log_tag","id: "+userIdDb+
                         ", usrname: "+usernameDb+
                         ", pswd: "+passwordDb
                );

        	    //check usrname and pswd typed in from user
        	    if(usernameDb.compareTo(usernameTypedIn)==0 && passwordDb.compareTo(passwordTypedIn)==0)
        	    {
        	    	//create new user object
        	    	boolean userLoggedIn=true;
        	    	
        	    	newUserObjWrapper(userIdDb,usernameDb,passwordDb,userLoggedIn);
        	    }	
                
	    	}
	    	return true;
	    }
	    catch(JSONException e){
	    	Log.e("log_tag", "Error parsing data "+e.toString());
	      	return false;
	    }

	    //return if user is loggedIn or not
    	
    }


    public static void newUserObjWrapper(int userIdDb,String usernameDb,String passwordDb,boolean userLoggedIn)
    {
    	if(userObj==null)
    		userObj=new User(userIdDb,usernameDb,passwordDb,userLoggedIn);

    }
    
    public static ArrayList<String> linksParserJSONData(String result)
    {
    	
    	ArrayList<String> linksUrlArray=new ArrayList<String>();
    
    	//create list of Link object
    	if(linksObjList==null)
    		linksObjList=new ArrayList<Link>();
    	
    	//var of links db
    	int linkIdDb=0;
    	String iconPathDb="";
    	String linkUrlDb="";
    	int userIdDb=0;
    	String linkName="";
    	boolean deletedLinkFlag=false;
    	String delIconPathDb="";    	
    	
    	//Log.v("JSON_TAG","this is the result" + result);
	    //parse json data
	    try{

	    	
	    	databaseResultString = "";
	    	JSONArray jArray = new JSONArray(result);
	    	for(int i=0;i<jArray.length();i++){
	    		//getJSONObj 
	    		JSONObject json_data = jArray.getJSONObject(i); 

	    		
                //get usr and pswd from JASON data
                linkIdDb=json_data.getInt("link_id");
                iconPathDb=json_data.getString("iconPath");
                linkUrlDb=json_data.getString("linkUrl");
                userIdDb=json_data.getInt("links_user_id");
                linkName=json_data.getString("linkName");
         
                //add delIconPath to JSON data!!!!!!!!!
                
                //populate the list
                linksUrlArray.add(linkName);

                Log.v("JSON_links_result",""+json_data.toString());
                
                Link linkObjTMP=new Link(linkIdDb,iconPathDb,linkName,linkUrlDb, userIdDb,delIconPathDb,deletedLinkFlag);
                linksObjList.add(linkObjTMP);

                
                try
                {
                	
                	URL linkURLObj=new URL(linkUrlDb);
                	
                	infoURL=linkURLObj.getUserInfo();
                }
                catch(Exception e)
                {
                	Log.v("URL_TAG","error - "+e);
                }
                //set databaseResultString to print out all database entries
                databaseResultString += result;

                

                //Log all database entries
                Log.i("log_tag","id: "+linkIdDb+
                         ", iconPath: "+iconPathDb+
                         ", linkUrl: "+linkUrlDb+
                         ", userId: "+userIdDb+
                         ", linkName: "+linkName
                );
              
	    	}
	    }
	    catch(JSONException e){
	    	Log.e("log_tag", "Error parsing data "+e.toString());
	    	linksUrlArray.add("Empty URLs list");
	    }

	    //return the list of all links
    	return linksUrlArray;
    }

    
	/**
	 * 
	 * @param choicedDB
	 * @return
	 */
    public static boolean insertUrlEntryOnDb(int choicedDB,String urlString)
    {
    	
    	try
    	{

	  		//check if db is right
	  		if(choicedDB!=LINKS_DB && choicedDB!=USERS_DB)
	  			Log.e("MY_TAG", "NO DB FOUND - u must define the right database name");

	  		//new params array
	  		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

	  		//add choicedDB params
	  		postParameters.add(new BasicNameValuePair("insertUrlOnDb",""+INSERT_URL_ON_DB));
	  		postParameters.add(new BasicNameValuePair("choicedDB",""+choicedDB));
	  		
	  		if(choicedDB==LINKS_DB)
	  		{
	  			//get my userId to fetch all liks I stored before
	  			int userIdTMP=userObj.getUserId();
	  			postParameters.add(new BasicNameValuePair("userId",""+userIdTMP));

	  			//get url to be stored
	  			if(urlString!=null)
	  				postParameters.add(new BasicNameValuePair("linkUrl",""+urlString));
	  			
	  			//get url name
	  			//add url name to be added to the url above
	  			String linkNameTMP=getUrlTitle(urlString);
	  			
	  			if(linkNameTMP!=null)
	  				postParameters.add(new BasicNameValuePair("linkName",""+linkNameTMP));

	  		}	
	  		
	  		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	  	    StrictMode.setThreadPolicy(policy);
	        
	  	    
    		String response = CustomHttpClient.executeHttpPost(DBUrl,postParameters);
    		
    		Log.d("MY_TAG",response);

	  	}
	  	catch (Exception e) {
	  		Log.e("log_tag","Error in http connection!!" + e.toString());     
    		
    		return false;
    	}
    	
    	
    	return true;
    }

    
    
	/**
	 * 
	 * @param choicedDB
	 * @return
	 */
    public static boolean deleteUrlEntryFromDb(int choicedDB,String linkName)
    {
    	
    	try
    	{

	  		//check if db is right
	  		if(choicedDB!=LINKS_DB && choicedDB!=USERS_DB)
	  			Log.e("MY_TAG", "NO DB FOUND - u must define the right database name");

	  		//new params array
	  		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

	  		//add choicedDB params
	  		postParameters.add(new BasicNameValuePair("deleteUrlFromDb",""+DELETE_URL_FROM_DB));
	  		postParameters.add(new BasicNameValuePair("choicedDB",""+choicedDB));
	  		
  			//get my userId to fetch all liks I stored before
  			int userIdTMP=userObj.getUserId();
  			if(userIdTMP!=EMPTY_USERID)
  				postParameters.add(new BasicNameValuePair("links_user_id",""+userIdTMP));
  			else
  				return false;

  			
  			//getLinksId to be deleted
  			int linkIdTMP=getLinkIdFromList(linkName);
  			
  			//DEBUG
  			Log.v("id_link_TAG",""+linkIdTMP);
  			
  			//check linkId!=null
  			if(linkIdTMP!=EMPTY_LINKID)
  				postParameters.add(new BasicNameValuePair("linkId",""+linkIdTMP));
  			else
  				return false;

  			
  			
	  		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	  	    StrictMode.setThreadPolicy(policy);
	        
	  	    
    		String response = CustomHttpClient.executeHttpPost(DBUrl,postParameters);
    		
    		Log.d("DEL_RESULT_TAG","this is the result" + response);
	  	}
	  	catch (Exception e) {
	  		Log.e("log_tag","Error in http connection!!" + e.toString());     
    		
    		return false;
    	}
    	
    	
    	return true;
    }

    public static int getLinkIdFromList(String value)
    {
    	int linkId=EMPTY_LINKID;
    	if(linksObjList!=null)
    	{
    		for(int i=0;i<linksObjList.size();i++)
    		{
    			linkId=linksObjList.get(i).getLinkId(value);
    			
//    			Log.v("linkID_TAG",""+linkId);
    			if(linkId!=EMPTY_LINKID)
    				return linkId;
    		}	
    		return EMPTY_LINKID;
    	}
    	else
    		return EMPTY_LINKID;

    }
    
    public static String findUrlFromLinkName(String linkName)
    {
    	for(int i=0;i<linksObjList.size();i++)
    	{
    		if(linksObjList.get(i).findLinkNameBool(linkName)!=false)
    			return linksObjList.get(i).getLinkUrl();
    	}
    	
    	return null;
    }
    

	/**
	 * 
	 *     
	 * @param URLString
	 * @return
	 */
    public static String getUrlTitle(String URLString)
    {
    	//URL title 
    	String URLTitleString;

    	try
    	{
	    	Document doc = Jsoup.connect(URLString).get();
	
	    	Elements URLtitle=doc.select("title");
//	    	toastMessageWrapper(URLtitle.text());
	    	URLTitleString=URLtitle.text();
	    	Log.v("JSOUP_TAG",URLTitleString);
	    	
	    	return URLTitleString;
    	}
    	catch(Exception e)
    	{
	    	Log.v("JSOUP_TAG",""+e);
    		
    	}
    	
    	return EMPTY_STRING;
    }
    
    
}
