package com.example.googleimagesearch;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


/**
 * Main activity called initially first
 * */


public class MainActivity extends Activity {

	EditText input;
	Button search;
	public static String searched;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = (EditText) findViewById(R.id.inputtext);
        search = (Button) findViewById(R.id.searchbutton);
        
        search.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				CharSequence cs = input.getText();
				if(cs!=null && cs.length()>0)
					searched = cs.toString();
				
		    	Intent intent = new Intent(MainActivity.this,SearchActivity.class);
				startActivity(intent);

			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
