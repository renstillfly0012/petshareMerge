package com.example.petshare;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FosterJsonParser extends AsyncTask<String,String,Void> {

    private static final String TAG = "FosterJsonParser";
    public List<Fosterlist> fostersList = new ArrayList<Fosterlist>();
    BufferedInputStream inputStream;
    JSONArray jsonArray;
    String result="";
    public ProgressDialog dialog;
    Activity activity;
    Context context;

    public FosterJsonParser(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
        this.dialog = new ProgressDialog(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.dismiss();
        dialog.setMessage("Loading...");
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                FosterJsonParser.this.cancel(true);
                //dialog.setMessage("Canceled");
            }
        });
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpURLConnection httpURLConnection;
        try {
            URL url = new URL(Url.fosterurl);
            httpURLConnection= (HttpURLConnection) url.openConnection();
            inputStream=new BufferedInputStream(httpURLConnection.getInputStream());
            result=readStream();
            result = result.substring(result.indexOf(""), result.lastIndexOf(""));
            Log.e(TAG,"sad: "+ result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readStream() throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder=new StringBuilder();
        String line;
        try{
            while((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try{
            //Fosterlist msg = new Gson().fromJson(result, Fosterlist.class);
            //Log.e(TAG,"sad: "+ msg);
            JSONObject jsonObject = new JSONObject();
            jsonArray = jsonObject.getJSONArray(result);
            if(jsonArray!=null) {
                for (int index = 0; index < jsonArray.length(); index++) {
                    Fosterlist fosterlist = new Fosterlist();
                    fosterlist.Id = jsonArray.getJSONObject(index).getString("id");
                    fosterlist.Name = jsonArray.getJSONObject(index).getString("name");
                    //fosterlist.Email = jsonArray.getJSONObject(index).getString("email");
                    fosterlist.Status = jsonArray.getJSONObject(index).getString("status");
                    fosterlist.Img_Url = Url.fosterurl + fosterlist.Id;
                    fostersList.add(fosterlist);
                }
            }
            ListView listView;
            listView=(ListView) this.activity.findViewById(R.id.listview);
            CustomAdapter adapter = new CustomAdapter(this.context,fostersList);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onPostExecute(aVoid);
    }
}
