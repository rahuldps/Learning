package com.example.rj.learning;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;


public class PostQuestion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_question);

        Button post = (Button) findViewById(R.id.Postquestion);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText query = (EditText) findViewById(R.id.postdata);

                JSONObject obj = new JSONObject();
                try {
                    SharedPreferences pref = getSharedPreferences("Location", Context.MODE_WORLD_WRITEABLE);
                    SharedPreferences getvalue = getSharedPreferences("Login_boolean", Context.MODE_WORLD_WRITEABLE);
                    obj.put("content", query.getText().toString());
                    obj.put("email", getvalue.getString("email", ""));
                    obj.put("token", getvalue.getString("hashkey", ""));
                    obj.put("longitude", Float.toString(pref.getFloat("longitude", 0.00F)));
                    obj.put("latitude", Float.toString(pref.getFloat("latitude", 0.00F)));
                    Log.w("hi post", obj.toString());

                } catch (Exception e) {
                }

                new PostNetworkTask(PostQuestion.this, Constant.baseip + "posts", obj).execute();
                finish();
            }
        });

    }

}

