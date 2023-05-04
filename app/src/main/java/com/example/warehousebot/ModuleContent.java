package com.example.warehousebot;

import static android.content.ContentValues.TAG;
import static com.example.warehousebot.R.id.material_title;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModuleContent extends AppCompatActivity implements RecyclerVIewInterface {

    RecyclerView recyclerView;
    ArrayList<LecMaterials> lecMaterialsArrayList;
    AdapterMaterials adapterMaterials;
    FirebaseFirestore db;
    Button buttonmaterialSubmit;
    private EditText title, deadline, extraDetails, link;
    ProgressDialog progressDialog;
    private FloatingActionButton btnAddMaterials;
    private TextView txtModuleCode, txtModuleName;
    String moduleCode, moduleName;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_content);
        btnAddMaterials = (FloatingActionButton) findViewById(R.id.btn_add_materials);
        db = FirebaseFirestore.getInstance();

        if (LecturerData.isIsLecturer()) {
            btnAddMaterials.setVisibility(View.VISIBLE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please Enter Details");

        View view = getLayoutInflater().inflate(R.layout.material_dialogbox, null);

        title = view.findViewById(R.id.material_title);
        deadline = view.findViewById(R.id.material_details);
        extraDetails = view.findViewById(R.id.material_extra_details);
        link = view.findViewById(R.id.material_link);
        buttonmaterialSubmit = view.findViewById(R.id.material_submit);

        builder.setView(view);
        dialog = builder.create();
        buttonmaterialSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (title.getText().toString().isEmpty()) {
                    title.setError("title cannot be empty");
                    return;
                } else if (deadline.getText().toString().isEmpty()) {
                    deadline.setError("Deadline cannot be empty");
                    return;
                } else if (extraDetails.getText().toString().isEmpty()) {
                    extraDetails.setError("Details cannot be empty");
                    return;
                } else if (link.getText().toString().isEmpty()) {
                    link.setError("link cannot be empty");
                    return;
                } else {

                    Map<String, Object> data = new HashMap<>();
                    data.put("materialName", title.getText().toString());
                    data.put("materialDetails", deadline.getText().toString());
                    data.put("materialLink", link.getText().toString());
                    data.put("materialExtraDetails", extraDetails.getText().toString());

                    db.collection("modules").document(moduleCode).collection("materials")
                            .document(title.getText().toString()).set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    Toast.makeText(ModuleContent.this, "Added Successfully", Toast.LENGTH_SHORT).show();
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

        btnAddMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewItems);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            moduleCode = extras.getString("ID");
            moduleName = extras.getString("moduleName");
        }

        txtModuleCode = findViewById(R.id.UIModuleCode);
        txtModuleName = findViewById(R.id.UIModuleName);
        txtModuleCode.setText(moduleCode);
        txtModuleName.setText(moduleName);

        db = FirebaseFirestore.getInstance();
        lecMaterialsArrayList = new ArrayList<LecMaterials>();
        adapterMaterials = new AdapterMaterials(this, lecMaterialsArrayList, this);

        eventChangesListener();

        recyclerView.setAdapter(adapterMaterials);

    }

    private void eventChangesListener() {
        db.collection("modules").document(moduleCode).collection("materials").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        lecMaterialsArrayList.add(dc.getDocument().toObject(LecMaterials.class));
                        Log.d("detailsF", "data reading");
                    }
                    adapterMaterials.notifyDataSetChanged();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }

            }

        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getApplicationContext(), MaterialItemContent.class);
        String title = lecMaterialsArrayList.get(position).getMaterialName();
        String details = lecMaterialsArrayList.get(position).getMaterialDetails();
        String link = lecMaterialsArrayList.get(position).getMaterialLink();
        String extraDetails = lecMaterialsArrayList.get(position).getMaterialExtraDetails();
        intent.putExtra("title", title);
        intent.putExtra("details", details);
        intent.putExtra("link", link);
        intent.putExtra("extraDetails", extraDetails);
        intent.putExtra("moduleCode",moduleCode);
        startActivity(intent);
    }
}