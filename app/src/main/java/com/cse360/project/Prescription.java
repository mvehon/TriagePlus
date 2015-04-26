package com.cse360.project;

import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by Kody on 4/15/2015.
 */
public class Prescription implements Serializable{

    //this is the prescription class that helps the prescirptionForm activity
    private String rx_name;
    private boolean allergies;
    private boolean refil;
    private String fill_date;
    private int duration;
    private String patient;

    public Prescription() {

        rx_name = "";
        allergies = false;
        refil = false;
        fill_date = "";
        patient="";
    }


    public Prescription(String rx_name, String fill_date, int duration) {
        this.rx_name = rx_name;
        this.fill_date = fill_date;
        this.duration = duration;
    }

    public void createOnServer(){
        ParseObject presc = new ParseObject("Prescription");
        presc.put("patient", patient);
        presc.put("rx_name", rx_name);
        presc.put("allergies", allergies);
        presc.put("refil", refil);
        presc.put("fill_date", fill_date);
        presc.put("duration", duration);
        presc.saveEventually();
    }
    public String toEmail(String doctorName){
        String email;
        email = "Dear pharmacy,\n\n";
        email = email + "The patient " + patient + " was prescribed " + rx_name + " by " + doctorName + ".\n";
        email = email + "It will start " + fill_date + " and will last " + duration + " days. \n";

        if(allergies)
            email += "The patient has allergies\n";
        else
            email += "The patient does not have allergies\n";

        if(refil)
            email += "The prescription is refillable\n";
        else
            email += "The prescription is not refillable\n";

        email+= "\nHave a good day,\nTriage+";
        return email;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getRx_name() {
        return rx_name;
    }

    public void setRx_name(String rx_name) {
        this.rx_name = rx_name;
    }

    public boolean isAllergies() {
        return allergies;
    }

    public void setAllergies(boolean allergies) {
        this.allergies = allergies;
    }

    public boolean isRefil() {
        return refil;
    }

    public void setRefil(boolean refil) {
        this.refil = refil;
    }

    public String getFill_date() {
        return fill_date;
    }

    public void setFill_date(String fill_date) {
        this.fill_date = fill_date;
    }

    public void setPatient(String pt){
        pt = pt.replaceAll("\\s+", "");     //remove whitespace
        patient = pt;}
    public String getPatient(){return patient;}

}
