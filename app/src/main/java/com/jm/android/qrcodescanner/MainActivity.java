package com.jm.android.qrcodescanner;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;


public class MainActivity extends AppCompatActivity {
    DatabaseHelper qrDB;
    Button scanQRCodeBtn , getAllDataBtn;
    TextView qrCodeData;

    public static final int REQUEST_CODE = 1;

    public static final int PERMISSION_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanQRCodeBtn = findViewById(R.id.qrcodescanbtn);
        qrCodeData = findViewById(R.id.qrcodedata);
        getAllDataBtn = findViewById(R.id.retrivedata);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }
        scanQRCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        getAllDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DisplayDbData.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                final Barcode barcode = data.getParcelableExtra("QRCODE");
                qrCodeData.post(new Runnable() {
                    @Override
                    public void run() {
                        qrCodeData.setText("Scanned Data:- \n"+barcode.displayValue);
                        Toast.makeText(MainActivity.this, "QR Code Scanned", Toast.LENGTH_SHORT).show();
                        String data = barcode.displayValue.toString();
                        addtoDB(data);
                        Log.i("DB","DATA SENT");
                        Log.i("DAtaValue",data);
                    }
                });
            }
        }
    }
    public void addtoDB(String ReceivedData){
        qrDB = new DatabaseHelper(this);
        Log.i("DB","DATA LOGGED");
        Log.d("RECEVIED DATA",ReceivedData);




        Boolean result = qrDB.insertData(ReceivedData);
        if (result == true){
            Toast.makeText(getApplicationContext(),"Inserted :"+ ReceivedData,Toast.LENGTH_SHORT).show();

            Log.i("New IP Entry",ReceivedData);
            // Log.i("Time of new Entry",UPDATEDTIME);
        }else {
            Log.i("FAIL","At addtoDB");
        }
    }


}
