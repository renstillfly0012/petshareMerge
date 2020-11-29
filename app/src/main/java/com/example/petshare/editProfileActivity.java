package com.example.petshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

public class editProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnSave;
    EditText txtfname, txtEmail, txtPass;
    ImageView userImg;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    String ID, NAME, ROLE_ID, IMAGE, STATUS, IMGURL,EMAIL;
    TextView txtUser,txtRole;
    ImageView imgUserImg;
    private SharedPreferences sharedPreferences;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        drawerLayout = findViewById(R.id.edituser_drawer_layout);
        navigationView = findViewById(R.id.edituser_nav_view);
        toolbar = findViewById(R.id.edituser_toolbar);
        txtfname = findViewById(R.id.edit_txtFname);
        txtEmail = findViewById(R.id.edit_txtEmail);
        txtPass = findViewById(R.id.edit_txtPassword);

        Intent i = getIntent();
        ID = i.getStringExtra("KEY_ID_INT");
        NAME = i.getStringExtra("KEY_NAME_INT");
        EMAIL = i.getStringExtra("KEY_EMAIL_INT");
        STATUS = i.getStringExtra("KEY_STATUS_INT");

        txtfname.setText(NAME);
        txtEmail.setText(EMAIL);
//        txtPass.setText("******");

//        sharedPreferences = getSharedPreferences("KEY_USER_INFO", MODE_PRIVATE);
//        ID = sharedPreferences.getString("KEY_ID", null);
//        NAME = sharedPreferences.getString("KEY_NAME", null);
//        ROLE_ID = sharedPreferences.getString("KEY_ROLE_ID", null);
//        IMAGE = sharedPreferences.getString("KEY_IMAGE", null);
//        STATUS = sharedPreferences.getString("KEY_STATUS", null);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_close, R.string.navigation_drawer_open);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        IMGURL = "https://pet-share.com/assets/images/"+ IMAGE;
        Glide.with(this).load(IMGURL).into(imgUserImg);
        txtUser.setText(NAME);
        setRole(getRole(ROLE_ID));

        btnSave = findViewById(R.id.edit_btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"save",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getBaseContext(), dashboard_activity.class);
//                startActivity(intent);
            }
        });
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
                Toast.makeText(editProfileActivity.this,"profile to: " + EMAIL,Toast.LENGTH_SHORT).show();
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