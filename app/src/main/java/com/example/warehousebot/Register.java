package com.example.warehousebot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    TextView textViewLogNow;
    String PUemail;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextInputEditText inp_plymuid = findViewById(R.id.inp_PUid);
        TextInputEditText inp_plympass = findViewById(R.id.inp_PUpass);
        TextInputEditText inp_apppass = findViewById(R.id.inp_AppPass);
        TextInputEditText inp_dname = findViewById(R.id.inp_Dname);
        TextInputEditText inp_plymemail = findViewById(R.id.inp_PUemail);

        Button btn_reg = findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PUid = inp_plymuid.getText().toString().trim();
                String PUpass = inp_plympass.getText().toString().trim();
                String Apass = inp_apppass.getText().toString().trim();
                String Dname = inp_dname.getText().toString().trim();
                PUemail = inp_plymemail.getText().toString().trim();
                ProgressBar progressReg = findViewById(R.id.progressBarReg);

                if (TextUtils.isEmpty(PUid)) {
                    inp_plymuid.setError("ID Cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(PUemail)) {
                    inp_plymemail.setError("Email Cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(PUpass) || PUpass.length() < 9) {
                    inp_plympass.setError("Password should contain more than 8 characters");
                    return;
                }

                if (TextUtils.isEmpty(Dname)) {
                    inp_dname.setError("Cannot be empty");
                    return;
                }

                if (TextUtils.isEmpty(Apass)) {
                    inp_apppass.setError("Cannot be empty");
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference docRef = db.collection("Student").document(PUid);
                progressReg.setVisibility(View.VISIBLE);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String password = document.getString("password");
                                if (password.equals(PUpass)) {
                                    docRef.update("display name", Dname);
                                    docRef.update("App password", Apass);
                                    docRef.update("email", PUemail);
                                    Toast.makeText(getApplicationContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
                                    mAuth.createUserWithEmailAndPassword(PUemail, PUpass)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                                        startActivity(intent);
                                                        finish();

                                                        Toast.makeText(Register.this, "Account Created.",
                                                                Toast.LENGTH_SHORT).show();
                                                        progressReg.setVisibility(View.GONE);

                                                    } else {
                                                        Toast.makeText(Register.this, "Authentication failed.",
                                                                Toast.LENGTH_SHORT).show();
                                                        progressReg.setVisibility(View.GONE);

                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid user!", Toast.LENGTH_SHORT).show();
                                    progressReg.setVisibility(View.GONE);

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid user!", Toast.LENGTH_SHORT).show();
                                progressReg.setVisibility(View.GONE);

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
