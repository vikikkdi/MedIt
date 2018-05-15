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

public class medicineCount extends Activity {
    double latitude, longitude;
    String userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicinecount);
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        ArrayList<String> medicines = new ArrayList<>();
        String res="";
        int len = intent.getIntExtra("length",0);
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
        String medicinesNeeded = medicines.get(0);
        for(int i=1;i<medicines.size();i++){
            medicinesNeeded = medicinesNeeded + ">" + medicines.get(i);
        }

        GPSTracker gps = new GPSTracker(getApplicationContext());
        if(gps.canGetLocation()){

            LocationManager currLoc = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (currLoc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                longitude = gps.getLongitude();
                latitude = gps.getLatitude();

            }
        }

        String loc = String.valueOf(latitude) +":"+ String.valueOf(longitude);

        int[] cost = {7,49,73,58,30,72,44,78,23,9,40,65,92,42,87,3,27,29,40,12,3,69,9,57,60,33,99,78,16,35,97,26,12,67,10,33,79,49,79,21,67,72,93,36,85,45,28,91,94,57};
        String[] medicine = {"Abilify","Nexium","Humira","Crestor","Advair Diskus","Enbrel","Remicade","Cymbalta","Copaxone","Neulasta","Lantus Solostar","Rituxan","Spiriva Handihaler","Januvia","Atripla","Lantus","Avastin","Lyrica","Oxycontin","Epogen","Celebrex","Truvada","Diovan","Gleevec","Herceptin","Lucentis","Namenda","Vyvanse","Zetia","Levemir","Symbicort","Sovaldi","Novolog Flexpen","Novolog","Tecfidera","Suboxone","Humalog","Xarelto","Seroquel XR","Viagra","Alimta","Victoza 3-Pak","Avonex","Nasonex","Cialis","Gilenya","Stelara","Flovent HFA","Prezista","Procrit"};

        Log.d("NVIKRAMA",medicinesNeeded);

        if(MainActivity.myDb.makeOrder(userName,loc,medicinesNeeded)){
            Toast.makeText(getApplicationContext(),"Your order has been placed",Toast.LENGTH_LONG).show();
            int costT = 0;
            for(int i=0;i<medicines.size();i++){
                String [] part = medicines.get(i).split(":");
                int c = Integer.parseInt(part[1]);
                String nam = part[0];
                for(int j=0;j<50;j++){
                    if(nam.equals(medicine[j])){
                        costT=costT+(c*cost[j]);
                        break;
                    }
                }
            }
            Intent intent1 = new Intent(getApplicationContext(),signout1.class);
            intent1.putExtra("length",medicines.size());
            intent1.putExtra("cost",costT);
            for(int i=0;i<medicines.size();i++)   intent1.putExtra(String.valueOf(i),medicines.get(i));
            startActivity(intent1);
        }else{
            MainActivity.medicines = new ArrayList<>();
            Toast.makeText(getApplicationContext(),"Your request cannot be completed because u either have a pending request or some medicines are unavailable",Toast.LENGTH_LONG).show();
            Intent intent1 = new Intent(getApplicationContext(),customerpage.class);
            startActivity(intent1);
        }

    }
}
