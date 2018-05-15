package com.example.vikikkdi.askit;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by vikikkdi on 10/17/17.
 */

//java.lang.RuntimeException: Unable to start activity ComponentInfo{com.example.vikikkdi.askit/com.example.vikikkdi.askit.customerpage}: java.lang.RuntimeException: Your content must have a ListView whose id attribute is 'android.R.id.list'

public class customerpage extends Activity {
    private ListView mSearchNFilterLv;
    private EditText mSearchEdt;
    private ArrayList<String> mStringList;
    private ValueAdapter valueAdapter;
    private TextWatcher mSearchTw;
    public Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_filter_list);
        initUI();
        initData();
        intent = getIntent();
        valueAdapter=new ValueAdapter(mStringList,this);
        mSearchNFilterLv.setAdapter(valueAdapter);
        mSearchEdt.addTextChangedListener(mSearchTw);
        mSearchNFilterLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String ans = (String)(mSearchNFilterLv.getItemAtPosition(position));
//                ans=ans+":"+"1";
//                MainActivity.medicines.add(ans);
            }
        });
        mSearchNFilterLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(customerpage.this);
                alertDialog.setTitle("Enter the required amount");
                final EditText input = new EditText(customerpage.this);
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
                                MainActivity.medicines.add(ans);

                                Intent myIntent1 = new Intent(customerpage.this, customerpage.class);
                                myIntent1.putExtra("username",intent.getStringExtra("username"));
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
                    Intent intent1 = new Intent(customerpage.this, medicineCount.class);
                    intent1.putExtra("username",intent.getStringExtra("username"));
                    intent1.putExtra("length",MainActivity.medicines.size());
                    Toast.makeText(getApplicationContext(),String.valueOf(MainActivity.medicines.size()),Toast.LENGTH_LONG).show();
                    for(int i=0;i<MainActivity.medicines.size();i++){
                        intent1.putExtra(String.valueOf(i),MainActivity.medicines.get(i));
                    }
                    MainActivity.medicines = new ArrayList<>();
                    startActivity(intent1);
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