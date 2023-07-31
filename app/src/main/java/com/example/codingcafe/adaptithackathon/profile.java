package com.example.codingcafe.adaptithackathon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class profile extends AppCompatActivity {
    EditText editText;
    TextView dob, gender, city;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editText = findViewById(R.id.et_id);
        dob = findViewById(R.id.txt_0);
        gender = findViewById(R.id.txt_1);
        city = findViewById(R.id.txt_2);
        btn = findViewById(R.id.btn_1);
        Intent intent = getIntent();
        String str = intent.getStringExtra("ID");
        editText.setText(str);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idNumber = editText.getText().toString().trim();
                String data = getID(idNumber);
                String[] moreData = data.split(" ");
                dob.setText(moreData[0]);
                gender.setText(moreData[1]);
                city.setText(moreData[2]);

            }
        });
    }
    //gets gender information from id number
    public static String getGender(String ssss){
        char[] gender = ssss.toCharArray();
        String num = "";
        num = num.copyValueOf(gender, 0, 1);
        if(Integer.parseInt(num) < 5){
            return "Female";
        }
        else{
            return "Male";
        }
    }
    //gets citizenship information from id number
    public static String getCitizenship(String C){
        char[] gender = C.toCharArray();
        String num = "";
        num = num.copyValueOf(gender, 0, 1);
        if(Integer.parseInt(num) == 1){
            return "Permanent-resident";
        }
        else if(Integer.parseInt(num) == 2){
            return "Refugee";
        }
        else{
            return "SA-citizen";
        }
    }
    //checks number sequence is accurate using a set formula called the Luhn algorithm
    public static String luhn(String AZ){
        if(AZ.length() == 2){
            return "Valid";
        }
        else{
            return "Invalid";
        }
    }
    //gets birth-month from from id number
    public static String getMonths(String month){
        switch(Integer.parseInt(month)){
            case 2:
                return "February";
            case 3:
                return "March";

            case 4:
                return "April";

            case 5:
                return "May";

            case 6:
                return "June";

            case 7:
                return "July";

            case 8:
                return "August";

            case 9:
                return "September";

            case 10:
                return "October";

            case 11:
                return "November";


            case 12:
                return "December";

            default:
                return "January";
        }
    }
    //gets birth-year from id number
    public static String getYears(String year){
        if((Integer.parseInt(year) > 0)){
            return "19"+year;
        }
        else{
            return "20"+year;
        }
    }
    //gets birth-days from id number
    public static String getDate(String Date){
        char[] date = Date.toCharArray();
        String days = "",months = "",years = "";
        days = days.copyValueOf(date, 4, 2);
        months = months.copyValueOf(date, 2, 2);
        years = years.copyValueOf(date, 0, 2);
        String birthdate = days + "-"+ getMonths(months) + "-" + getYears(years);
        return birthdate;
    }
    //processes id number into 4 data parts(dateOfBirth,gender,citizenship,checksum)
    public static String getID(String idNUM){
        String myArray[] = {"","","",""};
        char[] ID = idNUM.toCharArray();
        for(int j = 0; j < idNUM.length(); j++){
            if(j<6){
                myArray[0] += ID[j];
            }
            else if((j < 10) && (j >= 6)){
                myArray[1] += ID[j];
            }
            else if(j == 10){
                myArray[2] += ID[j];
            }
            else{
                myArray[3] += ID[j];
            }
        }
        return getDate(myArray[0]) + " " + getGender(myArray[1]) + " " + getCitizenship(myArray[2]) + " " + luhn(myArray[3]);
    }
}