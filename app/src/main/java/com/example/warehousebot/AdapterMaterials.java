package com.example.warehousebot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterMaterials extends RecyclerView.Adapter<AdapterMaterials.meterialViewHolder> {

    Context context;
    ArrayList<LecMaterials> lecMaterialsArrayList;
    private final RecyclerVIewInterface recyclerVIewInterface;

    public AdapterMaterials(Context context, ArrayList<LecMaterials> lecMaterialsArrayList, RecyclerVIewInterface recyclerVIewInterface) {
        this.context = context;
        this.lecMaterialsArrayList = lecMaterialsArrayList;
        this.recyclerVIewInterface = recyclerVIewInterface;
    }

    @NonNull
    @Override
    public AdapterMaterials.meterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.module_content_item, parent, false);
        return new meterialViewHolder(v, recyclerVIewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMaterials.meterialViewHolder holder, int position) {

        LecMaterials lecMaterials = lecMaterialsArrayList.get(position);
        holder.itemTitle.setText(lecMaterials.getMaterialName());
        holder.itemDetails.setText(lecMaterials.getMaterialDetails());

    }

    @Override
    public int getItemCount() {
        return lecMaterialsArrayList.size();
    }

    public static class meterialViewHolder extends RecyclerView.ViewHolder{

        TextView itemTitle, itemDetails;

        public meterialViewHolder(@NonNull View itemView, RecyclerVIewInterface recyclerVIewInterface) {
            super(itemView);
            itemDetails = itemView.findViewById(R.id.item_details);
            itemTitle = itemView.findViewById(R.id.item_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerVIewInterface != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerVIewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
