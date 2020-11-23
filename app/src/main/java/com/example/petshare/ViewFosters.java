package com.example.petshare;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewFosters extends AppCompatActivity {
    TextView usercount;

    private String TAG = "ViewFosters";
    private ListView lv;
    ArrayList<HashMap<String,String>> arrayList;
    ProgressDialog dialog;
    private HashMap<String, String> jsonUserData;
    FosterJsonParser jsonParser;
    Intent intent;
    SharedPreferences sharedPreferences;

    ImageView imgview;
    ImageButton imgbutton;

    AlertDialog.Builder alertDialog;
    AlertDialog Adialog;

    EditText txtname,txtemail,txtpassword,txtconfirm;
    Button popsubmit,cancel;
    private String RNAME, REMAIL, RPASSWORD, RCONFIRM;

    EditText AEname,AEpopemail,AEpoppassword,AEpopconfirm;
    TextView AEtxtname, AEtxtemail,AEtxtrole, Aetxtstatus;
    Button AEbtnSubmit;

    //Updatefoster
    String name,email,role,status,password,confirm;
    int id;
    int convertedId;
    private Constant constant;

    //DELETE FOSTER
    private ApiUser apiUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fosters);

        imgview = findViewById(R.id.foster_image);
        imgbutton = findViewById(R.id.add_btn);
        arrayList = new ArrayList<>();
        lv = findViewById(R.id.listview);

        final OkHttpClient client = new OkHttpClient();
        final String url = Url.fosterurl;

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    ViewFosters.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //JSONObject reader = new JSONObject(myResponse);
                                final JSONArray fosters = new JSONArray(myResponse);
                                for(int i = 0;i < fosters.length() ;i++){
                                    JSONObject read = fosters.getJSONObject(i);
                                    final String NAME = read.getString("name");
                                    final String STATUS = read.getString("status");
                                    String IMG = read.getString("image");
                                    final String ROLE = read.getString("role_id");

                                    Log.e(TAG,"sadviewfoster: " + NAME);

                                    final HashMap<String,String> data = new HashMap<>();
                                    data.put("name",NAME);
                                    data.put("status",STATUS);
                                    data.put("image",IMG);
                                    data.put("role_id",ROLE);

                                    arrayList.add(data);
                                    //Glide.with(ViewFosters.this).load("https://pet-share.com/assets/images/" + IMG).into(imgview);
                                    final ListAdapter adapter = new SimpleAdapter(ViewFosters.this,
                                            arrayList,R.layout.single_foster_data,new String[]{"name","status","image"},
                                            new int[]{R.id.foster_name,R.id.foster_status,R.id.foster_image});
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            final int index = i;
                                            alertDialog = new AlertDialog.Builder(ViewFosters.this);
                                            view = getLayoutInflater().inflate(R.layout.activity_admin_edit_fosters,null);

                                            AEbtnSubmit = view.findViewById(R.id.popup_admin_edit_foster_btnSubmit);
                                            AEname = view.findViewById(R.id.popup_admin_edit_foster_fname);
                                            AEpopemail = view.findViewById(R.id.popup_admin_edit_foster_email);
                                            AEpoppassword = view.findViewById(R.id.popup_admin_edit_foster_password);
                                            AEpopconfirm = view.findViewById(R.id.popup_admin_edit_foster_confirm);
                                            AEtxtname = view.findViewById(R.id.popup_admin_edit_foster_textname);
                                            AEtxtemail = view.findViewById(R.id.popup_admin_edit_foster_textemail);
                                            AEtxtrole = view.findViewById(R.id.popup_admin_edit_foster_textrole);
                                            Aetxtstatus = view.findViewById(R.id.popup_admin_edit_foster_textstatus);

                                            AEname.setText(arrayList.get(index).put("name",NAME));
                                            AEtxtrole.setText(arrayList.get(index).put("role_id",ROLE));
//                                            if(arrayList.get(index).put("role_id",ROLE).equals(1)){
//                                                AEtxtrole.setText("Admin");
//                                            }else if(arrayList.get(index).put("role_id",ROLE).equals(2)){
//                                                AEtxtrole.setText("Foster");
//                                            }
                                            Aetxtstatus.setText(arrayList.get(index).put("status",STATUS));

                                            AEbtnSubmit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = index + 1;
//                                                    Gson gson = new GsonBuilder()
//                                                            .setLenient()
//                                                            .create();
                                                    responseService responseService = new responseService(
                                                            id = pos,
                                                            name = AEname.getText().toString(),
                                                            role = AEtxtrole.getText().toString(),
                                                            status = Aetxtstatus.getText().toString());
//                                                    name = AEname.getText().toString()
//                                                    status = Aetxtstatus.getText().toString();
//                                                    role = AEtxtrole.getText().toString();
//                                                    id = pos;

//                                                        HashMap<String,String> maps = new HashMap<>();
////                                                        String map = gson.toJson(maps);
//                                                        maps.put("name",name);
////                                                        maps.put("status",status);
////                                                        maps.put("role_id", role);
//                                                        GsonBuilder gsonBuilder = new GsonBuilder();
//                                                        Gson gsonobject = gsonBuilder.create();
//                                                        gsonobject.toJson(maps);

                                                        Toast.makeText(getBaseContext(),"sad:" ,Toast.LENGTH_SHORT).show();
                                                        Log.e(TAG,"testing: " +name);
                                                        update_foster(id,name/*,gson.toJson(maps.get("status")),gson.toJson(maps.get("role_id"))*/);
                                                }
                                            });

                                            alertDialog.setView(view);
                                            Adialog = alertDialog.create();
                                            Adialog.show();
                                        }
                                    });
                                    lv.setAdapter(adapter);
                                    String count = String.valueOf(arrayList.size());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog = new AlertDialog.Builder(ViewFosters.this);
                view = getLayoutInflater().inflate(R.layout.admin_add_popup,null);

                txtname = view.findViewById(R.id.pop_fname);
                txtemail = view.findViewById(R.id.pop_email);
                txtpassword = view.findViewById(R.id.pop_password);
                txtconfirm = view.findViewById(R.id.pop_confirm);

                RNAME = txtname.getText().toString();
                REMAIL = txtemail.getText().toString();
                RPASSWORD = txtpassword.getText().toString();
                RCONFIRM = txtconfirm.getText().toString();

                popsubmit = view.findViewById(R.id.btnSubmitPop);

                popsubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("Test Data from fields", RPASSWORD +"\n"+ RCONFIRM);

                        submitR(newUser(RNAME, REMAIL, RPASSWORD, RCONFIRM));
                    }
                });

                alertDialog.setView(view);
                Adialog = alertDialog.create();
                Adialog.show();
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                popupMenu.inflate(R.menu.foster_long_click);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if (id == R.id.delete_user) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(ViewFosters.this);
                            builder.setTitle("Delete");
                            builder.setMessage("Are you sure you want to delete this motherfucker?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int pos = index + 1;
                                    Gson gson = new GsonBuilder().serializeNulls().setLenient().create();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("https://pet-share.com")
                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                            .build();
                                    apiUser = retrofit.create(ApiUser.class);
                                    delete_foster(pos);
                                    builder.setMessage("Successfully Deleted!");
                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getBaseContext(), "Canceled!" + index, Toast.LENGTH_SHORT).show();
                                }
                            });
                            AlertDialog ad = builder.create();
                            ad.show();
                        }
                        return false;
                    }
                });
                return false;
            }
        });

    }

    public void setImage(String url){
       Glide.with(this).load(url).into(imgview);
    }

    public static  RegisterRequest newUser(String name, String email, String password, String confirm){
        RegisterRequest data = new RegisterRequest();
        data.setfName(name);
        data.setEmail(email);
        data.setPassword(password);
        data.setConfirm(confirm);

        return data;
    }

    public void submitR(RegisterRequest registerRequest) {
        RNAME = txtname.getText().toString();
        REMAIL = txtemail.getText().toString();
        RPASSWORD = txtpassword.getText().toString();
        RCONFIRM = txtconfirm.getText().toString();




//    showDialog(registerRequest.toString());

        showToast(RNAME +"\n"+ REMAIL +"\n"+ RPASSWORD +"\n"+ RCONFIRM);
        Log.i("Form Datas", ""+registerRequest.toString());
        retrofit2.Call<RegisterResponse> registerResponseCall = Constant.getService().registerUser(registerRequest);
        try {
            registerResponseCall.enqueue(new retrofit2.Callback<RegisterResponse>() {
                @Override
                public void onResponse(retrofit2.Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {

                    showToast("GSON" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {

                        showDialog("Loading..");
                        showToast("Successful");

                    } else {
                        try {
                            showToast("An error ocured\nPlease try again later." + response.errorBody().string());
                            Log.e("Error Message", ""+response.errorBody().string());

                        } catch (IOException e) {
                            e.printStackTrace();
                            showToast(e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<RegisterResponse> call, Throwable t) {
                    String message = t.getLocalizedMessage();
                    Log.i("ERROR MESSAGE:", message);
                    Toast.makeText(ViewFosters.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showDialog(String message){
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.show();

    }

    public void showToast(String message){
        Toast.makeText(getBaseContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    public void delete_foster(int id){

        retrofit2.Call<Void> call = apiUser.deleteUser(id);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if(response.isSuccessful()){
                    Log.e(TAG,"Success: " + response.message());
                }
                else {
                    Log.e(TAG,"onResponse: " + response.raw());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                Log.e(TAG,"onFailure: " + t.getMessage());
            }
        });
    }

    public void update_foster(final int id, String name/*String status,String role, String email, String password,String confirm*/){
        Gson gson = new GsonBuilder()
                .setLenient()
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pet-share.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiUser apiUser = retrofit.create(ApiUser.class);
        retrofit2.Call<responseService> call = apiUser.updateUser(id,name);
        call.enqueue(new retrofit2.Callback<responseService>() {
            @Override
            public void onResponse(retrofit2.Call<responseService> call, retrofit2.Response<responseService> response) {
                if(response.isSuccessful()){
                    Log.e(TAG,"success: " + response);
                }else {
                    Log.e(TAG,"else: " + response.message());
                }

                int id = response.body().getId();
                String name = response.body().getName();
                Log.e(TAG,"onResp: " + id + " " + name);


            }

            @Override
            public void onFailure(retrofit2.Call<responseService> call, Throwable t) {
                String mess = t.getMessage();
                Log.e(TAG,"fail: " + mess);
            }
        });
//        ApiUser apiUser = Constant.getRetroFit().create(ApiUser.class);
//        //services services = Constant.getRetroFit().create(com.example.petshare.services.class);
//        retrofit2.Call<responseService> call = apiUser.updateUser(id,name/*gson.toJson(role),gson.toJson(status)email,password,confirm*/);
//
//        call.enqueue(new retrofit2.Callback<responseService>() {
//            @Override
//            public void onResponse(retrofit2.Call<responseService> call, retrofit2.Response<responseService> response) {
//                if(response.isSuccessful() && response.body() != null){
//                    Log.e(TAG,"success: " + response);
//                }else {
//                    Log.e(TAG,"else: " + response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(retrofit2.Call<responseService> call, Throwable t) {
//                String mess = t.getMessage();
//                Log.e(TAG,"fail: " + mess);
//            }
//        });
    }

}