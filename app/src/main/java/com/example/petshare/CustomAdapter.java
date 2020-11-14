package com.example.petshare;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomAdapter extends ArrayAdapter<Fosterlist> {
    List<Fosterlist> fosterlists;
    private final Context context;
    public CustomAdapter(@NonNull Context context, List<Fosterlist> fostersList) {
        super(context,R.layout.single_foster_data,fostersList);
        this.context=context;
        this.fosterlists=fostersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            convertView = inflater.inflate(R.layout.single_foster_data,null);
        }
        //TextView name =(TextView) convertView.findViewById(R.id.foster_name);
        //TextView email =(TextView) convertView.findViewById(R.id.foster_email);
        TextView status =(TextView) convertView.findViewById(R.id.foster_status);
        //ImageView img =(ImageView) convertView.findViewById(R.id.foster_image);

        final Fosterlist fosterList = fosterlists.get(position);
        //name.setText(fosterList.getName());
        //email.setText(fosterList.getEmail());
        status.setText(fosterList.getStatus());

//        try{
//            URL imgurl = new URL(fosterList.getImg_Url());
//            Glide.with(context).load(imgurl).into(img);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
       return super.getView(position, convertView, parent);
    }
}
