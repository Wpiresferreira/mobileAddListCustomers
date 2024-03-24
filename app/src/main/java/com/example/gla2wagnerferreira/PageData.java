package com.example.gla2wagnerferreira;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PageData extends AppCompatActivity {


    private RecyclerView recycler;
    private List<Customer> customerDataList;
    //private ActivityMainBinding binding;
    CustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_page_data);
        customerDataList = new ArrayList<>();
        recycler = findViewById(R.id.recycler);

    loadCustomers();
    setAdapter();
    }

    private void setAdapter() {

        adapter = new CustomerAdapter(customerDataList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(adapter);
    }


    private void loadCustomers() {

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Type listType = new TypeToken<ArrayList<Customer>>(){}.getType();
        Gson gson = new Gson();
        String json =sharedPref.getString("listusers","");

        customerDataList = gson.fromJson(json, listType);
        if (customerDataList == null){
            customerDataList = new ArrayList<>();
        }
    }

    public void goMain(View view) {
        Intent myIntent = new Intent(this, MainActivity.class);
        this.startActivity(myIntent);
    }
}