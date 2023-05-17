package com.example.warehousebot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ExamMarks extends AppCompatActivity {

    TextView mark1, module1, mark2, module2, mark3, module3, mark4, module4, mark5, module5, mark6, module6;
    FirebaseFirestore db;
    List<String> marksDB, modulesDB;
    String examID;
    TextView[] modules;
    TextView[] marks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_marks);

        Log.d("dberr", "onCreate: im in mehe");

        db = FirebaseFirestore.getInstance();

        mark1 = findViewById(R.id.mark1);
        mark2 = findViewById(R.id.mark2);
        mark3 = findViewById(R.id.mark3);
        mark4 = findViewById(R.id.mark4);
        mark5 = findViewById(R.id.mark5);
        mark6 = findViewById(R.id.mark6);

        module1 = findViewById(R.id.module1);
        module2 = findViewById(R.id.module2);
        module3 = findViewById(R.id.module3);
        module4 = findViewById(R.id.module4);
        module5 = findViewById(R.id.module5);
        module6 = findViewById(R.id.module6);

        marks = new TextView[]{mark1, mark2, mark3, mark4, mark5, mark6};
        modules = new TextView[]{module1, module2, module3, module4, module5};

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            examID = extras.getString("ID");
        }

        getDataFromDb();

        Log.d("dberr", "onCreate: meka run una");
    }

    public void getDataFromDb() {
        Log.d("dberr", "getStudentModules: athule inne");
        db.collection("Student")
                .document(StudentData.getEmail().substring(0, StudentData.getEmail().indexOf('@')))
                .collection("exams").document(examID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                marksDB = (List<String>) document.get("marks");
                                modulesDB = (List<String>) document.get("moduleNames");
                                for (int i = 0; i < modulesDB.size(); i++) {
                                    modules[i].setText(modulesDB.get(i));
                                }

                                for (int i = 0; i < marksDB.size(); i++) {
                                    marks[i].setText(marksDB.get(i));
                                }
                                Log.d("dberr", "onComplete: done done");
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