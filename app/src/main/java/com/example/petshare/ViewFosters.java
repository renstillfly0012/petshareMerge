package com.example.petshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewFosters extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String id,name,role_id,image,status,imgUrl;
    TextView txtUser,txtRole;
    ImageView imgUserImg;
    private Intent intent;
    private SharedPreferences sharedPreferences;

    private String TAG = "ViewFosters";
    private ListView lv;
    ArrayList<HashMap<String,String>> arrayList;
    ProgressDialog dialog;
    private HashMap<String, String> jsonUserData;
    FosterJsonParser jsonParser;

    ImageView imgview,fosterProfilepic;
    ImageButton imgbutton;

    AlertDialog.Builder alertDialog;
    AlertDialog Adialog;

    EditText txtname,txtemail,txtpassword,txtconfirm;
    Button popsubmit,cancel;
    private String RNAME, REMAIL, RPASSWORD, RCONFIRM;

    ImageView ImageFoster;
    EditText AEname,AEpopemail,AEpoppassword,AEpopconfirm;
    TextView AEtxtname, AEtxtemail,AEtxtrole, Aetxtstatus;
    Button AEbtnSubmit;

    //Global String
    String NAME,ROLE,STATUS,ID,IMG;

    //Updatefoster
    String updateName,updateEmail, updateRole, updateStatus,password,confirm;
    int updateID;
    int convertedId;
    private Constant constant;

    //DELETE FOSTER
    private ApiUser apiUser;
    private responseService responseService;

    //Swipe refresh
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_fosters);

        drawerLayout = findViewById(R.id.view_foster_drawer_layout);
        navigationView = findViewById(R.id.view_foster_nav_view);
        toolbar = findViewById(R.id.view_foster_toolbar);
//        imgview = findViewById(R.id.foster_image);
        imgbutton = findViewById(R.id.add_btn);
        arrayList = new ArrayList<>();
        refreshLayout = findViewById(R.id.refresh_foster);
        lv = findViewById(R.id.listview);

        Intent intent = getIntent();
