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

public class AdapterExams extends RecyclerView.Adapter<AdapterExams.MyViewHolder>{

    private final RecyclerVIewInterface recyclerVIewInterface;
    Context context;
    ArrayList<Exams> examsArrayList;

    public AdapterExams(Context context, ArrayList<Exams> examsArrayList, RecyclerVIewInterface recyclerVIewInterface) {
        this.context = context;
        this.examsArrayList = examsArrayList;
        this.recyclerVIewInterface = recyclerVIewInterface;
    }

    @NonNull
    @Override
    public AdapterExams.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.exam, parent,false);
        return new AdapterExams.MyViewHolder(v, recyclerVIewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterExams.MyViewHolder holder, int position) {

        Exams exam = examsArrayList.get(position);
        holder.examName.setText(exam.examName);
        holder.examDate.setText(exam.dateReleased);
        holder.examDetails.setText(exam.description);

    }

    @Override
    public int getItemCount() {
        return examsArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView examName, examDate, examDetails;

        public MyViewHolder(@NonNull View itemView, RecyclerVIewInterface recyclerVIewInterface) {
            super(itemView);
            examName= itemView.findViewById(R.id.exam_name);
            examDate = itemView.findViewById(R.id.exam_date);
            examDetails = itemView.findViewById(R.id.exam_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(recyclerVIewInterface != null){
                        int pos = getAdapterPosition();
                        TextView examName = itemView.findViewById(R.id.exam_name);
                        String examNameString = examName.getText().toString();
                        if(pos != RecyclerView.NO_POSITION){
                            recyclerVIewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
