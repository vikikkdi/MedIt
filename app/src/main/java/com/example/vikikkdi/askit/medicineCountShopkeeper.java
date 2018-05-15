package com.example.vikikkdi.askit;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.attr.id;

/**
 * Created by vikikkdi on 10/22/17.
 */

public class medicineCountShopkeeper extends Activity {
    double latitude, longitude;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinecount);
        ArrayList<String> medicines = new ArrayList<>();
        String res="";
        Intent intent = getIntent();
        int len = intent.getIntExtra("length",0);
        String userName = intent.getStringExtra("username");
        for(int i=0;i<len;i++){
            String medicine = intent.getStringExtra(String.valueOf(i));
            boolean flag = true;
            for(int j=0;j<medicines.size();j++){
                if(medicines.get(j).equals(medicine)) {
                    flag = false;
                    break;
                }
            }
            if(flag)
                medicines.add(medicine);
        }
        String toBeAdded = "";
        for(int i=0;i<medicines.size();i++){
            if(toBeAdded.equals(""))    toBeAdded = medicines.get(i);
            else toBeAdded = toBeAdded + ">" + medicines.get(i);
        }

        if(MainActivity.myDb.addInventories(userName,toBeAdded)){
            Intent myIntent = new Intent(getApplicationContext(),shopkeeperpage.class);
            myIntent.putExtra("username",userName);
            startActivity(myIntent);
        }
    }
}
