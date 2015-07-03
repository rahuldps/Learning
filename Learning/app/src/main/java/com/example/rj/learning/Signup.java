package com.example.rj.learning;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rj on 6/28/15.
 */
public class Signup extends Activity implements NetworkRegistration.onclickinterface {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("dsadasd", "sadasda");

        Button button = (Button) findViewById(R.id.register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText user_login = (EditText) findViewById(R.id.login_username);
                EditText user_password = (EditText) findViewById(R.id.login_password);
                EditText user_mail = (EditText) findViewById(R.id.login_email);
                EditText user_phone = (EditText) findViewById(R.id.login_phone);


                JSONObject obj = new JSONObject();
                try {
                    obj.put("name", user_login.getText().toString());
                    obj.put("password", user_password.getText().toString());
                    obj.put("email", user_mail.getText().toString());
                    obj.put("phone", user_phone.getText().toString());
                    Log.w(user_login.getText().toString(), user_password.getText().toString());
                    new NetworkRegistration(Constant.baseip + "users", obj, Signup.this).execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    public void getcall() {
        finish();
    }
}
