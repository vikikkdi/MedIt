package com.example.vikikkdi.askit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Set;

/**
 * Created by vikikkdi on 10/25/17.
 */

public class noOrders extends Activity {
    String userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noorders);
        Intent inte = getIntent();
        userName = inte.getStringExtra("username");
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), shopkeeperpage.class);
        intent.putExtra("username",userName);
        startActivity(intent);
    }
}
