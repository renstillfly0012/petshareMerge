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
import retrofit2.Retrofit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewPets extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String id,name,role_id,image,status,imgUrl;
    TextView txtUser,txtRole;
    ImageView imgUserImg;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private ListView lv;

    AlertDialog.Builder alertdialog;
    AlertDialog Adialog;

    EditText Pname,Page,Pbreed,Pdesc;
    Button PbtnSubmit, PbtnCancel;
    ImageView PprofileImage;
    TextView Pstatus,Phealth;

    private static final String TAG = "ViewPets" ;

    ImageButton addPet;
    ImageView petImage;
    ArrayList<HashMap<String,String>> arrayList;
    private HashMap<String,String> jsonUserData;
    String SPNAME,SPAGE,SPBREED,SPIMAGE,SPDESC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pets);
        drawerLayout = findViewById(R.id.view_pet_drawer_layout);
        navigationView = findViewById(R.id.view_pet_nav_view);
        toolbar = findViewById(R.id.view_pet_toolbar);
        lv = findViewById(R.id.listviewpets);
        arrayList = new ArrayList<>();

//        Intent intent = getIntent();
//        id = intent.getStringExtra("id");
//        name = intent.getStringExtra("name");
//        role_id = intent.getStringExtra("role_id");
//
//        image = intent.getStringExtra("image");
//        status = intent.getStringExtra("status");

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

        navigationView.setCheckedItem(R.id.nav_view_pet);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        imgUrl = "https://pet-share.com/assets/images/"+image;
        Glide.with(this).load(imgUrl).into(imgUserImg);
        txtUser.setText(name);
        setRole(getRole(role_id));

        //GET JSON OKHTTP
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url.peturl)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    Log.d(TAG,"get: " + myResponse);
                    ViewPets.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final JSONArray jsonArray = new JSONArray(myResponse);
                                for(int i = 0;i < jsonArray.length(); i++){
                                    final JSONObject readPet = jsonArray.getJSONObject(i);
                                    final String NAME = readPet.getString("name");
                                    String IMAGE = readPet.getString("image");
                                    final String AGE = readPet.getString("age");
                                    final String BREED = readPet.getString("breed");
                                    final String STATUS = readPet.getString("status");
                                    final String DESC = readPet.getString("description");

                                    //Log.e(TAG,"pet: " + NAME);

                                    final HashMap<String,String> data = new HashMap<>();
                                    data.put("name",NAME);
                                    data.put("image",IMAGE);
                                    data.put("age",AGE);
                                    data.put("breed",BREED);
                                    data.put("status",STATUS);
                                    data.put("description",DESC);

                                    arrayList.add(data);
                                    final ListAdapter listAdapter = new SimpleAdapter(ViewPets.this,arrayList,R.layout.single_pet_data,
                                            new String[]{"image","name","breed","age"},new int[]{R.id.pet_image, R.id.pet_name, R.id.pet_breed, R.id.pet_age});
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            final int index = i;

                                            alertdialog = new AlertDialog.Builder(ViewPets.this);
                                            view = getLayoutInflater().inflate(R.layout.admin_edit_pets,null);
                                            Pname = view.findViewById(R.id.popup_admin_edit_pet_name);
                                            Page = view.findViewById(R.id.popup_admin_edit_pet_age);
                                            Pbreed = view.findViewById(R.id.popup_admin_edit_pet_breed);
                                            Pdesc = view.findViewById(R.id.popup_admin_edit_pet_desc);
                                            Pstatus = view.findViewById(R.id.popup_admin_pet_status);
                                            Phealth = view.findViewById(R.id.popup_admin_edit_pet_qr);
                                            PbtnSubmit = view.findViewById(R.id.popup_admin_edit_pet_btnSubmit);

                                            Pname.setText(arrayList.get(index).put("name",NAME));
                                            Page.setText(arrayList.get(index).put("age",AGE));
                                            Pbreed.setText(arrayList.get(index).put("breed",BREED));
                                            Pdesc.setText(arrayList.get(index).put("description",DESC));
                                            Pstatus.setText(arrayList.get(index).put("status",STATUS));

                                            PbtnSubmit.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    int pos = index;
                                                    String ENAME = Pname.getText().toString();
                                                    String EAGE = Pname.getText().toString();
                                                    String EBREED = Pname.getText().toString();
                                                    String EDESC = Pname.getText().toString();
                                                    String ESTATUS = Pname.getText().toString();

                                                    if(ENAME.isEmpty()){
                                                        Pname.setError("Field cannot be empty");
                                                        Pname.requestFocus();
                                                    }else if(EAGE.isEmpty()){
                                                        Page.setError("Field cannot be empty");
                                                        Page.requestFocus();
                                                    }else if(EBREED.isEmpty()){
                                                        Pbreed.setError("Field cannot be empty");
                                                        Pbreed.requestFocus();
                                                    }else if(EDESC.isEmpty()){
                                                        Pdesc.setError("Field cannot be empty");
                                                        Pdesc.requestFocus();
                                                    }

                                                }
                                            });

                                            alertdialog.setView(view);
                                            Adialog = alertdialog.create();
                                            Adialog.show();
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
                Toast.makeText(ViewPets.this,"profile to: " + EMAIL,Toast.LENGTH_SHORT).show();
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