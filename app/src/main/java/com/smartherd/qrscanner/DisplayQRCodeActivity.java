package com.smartherd.qrscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DisplayQRCodeActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> itemListArray;

    private SharedPreferences sharedPreferences;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_qrcode);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listView = findViewById(R.id.listView);
        searchEditText = findViewById(R.id.search_bar);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Retrieve the set of items from SharedPreferences
        Set<String> itemList = sharedPreferences.getStringSet("ITEM_LIST", new HashSet<String>());

        // Convert the set to an ArrayList for the adapter
        itemListArray = new ArrayList<>(itemList);

        // Set up the adapter and attach it to the ListView
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemListArray);
        listView.setAdapter(adapter);

        // Set up the text change listener for search
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Filter the list based on the search query
                filterList(editable.toString());
            }
        });
    }

    private void filterList(String query) {
        ArrayList<String> filteredList = new ArrayList<>();

        for (String item : itemListArray) {
            if (item.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredList);
        listView.setAdapter(adapter);
    }
}
