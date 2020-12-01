package com.example.petshare.donation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.petshare.Constant;
import com.example.petshare.MainActivity;
import com.example.petshare.R;
import com.example.petshare.dashboard_activity;
import com.example.petshare.editProfileActivity;
import com.example.petshare.howToAdopt;
import com.example.petshare.viewProfileActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class donationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(paypalConfig.PAYPAL_CLIENT_ID);

    Button btnPaynow;
    EditText txtAmount,txtName;

    private String amount,name;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView txtUser, txtRole;
    ImageView imgUserImg;
    Log log;
    String id, userid, username, role_id, image, status, imgUrl;
    Intent intent;
    private SharedPreferences sharedPreferences;

    int transaction_id;

    @Override
    protected void onDestroy() {
        stopService(new Intent (this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

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

        intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        btnPaynow = findViewById(R.id.donation_btnSubmit);
        txtAmount = findViewById(R.id.donation_txtAmount);
        txtName = findViewById(R.id.donation_txtName);






        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        if(!txtAmount.getText().toString().isEmpty()) {
            amount = txtAmount.getText().toString();
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)),
                    "PHP", "Donation for PSPCA", PayPalPayment.PAYMENT_INTENT_SALE);
            intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }else{
            Toast.makeText(this, "Amount is required", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("Start", ""+requestCode+"\n"+resultCode+"\n"+data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            Log.e("Request Code", ""+PAYPAL_REQUEST_CODE+"\n"+requestCode);
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                Log.e("RequestCode_OK", ""+RESULT_OK);
                if(confirmation != null){
                    try{

                        String paymentDetails = confirmation.toJSONObject().toString(4);

                        Toast.makeText(this, "Payment Details\n"+paymentDetails, Toast.LENGTH_SHORT).show();
                        Log.e("Payment Details", paymentDetails+amount);

                        JSONObject jsonObject = new JSONObject(paymentDetails);
                        JSONObject jsonObject1 = jsonObject.optJSONObject("response");// return null or found object
                        if(jsonObject1 != null){
                            String id = jsonObject1.optString("id",""); //return value or empty string
                            String status = jsonObject1.optString("state","");
                            Log.e(id,status);
                            name = txtName.getText().toString();
                            Log.e("FIELDS",id+"\n"+amount+"\n"+transaction_id+"\n"+name);
                            storeToServer(newDonation(Integer.parseInt(userid),Integer.parseInt(amount),id,name));



                        }

                        





                    }catch (JSONException e){
                        e.printStackTrace();
                        Log.e("response", e.getLocalizedMessage());
                    }
                }
                else{
                    Log.e("Details", confirmation.toJSONObject().toString());
                }

            }
            else if(resultCode == Activity.RESULT_CANCELED){
                Log.e("Cancelled", ""+Activity.RESULT_CANCELED);
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
                Log.e("INVALID", ""+ PaymentActivity.RESULT_EXTRAS_INVALID);
                Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static DonationRequest newDonation(int user_id, int amount, String transaction_id, String donation_name){
        DonationRequest data = new DonationRequest();
        data.setUser_id(user_id);
        data.setDonation_name(donation_name);
        data.setAmount(amount);
        data.setTransaction_id(transaction_id);
        Log.e("DONATION REQUEST",user_id+"\n"+amount+"\n"+transaction_id+"\n"+donation_name);
        return data;


    }

    private void storeToServer(DonationRequest donationRequest) {

        Call<DonationResponse> donationResponseCall = Constant.getDonateService().storeDonation(donationRequest);
        try {
            donationResponseCall.enqueue(new Callback<DonationResponse>() {
                @Override
                public void onResponse(Call<DonationResponse> call, Response<DonationResponse> response) {
                    Log.e("onReponse", "GSON:\n" + response.isSuccessful());
                    showToast("GSON" + new Gson().toJson(response.body()));
                    if (response.isSuccessful()) {
                        Log.e("SUCCESSFUL", new Gson().toJson(response.body()));
                        intent = new Intent(donationActivity.this, dashboard_activity.class);
                        Toast.makeText(donationActivity.this, "Thank you for supporting the organization", Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<DonationResponse> call, Throwable t) {
                    String message = t.getLocalizedMessage();
                    Log.e("ERROR MESSAGE:", message);
                    Toast.makeText(donationActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }catch (Exception e){
            Log.e("EXCEPTION ERROR", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void showToast(String message){
        Toast.makeText(getBaseContext(),
                message,
                Toast.LENGTH_SHORT).show();
    }

    public String getRole(String role_id){
        return role_id == "1" ? "Admin User": "Foster User";
    }

    public void setRole(String role_id){
        txtRole.setText(role_id);
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

                break;
            case R.id.nav_adopt:
                intent = new Intent(this, howToAdopt.class);
                startActivity(intent);
                break;
            case R.id.nav_report:
                break;
            case R.id.nav_donate:
                intent = new Intent(this, donationActivity.class);
                startActivity(intent);
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
                Toast.makeText(this, "Log out Successfully", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();

                break;
            default:

        }

        drawerLayout.closeDrawer((GravityCompat.START));

        return true;
    }
}