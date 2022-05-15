package com.example.cameracontrol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //    Camera camera;
//    FrameLayout frameLayout;
//    ShowCamera showCamera;
    Button OpenCamera;
    private static final int REQUEST_CODE = 1;
    private boolean PermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        frameLayout = findViewById(R.id.CameraFramLayout);
//        //Open The Camera
//        camera = Camera.open();
//
//        showCamera = new ShowCamera(this,camera);
//        frameLayout.addView(showCamera);

        verifyPermissions();
        OpenCamera = findViewById(R.id.OpenCamera);
        OpenCamera.setOnClickListener(v -> {

            if(PermissionGranted)
            startActivity(new Intent(MainActivity.this, CameraActivity.class));
        });

    }

    private void verifyPermissions() {
        Log.d("InternshipProjectError", "Verifying The Permissions");
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED){
            Log.d("InternshipProjectError","READ_EXTERNAL_STORAGE");
        }
                if( ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED){
                    Log.d("InternshipProjectError","WRITE_EXTERNAL_STORAGE");

                }
                if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[2]) == PackageManager.PERMISSION_GRANTED){
            PermissionGranted = true;
            Log.d("InternshipProjectError", "Verified The Permissions");

            //Permissions Are Granted
        }else {
            Log.d("InternshipProjectError","Permissions Are Requested");
            ActivityCompat.requestPermissions(MainActivity.this,permissions,REQUEST_CODE);

        }
    }

}