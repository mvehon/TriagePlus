package com.cse360.project;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.content.Context;
import android.widget.TextView;
import android.widget.LinearLayout;

import java.io.IOException;


public class Assessment extends Activity implements SeekBar.OnSeekBarChangeListener {
    private Context context;
    private TextView pslide0_txt,pslide1_txt,pslide2_txt;
    private SharedPreferences prefs;
    private Patient curUser;
    private String username;
    private ImageButton pslide0_leftbtn, pslide0_rightbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assessment);

        context = this;
        prefs = this.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        username = prefs.getString("curUser", "");


        //Read in patient from internal memory
        try {
            curUser = (Patient) InternalStorage.readObject(getBaseContext(),
                    username);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Assign to UI elements
        final SeekBar pslide0 = (SeekBar) findViewById(R.id.pslide0);
        pslide0_txt= (TextView) findViewById(R.id.pslide0_txt);
        final SeekBar pslide1 = (SeekBar) findViewById(R.id.pslide1);
        pslide1_txt= (TextView) findViewById(R.id.pslide1_txt);
        final SeekBar pslide2 = (SeekBar) findViewById(R.id.pslide2);
        pslide2_txt= (TextView) findViewById(R.id.pslide2_txt);

        //Could set to last reported values here
        pslide0.setProgress(0);
        pslide1.setProgress(0);
        pslide2.setProgress(0);


        //Set the text of the initial seekbar level
        pslide0_txt.setText("Symptom 0: " + Integer.toString(pslide0.getProgress()+1));
        pslide1_txt.setText("Symptom 1: " + Integer.toString(pslide1.getProgress()+1));
        pslide2_txt.setText("Symptom 2: " + Integer.toString(pslide2.getProgress()+1));

        //Set custom seekbar drawables
        pslide0.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress1));
        pslide1.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress1));
        pslide2.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress1));

        //Set seekbar listener
        pslide0.setOnSeekBarChangeListener(this);
        pslide1.setOnSeekBarChangeListener(this);
        pslide2.setOnSeekBarChangeListener(this);

        //Instantiate layout object ( test )
        LinearLayout submit = (LinearLayout) findViewById(R.id.submit);

        //Submit form here
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int painArray[] = {pslide0.getProgress(),pslide1.getProgress(),pslide2.getProgress()};
                curUser.addValues(painArray);
                //Close activity and start Patient_Main
                startActivity(new Intent(Assessment.this, Patient_Main.class));
                Assessment.this.finish();
            }
        });

        //The plus and minus buttons to the side
        pslide0_leftbtn = (ImageButton) findViewById(R.id.pslide0_leftbtn);
        pslide0_rightbtn = (ImageButton) findViewById(R.id.pslide0_rightbtn);

        pslide0_leftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pslide0.setProgress(pslide0.getProgress()-1);
            }
        });
        pslide0_rightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pslide0.setProgress(pslide0.getProgress()+1);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Set the updated background of the seekbar and change the text to the left
        sliderStyle(seekBar, progress);
        switch(seekBar.getId()){
            case R.id.pslide0:
                pslide0_txt.setText("Symptom 0: " + Integer.toString(progress+1));
                break;
            case R.id.pslide1:
                pslide1_txt.setText("Symptom 1: " + Integer.toString(progress+1));
                break;
            case R.id.pslide2:
                pslide2_txt.setText("Symptom 2: " + Integer.toString(progress+1));
                break;

        }
    }

    //Required methods
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    //Create action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_menu, menu);
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
            Assessment.this.finish();
            return true;
        }
        prefs.edit().clear().commit();
        return super.onOptionsItemSelected(item);
    }

    public void sliderStyle(SeekBar seekBar, int progress){
        //Sets the background color of the slider based on its current value
        if(progress==0 || progress==1){
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress1));
        }
        else if(progress==2 || progress==3){
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress3));
        }
        else if(progress==4 || progress==5){
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress5));
        }
        else if(progress==6 || progress==7){
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress7));
        }
        else if(progress==8 || progress==9){
            seekBar.setProgressDrawable(getResources().getDrawable(R.drawable.seekbar_progress9));
        }
    }
}
