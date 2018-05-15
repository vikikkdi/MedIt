package com.example.vikikkdi.askit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vikikkdi on 10/24/17.
 */

public class signout1 extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordersuccess);

        Intent inte = getIntent();
        int len = inte.getIntExtra("length",0);
        int cost = inte.getIntExtra("cost",0);
        ArrayList<String> medicines = new ArrayList<>();
        for(int i=0;i<len;i++){
            String medicine = inte.getStringExtra(String.valueOf(i));
            medicines.add(medicine);
        }

        String result="\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\t\t\tMEDICINES ::: \n";

        for(int i=0;i<medicines.size();i++){
            if(result.equals("MEDICINES ::: \n"))   result += "\t\t\t"+medicines.get(i);
            else    result = result + "\n\t\t\t" + medicines.get(i);
        }

        result = result + "\n\t\t\t" + "COST ::: " + String.valueOf(cost);

        RelativeLayout linearlayout = findViewById(R.id.ll);
        TextView htext =new TextView(this);
        htext.setText(result);
        htext.setId(5);
        htext.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        linearlayout.addView(htext);

        Button btn = findViewById(R.id.signout1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),customer.class);
                startActivity(intent);
            }
        });
    }
}
