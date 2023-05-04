package com.example.warehousebot;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends Fragment implements RecyclerVIewInterface {

    RecyclerView recyclerView;
    ArrayList<Modules> modulesArrayList;
    AdapterModules adapterModules;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    List<String> teachingModules;
    List<String> studentModules;
    List<String> showingModules;

    String degreeName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        progressDialog = new ProgressDialog(container.getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();

        db = FirebaseFirestore.getInstance();
        modulesArrayList = new ArrayList<Modules>();
        adapterModules = new AdapterModules(getActivity(), modulesArrayList, this);

        recyclerView.setAdapter(adapterModules);

        if (LecturerData.isIsLecturer()) {
            showingModules = LecturerData.getTeachingModules();
        } else {
            showingModules = StudentData.getModules();
        }

        eventChangesListener();
        return view;
    }

    private void eventChangesListener() {
        if(showingModules != null){
            db.collection("modules").whereIn("moduleCode", showingModules)
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
                                    modulesArrayList.add(dc.getDocument().toObject(Modules.class));
                                }
                                adapterModules.notifyDataSetChanged();
                                if (progressDialog.isShowing()) {
                                    progressDialog.dismiss();
                                }
                            }

                        }

                    });
        }

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(getActivity(), ModuleContent.class);
        String moduleCode = modulesArrayList.get(position).getModuleCode();
        String moduleName = modulesArrayList.get(position).getName();
        intent.putExtra("ID", moduleCode);
        intent.putExtra("moduleName", moduleName);
        startActivity(intent);
    }
}
