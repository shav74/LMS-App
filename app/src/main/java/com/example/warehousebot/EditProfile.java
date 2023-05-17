package com.example.warehousebot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {
    Button updte;
    private String userId;
    TextInputEditText name, password, contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        updte = findViewById(R.id.btn_update);
        name = findViewById(R.id.inp_new_name);
        password = findViewById(R.id.inp_new_app_pass);
        contact = findViewById(R.id.inp_new_contact);

        userId = getIntent().getStringExtra("userId");

        try {
            if (userId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Student").document(userId);

                updte.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String n_name = name.getText().toString().trim();
                        String n_pass = password.getText().toString().trim();
                        String n_cont = contact.getText().toString().trim();

                        // Create a map to store the non-null fields to be updated
                        Map<String, Object> updates = new HashMap<>();
                        if (!TextUtils.isEmpty(n_name)) {
                            updates.put("display name", n_name);
                        }
                        if (!TextUtils.isEmpty(n_pass)) {
                            updates.put("App password", n_pass);
                        }
                        if (!TextUtils.isEmpty(n_cont)) {
                            updates.put("phone", n_cont);
                        }

                        // Update the document with the non-null fields
                        if (!updates.isEmpty()) {
                            docRef.update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
//                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        } else {
                            Toast.makeText(EditProfile.this, "Nothing to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else {
                Log.e("EditProfile", "userId is null");
            }
        } catch (Exception e) {
            Log.e("EditProfile", "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}