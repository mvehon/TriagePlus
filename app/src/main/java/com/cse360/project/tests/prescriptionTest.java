package com.cse360.project.tests;

        import com.cse360.project.Prescription;

/**
 * Created by Kody on 4/29/2015.
 */

//this verifies the prescriptions built in date normalization as well as the error checking
public class prescriptionTest {
   // public static void main(String[] args){
        Prescription p1 = new Prescription("Drug", "04/27/15", 3, "Kody", false, false);

        Prescription p2 = new Prescription("Drug", "4/27/15", 3, "Kody", true, false);

        Prescription p3 = new Prescription("Drug", "04/27/15", 3, "", false, false);

        Prescription p4 = new Prescription("Drug", "04/34/15", 3, "Kody", false, false);

        Prescription p5 = new Prescription("D", "04/27/15", 3, "Kody", false, true);
  //  }
}
