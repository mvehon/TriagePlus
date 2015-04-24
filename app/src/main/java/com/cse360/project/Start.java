package com.cse360.project;

import android.app.ActionBar;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class Start extends Activity {
    SharedPreferences prefs;
    EditText firstname, lastname, password;
    CheckBox remember;
    boolean found;
    String type = "";
    Doctor dr = new Doctor();
    Patient pat = new Patient();
    Patient drpat = new Patient();

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

        if (prefs.getBoolean("firsttime", true)) { //If this is the first time app has been opened, send to add user page
            startActivity(new Intent(Start.this, AddUser.class));
            Start.this.finish();
        } else if (prefs.getBoolean("loggedin", false)) {
            setContentView(R.layout.splash);
            getActionBar().hide();
            RelativeLayout splashbg = (RelativeLayout)findViewById(R.id.splashbg);
            if (prefs.getInt("user_type", 0) == 2) {
                loadDoctorUser();
                splashbg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InternalStorage.writeObject(getBaseContext(), dr.getFirstName()+dr.getLastName(), dr);
                        } catch (IOException e1) {
                            Log.e("Writing", "Failed to write it to memory");
                            e1.printStackTrace();
                        }
                        startActivity(new Intent(Start.this, Doctor_Main.class));
                        Start.this.finish();
                    }
                }, 3000);
            } else if (prefs.getInt("user_type", 0) == 1) {
                loadPatientUser();
                splashbg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InternalStorage.writeObject(getBaseContext(), pat.getFirstName()+pat.getLastName(), pat);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        startActivity(new Intent(Start.this, Patient_Main.class));
                        Start.this.finish();
                    }
                }, 2000);
            } else {
                startActivity(new Intent(Start.this, AddUser.class));
                Start.this.finish();
            }
        } else {
            //startActivity(new Intent(Start.this, Login.class));
            setContentView(R.layout.login);

            firstname = (EditText) findViewById(R.id.textFirstName);
            lastname = (EditText) findViewById(R.id.textLastName);
            password = (EditText) findViewById(R.id.password);
            remember = (CheckBox) findViewById(R.id.checkBoxRemember);
            final Button login = (Button) findViewById(R.id.button_login);

            remember.setChecked(prefs.getBoolean("remember", false));
            if (remember.isChecked()) {
                firstname.setText(prefs.getString("user_fn", ""));
                lastname.setText(prefs.getString("user_ln", ""));
                password.setText(prefs.getString("user_pw", ""));
            }

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkServer("Patient");
                    checkServer("Doctor");
                    login.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (found) {
                                prefs.edit().putString("curUser", firstname.getText().toString() + lastname.getText().toString()).apply();
                                prefs.edit().putString("user_fn", firstname.getText().toString()).apply();
                                prefs.edit().putString("user_ln", lastname.getText().toString()).apply();
                                prefs.edit().putString("user_pw", password.getText().toString()).apply();
                                prefs.edit().putBoolean("loggedin", true).apply();
                                if (type.equals("Patient")) {
                                    loadPatientUser();
                                    prefs.edit().putInt("user_type", 1).apply();
                                    startActivity(new Intent(Start.this, Patient_Main.class));
                                    Start.this.finish();
                                } else if (type.equals("Doctor")) {
                                    loadDoctorUser(); //This may not entirely work yet
                                    prefs.edit().putInt("user_type", 2).apply();
                                    startActivity(new Intent(Start.this, Doctor_Main.class));
                                    //Start.this.finish();
                                }
                            }
                        }
                    }, 1000);
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
        if (id == R.id.clear_data) {
            prefs.edit().clear().commit();
            Start.this.finish();
            return true;
        }
        if (id == R.id.logout) {
            prefs.edit().clear().commit();
            prefs.edit().putBoolean("firsttime", false).commit();
            startActivity(new Intent(Start.this, Start.class));
            Start.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void checkServer(final String s) {
        found = false;
        final String temptype = s;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(s);
        query.whereEqualTo("username", firstname.getText().toString() + lastname.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + parseObjects.size() + " people");
                    if (parseObjects.size() == 0) {
                    } else {
                        Log.d("entered:", password.getText().toString());
                        Log.d("server:", parseObjects.get(0).get("password").toString());
                        if (password.getText().toString().equals(parseObjects.get(0).get("password").toString())) {
                            Log.d("comp: ", "The same");
                            prefs.edit().putBoolean("remember", remember.isChecked()).commit();
                            found = true;
                            type = temptype;
                        } else {
                            Log.d("comp: ", "Not the same");
                        }
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void loadPatientUser(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
        query.whereEqualTo("username", prefs.getString("curUser",""));
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    Log.d("score", "Retrieved " + parseObjects.size() + " people");
                    if (parseObjects.size() > 0) {
                        ParseObject temppt = parseObjects.get(0);
                        pat.setFirstName(temppt.get("first_name").toString());
                        pat.setLastName(temppt.get("last_name").toString());
                        pat.setPassword(temppt.get("password").toString());
                        pat.setDoctor(temppt.get("doctor").toString());
                        //TODO get pain values and put them into array
                        //TODO get prescriptions belonging to this patient
                        List<Integer> temp = new ArrayList<Integer>();
                        temp = temppt.getList("symptom0");
                        pat.setSymptom0(temp);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void loadDoctorUser(){
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
            query.whereEqualTo("username", prefs.getString("curUser",""));
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                    if (e == null) {
                        Log.d("score", "Retrieved " + parseObjects.size() + " people");
                        if (parseObjects.size() > 0) {
                            ParseObject tempdr = parseObjects.get(0);
                            dr.setFirstName(tempdr.get("first_name").toString());
                            dr.setLastName(tempdr.get("last_name").toString());
                            dr.setPassword(tempdr.get("password").toString());
                            //dr.setSymptom0((List<Integer>) temppt.getList("symptom0"));
                            //pt.setSymptom0(temppt.getList("symptom0"));

                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
                            query.whereEqualTo("doctor", prefs.getString("curUser",""));
                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                                    if (e == null) {
                                        Log.d("score", "Retrieved " + parseObjects.size() + " people");
                                        if (parseObjects.size() > 0) {
                                            for(int i=0; i<parseObjects.size();i++){
                                                //Toast.makeText(getBaseContext(), "adding patient"+ Integer.toString(i), Toast.LENGTH_SHORT).show();
                                                ParseObject temppt = parseObjects.get(i);
                                                drpat = new Patient();
                                                drpat.setFirstName(temppt.get("first_name").toString());
                                                drpat.setLastName(temppt.get("last_name").toString());
                                                drpat.setPassword(temppt.get("password").toString());
                                                drpat.setDoctor(temppt.get("doctor").toString());
                                                Patient temppat = drpat;
                                                //TODO get pain values and put them into array
                                                //TODO get prescriptions belonging to this patient
                                                //pt.setSymptom0((List<Integer>) temppt.getList("symptom0"));
                                                //pt.setSymptom0(temppt.getList("symptom0"));
                                                dr.addPatient(temppat);
                                            }
                                        }
                                    } else {
                                        Log.d("score", "Error: " + e.getMessage());
                                    }
                                }
                            });



                        }
                    } else {
                        Log.d("score", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }
}