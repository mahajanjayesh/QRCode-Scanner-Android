package com.jm.android.qrcodescanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    SurfaceView cameraViewQR;

    BarcodeDetector barcodeQR;

    CameraSource cameraSourcePhone;

    SurfaceHolder qrHolderScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);


        cameraViewQR = findViewById(R.id.cameraView);
        cameraViewQR.setZOrderMediaOverlay(true);
        qrHolderScreen = cameraViewQR.getHolder();
        barcodeQR = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();
        if (!barcodeQR.isOperational()) {
            Toast.makeText(getApplicationContext(), "Problem Occurred in Setup", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        cameraSourcePhone = new CameraSource.Builder(this, barcodeQR)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setRequestedFps(24)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(480, 480)
                .build();
        cameraViewQR.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSourcePhone.start(cameraViewQR.getHolder());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        barcodeQR.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcode = detections.getDetectedItems();
                if (barcode.size() > 0) {
                    Intent intent = new Intent();
                    intent.putExtra("QRCODE", barcode.valueAt(0));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
