package com.example.warehousebot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Document;

import java.util.List;

public class Login extends AppCompatActivity {

    private TextInputEditText editTextEmail, editTextPassword;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    FirebaseFirestore db;
    CollectionReference studentRef;

    StudentData studentData;
    LecturerData lecturerData;

    String name, user_password, user_email, dbPassword, degreeName, id, degreeID;
    List<String> teachingModules;
    List<String> studentModules;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();
        studentRef = db.collection("Student");
        TextView inp_id = findViewById(R.id.inp_id);
        TextView inp_pass = findViewById(R.id.inp_pass);
        Button loginButton = findViewById(R.id.btn_log);
        TextView registerButton = findViewById(R.id.btn_reg1);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = inp_id.getText().toString().trim();
                String password = inp_pass.getText().toString().trim();
                progressBar.setVisibility(View.VISIBLE);

                if (id.isEmpty()) {
                    inp_id.setError("ID cannot be empty");
                    return;
                }

                if (password.isEmpty()) {
                    inp_pass.setError("Password cannot be empty");
                    return;
                }

                //checking if the user is student or lecturer and passing data to the according class
                //todo check this

                if (id.charAt(0) == 'l') {
                    getLecturerData();
                    lecturerData = new LecturerData(dbPassword, name, user_email, user_password, teachingModules);
                    LecturerData.setIsLecturer(true);
                } else {
                    getStudentData();
                    studentData = new StudentData(dbPassword, degreeID, name, user_email, user_password, studentModules, degreeName);
                    StudentData.setIsStudent(true);
                }

                if (dbPassword != null && dbPassword.equals(password)) {
                    mAuth.signInWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                progressBar.setVisibility(View.GONE);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getStudentData() {
        db.collection("Student").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        degreeID = document.getString("degreeId");
                        dbPassword = document.getString("App password");
                        user_email = document.getString("email");
                        user_password = document.getString("password");
                        name = document.getString("display name");
                        studentModules = (List<String>) document.get("learning modules");
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "data adding failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getLecturerData() {
        db.collection("Lecturer").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        dbPassword = document.getString("App password");
                        user_email = document.getString("email");
                        user_password = document.getString("password");
                        name = document.getString("display name");
                        teachingModules = (List<String>) document.get("teaching modules");
                    } else {
                        Toast.makeText(getApplicationContext(), "data adding failed!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}