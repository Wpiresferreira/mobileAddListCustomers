package com.example.gla2wagnerferreira;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText firstName, lastName, birthday, phoneNumber, email;
    private List<Customer> customerDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialize();
    }

    private void initialize() {

        firstName = findViewById(R.id.edit_FirstName);
        lastName = findViewById(R.id.edit_LastName);
        birthday = findViewById(R.id.edit_Birthday);
        phoneNumber = findViewById(R.id.edit_PhoneNumber);
        email = findViewById(R.id.edit_Email);
    }

    public void signup(View view) {

        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Customer customer = new Customer(firstName.getText().toString(), lastName.getText().toString(),
                birthday.getText().toString(), phoneNumber.getText().toString(), email.getText().toString());
        Type listType = new TypeToken<ArrayList<Customer>>(){}.getType();



        Gson gson = new Gson();
        String json =sharedPref.getString("listusers","");
        customerDataList = gson.fromJson(json, listType);

        if (customerDataList == null){
            customerDataList = new ArrayList<>();
        }
        customerDataList.add(customer);

        json = gson.toJson(customerDataList);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("listusers", json);
        editor.apply();
        goPageData(view);
    }

    public void goPageData(View view) {

        Intent myIntent = new Intent(this, PageData.class);
        this.startActivity(myIntent);

    }
}