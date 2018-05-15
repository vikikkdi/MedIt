package com.example.vikikkdi.askit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vikikkdi on 10/22/17.
 */

public class orders extends Activity {
    private ListView mSearchNFilterLv;
    private ArrayList<String> mStringList;
    private ArrayAdapter valueAdapter;
    String userName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orderlistview);
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");
        String toBeShown = MainActivity.myDb.getOrders(userName);

        if(toBeShown.equals("")){
            Intent inte = new Intent(getApplicationContext(),noOrders.class);
            inte.putExtra("username",userName);
            startActivity(inte);
        }

        String[] parts = toBeShown.split(",");
        final HashMap<String,String> hashMap = new HashMap<>();

        for(int i=0;i<parts.length;i++){
            String[] pa = parts[i].split("-");
            String [] second = pa[1].split(">");
            String result="";
            for(int j=0;j<second.length;j++){
                if(result.equals(""))   result = second[j];
                else    result = result +","+ second[j];
            }
            hashMap.put(pa[0],result);
        }
        initUI();
        mStringList=new ArrayList<String>();
        for(int i=0;i<parts.length;i++){
            String[] pa = parts[i].split("-");
            mStringList.add(pa[0]);
        }

        Log.d("NVIKRAMA",String.valueOf(mStringList.size()));

        valueAdapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,mStringList);
        mSearchNFilterLv.setAdapter(valueAdapter);
        mSearchNFilterLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(orders.this);
                alertDialog.setTitle("order details");
                TextView input1 = new TextView(orders.this);
                final EditText input = new EditText(orders.this);
                final String name = (String)(mSearchNFilterLv.getItemAtPosition(position));
                input1.setText(hashMap.get(name));
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                input1.setLayoutParams(lp);
                alertDialog.setView(input1);
                alertDialog.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
//                                Toast.makeText(customerpage.this,input.getText().toString(), Toast.LENGTH_SHORT).show();
                                String ans = (String)(mSearchNFilterLv.getItemAtPosition(position));
                                Toast.makeText(getApplicationContext(),"This order has been deleted from the databse",Toast.LENGTH_LONG).show();
                                String location = MainActivity.myDb.getOrderLocation(name);
                                MainActivity.myDb.deleteOrder(name);
                                double latitude = Double.parseDouble(location.split(":")[0]);
                                double longitude = Double.parseDouble(location.split(":")[1]);
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                String data = String.format("geo:%s,%s", latitude, longitude);
                                intent.setData(Uri.parse(data));
                                startActivity(intent);
//                                Intent myIntent1 = new Intent(orders.this, orders.class);
//                                myIntent1.putExtra("username",userName);
//                                startActivityForResult(myIntent1, 0);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog
                                dialog.cancel();
                            }
                        });

                // closed

                // Showing Alert Message
                alertDialog.show();
                return false;
            }
        });
    }

    private void initUI() {
        mSearchNFilterLv=(ListView) findViewById(R.id.list_view1);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), shopkeeperpage.class);
        intent.putExtra("username",userName);
        startActivity(intent);
    }
}
