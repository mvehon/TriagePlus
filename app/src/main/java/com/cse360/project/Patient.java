package com.cse360.project;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Patient implements Serializable {
    private static final long serialVersionUID = -6348718630459274463L;
    private String first_name;
    private String last_name;
    private String password;
    private String doctor;
    private ArrayList<Integer> symptom0, symptom1, symptom2, symptom3, symptom4;
    private ArrayList<Prescription> presc;

    public Patient() {
        first_name = "";
        last_name = "";
        password = "";
        symptom0 = new ArrayList<Integer>();
        symptom1 = new ArrayList<Integer>();
        symptom2 = new ArrayList<Integer>();
        symptom3 = new ArrayList<Integer>();
        symptom4 = new ArrayList<Integer>();
        presc = new ArrayList<Prescription>();
    }

    public Patient(String fn, String ln, String pw, String pt) {
        first_name = fn;
        last_name = ln;
        password = pw;
        doctor = pt;
        symptom0 = new ArrayList<Integer>();
        symptom1 = new ArrayList<Integer>();
        symptom2 = new ArrayList<Integer>();
        symptom3 = new ArrayList<Integer>();
        symptom4 = new ArrayList<Integer>();
        presc = new ArrayList<Prescription>();

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

    public String getDoctor() {
        return doctor;
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

    public void setDoctor(String pt) {
        doctor = pt;
    }

    public ArrayList<Integer> getSymptom0() {
        return symptom0;
    }

    public ArrayList<Integer> getSymptom1() {
        return symptom1;
    }

    public ArrayList<Integer> getSymptom2() {
        return symptom2;
    }

    public ArrayList<Integer> getSymptom3() {
        return symptom3;
    }

    public ArrayList<Integer> getSymptom4() {
        return symptom4;
    }

    public void setSymptom0(List<Integer> sym) {
        symptom0 = (ArrayList<Integer>) sym;
    }

    public void setSymptom1(List<Integer> sym) {
        symptom1 = (ArrayList<Integer>) sym;
    }

    public void setSymptom2(List<Integer> sym) {
        symptom2 = (ArrayList<Integer>) sym;
    }

    public void setSymptom3(List<Integer> sym) {
        symptom3 = (ArrayList<Integer>) sym;
    }

    public void setSymptom4(List<Integer> sym) {
        symptom4 = (ArrayList<Integer>) sym;
    }

    public ArrayList<Prescription> getPrescList() {
        return presc;
    }

    public void setPrescList(List<Prescription> pre) {
        presc = (ArrayList<Prescription>) pre;
    }

    public void addPrescription(Prescription pre){
        presc.add(pre);
    }

    public void createOnServer() {
        ParseObject pt = new ParseObject("Patient");
        pt.put("first_name", first_name);
        pt.put("last_name", last_name);
        pt.put("password", password);
        pt.put("username", first_name + last_name);
        String tempdr = getDoctor();
        tempdr = tempdr.substring(4);
        tempdr = tempdr.replaceAll("\\s+", "");
        pt.put("doctor", tempdr);
        pt.saveEventually();
    }

    public void updateOnServer() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Patient");
        query.whereEqualTo("username", first_name + last_name);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    if (parseObjects.size() > 0) {
                        ParseObject pt = parseObjects.get(0);
                        pt.add("symptom0", getSymptom0().get(getSymptom0().size() - 1));
                        pt.add("symptom1", getSymptom1().get(getSymptom1().size() - 1));
                        pt.add("symptom2", getSymptom2().get(getSymptom2().size() - 1));
                        pt.add("symptom3", getSymptom3().get(getSymptom3().size() - 1));
                        pt.add("symptom4", getSymptom4().get(getSymptom4().size() - 1));
                        pt.saveInBackground();
                    }
                }
            }
        });
    }

    public void addValues(int[] pains) {
        for (int i = 0; i < pains.length; i++) {
            switch (i) {
                case 0:
                    symptom0.add(pains[i]);
                    break;
                case 1:
                    symptom1.add(pains[i]);
                    break;
                case 2:
                    symptom2.add(pains[i]);
                    break;
                case 3:
                    symptom3.add(pains[i]);
                    break;
                case 4:
                    symptom4.add(pains[i]);
                    break;
            }
        }
    }

    @Override
    public String toString() {
        return first_name + " " + last_name;
    }
}
