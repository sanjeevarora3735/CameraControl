package com.example.cameracontrol;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.slider.Slider;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CameraActivity extends AppCompatActivity {
    Button TakePicture;
    PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    private ImageCapture imageCapture;
    private Slider slider;
    private Camera camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        TakePicture = findViewById(R.id.TakePicture);
        slider = findViewById(R.id.sliderExposure);
        previewView = findViewById(R.id.PreviewView);
        TakePicture.setOnClickListener(v -> {
            capturePhoto();
        });
        slider.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                int ExposureValue = (int) slider.getValue();
                Toast.makeText(CameraActivity.this, String.valueOf(ExposureValue), Toast.LENGTH_SHORT).show();
                SetupCameraExposure(camera, slider);
            }
        });



        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderListenableFuture.addListener(() -> {
            try {
                ProcessCameraProvider processCameraProvider = cameraProviderListenableFuture.get();
                startCamerax(processCameraProvider);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, getExecutor());

    }

    @SuppressLint("UnsafeOptInUsageError")
    private void SetupCameraExposure(Camera camera, Slider slider) {

        int value = (int) slider.getValue();
        try {
            Log.d("InternshipProjectErrorExposureValue",camera.getCameraInfo().getExposureState().getExposureCompensationRange().getLower().toString());
            Log.d("InternshipProjectErrorExposureValue",camera.getCameraInfo().getExposureState().getExposureCompensationRange().getUpper().toString());
            camera.getCameraControl().setExposureCompensationIndex(value);
        }
        catch (Exception e){
            Log.d("InternshipProjectError",e.getMessage());
        }

    }

    private void capturePhoto() {
//        File photoDIR = new File("/CameraxPhotos");
//        if(!photoDIR.exists()){
//            photoDIR.mkdir();
//        }


//        Date date = new Date();
//        String TimeStamp = String.valueOf(date.getTime());
//        String PhotoFilePath = photoDIR.getAbsolutePath() + "/" + TimeStamp +".jpg";
//        File PhotoFile = new File(PhotoFilePath);

        long TimeStamp = System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME,TimeStamp);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"image/jpeg");

        imageCapture.takePicture(new ImageCapture.OutputFileOptions.Builder(
                getContentResolver(),MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues
        ).build(),getExecutor(),new ImageCapture.OnImageSavedCallback(){
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                Toast.makeText(CameraActivity.this, "Photo Has Been Saved Succesfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.d("InternshipProjectError",exception.getMessage());
                Toast.makeText(CameraActivity.this,  "Error Occurred : " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }


    private void startCamerax(ProcessCameraProvider processCameraProvider) {
        processCameraProvider.unbindAll();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());
        imageCapture = new ImageCapture.Builder()
                .setFlashMode(ImageCapture.FLASH_MODE_AUTO)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build();
        camera = processCameraProvider.bindToLifecycle((LifecycleOwner) this,cameraSelector,preview,imageCapture);


    }
}