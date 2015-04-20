package com.cse360.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.text.ParseException;
import java.util.List;


public class Start extends Activity {
	SharedPreferences prefs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = this.getSharedPreferences("com.cse360.project",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();


        //THIS IS A TEST OBJECT FOR PARSE
        /*ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */

		if(prefs.getBoolean("firsttime",true)){ //If this is the first time app has been opened, send to add user page
			startActivity(new Intent(Start.this, AddUser.class));
			Start.this.finish();
		}

        if(prefs.getBoolean("loggedin",false)){
            if(prefs.getInt("user_type", 0)==2){
                startActivity(new Intent(Start.this, Doctor_Main.class));
                Start.this.finish();
            }else if(prefs.getInt("user_type", 0)==1){
                startActivity(new Intent(Start.this, Patient_Main.class));
                Start.this.finish();
            }else{
                startActivity(new Intent(Start.this, AddUser.class));
                Start.this.finish();
            }
        }else{
            //startActivity(new Intent(Start.this, Login.class));
            setContentView(R.layout.login);

            final EditText firstname = (EditText)findViewById(R.id.textFirstName);
            final EditText lastname = (EditText)findViewById(R.id.textLastName);
            EditText password = (EditText)findViewById(R.id.password);
            CheckBox remember = (CheckBox)findViewById(R.id.checkBoxRemember);
            Button login = (Button) findViewById(R.id.button_login);

            remember.setChecked(prefs.getBoolean("remember", false));
            if(remember.isChecked()){
                firstname.setText(prefs.getString("user_fn", ""));
                lastname.setText(prefs.getString("user_ln", ""));
                password.setText(prefs.getString("user_pw", ""));
            }

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String pt_type;
                    if(prefs.getInt("user_type",1)==1){pt_type="Patient";}
                    else{pt_type="Doctor";}
                    ParseQuery<ParseObject> query = ParseQuery.getQuery(pt_type);
                    query.whereEqualTo("username", firstname.getText().toString()+lastname.getText().toString());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> scoreList, ParseException e) {
                            if (e == null) {
                                Log.d("score", "Retrieved " + scoreList.size() + " scores");
                            } else {
                                Log.d("score", "Error: " + e.getMessage());
                            }
                        }
                    });
                    //user.has(firstname.getText().toString()+lastname.getText().toString());
                }
            });
        }



    /*
        else if(prefs.getInt("user_type", 0)==1){ //If current user is a patient, send to patient activity
			//This is where we would check the time against time of last assessment
            startActivity(new Intent(Start.this, Patient_Main.class));
			Start.this.finish();
		}
		else if(prefs.getInt("user_type", 0)==2){ //If current user is a doctor, send to doctor activity
			startActivity(new Intent(Start.this, Doctor_Main.class));
			Start.this.finish();
		}
		else{									//If no current user is found, send to page to add user
			startActivity(new Intent(Start.this, AddUser.class));
			Start.this.finish();
		}
		*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
