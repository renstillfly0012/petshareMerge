package com.example.petshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewPetHealth extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    ArrayList<HashMap<String,String>> arrayList;
    ListView lv;

    String id,role_id,imgUrl,name,image,status,imgPetUrl;
    SharedPreferences sharedPreferences;
    ImageView imgUserImg,imgPet;
    TextView txtUser, txtRole;
    Intent intent;

    AlertDialog.Builder alertbuilder;
    AlertDialog dialog;

    //
    TextView petId,ownerId,petAllergies,petConditions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pet_health);

        drawerLayout = findViewById(R.id.view_health_drawer_layout);
        navigationView = findViewById(R.id.view_health_nav_view);
        toolbar = findViewById(R.id.view_health_toolbar);
        lv = findViewById(R.id.listviewhealth);
        arrayList = new ArrayList<>();
        petId = findViewById(R.id.pet_health_id_here);
        ownerId = findViewById(R.id.pet_health_owner_id);
        petAllergies = findViewById(R.id.pet_health_allergies);
        petConditions = findViewById(R.id.pet_health_condition);

        sharedPreferences = getSharedPreferences("KEY_USER_INFO", MODE_PRIVATE);
        id = sharedPreferences.getString("KEY_ID", null);
        name = sharedPreferences.getString("KEY_NAME", null);
        role_id = sharedPreferences.getString("KEY_ROLE_ID", null);
        image = sharedPreferences.getString("KEY_IMAGE", null);
        status = sharedPreferences.getString("KEY_STATUS", null);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_view_pethealth);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        imgUrl = "https://pet-share.com/assets/images/" + image;
        Glide.with(this).load(imgUrl).into(imgUserImg);
        txtUser.setText(name);
        setRole(getRole(role_id));

        View vivi = getLayoutInflater().inflate(R.layout.single_pet_health,null);
        imgPetUrl = "https://pet-share.com/assets/images/pets/";

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Pet Health Records....");
        progressDialog.show();


        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Url.pethealth)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                progressDialog.dismiss();
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    ViewPetHealth.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject read = new JSONObject(myResponse);
                                JSONArray jsonArray = read.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++){
                                    JSONObject readhealth = jsonArray.getJSONObject(i);
                                    String OWNERID = readhealth.getString("pet_owner_id");
                                    String PETID = readhealth.getString("pet_id");
                                    String ALLER = readhealth.getString("pet_allergies");
                                    String COND = readhealth.getString("pet_existing_conditions");

                                    HashMap<String,String> data = new HashMap<>();
                                    data.put("pet_owner_id",OWNERID);
                                    data.put("pet_id",PETID);
                                    data.put("pet_allergies",ALLER);
                                    data.put("pet_existing_conditions",COND);
                                    arrayList.add(data);

                                    ListAdapter listAdapter = new SimpleAdapter(ViewPetHealth.this,arrayList,R.layout.single_pet_health,
                                            new String[]{"pet_owner_id","pet_id","pet_allergies","pet_existing_conditions"}, new int[]{R.id.pet_health_owner_id,R.id.pet_health_id_here,
                                    R.id.pet_health_allergies,R.id.pet_health_condition});

                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            alertbuilder = new AlertDialog.Builder(ViewPetHealth.this);
                                            view = getLayoutInflater().inflate(R.layout.activity_admin_edit_pethealth,null);

                                            alertbuilder.setView(view);
                                            dialog = alertbuilder.create();
                                            dialog.show();
                                        }
                                    });
                                    lv.setAdapter(listAdapter);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

    }

    public void loadImage(){
        String url ="https://pet-share.com/assets/images/pets/";
    }

    private Toolbar setActionBar(Toolbar toolbar) {
        return toolbar;
    }

    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
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
                Toast.makeText(ViewPetHealth.this,"profile to: " + EMAIL,Toast.LENGTH_SHORT).show();
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
}