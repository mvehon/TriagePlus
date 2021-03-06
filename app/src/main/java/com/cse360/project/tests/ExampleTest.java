package com.cse360.project.tests;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TableLayout;
import com.cse360.project.InternalStorage;
import com.cse360.project.Patient;
import com.cse360.project.Patient_Main;
import com.cse360.project.Prescription;
import com.cse360.project.R;

/**
 * Created by Matthew on 4/27/2015.
 */

//This is test #3 from the email I sent, feel free to use this as a guideline for other test cases
public class ExampleTest
        extends ActivityInstrumentationTestCase2<Patient_Main> { //The <Patient_Main> indicates the class being tested

    private Patient_Main mFirstTestActivity;

    public ExampleTest() {
        super(Patient_Main.class);
    }

    @Override
    protected void setUp() throws Exception {
        //This method is where you can set data before the activity loads
        super.setUp();
        mFirstTestActivity = getActivity();
        SharedPreferences prefs = mFirstTestActivity.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);

        Prescription pre = new Prescription();
        pre.setDuration(5);
        pre.setFill_date("1/1/1");
        pre.setRx_name("Penicillin");
        pre.setRefil(false);
        pre.setAllergies(false);
        pre.setPatient("HerpDerp");


        Patient pt = new Patient();
        pt.setFirstName("Herp");
        pt.setLastName("Derp");
        pt.setDoctor("MatthewVehon");
        pt.setPassword("abc");
        pt.addValues(new int[]{1,2,3,4,5});
        pt.addPrescription(pre);

        String username = pt.getFirstName()+pt.getLastName();
        prefs.edit().putString("curUser", username);
        InternalStorage.writeObject(mFirstTestActivity.getBaseContext(), "curUser", pt);

    }
    @SmallTest
    public void testBlah(){
        //Set the things you want to test here, the function name MUST BEGIN with "test" OR IT WON'T WORK
        TableLayout loginrl = (TableLayout) mFirstTestActivity.findViewById(R.id.table);
        assertEquals(loginrl.getChildCount(), 2); //This is the actual test statement, passes or fails
    }
}
