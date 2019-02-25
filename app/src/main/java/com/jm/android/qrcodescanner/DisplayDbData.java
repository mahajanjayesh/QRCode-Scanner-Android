package com.jm.android.qrcodescanner;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

public class DisplayDbData extends Activity {
    DatabaseHelper qrDB;
    TextView dataDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayalldata);
        qrDB = new DatabaseHelper(this);
        Cursor res = qrDB.getAllData();
        StringBuffer stringBuffer = new StringBuffer();
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                stringBuffer.append("\n "+res.getString(0));
                stringBuffer.append("  "+res.getString(1)+"\n ");

            }
            dataDisplay = findViewById(R.id.textView3);
            dataDisplay.setText(stringBuffer.toString());
            Log.i("DataDisplay", stringBuffer.toString());
            dataDisplay.setMovementMethod(new ScrollingMovementMethod());
        }
    }
}
