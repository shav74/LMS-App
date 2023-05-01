package com.example.warehousebot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class ModuleContent extends AppCompatActivity {

    private TextView txtModuleCode;
    String moduleCode,moduleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_module_content);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            moduleCode = extras.getString("ID");
            moduleName = extras.getString("moduleName");
            //hello this is the new comment
        }

        txtModuleCode = findViewById(R.id.UIModuleCode);
        txtModuleCode.setText(moduleCode);
    }
}