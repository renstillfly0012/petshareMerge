package com.example.petshare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class howToAdopt extends AppCompatActivity {

    public static ViewPager viewPager;
    HowViewPagerAdapater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);

        viewPager=findViewById(R.id.viewpager);
        adapter=new HowViewPagerAdapater(this);
        viewPager.setAdapter(adapter);


        if(isAdoptAlready()){
            SharedPreferences.Editor editor = getSharedPreferences("howtoadopt", MODE_PRIVATE).edit();
            editor.putBoolean("howtoadopt",true);
            editor.commit();
        }
    }

    private boolean isAdoptAlready() {
        SharedPreferences sharedPreferences=getSharedPreferences("howtoadopt",MODE_PRIVATE);
        boolean result = sharedPreferences.getBoolean("howtoadopt", false);
        return result;
    }
}