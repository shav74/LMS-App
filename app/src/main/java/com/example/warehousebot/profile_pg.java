package com.example.warehousebot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class profile_pg extends AppCompatActivity {

    private TextView nameTextView, emailTextView, indexTextView, degreeTextView, contactTextView, batchTextView;
    private FirebaseFirestore db;
    private String userId;
    Button editButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_pg);

        nameTextView = findViewById(R.id.d_name_view);
        emailTextView = findViewById(R.id.pu_email_view);
        indexTextView = findViewById(R.id.pu_index_view);
        degreeTextView = findViewById(R.id.degree_view);
        contactTextView = findViewById(R.id.contact_view);
        batchTextView = findViewById(R.id.batch_view);
        editButton = findViewById(R.id.btn_edit);

        db = FirebaseFirestore.getInstance();
        userId = getIntent().getStringExtra("userId");

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
                    Toast.makeText(profile_pg.this, "No such document", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(profile_pg.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EditProfile.class);
                intent.putExtra("userId", userId);
                Log.d("USERID", "userid value in profile page: " + userId);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
