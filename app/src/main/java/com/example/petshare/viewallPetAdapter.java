package com.example.petshare;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public abstract class viewallPetAdapter extends RecyclerView.Adapter<viewallPetAdapter.viewPetHolder> {
    private ArrayList<viewallPet> mpetArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onViewClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
         mListener = listener;
    }

    public static class viewPetHolder extends RecyclerView.ViewHolder {

        public ImageView img;
        public TextView txtPetCode;
        public Button btnViewPet;


        public viewPetHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            img = itemView.findViewById(R.id.cardviewImg);
            txtPetCode = itemView.findViewById(R.id.txtPetCode);
            btnViewPet = itemView.findViewById(R.id.btnViewPet);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        Log.e("OnCLick", ""+getAdapterPosition()+"\n"+listener);
                        int position = getAdapterPosition();
                        viewAllPets vap = new viewAllPets();
                        vap.getCurrentPos(position);
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);



                        }
                    }
                }
            });

            btnViewPet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        Log.e("btnViewPet", "");
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onViewClick(position);


                        }
                    }
                }
            });
        }
    }


    public viewallPetAdapter(ArrayList<viewallPet> petArrayList) {
        mpetArrayList = petArrayList;

    }

    @NonNull
    @Override
    public viewPetHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_all_pets_row, parent, false);
        viewPetHolder vph = new viewPetHolder(view, mListener);
        return vph;
    }



    @Override
    public int getItemCount() {
        return mpetArrayList.size();
    }


}
