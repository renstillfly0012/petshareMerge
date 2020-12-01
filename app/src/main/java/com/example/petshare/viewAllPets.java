package com.example.petshare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class viewAllPets extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String id, name, role_id, image, status, imgUrl;
    TextView txtUser, txtRole, txtPetCode;
    ImageView imgUserImg, cardviewImg;
    private Intent intent;
    private SharedPreferences sharedPreferences;
    private ProgressDialog dialog;
    private PetResponse[] petResponses;
    private ArrayList<viewallPet> petArrayList = new ArrayList<viewallPet>();
    private RecyclerView recyclerView;
    private viewallPetAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int day,month,year,hour,minute;
    private int day_x,month_x,year_x,hour_x,minute_x;
    private int currentPos;
    private Context ct;
    private int a;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_pets);

        drawerLayout = findViewById(R.id.viewall_drawer_layout);
        navigationView = findViewById(R.id.viewall_nav_view);
        toolbar = findViewById(R.id.viewall_toolbar);

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

        navigationView.setCheckedItem(R.id.nav_adopt);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        imgUrl = "https://pet-share.com/assets/images/pets/" + image;
        Glide.with(this).load(imgUrl).into(imgUserImg);
        txtUser.setText(name);
        setRole(getRole(role_id));


        cardviewImg = findViewById(R.id.cardviewImg);
        txtPetCode = findViewById(R.id.txtPetCode);

        Log.e("ViewAllPetSharedpref", ""+id+name+role_id);
        getAllPetDetails();



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

        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                intent = new Intent(this, dashboard_activity.class);
                startActivity(intent);
                break;
            case R.id.nav_adopt:
                intent = new Intent(this, viewAllPets.class);
                startActivity(intent);
                break;
            case R.id.nav_report:
                break;
            case R.id.nav_donate:
                break;
            case R.id.nav_view_profile:
                intent = new Intent(this, viewProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_edit:
                intent = new Intent(this, editProfileActivity.class);
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

    public void getAllPetDetails() {

        showDialog("Fetching Data from Server");
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        final Request request = new Request.Builder()
                .url("http://pet-share.com/api/user/viewallpets")
                .method("GET", null)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.e("Fail Fetch", e.getLocalizedMessage());
//                showToast(e.getLocalizedMessage());
                dialog.dismiss();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                dialog.dismiss();
                try {
                    final String myResponse = response.body().string();

                    petResponses = new Gson().fromJson(myResponse, PetResponse[].class);


                    Log.e("TEST ARRAY", "" + petResponses.length);



                    viewAllPets.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //inserting datas from json to our arraylist
                            petArrayList = new ArrayList<>();
                            for (int i = 0; i < petResponses.length; i++) {

                                petArrayList.add(new viewallPet(petResponses[i].getId(),
                                        petResponses[i].getName(),
                                        petResponses[i].getImage(),
                                        petResponses[i].getAge(),
                                        petResponses[i].getBreed(),
                                        petResponses[i].getStatus(),
                                        petResponses[i].getDescription(),
                                        Integer.parseInt(id))
                                {

                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int i, int i1) {

                                    }

                                    @Override
                                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                                    }
                                });
                            }
//                            petArrayList = new ArrayList<>();
//                            petArrayList.add(new viewallPet(petResponses[0].getId(),petResponses[0].getName(),petResponses[0].getImage(),petResponses[0].getAge(),petResponses[0].getBreed(),petResponses[0].getStatus(), petResponses[0].getDescription()));

                            recyclerView = findViewById(R.id.viewall_recycle);
                            recyclerView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(viewAllPets.this);
                            adapter = new viewallPetAdapter(petArrayList) {
                                //bindhereso I that I can use glide within this activity.
                                @Override
                                public void onBindViewHolder(@NonNull viewPetHolder holder, int position) {
                                    viewallPet currentItem = petArrayList.get(position);
                                    holder.txtPetCode.setText(currentItem.getId().toString());
                                    String imgUrl;
                                    imgUrl = currentItem.getImage();
                                    Glide.with(viewAllPets.this).load(imgUrl).into(holder.img);


                                }
                            };


                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);

                           adapter.setOnItemClickListener(new viewallPetAdapter.OnItemClickListener() {
                               @Override
                               public void onItemClick(int position) {


                               }

                               @Override
                               public void onViewClick(int position) {

                                   petArrayList.get(position).showToast(petArrayList.get(position).getId().toString(), viewAllPets.this);
                                   petArrayList.get(position).showPet(viewAllPets.this, petArrayList.get(position));

                               }
                           });



//                            txtPetCode.setText(petResponses[0].getName());
//                            imgUrl = petResponses[0].getImage();
//                            Glide.with(viewAllPets.this).load(imgUrl).into(cardviewImg);


                        }

                    });


//                    showToast("Total Pets"+petResponses);

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }
        });

//        Call<List<PetResponse>> petlist = Constant.getPetService().getAllPets();
//
//        petlist.enqueue(new Callback<List<PetResponse>>() {
//            @Override
//            public void onResponse(Call<List<PetResponse>> call, Response<List<PetResponse>> response) {
//
//                if(response.isSuccessful()){
//
//
//                    showToast( response.body().);
//                    dialog.dismiss();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<PetResponse>> call, Throwable t) {
//                Log.e("Success", t.getLocalizedMessage());
//                dialog.dismiss();
//            }
//        });


    }




    public void showDate(Context context){




    }

    public void setContextToTimeDialog(Context context){
        this.ct = context;
    }

    public void setCurrentPos(int pos){
        this.currentPos = pos;
    }

    public int getCurrentPos(int pos){
        a = pos;
        Log.e("A", ""+a);
        return a;
    }







    public void showToast(String message) {
        Toast.makeText(getBaseContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    public void showDialog(String message) {

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage(message);
        dialog.show();

    }

//    public void showToast(String message) {
//        Toast.makeText(getBaseContext(),
//                message,
//                Toast.LENGTH_SHORT).show();
//    }

    public String getRole(String role_id) {
        return role_id == "1" ? "Admin User" : "Foster User";
    }

    public void setRole(String role_id) {
        txtRole.setText(role_id);
    }




}