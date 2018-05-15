package com.example.vikikkdi.askit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

/**
 * Created by vikikkdi on 10/17/17.
 */

public class shopkeeperpage extends Activity {
    Button suppliesBtn, ordersBtn, signoutBtn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoppage);

        Intent intent = getIntent();
        final String userName = intent.getStringExtra("username");
        suppliesBtn = findViewById(R.id.supplies);
        ordersBtn = findViewById(R.id.orders);
        signoutBtn = findViewById(R.id.signout2);

        suppliesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),supplies.class);
                intent.putExtra("username",userName);
                startActivity(intent);
            }
        });

        ordersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),orders.class);
                intent.putExtra("username",userName);
                startActivity(intent);
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
