package com.smartherd.qrscanner;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.smartherd.qrscanner.databinding.ActivityMainBinding;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FloatingActionButton generate_qr_code;
    private BottomAppBar bottomAppBar;
    private SharedPreferences sharedPreferences;

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
               if(isGranted){
                    showCamera();
               }else {
                   //Show why the user needs this permission
               }
            });

    private ActivityResultLauncher<ScanOptions> qrCodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() == null){
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        }else{
            setResult(result.getContents());
        }
    });

    private void setResult(String contents) {
        if (isUrl(contents)) {
            // Format the text as a clickable link
            Spannable spannable = new SpannableString(contents);
            spannable.setSpan(new URLSpan(contents), 0, contents.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Set the formatted text to the TextView
            binding.textResult.setText(spannable);

            // Make the link clickable
            binding.textResult.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            // Set the text as is if it's not a URL
            binding.textResult.setText(contents);
            // Add the scanned QR code to the DisplayQRCodeActivity
        }
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        String inputText = (String) binding.textResult.getText();

        // Retrieve existing set of items
        Set<String> itemList = sharedPreferences.getStringSet("ITEM_LIST", new HashSet<String>());

        // Add the new item
        itemList.add(inputText);

        // Save the updated set of items
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("ITEM_LIST", itemList);
        editor.apply();
    }

    private boolean isUrl(String contents) {
        Pattern pattern = Patterns.WEB_URL;
        Matcher matcher = pattern.matcher(contents);
        return matcher.matches();
    }

    private void showCamera() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Set QR Code");
        options.setCameraId(0);
        options.setBeepEnabled(false);
        options.setBarcodeImageEnabled(true);
        options.setOrientationLocked(false);

        qrCodeLauncher.launch(options);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        initViews();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initViews() {
        binding.fab.setOnClickListener(view -> {
            checkPermissionAndShowActivity(this);
        });

        binding.generateQrCode.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, GenerateQRCode.class);
            startActivity(intent);
        });
        binding.fab1.setOnClickListener(view ->{
            Intent intent = new Intent(MainActivity.this, DisplayQRCodeActivity.class);
            startActivity(intent);
        });
    }

    @SuppressLint("NewApi")
    private void checkPermissionAndShowActivity(Context context) {
        if(ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
        ) ==PackageManager.PERMISSION_GRANTED) {
            showCamera();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {

            Toast.makeText(context, "Camera Permission Required", Toast.LENGTH_SHORT).show();

        }else {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void initBinding() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}