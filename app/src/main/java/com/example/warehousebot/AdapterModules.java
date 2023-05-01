package com.example.warehousebot;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterModules extends RecyclerView.Adapter<AdapterModules.MyViewHolder>  {

    private final RecyclerVIewInterface recyclerVIewInterface;
    Context context;
    ArrayList<Modules> modulesArrayList;

    public AdapterModules(Context context, ArrayList<Modules> modulesArrayList, RecyclerVIewInterface recyclerVIewInterface) {
        this.context = context;
        this.modulesArrayList = modulesArrayList;
        this.recyclerVIewInterface = recyclerVIewInterface;
    }

    @NonNull
    @Override
    public AdapterModules.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent,false);
        return new MyViewHolder(v, recyclerVIewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterModules.MyViewHolder holder, int position) {

        Modules module = modulesArrayList.get(position);
        holder.moduleName.setText(module.name);
        holder.moduleCode.setText(module.moduleCode);
        holder.itemView.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(module.colorCode)));
        holder.moduleDetails.setText(module.details);

    }

    @Override
    public int getItemCount() {
        return modulesArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView moduleName, moduleCode, moduleDetails;

        public MyViewHolder(@NonNull View itemView, RecyclerVIewInterface recyclerVIewInterface) {
            super(itemView);
            moduleName = itemView.findViewById(R.id.moduleName);
            moduleCode = itemView.findViewById(R.id.moduleId);
            moduleDetails = itemView.findViewById(R.id.moduleDetails);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerVIewInterface != null){
                        int pos = getAdapterPosition();
                        TextView moduleIdCode = itemView.findViewById(R.id.moduleId);
                        String moduleCode = moduleIdCode.getText().toString();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerVIewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
