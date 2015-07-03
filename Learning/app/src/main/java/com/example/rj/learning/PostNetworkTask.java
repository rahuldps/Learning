package com.example.rj.learning;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Rj on 7/1/15.
 */
public class PostNetworkTask extends AsyncTask<Void, Void, JSONObject> {

    PostQuestion ins;
    String url;
    private JSONObject obj;


    PostNetworkTask(PostQuestion ins, String Url, JSONObject obj) {
        this.ins = ins;
        this.url = Url;
        this.obj = obj;
    }

    protected JSONObject doInBackground(Void... params) {
        Log.w("timeeeeeee", "functioncalled");

        HttpParams myParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(myParams, 10000);
        HttpConnectionParams.setSoTimeout(myParams, 10000);
        HttpClient httpclient = new DefaultHttpClient(myParams);
        String json = obj.toString();

        try {

            HttpPost httppost = new HttpPost(url.toString());
            httppost.setHeader("Content-type", "application/json");

            StringEntity se = new StringEntity(json);
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);
            String temp = EntityUtils.toString(response.getEntity());
            try {


                JSONObject jsobj;
                jsobj = new JSONObject(temp);
                return jsobj;
            } catch (Exception e) {
            }


            Log.w("see check", temp);


        } catch (ClientProtocolException e) {

        } catch (IOException e) {
        }
        return null;
    }


    @Override
    protected void onPostExecute(JSONObject o) {
        super.onPostExecute(o);
        try {
            Log.w("Welcome India", "This is an Unreal World" + o);

            String status = o.getString("status");
            if (status.equals("1"))
                Toast.makeText(ins, "Query Posted", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ins, "Query Not Posted", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