//        id = intent.getStringExtra("id");
//        name = intent.getStringExtra("name");
//        role_id = intent.getStringExtra("role_id");
//
//        image = intent.getStringExtra("image");
//        status = intent.getStringExtra("status");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Fosters....");
        progressDialog.show();

        sharedPreferences = getSharedPreferences("KEY_USER_INFO", MODE_PRIVATE);
        id = sharedPreferences.getString("KEY_ID", null);
        name = sharedPreferences.getString("KEY_NAME", null);
        role_id = sharedPreferences.getString("KEY_ROLE_ID", null);
        image = sharedPreferences.getString("KEY_IMAGE", null);
        updateStatus = sharedPreferences.getString("KEY_STATUS", null);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_view_fosters);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        imgUrl = "https://pet-share.com/assets/images/"+image;
        Glide.with(this).load(imgUrl).into(imgUserImg);
        txtUser.setText(name);
        setRole(getRole(role_id));

        final OkHttpClient client = new OkHttpClient();
        final String url = Url.fosterurl;

        final Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    final String myResponse = response.body().string();
                    ViewFosters.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                //JSONObject reader = new JSONObject(myResponse);
                                final JSONArray fosters = new JSONArray(myResponse);
                                for(int i = 0;i < fosters.length() ;i++){
                                    JSONObject read = fosters.getJSONObject(i);
                                     ID = read.getString("id");
                                     NAME = read.getString("name");
                                     STATUS = read.getString("status");
                                     IMG = read.getString("image");
                                     ROLE = read.getString("role_id");

                                    Log.e(TAG,"sadviewfoster: " + NAME);

                                    final HashMap<String,String> data = new HashMap<>();
                                    data.put("id",ID);
                                    data.put("name",NAME);
                                    data.put("status",STATUS);
                                    data.put("image",IMG);
                                    data.put("role_id",ROLE);

                                    arrayList.add(data);
                                    //Glide.with(ViewFosters.this).load("https://pet-share.com/assets/images/" + IMG).into(imgview);
                                    final ListAdapter adapter = new SimpleAdapter(ViewFosters.this,
                                            arrayList,R.layout.single_foster_data,new String[]{"name","status","image"},
                                            new int[]{R.id.foster_name,R.id.foster_status/*,R.id.foster_image*/});
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            final int index = i;
                                            alertDialog = new AlertDialog.Builder(ViewFosters.this);
                                            view = getLayoutInflater().inflate(R.layout.activity_admin_edit_fosters,null);

                                            ImageFoster = view.findViewById(R.id.popup_admin_edit_foster_image);
                                            AEbtnSubmit = view.findViewById(R.id.popup_admin_edit_foster_btnSubmit);
                                            AEname = view.findViewById(R.id.popup_admin_edit_foster_fname);
                                            AEpopemail = view.findViewById(R.id.popup_admin_edit_foster_email);
                                            AEpoppassword = view.findViewById(R.id.popup_admin_edit_foster_password);
                                            AEpopconfirm = view.findViewById(R.id.popup_admin_edit_foster_confirm);
                                            AEtxtname = view.findViewById(R.id.popup_admin_edit_foster_textname);
                                            AEtxtemail = view.findViewById(R.id.popup_admin_edit_foster_textemail);
                                            AEtxtrole = view.findViewById(R.id.popup_admin_edit_foster_textrole);
                                            Aetxtstatus = view.findViewById(R.id.popup_admin_edit_foster_textstatus);

                                            String fosterurlimg = "https://pet-share.com/assets/images/users/" + arrayList.get(index).put("image",IMG);
                                            Glide.with(ViewFosters.this).load(fosterurlimg).into(ImageFoster);

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
                                                            updateID = responseService.getId();
//                                                    updateName = AEname.getText().toString();
//                                                    updateStatus = Aetxtstatus.getText().toString();
//                                                    updateRole = AEtxtrole.getText().toString();
////                                                    id = pos;

                                                    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                                                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                                                    OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
                                                    Gson gson = new GsonBuilder()
                                                            .setLenient()
                                                            .serializeNulls()
                                                            .create();

                                                    Retrofit retrofit = new Retrofit.Builder()
                                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                                            .baseUrl("https://pet-share.com")
                                                            .client(okHttpClient)
                                                            .build();
                                                    apiUser = retrofit.create(ApiUser.class);
                                                        update_foster(updateID/*,gson.toJson(maps.get("status")),gson.toJson(maps.get("role_id"))*/);
                                                }
                                            });

                                            alertDialog.setView(view);
                                            Adialog = alertDialog.create();
                                            Adialog.show();
                                        }
                                    });
                                    lv.setAdapter(adapter);
                                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            final ListAdapter adapter = new SimpleAdapter(ViewFosters.this,
                                                    arrayList,R.layout.single_foster_data,new String[]{"name","status","image"},
                                                    new int[]{R.id.foster_name,R.id.foster_status/*,R.id.foster_image*/});
                                            lv.setAdapter(adapter);
                                            refreshLayout.setRefreshing(false);
                                        }
                                    });
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
                fosterProfilepic = view.findViewById(R.id.foster_profile_image);

                RNAME = txtname.getText().toString();
                REMAIL = txtemail.getText().toString();
                RPASSWORD = txtpassword.getText().toString();
                RCONFIRM = txtconfirm.getText().toString();

                popsubmit = view.findViewById(R.id.btnSubmitPop);

                fosterProfilepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openImageChooser();
                    }
                });

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
                            builder.setMessage("Are you sure you want to delete this foster?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    int pos = Integer.parseInt(arrayList.get(index).put("id",ID));
                                    Gson gson = new GsonBuilder().serializeNulls().create();

                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl("https://pet-share.com")
                                            .addConverterFactory(GsonConverterFactory.create(gson))
                                            .build();
                                    apiUser = retrofit.create(ApiUser.class);
                                    Log.e(TAG,"id: " + pos);
                                    delete_foster(pos);
                                    progressDialog.dismiss();
                                    refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                        @Override
                                        public void onRefresh() {
                                            refreshLayout.setRefreshing(false);
                                        }
                                    });

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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Foster...");
        progressDialog.show();

        RNAME = txtname.getText().toString();
        REMAIL = txtemail.getText().toString();
        RPASSWORD = txtpassword.getText().toString();
        RCONFIRM = txtconfirm.getText().toString();


        Log.i("Form Datas", ""+registerRequest.toString());
        retrofit2.Call<RegisterResponse> registerResponseCall = Constant.getService().registerUser(registerRequest);
        try {
            registerResponseCall.enqueue(new retrofit2.Callback<RegisterResponse>() {
                @Override
                public void onResponse(retrofit2.Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {

                    showToast("GSON" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        progressDialog.dismiss();
                        showToast("Successful");

                    } else {
                        progressDialog.dismiss();
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
                    progressDialog.dismiss();
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
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.show();

        retrofit2.Call<Void> call = apiUser.deleteUser(id);
        call.enqueue(new retrofit2.Callback<Void>() {
            @Override
            public void onResponse(retrofit2.Call<Void> call, retrofit2.Response<Void> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Log.e(TAG,"Success: " + response.raw());
                }
                else {
                    progressDialog.dismiss();
                    Log.e(TAG,"else: " + response.raw());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Void> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG,"onFailure: " + t.getMessage());
            }
        });
    }

    public void update_foster(final int id/*String status,String role, String email, String password,String confirm*/){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        updateName = AEname.getText().toString();
        updateStatus = Aetxtstatus.getText().toString();
        updateRole = AEtxtrole.getText().toString();

        final HashMap<String,String> fields = new HashMap<>();
        fields.put("name",updateName);
//        responseService responseService = new responseService(id, updateName, updateStatus, updateRole);

//        apiUser  = Constant.getRetroFit().create(ApiUser.class);
        retrofit2.Call<RegisterRequest> call = apiUser.updateUser(id,fields);
        call.enqueue(new retrofit2.Callback<RegisterRequest>() {
            @Override
            public void onResponse(@NotNull retrofit2.Call<RegisterRequest> call, @NotNull retrofit2.Response<RegisterRequest> response) {
                if(response.isSuccessful()){
                    progressDialog.dismiss();
                    Log.e(TAG,"success: " + fields);
                }else {
                    progressDialog.dismiss();
                    Log.e(TAG,"else: " + fields);
                }

            }

            @Override
            public void onFailure(@NotNull retrofit2.Call<RegisterRequest> call, @NotNull Throwable t) {
                String mess = t.getMessage();
                Log.e(TAG,"fail: " + mess + fields);
                progressDialog.dismiss();
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

    private Toolbar setActionBar(Toolbar toolbar) {
        return toolbar;
    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch(menuItem.getItemId()){
            case R.id.nav_home:
                intent = new Intent(this, dashboard_activity.class);
                startActivity(intent);
                break;
            case R.id.nav_donation:
                intent = new Intent(this,ViewDonations.class);
                startActivity(intent);
                break;
            case R.id.nav_view_pet:
                intent = new Intent(this,ViewPets.class);
                startActivity(intent);
                break;
            case R.id.nav_view_pethealth:
                intent = new Intent(this,ViewPetHealth.class);
                startActivity(intent);
                break;
            case R.id.nav_view_fosters:
                intent = new Intent(this, ViewFosters.class);
                startActivity(intent);
                break;
            case R.id.nav_edit:
                intent = getIntent();
                String ID = intent.getStringExtra("KEY_ID_INT");
                String NAME = intent.getStringExtra("KEY_NAME_INT");
                String ROLE_ID = intent.getStringExtra("KEY_ROLE_ID");

                String IMAGE = intent.getStringExtra("KEY_IMAGE_INT");
                String STATUS = intent.getStringExtra("KEY_STATUS_INT");
                String EMAIL = intent.getStringExtra("KEY_EMAIL_INT");
                intent = new Intent(this, editProfileActivity.class);
                Toast.makeText(ViewFosters.this,"profile to: " + EMAIL,Toast.LENGTH_SHORT).show();
                intent.putExtra("KEY_NAME_INT",NAME);
                intent.putExtra("KEY_IMAGE_INT",IMAGE);
                intent.putExtra("KEY_EMAIL_INT",EMAIL);
                intent.putExtra("KEY_NAME_INT",NAME);
                startActivity(intent);
                break;
            case R.id.nav_report:
                intent = new Intent(getBaseContext(),ViewReports.class);
                startActivity(intent);
                break;
            case R.id.nav_admin_qr_scanner:
                intent = new Intent(getBaseContext(),AdminQRCodeScanner.class);
                startActivity(intent);
                break;
            case R.id.nav_logout:
                intent = new Intent(this, MainActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();
                Toast.makeText(getBaseContext(), "Log out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();


        }

        drawerLayout.closeDrawer((GravityCompat.START));

        return true;
    }

    public String getRole(String role_id){
        return role_id == "1" ? "Admin User": "Foster User";
    }

    public void setRole(String role_id){
        txtRole.setText(role_id);
    }

    private void openImageChooser(){
        intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 21);
    }
}