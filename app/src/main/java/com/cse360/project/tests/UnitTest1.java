package com.cse360.project.tests;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cse360.project.Doctor;
import com.cse360.project.Doctor_Main;
import com.cse360.project.InternalStorage;
import com.cse360.project.Patient;
import com.cse360.project.Patient_Main;
import com.cse360.project.Prescription;
import com.cse360.project.R;

/**
 * Created by Daniel on 4/29/15.
 */

//This is test #1 from the email
public class UnitTest1
        extends ActivityInstrumentationTestCase2<Doctor_Main> {

    private Doctor_Main mFirstTestActivity;

    public UnitTest1() {
        super(Doctor_Main.class);
    }

    @Override
    protected void setUp() throws Exception {
        //This method is where you can set data before the activity loads
        super.setUp();
        mFirstTestActivity = getActivity();
        SharedPreferences prefs = mFirstTestActivity.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);


        Patient ptNotCritical = new Patient();
        ptNotCritical.setFirstName("John");
        ptNotCritical.setLastName("Doe");
        ptNotCritical.setDoctor("DanielButtars");
        ptNotCritical.setPassword("abc");
        ptNotCritical.addValues(new int[]{1,2,3,4,5});

        Patient ptCritical = new Patient();
        ptCritical.setFirstName("Jane");
        ptCritical.setLastName("Doe");
        ptCritical.setDoctor("DanielButtars");
        ptCritical.setPassword("abc");
        ptCritical.addValues(new int[]{10,10,10,10,10});

        Doctor doc = new Doctor();
        doc.setFirstName("Daniel");
        doc.setLastName("Buttars");
        doc.setPassword("abc");
        doc.addPatient(ptNotCritical);
        doc.addPatient(ptCritical);


        String username = doc.getFirstName()+doc.getLastName();
        prefs.edit().putString("curUser", username);
        InternalStorage.writeObject(mFirstTestActivity.getBaseContext(), "curUser", doc);

    }
    @SmallTest
    public void test1(){
        //Tests the last pname added, which in this case is John Doe
        TextView pname = (TextView) mFirstTestActivity.findViewById(R.id.pname);
        assertEquals(pname.getText().toString(), "John Doe");
    }
}
