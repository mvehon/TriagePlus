package com.cse360.project.tests;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.cse360.project.AddUser;
import com.cse360.project.PrescriptionForm;
import com.cse360.project.R;

/**
 * Created by Kody on 4/29/2015.
 */

//this will test if a prescription form is initialized correctly
public class prescriptionFormTest extends ActivityInstrumentationTestCase2<PrescriptionForm> {

    private PrescriptionForm mTest1;
    private EditText mTest1Text, mTest2Text, mTest3Text;
    private CheckBox allergiesState, refillState;

    public prescriptionFormTest() {
        super(PrescriptionForm.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mTest1 = getActivity();
        //String expected = "Prescription Duration";
        // assertEquals(expected, mTest1Text.getHint().toString());
    }

    //checks that the prescription duration is set to the hint
    @SmallTest
    public void test1(){
        String expected = "Prescription Duration";
        mTest1Text = (EditText) mTest1.findViewById(R.id.durText);
        assertEquals(expected, mTest1Text.getHint());
    }

    //checks dateTexts setup
    @SmallTest
    public void test2(){
        mTest2Text = (EditText) mTest1.findViewById(R.id.dateText);
        String expected = "Date (mm/dd/yy)";
        assertEquals(expected, mTest2Text.getHint().toString());
    }
    //checks the setup of the prescriptionText
    @SmallTest
    public void test3(){
        mTest3Text = (EditText) mTest1.findViewById(R.id.prescriptionText);
        String expected = "Prescription Name";
        assertEquals(expected, mTest3Text.getHint().toString());
    }
    //verifies that the checkbox is correctly created with a false default
    @SmallTest
    public void test4(){
        boolean state = false;
        allergiesState = (CheckBox) mTest1.findViewById(R.id.allergiesCheck);
        assertEquals(state, allergiesState.isChecked());
    }
    //verifies that the checkbox is correctly created with a false default value
    @SmallTest
    public void test5(){
        refillState = (CheckBox) mTest1.findViewById(R.id.refilCheck);
        boolean state = false;
        assertEquals(state, refillState.isChecked());
    }
}