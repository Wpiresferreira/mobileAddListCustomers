package com.example.gla2wagnerferreira;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText firstName, lastName, birthday, phoneNumber, email;
    DatePickerDialog datePickerDialog;
    TextView text_Validate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initialize();
    }

    private void initialize() {

        firstName = findViewById(R.id.edit_FirstName);
        lastName = findViewById(R.id.edit_LastName);
        birthday = findViewById(R.id.edit_Birthday);
        phoneNumber = findViewById(R.id.edit_PhoneNumber);
        email = findViewById(R.id.edit_Email);
        text_Validate = findViewById(R.id.text_Validate);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                StringBuilder inputNumbers = new StringBuilder();
                for(String str : s.toString().split("")) if (isNumber(str)) inputNumbers.append(str);

                StringBuilder output = new StringBuilder();
                String mask = "+_ (___) ___-____";

                int index = 0;

                for (String str : mask.split("")){
                    if (str.equals("_") && index < inputNumbers.length()) {
                        output.append(inputNumbers.charAt(index));
                        index++;
                    }else{
                        output.append(str);
                    }
                }

                int posCursor = 0;

                for (int i = 0; i < output.length(); i++){
                    if (isNumber(String.valueOf(output.charAt(i)))){
                        posCursor = i;
                    }
                }

                if(!output.toString().equals(s.toString())){
                    phoneNumber.setText(output.toString());
                    phoneNumber.setSelection(posCursor+1);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        birthday.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            datePickerDialog = new DatePickerDialog(MainActivity.this, R.style.MySpinnerDatePickerStyle,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        // set day of month , month and year value in the edit text
                        String displayedText = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        birthday.setText(displayedText);

                    }, mYear, mMonth, mDay);
            datePickerDialog.show();


        });   // calender class's instance and get current date , month and year from calender

    }

    private boolean isNumber(String str) {

        try {
            Integer.parseInt(str);
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public void signup(View view) {
        if (!validateFields()) {
            return;
        }
        Context context = this;
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Customer newCustomer = new Customer(firstName.getText().toString(), lastName.getText().toString(),
                birthday.getText().toString(), phoneNumber.getText().toString(), email.getText().toString());
        Type listType = new TypeToken<ArrayList<Customer>>() {
        }.getType();


        Gson gson = new Gson();
        String json = sharedPref.getString("listusers", "");
        List<Customer> customerDataList = gson.fromJson(json, listType);

        if (customerDataList == null) {
            customerDataList = new ArrayList<>();
        }

        //Make sure there is no Customer with the same email.
        for (Customer c : customerDataList){
            if (c.email.equals(newCustomer.email)){
                text_Validate.setText(R.string.this_email_is_already_registered);
                return;
            }
        }

        customerDataList.add(newCustomer);

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

    private boolean validateFields() {

        if (firstName.getText().length() < 2) {
            text_Validate.setText(R.string.error_too_short);
            firstName.requestFocus();
            return false;
        }
        if (lastName.getText().length() < 2) {
            text_Validate.setText(R.string.error_too_short);
            lastName.requestFocus();
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!isValidDate(birthday.getText().toString())) {
                text_Validate.setText(R.string.error_invalid_date);
                birthday.requestFocus();
                return false;
            }
        }

        if (!isValidPhone(phoneNumber.getText().toString())){
            text_Validate.setText(R.string.invalid_phone_number);
            phoneNumber.requestFocus();
            return false;
        }


        if (!isValidEmail(email.getText().toString())) {
            text_Validate.setText(R.string.invalid_email);
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String string) {
        if(string.length()<8){
            return false;
        }

        if(!string.substring(2).contains("@")){
            return false;
        }

        return string.substring(string.indexOf("@")).contains(".");
    }

    private boolean isValidPhone(String string) {

    return !string.contains("_");

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isValidDate(String birthday) {


        try {
            String[] dateSeparated = birthday.split("/");
            String dd = ("0"+dateSeparated[0]).substring(("0"+dateSeparated[0]).length()-2);
            String MM = ("0"+dateSeparated[1]).substring(("0"+dateSeparated[1]).length()-2);
            String yyyy = dateSeparated[2];

            String dateToValidate = yyyy+"-"+MM+"-"+dd;
            LocalDate.parse(dateToValidate);
            return true;
        } catch (Exception e) {
            Log.d("IsValidDate", "isValidDate: "+ e);
            return false;
        }
    }
}