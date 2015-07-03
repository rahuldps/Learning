package com.example.rj.learning;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Rj on 6/28/15.
 */
public class QyeryPostActivity extends Activity implements UpdateFeedNetworkTask.onclickinterface {

    private static final String TAG_CONTENTS = "content";
    private static final String TAG_CREATE = "created_at";
    private static final String TAG_USER_ID = "user_id";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_PHONE = "phone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_layout);

        Button post_it = (Button) findViewById(R.id.Postquestion);
        post_it.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent launch = new Intent(QyeryPostActivity.this, PostQuestion.class);
                startActivity(launch);

            }
        });

        Button but1 = (Button) findViewById(R.id.Refreshfeed);
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences("Location", Context.MODE_WORLD_WRITEABLE);
                SharedPreferences getdata = getSharedPreferences("Login_boolean", Context.MODE_WORLD_WRITEABLE);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("longitude", Float.toString(pref.getFloat("longitude", 0.00F)));
                    obj.put("latitude", Float.toString(pref.getFloat("latitude", 0.00F)));
                    obj.put("email", getdata.getString("email", ""));
                    obj.put("token", getdata.getString("hashkey", ""));
                    Log.w("hi people", obj.toString());
                    new UpdateFeedNetworkTask(Constant.baseip + "posts/nearby", obj, QyeryPostActivity.this).execute();
                } catch (Exception e) {
                }


            }
        });


    }


    @Override
    public void getcall(JSONObject o) {

        Log.w("he there ", o.toString());

        JSONArray contacts = null;
        if (o == null)
            return;


        ArrayList<HashMap<String, String>> contactList;
        contactList = new ArrayList<HashMap<String, String>>();
/*
        {"user_list":[{"id":4,"name":"ghuseda","email":"ghuseda@gmail.com",
                "password":"ghuseda","created_at":"2015-07-02T14:35:24.600Z",
                "updated_at":"2015-07-02T14:35:24.600Z","phone":"12345"},
            {"id":5,"name":"rahul","email":"rahul@gmail.com","password":"rahul",
                    "created_at":"2015-07-02T14:42:27.789Z","updated_at":"2015-07-02T14:42:27.789Z",
                    "phone":"8130170159"}]}
*/
        try {

            contacts = o.getJSONArray("user_detail");

            for (int i = 0; i < contacts.length(); i++) {
                JSONObject c = contacts.getJSONObject(i);


                String content = c.getString(TAG_CONTENTS);
                String created_at = c.getString(TAG_CREATE);
                String user_id = c.getString(TAG_USER_ID);
                String latitude = c.getString(TAG_LATITUDE);
                String longitude = c.getString(TAG_LONGITUDE);
                String phone = c.getString(TAG_PHONE);
                String name = c.getString(TAG_NAME);

                String[] tokens = created_at.split("T");
                String date = tokens[0];
                String time = tokens[1].split("\\.")[0];


// tmp hashmap for single contact
                HashMap<String, String> contact = new HashMap<String, String>();

// adding each child node to HashMap key => value
                // Geocoder gcd = new Geocoder(QyeryPostActivity.this, Locale.getDefault());
                //  List<Address> addresses;
                //  addresses = gcd.getFromLocation(Double.parseDouble(latitude),Double.parseDouble(longitude), 1);
                //  contact.put("locality",addresses.get(0).getLocality());
                contact.put(TAG_USER_ID, user_id);
                contact.put(TAG_CONTENTS, content);

                contact.put("Date", date);
                contact.put("Time", time);
                contact.put(TAG_NAME, name);


// adding contact to contact list
                contactList.add(contact);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ListView lview = (ListView) findViewById(R.id.lview1);

        ListAdapter adapter = new SimpleAdapter(
                QyeryPostActivity.this, contactList,
                R.layout.listitem, new String[]{TAG_NAME, "Date", "Time", TAG_CONTENTS}, new int[]{R.id.tname,
                R.id.tdate, R.id.ttime, R.id.content});

        lview.setAdapter(adapter);


    }


}
