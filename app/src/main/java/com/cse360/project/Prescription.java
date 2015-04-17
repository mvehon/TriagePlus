package com.cse360.project;

/**
 * Created by Kody on 4/15/2015.
 */
public class Prescription {

//this is the prescription class that helps the prescirptionForm activity
    String rx_name;
    boolean allergies;
    boolean refil;
    String fill_date;

    public Prescription(){

        rx_name = "";
        allergies = false;
        refil = false;
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
