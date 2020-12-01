package com.example.petshare;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class viewallPet implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Integer id;
    Integer user_id;
    String name;
    String image;
    Integer age;
    String breed;
    String status;
    String description;
    String createdAt;
    String updatedAt;
    TextView txtPetCode,txtPetBreed,txtPetDescription,txtDate;
    viewAllPets vap= new viewAllPets();
    int day,month,year,hour,minute;
    int day_x,month_x,year_x,hour_x,minute_x;
    Context ct;
    String dateAndTime;
    Dialog dialog;



    public viewallPet(Integer id, String name, String image, Integer age, String breed, String status, String description, int user_id) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.age = age;
        this.breed = breed;
        this.status = status;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user_id = user_id;
    }

    public void showToast(String message, Context context){
        Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT).show();
    }

    public void showPet(final Context context, final viewallPet petArrayList){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.view_pet);
        bottomSheetDialog.setCanceledOnTouchOutside(false);

        ImageView img = bottomSheetDialog.findViewById(R.id.viewpet_img);
         txtPetCode = bottomSheetDialog.findViewById(R.id.viewpet_txtPetCode);
         txtPetBreed = bottomSheetDialog.findViewById(R.id.viewpet_txtPetBreed);
         txtPetDescription = bottomSheetDialog.findViewById(R.id.viewpet_txtPetDescription);
        txtDate = bottomSheetDialog.findViewById(R.id.viewpet_txtDate);
        Button btnAdopt = bottomSheetDialog.findViewById(R.id.viewpet_btnAdopt);
        Button btnDate = bottomSheetDialog.findViewById(R.id.viewpet_btnDate);
        Button btnCancel = bottomSheetDialog.findViewById(R.id.viewpet_btnCancel);

        txtPetCode.setText("Pet Code:\n"+petArrayList.getName());
        txtPetBreed.setText("Pet Breed:\n"+petArrayList.getBreed());
        txtPetDescription.setText("Pet Description:\n"+petArrayList.getDescription());


        String imgUrl;
        imgUrl ="https://pet-share.com/assets/images/pets/" + petArrayList.getImage();
        Glide.with(context).load(imgUrl).into(img);
        this.ct = context;








        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(context,  myDateListener, year, month, day);
                datePickerDialog.show();

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        btnAdopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dateAndTime != null){
                    submitForm(newAppoinment(user_id,petArrayList.getId(),dateAndTime));
                }

            }
        });

        bottomSheetDialog.show();
    }

    public static AdoptRequest newAppoinment(int user_id, int pet_id, String dateAndTime){
        AdoptRequest data = new AdoptRequest();
        data.setUser_id(user_id);
        data.setRequested_pet_id(pet_id);
        data.setRequested_date(dateAndTime);

        return data;
    }

    public void submitForm(AdoptRequest adoptRequest){
        Log.e("Form Data",""+user_id+"\n"+id+"\n"+dateAndTime);
        Call<AdoptResponse> bookAnAppointmentResponseCall = Constant.getAdoptService().bookAnAppointment(adoptRequest);
        try{
            Log.e("Form Data",""+adoptRequest.requested_date+
                    "\n"+adoptRequest.requested_pet_id+
                    "\n"+adoptRequest.user_id
            );
            bookAnAppointmentResponseCall.enqueue(new Callback<AdoptResponse>() {
                @Override
                public void onResponse(Call<AdoptResponse> call, Response<AdoptResponse> response) {
                    Log.e("onReponse", "GSON:\n" + new Gson().toJson(response.body()));
                    if(response.isSuccessful()){
                        showToast("Appoinment request is successful",ct);
                        Log.e("Successful", "Data"+new Gson().toJson(response.body()));


                        Intent intent = new Intent(ct, dashboard_activity.class);
                        ct.startActivity(intent);

                    }
                    else{
                        try {
                            Log.e("Error Not successful", ""+response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("Error Message exception", ""+e.getLocalizedMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<AdoptResponse> call, Throwable t) {

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            year_x = arg1;
            month_x =arg2+1;
            day_x = arg3;
            Calendar calendar = Calendar.getInstance();
            hour = calendar.get(Calendar.HOUR);
            minute = calendar.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(ct,
                    myTimeListener, hour,minute, true);
            timePickerDialog.show();
        }
    };

    public TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            hour_x=i;
            minute_x=i1;

            dateAndTime = month+"/"+day_x+"/"+year_x+"\n"+hour_x+":"+minute_x;

            if(hour<=12)
            {
                txtDate.setText(dateAndTime+" PM");
            }else{
                txtDate.setText(dateAndTime+" AM");
            }



            txtDate.setVisibility(View.VISIBLE);
        }
    };




    public void setUserId(Integer id) {
        this.user_id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }








}
