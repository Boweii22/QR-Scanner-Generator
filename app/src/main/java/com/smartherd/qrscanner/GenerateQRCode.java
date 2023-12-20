package com.smartherd.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GenerateQRCode extends AppCompatActivity {

    private EditText qrcodeText;
    private Button generateButton;
    private BottomAppBar bottomAppBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_qrcode);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        qrcodeText = findViewById(R.id.qrcode_text);
        generateButton = findViewById(R.id.generate_qr_code);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });
    }

    private void generateQRCode() {
        String textToEncode = qrcodeText.getText().toString().trim();
        if (!textToEncode.isEmpty()) {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            try {
                // Generate QR Code from the text
                Bitmap bitmap = barcodeEncoder.encodeBitmap(textToEncode, BarcodeFormat.QR_CODE, 400, 400);

                // Show the QR Code in a BottomSheetDialogFragment
                QRCodeBottomSheetDialogFragment bottomSheetDialogFragment =
                        QRCodeBottomSheetDialogFragment.newInstance(bitmap);
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
        if(textToEncode.isEmpty()){
            Toast.makeText(this, "Enter text for QR Code", Toast.LENGTH_SHORT).show();
        }
    }
    }
