package com.example.vikikkdi.askit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by vikikkdi on 10/17/17.
 */

public class customer extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logincustomer);
        Button submit = findViewById(R.id.submituser);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.editText);
                EditText password = findViewById(R.id.editText2);
                String userName = username.getText().toString();
                String passWord = password.getText().toString();

                String result = MainActivity.myDb.getDetails(userName,true);

                if(result==null){
                    Intent intent = new Intent(getApplicationContext(), customer.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(),"Username or password incorrect",Toast.LENGTH_LONG).show();
                }else {
                    String[] parts = result.split(":");
                    String user = parts[0];
                    String emailId = parts[1];
                    String phoneNo = parts[2];
                    String pass = parts[3];
                    if(!pass.equals(passWord)){
                        Intent intent = new Intent(getApplicationContext(), customer.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Username or password incorrect",Toast.LENGTH_LONG).show();
                    }else{
                        Intent intent = new Intent(getApplicationContext(), customerpage.class);
                        intent.putExtra("username",user);
                        getApplicationContext().startActivity(intent);
                    }
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }
}
