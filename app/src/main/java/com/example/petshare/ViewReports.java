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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewReports extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    String id, name, role_id, image, status, imgUrl;
    SharedPreferences sharedPreferences;
    TextView txtUser, txtRole;
    ImageView imgUserImg;
    Intent intent;

    ArrayList<HashMap<String,String>> arrayList;
    ListView lv;

    ImageView reportImage;
    TextView reportDesc;
    AlertDialog.Builder alertbuilder;
    AlertDialog adialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        drawerLayout = findViewById(R.id.view_report_drawer_layout);
        navigationView = findViewById(R.id.view_report_nav_view);
        toolbar = findViewById(R.id.view_report_toolbar);
        lv = findViewById(R.id.listviewreports);
        arrayList = new ArrayList<>();

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

        navigationView.setCheckedItem(R.id.nav_report);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        imgUrl = "https://pet-share.com/assets/images/" + image;
        Glide.with(this).load(imgUrl).into(imgUserImg);
        txtUser.setText(name);
        setRole(getRole(role_id));

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Reports....");
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Url.reporturl)
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
                    ViewReports.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject reader = new JSONObject(myResponse);
                                JSONArray jsonArray = reader.getJSONArray("data");
                                for(int i = 0;i < jsonArray.length();i++ ){
                                    JSONObject readreport = jsonArray.getJSONObject(i);
                                    String ID = readreport.getString("id");
                                    String USERID = readreport.getString("user_id");
                                    final String IMAGE = readreport.getString("image");
                                    final String DESC = readreport.getString("description");
                                    String STATUS = readreport.getString("report_status");

                                    HashMap<String,String> hash = new HashMap<>();
                                    hash.put("id",ID);
                                    hash.put("user_id",USERID);
                                    hash.put("image",IMAGE);
                                    hash.put("description",DESC);
                                    hash.put("report_status",STATUS);

                                    arrayList.add(hash);
                                    final ListAdapter listAdapter = new SimpleAdapter(ViewReports.this,arrayList,R.layout.single_report_data,
                                            new String[]{"description","report_status"},new int[]{R.id.report_description,R.id.report_status});
                                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            final int index = i;
                                            alertbuilder = new AlertDialog.Builder(ViewReports.this);
                                            view = getLayoutInflater().inflate(R.layout.activity_admin_edit_reports,null);
                                            String urlreport = "https://pet-share.com/assets/images/reports/" + arrayList.get(index).put("image",IMAGE);
                                            reportImage = view.findViewById(R.id.report_image);
                                            reportDesc = view.findViewById(R.id.report_description);

                                            reportDesc.setText(DESC);
                                            Glide.with(ViewReports.this).load(urlreport).into(reportImage);

                                            alertbuilder.setView(view);
                                            adialog = alertbuilder.create();
                                            adialog.show();
                                        }
                                    });

                                    lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                        @Override
                                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
                                            popupMenu.inflate(R.menu.report_long_click);
                                            popupMenu.show();
                                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                @Override
                                                public boolean onMenuItemClick(MenuItem menuItem) {
                                                    int id = menuItem.getItemId();
                                                        if(id == R.id.report_approve) {
                                                            alertbuilder = new AlertDialog.Builder(ViewReports.this);
                                                            alertbuilder.setTitle("Approve");
                                                            alertbuilder.setMessage("Are you sure you want to approve this report?");
                                                            alertbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                }
                                                            });
                                                            alertbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            adialog = alertbuilder.create();
                                                            adialog.show();
                                                        }
                                                        else if(id == R.id.report_decline) {
                                                            alertbuilder = new AlertDialog.Builder(ViewReports.this);
                                                            alertbuilder.setTitle("Decline");
                                                            alertbuilder.setMessage("Are you sure you want to Decline this report?");
                                                            alertbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                                }
                                                            });
                                                            alertbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Toast.makeText(getBaseContext(), "Canceled", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                            adialog = alertbuilder.create();
                                                            adialog.show();
                                                        }
                                                    return false;
                                                }
                                            });
                                            return false;
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
                intent = new Intent(this, admin_dashboard_activity.class);
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
                Toast.makeText(ViewReports.this,"profile to: " + EMAIL,Toast.LENGTH_SHORT).show();
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