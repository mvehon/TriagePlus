package com.cse360.project;

/**
 * Created by Kody on 4/15/2015.
 */
public class Prescription {

    //this is the prescription class that helps the prescirptionForm activity
    private String rx_name;
    private boolean allergies;
    private boolean refil;
    private String fill_date;
    private int duration;

    public Prescription() {

        rx_name = "";
        allergies = false;
        refil = false;
        fill_date = "";
    }


    public Prescription(String rx_name, String fill_date, int duration) {
        this.rx_name = rx_name;
        this.fill_date = fill_date;
        this.duration = duration;
    }

    //@Override
    public String toEmail(String patientName, String doctorName){
        String email;
        email = "Dear pharmacy,\n\n";
        email = email + "The patient " + patientName + " was prescribed " + rx_name + " by " + doctorName + ".\n";
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

}
