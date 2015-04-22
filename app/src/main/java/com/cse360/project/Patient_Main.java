package com.cse360.project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class Patient_Main extends Activity{

    SharedPreferences prefs;
    Button submitButton;
    Patient curUser;

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

        //TODO ADD IN PRESCRIPTION-VIEWING

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



