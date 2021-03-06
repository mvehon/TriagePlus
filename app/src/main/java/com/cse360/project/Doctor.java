package com.cse360.project;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Doctor implements Serializable {
    private String first_name;
    private String last_name;
    private String password;
    private List<Patient> patients;

    public Doctor() {
        first_name = "";
        last_name = "";
        password = "";
        patients = new ArrayList<Patient>();
    }

    public Doctor(String fn, String ln, String pw) {
        first_name = fn;
        last_name = ln;
        password = pw;
        patients = new ArrayList<Patient>();

    }

    public void createOnServer() {
        ParseObject doc = new ParseObject("Doctor");
        doc.put("first_name", first_name);
        doc.put("last_name", last_name);
        doc.put("password", password);
        doc.put("username", first_name+last_name);
        doc.saveEventually();
    }

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String fn) {
        first_name = fn;
    }

    public void setLastName(String ln) {
        last_name = ln;
    }

    public void setPassword(String pw) {
        password = pw;
    }

    public void addPatient(Patient pt) {
        patients.add(pt);
    }

    public List<Patient> getPts() {
        return patients;
    }


    @Override
    public String toString() {
        return "Dr. " + first_name + " " + last_name;
    }
}