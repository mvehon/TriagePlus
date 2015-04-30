package com.cse360.project.tests;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import com.cse360.project.InternalStorage;
import com.cse360.project.Patient;
import com.cse360.project.Patient_Main;
import com.cse360.project.Prescription;
import com.cse360.project.R;
import com.cse360.project.Start;

/**
 * Created by Matthew on 4/27/2015.
 */

//This is test #3 from the email I sent, feel free to use this as a guideline for other test cases
public class StartTest
        extends ActivityInstrumentationTestCase2<Start> { //The <Patient_Main> indicates the class being tested

    private Start mFirstTestActivity;

    public StartTest() {
        super(Start.class);
    }

    @Override
    protected void setUp() throws Exception {
        //This method is where you can set data before the activity loads
        super.setUp();
        mFirstTestActivity = getActivity();
        SharedPreferences prefs = mFirstTestActivity.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);

        prefs.edit().putBoolean("firsttime", false).commit();
        prefs.edit().putBoolean("loggedin", false).commit();
        prefs.edit().putBoolean("remember", true).commit();
        prefs.edit().putString("curUser", "TestPatient").commit();
        prefs.edit().putString("user_fn", "Test").commit();
        prefs.edit().putString("user_ln", "Patient").commit();
        prefs.edit().putString("user_pw", "abc").commit();
        CheckBox remember = (CheckBox) mFirstTestActivity.findViewById(R.id.checkBoxRemember);
        remember.setChecked(true);
    }

    @SmallTest
    public void testBlahBlah(){
        //Set the things you want to test here, the function name MUST BEGIN with "test" OR IT WON'T WORK
        EditText loginfn = (EditText) mFirstTestActivity.findViewById(R.id.textFirstName);
        assertEquals(loginfn.getText().toString(), "Test"); //This is the actual test statement, passes or fails
    }

    @SmallTest
    public void testBlahBlah1(){
        //Set the things you want to test here, the function name MUST BEGIN with "test" OR IT WON'T WORK
        EditText loginln = (EditText) mFirstTestActivity.findViewById(R.id.textLastName);
        assertEquals(loginln.getText().toString(), "Patient"); //This is the actual test statement, passes or fails
    }

    @SmallTest
    public void testBlahBlah2(){
        //Set the things you want to test here, the function name MUST BEGIN with "test" OR IT WON'T WORK
        EditText password = (EditText) mFirstTestActivity.findViewById(R.id.password);
        assertEquals(password.getText().toString(), "abc"); //This is the actual test statement, passes or fails
    }
}
