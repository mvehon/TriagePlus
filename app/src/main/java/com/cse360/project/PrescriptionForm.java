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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/*EDIT NOTES:
    The prescription form will now check for text in the prescription text box and text in the date box
    The only thing left seems to be pushing the prescription to the server and emailing the email.
*/

public class PrescriptionForm extends Activity {
    private SharedPreferences prefs;
    private LinearLayout  submit;
    private EditText prescription, date, durText;
    private CheckBox allergies, refil;
    private boolean allergies1, refil1;
    private String dateString, dateCorrect, duration;
    private int monthCheck, dayCheck, yearCheck;
    Doctor curUser;
    Spinner patient_spinner;
    List<Patient> pts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prescription_form);
        prefs = this.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        curUser = new Doctor();

        try {
            curUser = (Doctor) InternalStorage.readObject(getBaseContext(), prefs.getString("user_fn","")+prefs.getString("user_ln",""));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        pts = curUser.getPts();

        submit = (LinearLayout) findViewById(R.id.linearSubmit);
        prescription = (EditText) findViewById(R.id.prescriptionText);
        date = (EditText) findViewById(R.id.dateText);
        allergies = (CheckBox) findViewById(R.id.allergiesCheck);
        refil = (CheckBox) findViewById(R.id.refilCheck);
        durText = (EditText) findViewById(R.id.durText);
        patient_spinner = (Spinner)findViewById(R.id.patient_spinner);

        ArrayAdapter ptsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, pts);
        patient_spinner.setAdapter(ptsAdapter);

        //much new code here
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Check to make sure fields are filled out
                if(prescription.getText().length()<2 || date.getText().length()<2){
                    //Display error message if forms not filled out
                    Toast.makeText(getBaseContext(), "Both the prescription and date must be filled out to continue", Toast.LENGTH_LONG).show();
                }
                else {
                        dateString = date.getText().toString(); //this will store what is in the text field
                        if(dateString.charAt(2) == '/' && dateString.charAt(5) == '/' && dateString.length() >= 8) {
                          //this if statement verifies that the date is entered in the 'mm/dd/yy' format

                          //I also need to check the date here
                            monthCheck = Integer.parseInt(dateString.substring(0,2));
                            dayCheck = Integer.parseInt(dateString.substring(3, 5));
                            yearCheck = Integer.parseInt(dateString.substring(6));

                            //Verifying the date, and making number adjustments as necessary
                            while(dayCheck>=32){
                                dayCheck = dayCheck -31;
                                monthCheck++;
                            }
                            while(monthCheck>12){
                                monthCheck = monthCheck-12;
                                yearCheck++;
                            }

                            dateCorrect = (String.valueOf(monthCheck)) + "/" + (String.valueOf(dayCheck)) + "/" + (String.valueOf(yearCheck));

                            duration = durText.getText().toString();

                            allergies1 = allergies.isChecked();
                            refil1 = refil.isChecked();

                            //this should create the prescription which is then pushed to the server

                            Prescription prescription1 = new Prescription();

                            prescription1.setAllergies(allergies1);
                            prescription1.setRefil(refil1);
                            prescription1.setRx_name(prescription.getText().toString());
                            prescription1.setFill_date(dateCorrect);
                            prescription1.setDuration(Integer.parseInt(duration));

                            Mail m = new Mail("triageplusapp@gmail.com", "cse360project");
                            String[] toArr = {"poyopoyo91@gmail.com"};
                            m.setTo(toArr);
                            m.setBody(prescription1.toEmail(patient_spinner.getSelectedItem().toString(), curUser.toString()));

                            try {
                                if(m.send()) {
                                    Toast.makeText(getBaseContext(), "Prescription successful!", Toast.LENGTH_LONG).show();
                                    PrescriptionForm.this.finish();
                                } else {
                                    Toast.makeText(getBaseContext(), "Error here", Toast.LENGTH_LONG).show();
                                }
                            } catch(Exception e) {
                                //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                                Log.e("MailApp", "Could not send message", e);
                            }
                            //this will print out a success message for a correct prescription
                            //if(1)                            //this is just for testing
                        }
                        else{
                            Toast.makeText(getBaseContext(), "The date seems to be filled out incorrectly", Toast.LENGTH_LONG).show();
                        }
            }
        }
     });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
