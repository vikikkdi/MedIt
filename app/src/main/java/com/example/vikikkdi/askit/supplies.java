package com.example.vikikkdi.askit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by vikikkdi on 10/22/17.
 */

public class supplies extends Activity {
    private ListView mSearchNFilterLv;
    private EditText mSearchEdt;
    private ArrayList<String> mStringList;
    private ValueAdapter valueAdapter;
    private TextWatcher mSearchTw;
    String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_filter_list);
        Intent inte = getIntent();
        userName = inte.getStringExtra("username");
        initUI();
        initData();
        valueAdapter=new ValueAdapter(mStringList,this);
        mSearchNFilterLv.setAdapter(valueAdapter);
        mSearchEdt.addTextChangedListener(mSearchTw);
        mSearchNFilterLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String ans = (String)(mSearchNFilterLv.getItemAtPosition(position));
//                ans=ans+":"+"1";
//                MainActivity.medicinesShop.add(ans);
            }
        });
        mSearchNFilterLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(supplies.this);
                alertDialog.setTitle("Enter the number of tablet available");
                final EditText input = new EditText(supplies.this);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
//                                Toast.makeText(customerpage.this,input.getText().toString(), Toast.LENGTH_SHORT).show();
                                String ans = (String)(mSearchNFilterLv.getItemAtPosition(position));
                                ans = ans+":"+input.getText().toString();
                                Toast.makeText(getApplicationContext(),ans,Toast.LENGTH_LONG).show();
                                MainActivity.medicinesShop.add(ans);

                                Intent myIntent1 = new Intent(supplies.this, supplies.class);
                                myIntent1.putExtra("username",userName);
                                startActivityForResult(myIntent1, 0);
                            }
                        });
                // Setting Negative "NO" Button
                alertDialog.setNegativeButton("NO",
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
    private void initData() {
        mStringList=new ArrayList<String>();
        final String[] medicine = {"Abilify","Nexium","Humira","Crestor","Advair Diskus","Enbrel","Remicade","Cymbalta","Copaxone","Neulasta","Lantus Solostar","Rituxan","Spiriva Handihaler","Januvia","Atripla","Lantus","Avastin","Lyrica","Oxycontin","Epogen","Celebrex","Truvada","Diovan","Gleevec","Herceptin","Lucentis","Namenda","Vyvanse","Zetia","Levemir","Symbicort","Sovaldi","Novolog Flexpen","Novolog","Tecfidera","Suboxone","Humalog","Xarelto","Seroquel XR","Viagra","Alimta","Victoza 3-Pak","Avonex","Nasonex","Cialis","Gilenya","Stelara","Flovent HFA","Prezista","Procrit"};
        for(int i=0;i<medicine.length;i++){
            mStringList.add(medicine[i]);
        }

        mSearchTw=new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inText = s.toString();
                if(inText.equals("Done")){
                    Intent intent = new Intent(supplies.this, medicineCountShopkeeper.class);
                    intent.putExtra("length",MainActivity.medicinesShop.size());
                    Toast.makeText(getApplicationContext(),String.valueOf(MainActivity.medicinesShop.size()),Toast.LENGTH_LONG).show();
                    for(int i=0;i<MainActivity.medicinesShop.size();i++){
                        intent.putExtra(String.valueOf(i),MainActivity.medicinesShop.get(i));
                    }
                    MainActivity.medicinesShop = new ArrayList<>();
                    intent.putExtra("username",userName);
                    startActivity(intent);
                }

                else    valueAdapter.getFilter().filter(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

    }

    private void initUI() {
        mSearchNFilterLv=(ListView) findViewById(R.id.list_view);
        mSearchEdt=(EditText) findViewById(R.id.txt_search);
    }

}
