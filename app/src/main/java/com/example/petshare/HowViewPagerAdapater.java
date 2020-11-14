package com.example.petshare;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class HowViewPagerAdapater extends PagerAdapter {

    Context ctx;
    private  Intent intent;

    public HowViewPagerAdapater(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_how_to_adopt, container,false);


        ImageView img = view.findViewById(R.id.how_image1);
        ImageView circle1 = view.findViewById(R.id.circle1);
        ImageView circle2 = view.findViewById(R.id.circle2);
        ImageView circle3 = view.findViewById(R.id.circle3);
        ImageView circle4 = view.findViewById(R.id.circle4);

        TextView title = view.findViewById(R.id.txtTitle);
        TextView description = view.findViewById(R.id.how_txtDescription1);

        Button btnNext = view.findViewById(R.id.how_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                howToAdopt.viewPager.setCurrentItem(position+1);
            }
        });
        Button btnBack = view.findViewById(R.id.how_backBtn);

        switch (position){
            case 0:
                circle1.setImageResource(R.drawable.selected);
                circle2.setImageResource(R.drawable.unselected);
                circle3.setImageResource(R.drawable.unselected);
                circle4.setImageResource(R.drawable.unselected);

                description.setText("Step 1:\nChoose a pet.");
                img.setImageResource(R.drawable.adoptpet);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(ctx, dashboard_activity.class);
                        ctx.startActivity(intent);
                    }
                });
                break;
            case 1:
                circle1.setImageResource(R.drawable.unselected);
                circle2.setImageResource(R.drawable.selected);
                circle3.setImageResource(R.drawable.unselected);
                circle4.setImageResource(R.drawable.unselected);


                description.setText("Step 2:\nBook an appointment \n" +
                        "for meeting the pet.");
                img.setImageResource(R.drawable.booking);

                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        howToAdopt.viewPager.setCurrentItem(position-1);
                    }
                });
                break;
            case 2:
                circle1.setImageResource(R.drawable.unselected);
                circle2.setImageResource(R.drawable.unselected);
                circle3.setImageResource(R.drawable.selected);
                circle4.setImageResource(R.drawable.unselected);


                description.setText("Step 3:\nBackground check.");
                img.setImageResource(R.drawable.bgcheck);
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        howToAdopt.viewPager.setCurrentItem(position-1);
                    }
                });
                break;
            case 3:
                circle1.setImageResource(R.drawable.unselected);
                circle2.setImageResource(R.drawable.unselected);
                circle3.setImageResource(R.drawable.unselected);
                circle4.setImageResource(R.drawable.selected);


                description.setText("Step 4:\nTake care of it.");
                img.setImageResource(R.drawable.takecare);
                btnNext.setText("Get Started");
                btnBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        howToAdopt.viewPager.setCurrentItem(position-1);
                    }
                });

                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        intent = new Intent(ctx, viewAllPets.class);
                        ctx.startActivity(intent);
                    }
                });
                break;

        }

        container.addView(view);
        return view;


    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
