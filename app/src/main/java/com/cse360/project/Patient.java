package com.cse360.project;

import java.io.Serializable;
import java.util.ArrayList;

public class Patient implements Serializable {
	private String first_name;
    private String last_name;
    private String password;
    private String doctor;
    private ArrayList<Integer> symptom0, symptom1, symptom2;

	public Patient() {
		first_name = "";
		last_name = "";
		password = "";
		symptom0 = new ArrayList<Integer>();
		symptom1 = new ArrayList<Integer>();
		symptom2 = new ArrayList<Integer>();
	}

	public Patient(String fn, String ln, String pw, String doc) {
		first_name = fn;
		last_name = ln;
		password = pw;
		doctor = doc;
		symptom0 = new ArrayList<Integer>();
		symptom1 = new ArrayList<Integer>();
		symptom2 = new ArrayList<Integer>();
	}

    public String getFirstName(){
        return first_name;
    }

    public String getLastName(){
        return last_name;
    }

    public String getPassword(){
        return password;
    }

    public String getDoctor(){
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

    public void setDoctor(String doc){
		doctor = doc;
	}

    public ArrayList<Integer> getSymptom0(){
        return symptom0;
    }
    public ArrayList<Integer> getSymptom1(){
        return symptom1;
    }
    public ArrayList<Integer> getSymptom2(){
        return symptom2;
    }


	public void addValues(int[] pains){
		for(int i=0; i<pains.length;i++){
			switch(i){
			case 0:
				symptom0.add(pains[i]);
				break;
			case 1:
				symptom1.add(pains[i]);
				break;
			case 2:
				symptom2.add(pains[i]);
				break;
			}
		}
	}
}
