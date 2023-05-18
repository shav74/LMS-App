package com.example.warehousebot;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Settings extends Fragment {

    private TextView nameTextView, emailTextView, indexTextView, degreeTextView, contactTextView, batchTextView;
    private FirebaseFirestore db;
    private String userId;
    Button editButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        nameTextView = view.findViewById(R.id.d_name_view);
        emailTextView = view.findViewById(R.id.pu_email_view);
        indexTextView = view.findViewById(R.id.pu_index_view);
        degreeTextView = view.findViewById(R.id.degree_view);
        contactTextView = view.findViewById(R.id.contact_view);
        batchTextView = view.findViewById(R.id.batch_view);
        editButton = view.findViewById(R.id.btn_edit);

        db = FirebaseFirestore.getInstance();
        if (LecturerData.isIsLecturer()) {

        } else {
            userId = StudentData.getEmail().substring(0, StudentData.getEmail().indexOf("@"));
            DocumentReference docRef = db.collection("Student").document(userId);
            docRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String displayName = document.getString("display name");
                        String email = document.getString("email");
                        String degree = document.getString("degree");
                        String batch = document.getString("batch");
                        String phone = document.getString("phone");

                        nameTextView.setText(displayName != null ? displayName : "-");
                        emailTextView.setText(email != null ? email : "-");
                        indexTextView.setText(userId);
                        degreeTextView.setText(degree != null ? degree : "-");
                        batchTextView.setText(batch != null ? batch : "-");
                        contactTextView.setText(phone != null ? phone : "-");
                    } else {
                        Toast.makeText(getActivity(), "No such document", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfile.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        return view;
    }
}