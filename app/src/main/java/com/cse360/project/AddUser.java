package com.cse360.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SpinnerAdapter;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class AddUser extends Activity {
    private SharedPreferences prefs;
    private LinearLayout form_frag, doctor_ll, submit;
    private Spinner spin_doctor;
    private RadioButton rb_doctor, rb_patient;
    private EditText lastname, firstname, password;
    private RadioGroup rg;
    private List<String> doctorList;
    private ParseQuery<ParseObject> query;
    private boolean nonEmpty; //Used for doctor list testing
    private boolean testFlag = false; //Used for doctor list testing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_user);
        prefs = this.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //Assign to UI elements
        form_frag = (LinearLayout) findViewById(R.id.form_frag);
        doctor_ll = (LinearLayout) findViewById(R.id.doctor_ll);
        spin_doctor = (Spinner) findViewById(R.id.spin_doctor);
        submit = (LinearLayout) findViewById(R.id.submit);
        rb_doctor = (RadioButton) findViewById(R.id.rb_doctor);
        rb_patient = (RadioButton) findViewById(R.id.rb_patient);
        firstname = (EditText) findViewById(R.id.first_name_txt);
        lastname = (EditText) findViewById(R.id.last_name_txt);
        password = (EditText) findViewById(R.id.pw_txt);
        rg = (RadioGroup) findViewById(R.id.rg);
        doctorList = new ArrayList<String>();

        query = ParseQuery.getQuery("Doctor");
        createDoctorList();

        //Create array adapter for doctor drop down menu
        SpinnerAdapter mSpinnerAdapter;
        mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.doctor_array, android.R.layout.simple_spinner_dropdown_item);
        spin_doctor.setAdapter(mSpinnerAdapter);



        //Radio button selection listener
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                form_frag.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

                switch (checkedId) {
                    case R.id.rb_doctor:
                        //If doctor is selected, make spinner disappear
                        doctor_ll.setVisibility(View.GONE);
                        break;
                    case R.id.rb_patient:
                        //If patient selected, make spinner appear
                        doctor_ll.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        //Form submission
        submit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check to make sure fields are filled out
                if(firstname.getText().length()<2 || lastname.getText().length()<2  || password.getText().length()<2 ){
                    //Display error message if forms not filled out
                    Toast.makeText(getBaseContext(), "You must complete all fields to continue", Toast.LENGTH_LONG).show();
                }
                else {
                    //TODO THIS IS FOR TESTING PURPOSES ONLY, REAL USER CREATION NEEDS TO GO HERE
                    if (rb_doctor.isChecked()) { //If making a doctor
                        //Create a doctor object
                        Doctor curUser = new Doctor(firstname.getText().toString(),
                                lastname.getText().toString(), password.getText()
                                .toString());

                        /*For testing purposes, create 3 patients and assign them to this doctor
                        Patient tempPt = new Patient("Typhoid", "Mary", "", curUser.getLastName() + curUser.getFirstName());
                        int tempArr[] = {1, 2, 3};
                        tempPt.addValues(tempArr);
                        int tempArr1[] = {3, 3, 3};
                        tempPt.addValues(tempArr1);
                        curUser.addPatient(tempPt);

                        tempPt = new Patient("Patient", "Zero", "", curUser.getLastName() + curUser.getFirstName());
                        int tempArr2[] = {3, 2, 1};
                        tempPt.addValues(tempArr2);
                        int tempArr3[] = {1, 1, 1};
                        tempPt.addValues(tempArr3);
                        curUser.addPatient(tempPt);

                        tempPt = new Patient("Some", "Dude", "", curUser.getLastName() + curUser.getFirstName());
                        int tempArr4[] = {2, 4, 6};
                        tempPt.addValues(tempArr4);
                        int tempArr5[] = {8, 3, 1};
                        tempPt.addValues(tempArr5);
                        curUser.addPatient(tempPt);
                        */
                        //Write to internal storage
                        try {
                            InternalStorage
                                    .writeObject(getBaseContext(),
                                            curUser.getFirstName() + curUser.getLastName(),
                                            curUser);
                        } catch (IOException e) {
                            Log.e("ERR", e.getMessage());
                        }

                        curUser.createOnServer();

                        //Save user type and reference string
                        prefs.edit().putString("curUser",
                                curUser.getFirstName()+curUser.getLastName()).commit();
                        prefs.edit().putString("user_fn",
                                curUser.getFirstName()).commit();
                        prefs.edit().putString("user_ln",
                                curUser.getLastName()).commit();
                        prefs.edit().putString("user_pw", curUser.getPassword());
                        prefs.edit().putInt("user_type", 2).commit();
                    } else {
                        //If patient selected

                        String derp = spin_doctor.getSelectedItem().toString();

                        //Create a patient object
                        Patient curUser = new Patient(firstname.getText().toString(),
                                lastname.getText().toString(), password.getText()
                                .toString(), derp);


                        //FOR THE SAKE OF TESTING, ADDING A COUPLE PRE-REPORTED PAIN LEVELS
                        int tempArr[] = {1, 2, 3};
                        curUser.addValues(tempArr);
                        int tempArr1[] = {3, 3, 3};
                        curUser.addValues(tempArr1);

                        //Write to internal storage
                        try {
                            InternalStorage
                                    .writeObject(getBaseContext(),
                                            curUser.getFirstName() + curUser.getLastName(),
                                            curUser);
                        } catch (IOException e) {
                            Log.e("ERR", e.getMessage());
                        }

                        curUser.createOnServer();
                        derp = derp.substring(4);
                        derp = derp.replaceAll("\\s+","");
                        Log.d("derp", derp);
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Doctor");
                        query.whereEqualTo("username", derp);
                        query.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                                if (e == null) {
                                    if(parseObjects.size()>0){
                                        ParseObject dr = parseObjects.get(0);
                                        dr.add("ptusernames", firstname.getText().toString()+
                                                lastname.getText().toString());
                                        dr.saveInBackground();
                                    }
                                }
                            }
                        });

                        //Save user type and reference string
                        prefs.edit().putString("curUser",
                                curUser.getFirstName() + curUser.getLastName()).commit();
                        prefs.edit().putString("user_fn",
                                curUser.getFirstName()).commit();
                        prefs.edit().putString("user_ln",
                                curUser.getLastName()).commit();
                        prefs.edit().putString("user_pw", curUser.getPassword());
                        prefs.edit().putInt("user_type", 1).commit();
                    }

                    prefs.edit().putBoolean("firsttime", false).commit();
                    prefs.edit().putBoolean("loggedin", true).commit();

                    //Return to Start and close this activity
                    if(prefs.getInt("user_type",0)==1){
                        startActivity(new Intent(AddUser.this, Patient_Main.class));
                    }
                    else if(prefs.getInt("user_type",0)==2){
                        startActivity(new Intent(AddUser.this, Doctor_Main.class));
                    }
                    AddUser.this.finish();
                }
            }
        });
    }

    public void createParseUser(Boolean isDoctor){
        final ParseUser user = new ParseUser();
        user.setUsername(firstname.getText().toString()+lastname.getText().toString());
        user.setPassword(password.getText().toString());
        user.put("isDoctor", isDoctor);

        //user.setEmail(email.getText().toString());

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    ParseUser currentUser = ParseUser.getCurrentUser();
                   /* try {
                        user.logIn(user.getUsername(), password.getText().toString());
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    } */
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    int code = e.getCode();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.addusermenu, menu);
        return true;
    }

    public List<String> getDoctorList() { return doctorList; }

    public void setFlag() { testFlag = true; }


    public boolean createDoctorList() {
        query = ParseQuery.getQuery("Doctor");
        nonEmpty = true;

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    //Only for testing; not considered part of implementation
                    if (testFlag)
                        parseObjects.clear();

                    Log.d("score", "Retrieved " + parseObjects.size() + " names");
                    if (parseObjects.size() == 0)
                        nonEmpty = false;
                    for(int i=0; i<parseObjects.size(); i++){
                        doctorList.add("Dr. " + parseObjects.get(i).get("first_name")+ " "
                                + parseObjects.get(i).get("last_name"));
                    }
                    ArrayAdapter<String> doc_adapter = new ArrayAdapter<String>(getBaseContext(),
                            android.R.layout.simple_spinner_item, doctorList);
                    spin_doctor.setAdapter(doc_adapter);
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        return nonEmpty;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.login) {
            prefs.edit().putBoolean("firsttime", false).commit();
            startActivity(new Intent(AddUser.this, Start.class));
            AddUser.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
