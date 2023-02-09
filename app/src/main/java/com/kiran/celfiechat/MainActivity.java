package com.kiran.celfiechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.kiran.celfiechat.Models.User;
import com.kiran.celfiechat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    String [] permissions = new String[] {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    private int requestCode = 2;
    User user;
    KProgressHUD progressHUD;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressHUD = KProgressHUD.create(this);
        progressHUD.setDimAmount(0.5f);
        progressHUD.show();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();

      database.getReference().child("profiles")
              .child(currentUser.getUid())
              .addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      progressHUD.dismiss();
                       user = snapshot.getValue(User.class);

                      Glide.with(MainActivity.this)
                              .load(user.getProfile())
                              .into(binding.profileImage);

                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });

      binding.settings.setOnClickListener(v -> {
          startActivity(new Intent(MainActivity.this, SettingActivity.class));
      });


     binding.searchBtn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if ( isPermissionsGranted()
             ) {
                 Intent intent = new Intent(MainActivity.this, connectingActivity.class);
                 intent.putExtra("profile", user.getProfile());
                 startActivity(intent);
             }
             else {
                 askPermissions();
             }

         }
     });


    }

    void askPermissions(){
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }

    private boolean isPermissionsGranted() {
        for(String permission : permissions ){
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }

        return true;
    }

}