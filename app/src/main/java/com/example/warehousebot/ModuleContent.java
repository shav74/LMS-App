package com.example.warehousebot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ModuleContent extends AppCompatActivity implements RecyclerVIewInterface {

    RecyclerView recyclerView;
    ArrayList<LecMaterials> lecMaterialsArrayList;
    AdapterMaterials adapterMaterials;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    private TextView txtModuleCode, txtModuleName;
    String moduleCode, moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_content);

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
        //todo declare the correct path here date ekath danna
        db.collection("modules").document(moduleCode).collection("materials")
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
        intent.putExtra("extraDetails",extraDetails);
        startActivity(intent);
    }
}