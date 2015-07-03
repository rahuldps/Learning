package com.example.rj.learning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements Networktask.onclickinterface {
    TextView new_user;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_user);
        Intent strt = new Intent(this, BackgrndService.class);
        startService(strt);
        SharedPreferences pref = getSharedPreferences("Login_boolean", Context.MODE_WORLD_WRITEABLE);
        int value = pref.getInt("signed", 0);
        if (value != 0) {
            Intent registration = new Intent(this, QyeryPostActivity.class);
            startActivity(registration);
        }

        SharedPreferences variable = getSharedPreferences("Location", Context.MODE_WORLD_WRITEABLE);


        new_user = (TextView) findViewById(R.id.newuser);
        new_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.w("Registration_page", "page_Called");
                Intent reg = new Intent(getApplicationContext(), Signup.class);
                startActivity(reg);
                Toast.makeText(MainActivity.this, "button presssed", Toast.LENGTH_SHORT).show();


            }
        });

        Button register = (Button) findViewById(R.id.buti1);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user_login = (EditText) findViewById(R.id.username);
                EditText user_password = (EditText) findViewById(R.id.password);
                JSONObject obj = new JSONObject();
                try {
                    SharedPreferences pref = getSharedPreferences("Location", Context.MODE_WORLD_WRITEABLE);


                    obj.put("email", user_login.getText().toString());
                    obj.put("password", user_password.getText().toString());
                    obj.put("longitude", Float.toString(pref.getFloat("longitude", 0.00F)));
                    obj.put("latitude", Float.toString(pref.getFloat("latitude", 0.00F)));
                    Log.w(Float.toString(pref.getFloat("latitude", 0.00F)), Float.toString(pref.getFloat("longitude", 0.00F)));
                    new Networktask(Constant.baseip + "users/login", obj, MainActivity.this).execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    public void getcalling(JSONObject o) throws JSONException {

        SharedPreferences pref = getSharedPreferences("Login_boolean", Context.MODE_WORLD_WRITEABLE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("hashkey", o.getString("token"));
        editor.putString("email", o.getString("email"));
        editor.putInt("signed", 1);
        editor.commit();

        Intent registration = new Intent(this, QyeryPostActivity.class);
        startActivity(registration);
        finish();


    }
}



