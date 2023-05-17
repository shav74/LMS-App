package com.example.warehousebot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Marks extends Fragment implements RecyclerVIewInterface {

    RecyclerView recyclerView;
    ArrayList<Exams> examsArrayList;
    AdapterExams adapterExams;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_marks, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.exams_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        examsArrayList = new ArrayList<Exams>();
        adapterExams = new AdapterExams(getActivity(), examsArrayList, this);

        recyclerView.setAdapter(adapterExams);
        eventChangesListener();

        return view;
    }

    private void eventChangesListener() {
        db.collection("Student")
                .document(StudentData.getEmail().substring(0,StudentData.getEmail().indexOf('@')))
                .collection("exams")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            Log.e("Firebase error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED || dc.getType() == DocumentChange.Type.MODIFIED) {
                                examsArrayList.add(dc.getDocument().toObject(Exams.class));
                            }
                            adapterExams.notifyDataSetChanged();
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                        }

                    }

                });
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ExamMarks.class);
        String examName = examsArrayList.get(position).getExamName();
        intent.putExtra("ID", examName);
        Log.d("dberr", "onCreate: im in");
        startActivity(intent);
    }
}