package com.cse360.project.tests;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.widget.CheckBox;
import java.util.List;

import com.cse360.project.AddUser;
import com.cse360.project.R;


/**
 * Created by Joey on 4/29/2015.
 */

//Test how the add-user page loads the doctor list
public class DoctorListTest extends ActivityInstrumentationTestCase2<AddUser> {

    private AddUser activity;
    private List<String> docList;

    public DoctorListTest() {
        super(AddUser.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        SharedPreferences prefs = activity.getSharedPreferences("com.cse360.project",
                Context.MODE_PRIVATE);
    }

    //Ensure doctor list loads properly under normal circumstance
    public void test1(){
        boolean expectedState = true;
        assertEquals(expectedState, activity.createDoctorList());
        assert(activity.getDoctorList().get(0) != "");
    }

    //Check whether method can correctly detect when no doctor data has been recevied
    public void test2(){
        boolean expectedState = false;
        activity.setFlag(); //"Imitate" receiving no doctor data from server
        assertEquals(expectedState, activity.createDoctorList());
    }
}