package com.kiran.celfiechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kiran.celfiechat.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    ActivitySettingBinding binding;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();



        binding.share.setOnClickListener(v -> {
            String appUrl = "https://play.google.com/store/apps/details?id="+"com.kiran.celfiechat";

            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, appUrl);
            startActivity(Intent.createChooser(intent, getString(R.string.share_using)));
        });

        binding.logout.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(SettingActivity.this, signUpActivity.class));
            finishAffinity();

        });


    }
}