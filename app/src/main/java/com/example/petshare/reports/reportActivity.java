package com.example.petshare.reports;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.petshare.Constant;
import com.example.petshare.R;
import com.example.petshare.dashboard_activity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class reportActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private double address_lat,address_lng;
    private Button btnTakePic, btnCancel,btnSubmit;
    private EditText txtAddress, txtDescription;
    private  ImageView img;
    private Intent intent;
    private int IMG_REQUEST = 21;
    private Bitmap bitmap;
    private Uri path;
    private String address;
    private String report_img_name, report_img_file;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView txtUser, txtRole;
    ImageView imgUserImg;
   private String id, userid, username, role_id, image, status, imgUrl;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        btnTakePic = findViewById(R.id.report_btnSelectPicture);
         img = findViewById(R.id.report_image);
         txtAddress = findViewById(R.id.report_txtAddress);
         txtDescription = findViewById(R.id.report_txtDescription);
         btnCancel = findViewById(R.id.report_btnCancel);
         btnSubmit = findViewById(R.id.report_btnSubmit);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        sharedPreferences = getSharedPreferences("KEY_USER_INFO", dashboard_activity.MODE_PRIVATE);
        userid = sharedPreferences.getString("KEY_ID", null);
        username = sharedPreferences.getString("KEY_NAME", null);
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

        navigationView.setCheckedItem(R.id.nav_home);

        View header = navigationView.getHeaderView(0);
        txtUser = header.findViewById(R.id.txtUsername);
        txtRole = header.findViewById(R.id.txtRole);
        imgUserImg = header.findViewById(R.id.imgUserImg);
        imgUrl = "https://pet-share.com/assets/images/" + image;
        Glide.with(this).load(imgUrl).into(imgUserImg);
        txtUser.setText(username);

        setRole(getRole(role_id));

        //getting data from mainACtivity
        intent = getIntent();
        address_lat = intent.getDoubleExtra("user_lat", 0);
        address_lng = intent.getDoubleExtra("user_lng", 0);
        address = intent.getStringExtra("user_address");
//        role_id = intent.getStringExtra("role_id");

        txtAddress.setText(address);

         btnTakePic.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, IMG_REQUEST);
             }
         });
         btnCancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 intent = new Intent(reportActivity.this, dashboard_activity.class);
                 startActivity(intent);
             }
         });

         btnSubmit.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 storeToServer(Integer.parseInt(userid),address,txtDescription.getText().toString(),report_img_file,address_lat,address_lng);
             }
         });
    }


//    public static ReportRequest newReport(){
//        ReportRequest data = new ReportRequest();
//        data.setUser_id(user_id);
//        data.setAddress(address);
//        data.setDescription(description);
//        data.setImage(image+".png");
//        data.setAddress_lat(address_lat);
//        data.setAddress_lng(address_lng);
//
//
//        return data;
//
//    }

    private void storeToServer(int user_id,
                               String address,
                               String description,
                               String image,
                               double address_lat,
                               double address_lng)
    {



        File file = new File(String.valueOf(path));
        File selectedFile = Environment.getExternalStorageDirectory();
        File parentDir = selectedFile.getParentFile();
        File casted_image = new File(parentDir,selectedFile.getName());

        Uri uri = Uri.parse(String.valueOf(path));

        getPath(path);


       

        Log.e("FILE", String.valueOf(selectedFile) +
                "\n" + selectedFile+
                "\n" + parentDir +
                "\n" + casted_image +
                "\n" + path +
                "\n" + path.getEncodedPath() +
                "\n" + path.getPathSegments() +
                "\n" + path+
                "\n" + path.compareTo(uri));

        RequestBody fp = RequestBody.create(MediaType.parse("image/*"), file);



        MultipartBody.Part imgfile = MultipartBody.Part.
                createFormData("image", report_img_name, fp);
        Call<ReportResponse> reportResponseCall = Constant.getReportService().
                storeReport(user_id,address,description,imgfile,address_lat,address_lng);
        Log.e("REPOERT REQUEST",user_id+
                "\n"+address+
                "\n"+description+
                "\n"+uri+
                "\n"+fp+
                "\n"+report_img_name+
                "\n"+imgfile+
                "\n"+address_lat+
                "\n"+address_lng);
        try {
            reportResponseCall.enqueue(new Callback<ReportResponse>() {
                @Override
                public void onResponse(Call<ReportResponse> call,
                                       Response<ReportResponse> response) {
                    Log.e("onReponse", "GSON:\n" + response.isSuccessful());
                    showToast("GSON" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        Log.e("SUCCESSFUL", new Gson().toJson(response.body()));
                        intent = new Intent(reportActivity.this,
                                dashboard_activity.class);

                        Toast.makeText(reportActivity.this,
                                "Thank you for supporting the organization",
                                Toast.LENGTH_SHORT).show();

                        startActivity(intent);
                    } else {
                        Log.e("Error Not Successful", ""+response.isSuccessful());
                        Log.e("Error Not Successful", ""+response.body());
                        try {
                            Log.e("Error Not Successful", ""+response.errorBody().string());
                            Log.e("Error Not Successful", ""+response.raw());
                        }catch (IOException e){
                            Log.e("Error Message Exception", ""+e.getLocalizedMessage());
                            e.printStackTrace();
                        }


                        try {
                            showToast("An error ocured\nPlease try again later." + response.errorBody().string());
                            Log.e("Error Not Successful", ""+response.errorBody().string());

                        } catch (IOException e) {
                            Log.e("Error Message Exception", ""+e.getLocalizedMessage());
                            e.printStackTrace();
                            showToast(e.getMessage());
                        }


                    }
                }


                @Override
                public void onFailure(Call<ReportResponse> call, Throwable t) {
                    String message = t.getLocalizedMessage();
                    Log.e("ERROR MESSAGE:", message);
                    Toast.makeText(reportActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.e("EXCEPTION ERROR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST){
            if(resultCode == RESULT_OK){
                if(data != null){
                     path = data.getData();


                    try {
                        int width=500;
                        int height=500;
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                        bitmap=Bitmap.createScaledBitmap(bitmap, width,height, true);
                        img.setImageBitmap(bitmap);
                        Log.e("IMAGE", bitmap+"\n"+path.getPath());
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        report_img_file = Base64.encodeToString(byteArrayOutputStream.toByteArray(),Base64.DEFAULT);
                        report_img_name = path.getLastPathSegment();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }



    private double setLat(double lat){
        return address_lat = lat;
    }

    private double setLng(double lng){
        return address_lng = lng;
    }

    public String getRole(String role_id){
        return role_id == "1" ? "Admin User": "Foster User";
    }

    public void setRole(String role_id){
        txtRole.setText(role_id);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    public void showToast(String message){
        Toast.makeText(getBaseContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }
}