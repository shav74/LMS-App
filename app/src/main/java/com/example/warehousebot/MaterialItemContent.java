package com.example.warehousebot;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.moduleinstall.internal.ApiFeatureRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class MaterialItemContent extends AppCompatActivity {

    String title, details, link, extraDetails, moduleCode;

    TextView textViewTitle, textViewDetails, textViewLink, textViewExtras;

    private EditText editTextTitle, editTextDetails, editTextLink, editTextExtras;
    FloatingActionButton buttonEdit, buttonDelete;
    Button btnSubmitChanges;
    AlertDialog dialog;
    FirebaseFirestore db;
    Map<String, Object> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_item_content);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            title = extras.getString("title");
            details = extras.getString("details");
            link = extras.getString("link");
            extraDetails = extras.getString("extraDetails");
            moduleCode = extras.getString("moduleCode");
        }

        buttonEdit = (FloatingActionButton) findViewById(R.id.btn_edit_materials);
        buttonDelete = (FloatingActionButton) findViewById(R.id.btn_delete_materials);

        textViewTitle = findViewById(R.id.txt_mt_name);
        textViewDetails = findViewById(R.id.txt_mt_details);
        textViewExtras = findViewById(R.id.txt_mt_extras);
        textViewLink = findViewById(R.id.txt_mt_link);

        textViewTitle.setText(title);
        textViewDetails.setText(details);
        textViewExtras.setText(extraDetails);
        textViewLink.setText(link);

        //get builder and details

        db = FirebaseFirestore.getInstance();

        if (LecturerData.isIsLecturer()) {
            buttonEdit.setVisibility(View.VISIBLE);
            buttonDelete.setVisibility(View.VISIBLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Details Here");

        View view = getLayoutInflater().inflate(R.layout.material_dialogbox, null);

        editTextTitle = view.findViewById(R.id.material_title);
        editTextDetails = view.findViewById(R.id.material_details);
        editTextExtras = view.findViewById(R.id.material_extra_details);
        editTextLink = view.findViewById(R.id.material_link);
        btnSubmitChanges = view.findViewById(R.id.material_submit);

        builder.setView(view);
        dialog = builder.create();

        btnSubmitChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextTitle.getText().toString().isEmpty()) {
                    editTextTitle.setError("title cannot be empty");
                    return;
                } else if (editTextDetails.getText().toString().isEmpty()) {
                    editTextDetails.setError("Deadline cannot be empty");
                    return;
                } else if (editTextExtras.getText().toString().isEmpty()) {
                    editTextExtras.setError("Details cannot be empty");
                    return;
                } else if (editTextLink.getText().toString().isEmpty()) {
                    editTextLink.setError("link cannot be empty");
                    return;
                } else {

                    db.collection("modules").document(moduleCode).collection("materials").document(title).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            Toast.makeText(MaterialItemContent.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error deleting document", e);
                        }
                    });

                    data = new HashMap<>();
                    data.put("materialName", editTextTitle.getText().toString());
                    data.put("materialDetails", editTextDetails.getText().toString());
                    data.put("materialLink", editTextLink.getText().toString());
                    data.put("materialExtraDetails", editTextExtras.getText().toString());

                    db.collection("modules").document(moduleCode).collection("materials").document(title).set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
                }

                dialog.dismiss();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("modules").document(moduleCode).collection("materials").document(title).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        Toast.makeText(MaterialItemContent.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
            }
        });

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
}