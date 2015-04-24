package com.cse360.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Patient_Main extends Activity{

    private SharedPreferences prefs;
    private Button submitButton;
    private Patient curUser;
    private String[] tableWords = {"Prescription Name", "Fill Date", "Duration"};
    private TableLayout table;
    private ArrayList<Prescription> prescriptionList; //List of prescriptions for this patient
    private static final int NUM_COLUMNS = 3; //Number of columns in the prescription table

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_main);
        prefs = this.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);

        curUser = new Patient();
        try {
            curUser = (Patient) InternalStorage.readObject(this, "curUser");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Toast.makeText(this, curUser.getFirstName(), Toast.LENGTH_LONG).show();
        //Send user to assessment page on click of the button
        submitButton = (Button) findViewById(R.id.pmainbutton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_Main.this, Assessment.class));
                Patient_Main.this.finish();
            }
        });

        table = (TableLayout)findViewById(R.id.table);
        TableRow row = new TableRow(this); //First row containing titles

        //Add in three "header" columns
        for(int i = 0; i < NUM_COLUMNS; i++) {
            TextView text = new TextView(this); //Entry text
            text.setText(tableWords[i]);
            text.setBackgroundColor(Color.LTGRAY);
            row.addView(text);

            //Create padding between text entries; add text to appropriate column
            TableRow.LayoutParams params =
                    (TableRow.LayoutParams)text.getLayoutParams();
            params.column= i;
            params.span = 1;
            params.setMargins(3, 3, 3, 3);
            params.width = TableRow.LayoutParams.MATCH_PARENT;
            params.height = TableRow.LayoutParams.WRAP_CONTENT;
            text.setPadding(3, 3, 3, 3);
            text.setLayoutParams(params);
            row.setBackgroundResource(R.drawable.row_border);
        }
        table.addView(row,
                new TableLayout.LayoutParams
                        (TableLayout.LayoutParams.WRAP_CONTENT,
                                TableLayout.LayoutParams.WRAP_CONTENT));

        prescriptionList = new ArrayList<Prescription>();

        //Some dummy test data
        prescriptionList.add(new Prescription("Goofballs", "1/23/24", 30));
        prescriptionList.add(new Prescription("Vicodin", "1/22/24", 20));
        prescriptionList.add(new Prescription("Lunesta", "1/20/24", 40));

        for (int j = 0; j < prescriptionList.size(); j++) {
            TableRow rxRow = new TableRow(this); //Row showing prescription info
            for(int i = 0; i < NUM_COLUMNS; i++) {
                TextView text = new TextView(this);

                //Determine what content to display based on current loop index
                switch(i) {
                    case 0: //Prescription name
                        text.setText(prescriptionList.get(j).getRx_name());
                        break;
                    case 1: //Prescription fill date
                        text.setText(prescriptionList.get(j).getFill_date());
                        break;
                    case 2: //Prescription duration
                        text.setText(prescriptionList.get(j).getDuration() + " days.");
                        break;
                }
                text.setBackgroundColor(Color.LTGRAY);
                rxRow.addView(text);

                //Create padding between text entries; add text to appropriate column
                TableRow.LayoutParams params =
                        (TableRow.LayoutParams)text.getLayoutParams();
                params.column= i;
                params.span = 1;
                params.setMargins(3, 3, 3, 3);
                params.width = TableRow.LayoutParams.MATCH_PARENT;
                params.height = TableRow.LayoutParams.WRAP_CONTENT;
                text.setPadding(3, 3, 3, 3);
                text.setLayoutParams(params);
                rxRow.setBackgroundResource(R.drawable.row_border);
            }
            table.addView(rxRow,
                    new TableLayout.LayoutParams
                            (TableLayout.LayoutParams.WRAP_CONTENT,
                                    TableLayout.LayoutParams.WRAP_CONTENT));
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.doctor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.clear_data) {
            prefs.edit().clear().commit();
            Patient_Main.this.finish();
            return true;
        }
        if (id == R.id.logout) {
            prefs.edit().putBoolean("loggedin", false).commit();
            startActivity(new Intent(Patient_Main.this, Start.class));
            Patient_Main.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}



