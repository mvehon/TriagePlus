package com.cse360.project;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class AddUser extends Activity {
	private SharedPreferences prefs;
    private LinearLayout form_frag, doctor_ll, submit;
    private Spinner spin_doctor;
    private RadioButton rb_doctor, rb_patient;
    private EditText lastname, firstname, password;
    private RadioGroup rg;

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

                        //For testing purposes, create 3 patients and assign them to this doctor
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

                        //Write to internal storage
                        try {
                            InternalStorage
                                    .writeObject(getBaseContext(),
                                            curUser.getLastName() + curUser.getFirstName(),
                                            curUser);
                        } catch (IOException e) {
                            Log.e("ERR", e.getMessage());
                        }

                        //Save user type and reference string
                        prefs.edit().putString("curUser",
                                curUser.getLastName() + curUser.getFirstName()).commit();
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
                                            curUser.getLastName() + curUser.getFirstName(),
                                            curUser);
                        } catch (IOException e) {
                            Log.e("ERR", e.getMessage());
                        }

                        //Save user type and reference string
                        prefs.edit().putString("curUser",
                                curUser.getLastName() + curUser.getFirstName()).commit();
                        prefs.edit().putInt("user_type", 1).commit();

                    }

                    prefs.edit().putBoolean("firsttime", false).commit();

                    //Return to Start and close this activity
                    startActivity(new Intent(AddUser.this, Start.class));
                    AddUser.this.finish();
                }
			}
		});
	}
}