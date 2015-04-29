package com.cse360.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Patient_Main extends Activity{

    private SharedPreferences prefs;
    private Button submitButton;
    private Patient curUser;
    private String[] tableWords = {"Prescription Name", "Fill Date", "Duration"};
    private TableLayout table;
    private ArrayList<Prescription> prescriptionList; //List of prescriptions for this patient
    private final int NUM_COLUMNS = 3; //Number of columns in the prescription table

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_main);
        prefs = this.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);

        curUser = new Patient();
        String username = prefs.getString("curUser", "");


        try {
            curUser = (Patient) InternalStorage.readObject(this, username);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TextView welcomeText = (TextView) findViewById(R.id.welcome);
        prescriptionList = curUser.getPrescList();


        List<Integer> symp0vals = new ArrayList<Integer>();
        symp0vals = curUser.getSymptom0();

        //Send user to assessment page on click of the button
        submitButton = (Button) findViewById(R.id.pmainbutton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Patient_Main.this, Assessment.class));
                Patient_Main.this.finish();
            }
        });

        welcomeText.setText("\t\t\t\t\t\t\t\tWelcome " + curUser.getFirstName() + " " + curUser.getLastName());
        addPrescriptionTable(); //Add the table to the activity

        super.onCreate(savedInstanceState);

        final LinearLayout pt_ll = (LinearLayout) findViewById(R.id.pt_ll);
        LayoutInflater inflates = null;
        try {
            inflates = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (NullPointerException p) {
        }
        pt_ll.addView(inflates.inflate(R.layout.pt_stub, null));
        LinearLayout pt_ll_inner = (LinearLayout) pt_ll.findViewById(R.id.pt_ll_inner);
        TextView ps1 = (TextView) pt_ll_inner.findViewById(R.id.ps1);
        TextView ps2 = (TextView) pt_ll_inner.findViewById(R.id.ps2);
        TextView ps3 = (TextView) pt_ll_inner.findViewById(R.id.ps3);
        TextView ps4 = (TextView) pt_ll_inner.findViewById(R.id.ps4);
        TextView ps5 = (TextView) pt_ll_inner.findViewById(R.id.ps5);

        try{ curUser.getSymptom0();
            ps1.setText(Integer.toString(curUser.getSymptom0().get(curUser.getSymptom0().size() - 1)));
            ps2.setText(Integer.toString(curUser.getSymptom1().get(curUser.getSymptom1().size() - 1)));
            ps3.setText(Integer.toString(curUser.getSymptom2().get(curUser.getSymptom2().size() - 1)));
            ps4.setText(Integer.toString(curUser.getSymptom3().get(curUser.getSymptom3().size() - 1)));
            ps5.setText(Integer.toString(curUser.getSymptom4().get(curUser.getSymptom4().size() - 1)));
        }catch (NullPointerException e){
            e.printStackTrace();
            ps1.setText("N/A");
            ps2.setText("N/A");
            ps3.setText("N/A");
            ps4.setText("N/A");
            ps5.setText("N/A");
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            ps1.setText("N/A");
            ps2.setText("N/A");
            ps3.setText("N/A");
            ps4.setText("N/A");
            ps5.setText("N/A");
        }
        pt_ll_inner.setId(pt_ll.getChildCount());
    }

    public void addPrescriptionTable() {
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



        for (int j = 0; j <= prescriptionList.size(); j++) { // '<=' to handle case size() == 0
            TableRow rxRow = new TableRow(this); //Row showing prescription info
            for(int i = 0; i < NUM_COLUMNS; i++) {
                TextView text = new TextView(this);

                if (j == prescriptionList.size() && j == 0) {
                    if (i == 0)
                        text.setText("No prescription history.");
                    else
                        text.setText("N\\A");
                }
                else if (j == prescriptionList.size())
                    break;
                else {
                    //Determine what content to display based on current loop index
                    switch (i) {
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



