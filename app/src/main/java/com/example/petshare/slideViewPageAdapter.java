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

public class slideViewPageAdapter extends PagerAdapter {

    Context ctx;

    public slideViewPageAdapter(Context ctx){
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_screen, container,false);

        ImageView logo = view.findViewById(R.id.imageView2);
        ImageView circle1 = view.findViewById(R.id.imageView6);
        ImageView circle2 = view.findViewById(R.id.imageView7);
        ImageView circle3 = view.findViewById(R.id.imageView8);

        TextView title = view.findViewById(R.id.textView);
        TextView description = view.findViewById(R.id.textView2);

        ImageView back = view.findViewById(R.id.imageView9);
        ImageView next = view.findViewById(R.id.imageView11);
        Button btnGetStarted=view.findViewById(R.id.btnGetStarted);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideActivity.viewPager.setCurrentItem(position+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideActivity.viewPager.setCurrentItem(position-1);
            }
        });

        switch (position){
            case 0:
//                logo.setImageResource(R.drawable.adopt);
                circle1.setImageResource(R.drawable.unselected);
                circle2.setImageResource(R.drawable.selected);
                circle3.setImageResource(R.drawable.unselected);


                title.setText("Adopt");
                description.setText("First SLide Description");
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:

                logo.setImageResource(R.drawable.report);
                circle1.setImageResource(R.drawable.unselected);
                circle2.setImageResource(R.drawable.unselected);
                circle3.setImageResource(R.drawable.selected);


                title.setText("Report");
                description.setText("Second SLide Description");
                back.setVisibility(View.VISIBLE);
                break;

            case 2:
                logo.setImageResource(R.drawable.donate);
                circle1.setImageResource(R.drawable.selected);
                circle2.setImageResource(R.drawable.unselected);
                circle3.setImageResource(R.drawable.unselected);


                title.setText("Donate");
                description.setText("Third SLide Description");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
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
