package com.application.sharedlinkapp;


import java.util.ArrayList;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityLinksList extends Activity{

    // Debugging
    private static final String TAG = "ActivityLinksList_TAG";
    private static final boolean D = true;

	//they MUST BE EQUALS TO THE ONES IN THE PHP file !!!!
	private static final int USERS_DB = 98;
	private static final int LINKS_DB = 99;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	
	
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

    
    
    
    
//    contextMenu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
      super.onCreateContextMenu(menu, v, menuInfo);

      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.context_menu, menu);
      
      if(menuInfo!=null)
    	  toastMessageWrapper(menuInfo.toString());
      else
    	  toastMessageWrapper("menuInfo null");
    	  
      
//      String valueString=((TextView)(info.targetView)).getTag().toString();

      
      if (v.getId()==R.id.linksListId) {
        menu.setHeaderTitle("Sure to delete link?");
        
        menu.add(0,v.getId(),0,"first");
        
        
//        String[] menuItems = getResources().getStringArray(R.array.menu);
//        ArrayList<String> menuItems=new ArrayList<String>();
        
//        menuItems.add("click to delete");
        
//        for (int i = 0; i<menuItems.size(); i++) {
//          menu.add(Menu.NONE, i, i, menuItems.get(i).toString());
//        }
      }
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	super.onContextItemSelected(item);
    	
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();    	
//    	toastMessageWrapper(""+info.id);
  
    	if(info==null)
    		toastMessageWrapper("info null");
    	else
    		if(item!=null)
    			toastMessageWrapper(""+item.getMenuInfo().toString());
    	    		
    	if (item.getTitle() == "delete") 
		{
			// edit action
			toastMessageWrapper("delete");
			return true;

		}
		else if (item.getTitle() == "cancel")
		{
			// delete action
			toastMessageWrapper("cancel");
			return false;
		}
		else
		{
			return false;
		}
    	
    	/*    	
      AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
      int menuItemIndex = item.getItemId();
      String[] menuItems = getResources().getStringArray(R.array.menu);
      String menuItemName = menuItems[menuItemIndex];
      String listItemName = Countries[info.position];

      TextView text = (TextView)findViewById(R.id.footer);
      text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
      */
    }
    
    
    public void createLayout()
    {
    	ArrayList<String> linksUrlArray = null;
    	ArrayList<Link> linksObjList = null;
    	
    	/**get all view I need**/
    	final ListView linksListView = (ListView) findViewById(R.id.linksListId);
    	LinearLayout userProfile=(LinearLayout)findViewById(R.id.userProfileLayoutId);
    	
    	
    	/****set get your links layout action***/
    	userProfile.setOnClickListener(new View.OnClickListener(){        
	        public void onClick(View v) {
          	  	//launch new activity
	        	finish();
	        }
        });

    	

    	
    	
    	// SLIDING Gesture detection
    	//----------------------------------------
/*      gestureDetector = new GestureDetector(getBaseContext(),new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
                
            }
        };
        userProfile.setOnTouchListener(gestureListener);
*/
    	//----------------------------------------
    	
    	//TEST
//    	toastMessageWrapper(ApplicationCheckUserLoggedIn.getInfoURL());
//    	Log.v("INFOURL_TAG",""+ApplicationCheckUserLoggedIn.getInfoURL());
    	
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
    
    	
    	//TEST
    	if(D)
	    	if(linksUrlArray!=null)
	    		if(linksUrlArray.get(0).compareTo("Empty URLs list")==0)
	    			linksUrlArray=null;
    	
    	
    	if(linksUrlArray!=null)
    	{
    		//set adpater of linksListView
//    		linksListView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_row,linksUrlArray));	

        	//create new array to store all URLName
        	ArrayList<Link> linksDataList=new ArrayList<Link>();    	

        	//all the elements are not deleted - of course
        	boolean deletedLinkFlag=false;
        	int userId = 0;
			int linkId=0;
			String linkUrl="Empty";
			//Populate the list
        	for(int i=0;i<linksUrlArray.size();i++)
        		linksDataList.add(new Link(linkId,"ic_launcher", linksUrlArray.get(i),linkUrl,userId,"del_icon",deletedLinkFlag));

//    		Link(linkIdDb,iconPathDb,linkUrlDb,linkName, userIdDb,linkName,deletedLinkFlag);
        	//try add new set adapter
   			CustomAdapter adapter = new CustomAdapter(this,R.layout.listview_item_row,linksDataList );

   			linksListView.setAdapter(adapter);

//--------------------------------------------------------------   			
   			
    		//on long click I can delete the row    		
    		linksListView.setLongClickable(true);
    		
    		//register list to show contextmenu
//    		registerForContextMenu(linksListView);
    		 
    		//on click I can open the link
    		linksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				public void onItemClick(AdapterView parentView, View childView, int position, long id) {
					// TODO Auto-generated method stub

					RelativeLayout relViewRow=(RelativeLayout)childView;
	        		//cast View to text view (cos the listView obj is a textview)
	        		TextView urlSelectedTextView=(TextView)relViewRow.findViewById(R.id.linkNameTextId);
	        		 
	        		
	        		
	        		
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

    		linksListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
						public boolean onItemLongClick(AdapterView<?> parentView, View childView, int position, long id) {

							//get context menu - doesnt work :(
//							linksListView.showContextMenu();
							RelativeLayout relViewRow=(RelativeLayout)childView;
			        		//cast View to text view (cos the listView obj is a textview)
			        		TextView nameTextView=(TextView)relViewRow.findViewById(R.id.linkNameTextId);
//			        		nameTextView=(TextView)parentView.getChildAt(0);
			        		
			        		ImageView delIcon=(ImageView)relViewRow.findViewById(R.id.delIconId);
			        		
			        		delIcon.setVisibility(View.VISIBLE);
			        		/*
//			        		parentView.showContextMenuForChild(linksListView);
							if(nameTextView!=null)
							{
								boolean check=ApplicationCheckUserLoggedIn.deleteUrlEntryFromDb(LINKS_DB,nameTextView.getText().toString());
								if(check)
									toastMessageWrapper("ITEM DELETED - plez refresh");
								else
									toastMessageWrapper("DELETE FAILED");
									
							}*/
							return true ;
						}
			});
    		
    	}
    	else
    	{
    		Log.d(TAG,"error - no one url fetched");
    		
        	linksUrlArray=new ArrayList<String>();

    		linksUrlArray.add("heavy metal");
    		linksUrlArray.add("pop");
    		linksUrlArray.add("underground");
    		linksUrlArray.add("heavy metal");
    		linksUrlArray.add("underground");
    		linksUrlArray.add("hey_ure_fkin_my_shitty_dog_are_u_sure_u_want_to_cose_ure_crazy");
    		linksUrlArray.add("pop");
    		linksUrlArray.add("heavy metal");
    		
    		
        	ArrayList<Link> linksDataList=new ArrayList<Link>();
        	
        	
        	//all the items are not deleted
        	boolean deletedLinkFlag=false;
        	//populate list
        	
        	//TEST
        	int linkId=0;
        	String linkUrl="http://www.google.it";
        	int userId=0;        	
        	for(int i=0;i<linksUrlArray.size();i++)
        		linksDataList.add(new Link(linkId,"ic_launcher", linksUrlArray.get(i),linkUrl,userId,"del_icon",deletedLinkFlag));

        	//try add new set adapter
   			CustomAdapter adapter = new CustomAdapter(this,R.layout.listview_item_row,linksDataList );

   			linksListView.setAdapter(adapter);

    	}	

    	
    	
    	
    	
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
 
    
    class MyGestureDetector extends SimpleOnGestureListener {

		@Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ActivityLinksList.this, "Left Swipe", Toast.LENGTH_SHORT).show();
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    Toast.makeText(ActivityLinksList.this, "Right Swipe", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // nothing
            }
            return false;
		}
	}
    
    
    
    
    
    //TO BE IMPLEMENTED !!!! PLEZ take care of it
    public boolean checkURL(String urlString)
    {
    	//check URL with regex
    	
    	return true;
    }
    
    

    public static void checkLinkIsDeleted(boolean value,View view)
    {
    	
    	if(value)
    	{
    		String message="ITEM DELETED - plez refresh";
    		Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

//			toastMessageWrapper();
    	}
		else
		{
			String message="DELETE FAILED";
//			toastMessageWrapper();
	  		Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

		}

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
