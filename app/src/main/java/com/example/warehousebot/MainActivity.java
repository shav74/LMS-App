package com.example.warehousebot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.warehousebot.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    Button buttonLogout;
    FloatingActionButton btn_home;
    TextView textViewUserDetail;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new Home());
        binding.bottomNavigationView.setBackground(null);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.menu_calendar:
                    replaceFragment(new Calender());
                    break;
                case  R.id.btn_home:
                    replaceFragment(new Home());
                    break;
                case R.id.menu_test:
                    replaceFragment(new Marks());
                    break;
                case R.id.menu_settings:
                    replaceFragment(new Settings());
                    break;
                case R.id.menu_more:
                    replaceFragment(new More());
                    break;
            }

            return true;

        });



        auth = FirebaseAuth.getInstance();
        buttonLogout = findViewById(R.id.btn_logout);
        textViewUserDetail = findViewById(R.id.user_details);
        user = auth.getCurrentUser();
        btn_home = findViewById(R.id.btn_home);

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else {
            String fullEmail = user.getEmail();
            String nameOfUser = fullEmail.substring(0,fullEmail.indexOf('@'));
            textViewUserDetail.setText(String.format("Hello %s", nameOfUser));
        }

        buttonLogout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Home());
                binding.bottomNavigationView.setSelectedItemId(R.id.menu_home);
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}