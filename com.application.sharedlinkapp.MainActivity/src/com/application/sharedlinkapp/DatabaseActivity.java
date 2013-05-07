package com.application.sharedlinkapp;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class DatabaseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_test_layout);
        final DBAdapter db= new DBAdapter(this);
        
        
		Button button1=(Button)findViewById(R.id.button1DB);
		button1.setOnClickListener(new OnClickListener() {
    		  public void onClick(View view) {
    			  	//delete all rows from db
    		    	deleteAllRowsDb(db);

    		    	//get number of rows
    		    	getNumbRowsDb(db);

    		  }
		});

		Button button1a=(Button)findViewById(R.id.button2DB);
		button1a.setOnClickListener(new OnClickListener() {
    		  public void onClick(View view) {
    			  	//delete all rows from db
    		    	deleteRowByIdDb(db);

    		    	//get number of rows
    		    	getNumbRowsDb(db);

    		  }
		});

		Button button2=(Button)findViewById(R.id.button3DB);
		button2.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				//add a row to the db
				insertOneRowDb(db, "Mario Zida", "icon_path", true,false,false,false,false);

				//insertOneRowDb(db, "bbbb", "XXX_title", "Vikki");
    			  
				//get number of rows
				getNumbRowsDb(db);
				
				//getAllTitles
				//getAllRowsDb(db);
    		  }
		});
		
		Button button3=(Button)findViewById(R.id.button4DB);
		button3.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				getAllRowsDb(db);
			}
		});
       	//Toast.makeText(this, "new db testClass", Toast.LENGTH_LONG).show();
        

       	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    /**DB functions - used to get all db row, insert a new one or update and delete one*/

    
    /**INSERT ROW in db*/
    public void insertOneRowDb(DBAdapter db,String username, String userIcon, boolean onlineFriends,boolean closerFriends,boolean othersFriends,boolean blackList,boolean hiddenPosition)
    {
        //insert title
        db.open();
        
        //long id;
        db.insertTitle(username,userIcon,onlineFriends,closerFriends,othersFriends,blackList,hiddenPosition);
        db.close();
    	
    }

    
    

    /**GET ALL ROWS from db**/
    public void getNumbRowsDb(DBAdapter db)
    {
        int value;

        //get all title
        db.open();
 
        Cursor c=db.getAllTitles();
        value=c.getCount();
        ToastMessageWrapper(String.valueOf(value));
        
        db.close();
    }
    
    
    /**GET ALL ROWS from db**/
    public void getAllRowsDb(DBAdapter db)
    {
        //get all title
        db.open();
        Cursor c=db.getAllTitles();
        if(c.moveToFirst())
        {
        	do{
        		DisplayTitle(c);
        		
        	}while(c.moveToNext());
        }
        else
        	Toast.makeText(this, "no row in the db", Toast.LENGTH_LONG).show();
   
        //ToastMessageWrapper(String.valueOf(value));
        db.close();
    }
    
    /**GET ONE ROW from db**/
    public void getOneRowDb(DBAdapter db,int idRow)
    {

        //get one title
        db.open();
        Cursor c=db.getTitle(idRow);
        if(c.moveToFirst())
        {
        	DisplayTitle(c);
        }
        else
        	Toast.makeText(this, "no row in the db", Toast.LENGTH_LONG).show();
        db.close();
    }

    /**GET ONE ROW from db**/
    public void deleteAllRowsDb(DBAdapter db)
    {

        //get one title
        db.open();
    	
	  	//db.dropDbTable();
	  	//db= new DBAdapter(this);
	  	
        db.deleteAllRows();

    	db.close();
    }
    /**GET ONE ROW from db**/
    public void deleteRowByIdDb(DBAdapter db)
    {

        //get one title
        db.open();
    	
	  	//db.dropDbTable();
	  	//db= new DBAdapter(this);
	  	
        db.deleteRowById(5);

    	db.close();
    }
    
    
    public void DisplayTitle(Cursor c)
    {
    	Toast.makeText(this,"id "+ c.getString(0)+" icon "+ c.getString(1)+" bool "+ c.getString(2)+"  bool "+ c.getString(3)+" bool "+ c.getString(4)+"  bool "+ c.getString(5)+" bool " + c.getString(6)+" bool "+ c.getString(7), Toast.LENGTH_LONG).show();
    	
    }

    
    public void ToastMessageWrapper(String message)
    {

    	Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
    
    public void DBTestFunction(DBAdapter db)
    {
    	
       	//generate new db - insert rows into it
       	//insertOneRowDb(db,"aaaa","title1","author1");
       	//insertOneRowDb(db,"zzzz","title2","author2");
       	//db.deleteAllRows();
		
       	//get all row from db
       	//getOneRowDb(db,3);
    	//getAllRowsDb(db);
    	deleteAllRowsDb(db);
       	//insertOneRowDb(db,"aaaa","title1","author1");
       	getAllRowsDb(db);

    	
    }
}
