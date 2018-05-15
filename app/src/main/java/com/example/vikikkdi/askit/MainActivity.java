package com.example.vikikkdi.askit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends Activity {

    static DBHelper myDb ;
    Button shopkeeper, customer, signUp, location;
    ToggleButton custorshop;
    double latitude = -1, longitude = -1;
    static public ArrayList<String> medicines = new ArrayList<>();
    static public ArrayList<String> medicinesShop = new ArrayList<>();
    public void createInstance(){
        myDb = new DBHelper(this);
        shopkeeper= findViewById(R.id.shop);
        customer = findViewById(R.id.customer);
        signUp = findViewById(R.id.signup);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createInstance();

        shopkeeper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), shopkeeper.class);
                v.getContext().startActivity(intent);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), customer.class);
                v.getContext().startActivity(intent);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.signup);
                Button submit = findViewById(R.id.submit);
                custorshop = findViewById(R.id.customerOrnot);
                location = findViewById(R.id.location);

                custorshop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(custorshop.isChecked()){
                            location.setVisibility(buttonView.INVISIBLE);
                        }else{
                            location.setVisibility(buttonView.VISIBLE);
                        }
                    }
                });
                location.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GPSTracker gps = new GPSTracker(getApplicationContext());
                        if(gps.canGetLocation()){

                            LocationManager currLoc = (LocationManager) getSystemService(LOCATION_SERVICE);
                            if (currLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                                longitude = gps.getLongitude();
                                latitude = gps.getLatitude();

                            }
                        }
                        if(longitude ==0 && latitude ==0)   Toast.makeText(getApplicationContext(),"Failed to get location",Toast.LENGTH_LONG).show();
                        else    Toast.makeText(getApplicationContext(),"Location Obtained",Toast.LENGTH_LONG).show();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText username = findViewById(R.id.username);
                        EditText emailid = findViewById(R.id.emailid);
                        EditText phoneNo = findViewById(R.id.phone);
                        EditText pass1 = findViewById((R.id.pass1));
                        EditText pass2 = findViewById((R.id.pass2));

                        String userName = username.getText().toString();
                        String emailId = emailid.getText().toString();
                        String phoneNumber = phoneNo.getText().toString();
                        String password = pass1.getText().toString();
                        String confirmPassword = pass2.getText().toString();
                        boolean type = custorshop.isChecked();

                        boolean userflag = true, email = true, ph = true, pass = true;

                        if(userName.length()>15 || userName.length()<=4){
                            username.setError("Length of the Username should be within 5 to 14");
                            userflag = false;
                        }

                        email = android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches();

                        ph = (phoneNumber.length() == 10) ;

                        for(int i=0;i<phoneNumber.length();i++){
                            if(phoneNumber.charAt(i) >='0' && phoneNumber.charAt(i) <= '9'){
                                continue;
                            }else{
                                ph = false;
                            }
                        }

                        if(!password.equals(confirmPassword)){
                            pass = false;
                        }

                        if(!userflag){
                            username.setError("Length of Username has to be between 5 to 15");
                        }
                        if(!email){
                            emailid.setError("Invalid Email-ID");
                        }
                        if(!ph){
                            phoneNo.setError("Incorrect phone number");
                        }
                        if(!pass){
                            pass1.setError("Passwords not matching");
                        }
                        if(userflag && email && ph && pass){
                            String loc = "";
                            if(!type){
                                loc = String.valueOf(latitude) + ":" + String.valueOf(longitude);
                            }
                            if(myDb.addUser(userName, emailId, phoneNumber, password, type, loc)) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "User Added to Database", Toast.LENGTH_LONG).show();
                            }else{
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "User Already registered", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
