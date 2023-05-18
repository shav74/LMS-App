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
    TextInputEditText inp_plymuid,inp_plympass,inp_apppass,inp_dname,inp_plymemail;
    String PUemail;
    ProgressBar progressReg;
    FirebaseFirestore db;
    String PUid, PUpass,Apass,Dname;
    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inp_plymuid = findViewById(R.id.inp_PUid);
        inp_plympass = findViewById(R.id.inp_PUpass);
        inp_apppass = findViewById(R.id.inp_AppPass);
        inp_dname = findViewById(R.id.inp_Dname);
        inp_plymemail = findViewById(R.id.inp_PUemail);

        Button btn_reg = findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 PUid = inp_plymuid.getText().toString().trim();
                 PUpass = inp_plympass.getText().toString().trim();
                 Apass = inp_apppass.getText().toString().trim();
                 Dname = inp_dname.getText().toString().trim();
                PUemail = inp_plymemail.getText().toString().trim();
                progressReg = findViewById(R.id.progressBarReg);

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

                db = FirebaseFirestore.getInstance();
                String user;
                if (PUid.charAt(0) == 'l' || PUid.charAt(0) == 'L') {
                    lecturerRegister();
                } else {
                    registerStudent();
                }
            }
        });
    }

    void registerStudent(){
        DocumentReference studentref = db.collection("Student").document(PUid);
        progressReg.setVisibility(View.VISIBLE);
        studentref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("authhere", "match making");
                    Log.d("authhere", "puid is " + PUid);
                    Log.d("authhere", "PU pass is" + PUpass);

                    if (document.exists()) {
                        Log.d("authhere", "document is here");
                        String password = document.getString("password");
                        Log.d("authhere", "password is  + " + password);
                        if (password.equals(PUpass)) {
                            Log.d("authhere", "gotchiuuuuu");
                            studentref.update("display name", Dname);
                            studentref.update("App password", Apass);
                            studentref.update("email", PUemail);
                            Toast.makeText(getApplicationContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
                            mAuth.createUserWithEmailAndPassword(PUemail, PUpass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("authhere", "making user");
                                            if (task.isSuccessful()) {
                                                Log.d("authhere", "hareeee");
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

    void lecturerRegister(){
        DocumentReference lecRef = db.collection("lec").document("l1");
        progressReg.setVisibility(View.VISIBLE);
        lecRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Log.d("authhere", "match making");
                    Log.d("authhere", "lecturererrrr");
                    Log.d("authhere", "puid is " + PUid);
                    Log.d("authhere", "PU pass is" + PUpass);

                    if (document.exists()) {
                        Log.d("authhere", "document is here");
                        String password = document.getString("password");
                        Log.d("authhere", "password is  + " + password);
                        if (password.equals(PUpass)) {
                            Log.d("authhere", "gotchiuuuuu");
                            lecRef.update("display name", Dname);
                            lecRef.update("App password", Apass);
                            lecRef.update("email", PUemail);
                            Toast.makeText(getApplicationContext(), "User added successfully!", Toast.LENGTH_SHORT).show();
                            mAuth.createUserWithEmailAndPassword(PUemail, PUpass)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("authhere", "making user");
                                            if (task.isSuccessful()) {
                                                Log.d("authhere", "hareeee");
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
}
