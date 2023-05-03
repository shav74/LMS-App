package com.example.warehousebot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MaterialItemContent extends AppCompatActivity {

    String title, details, link, extraDetails;

    TextView textViewTitle, textViewDetails, textViewLink, textViewExtras;

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
        }

        textViewTitle = findViewById(R.id.txt_mt_name);
        textViewDetails = findViewById(R.id.txt_mt_details);
        textViewExtras = findViewById(R.id.txt_mt_extras);
        textViewLink = findViewById(R.id.txt_mt_link);

        textViewTitle.setText(title);
        textViewDetails.setText(details);
        textViewExtras.setText(extraDetails);
        textViewLink.setText(link);
    }
}