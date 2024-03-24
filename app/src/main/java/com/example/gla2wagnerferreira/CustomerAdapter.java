package com.example.gla2wagnerferreira;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.ViewHolder> {
    private final List<Customer> customerList;

    public CustomerAdapter(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView text_name;
        private final TextView text_birthday;
        private final TextView text_phone;
        private final TextView text_email;
        private final TextView text_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text_name = itemView.findViewById(R.id.text_Name);
            text_birthday = itemView.findViewById(R.id.text_Birthday);
            text_phone = itemView.findViewById(R.id.text_Phone);
            text_email = itemView.findViewById(R.id.text_Email);
            text_delete = itemView.findViewById(R.id.text_Delete);
        }
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        String name = customerList.get(position).getFirstName() + " " + customerList.get(position).getLastName();
        holder.text_name.setText(name);
        holder.text_birthday.setText(customerList.get(position).getBirthday());
        holder.text_phone.setText(customerList.get(position).getPhoneNumber());
        holder.text_email.setText(customerList.get(position).getEmail());
        holder.text_delete.setOnClickListener(v -> deleteItem(v.getParent()));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteItem(ViewParent v) {

        Customer customerToRemove = new Customer("", "", "", "", "");

        View emailView = ((View) v).findViewById(R.id.text_Email);
        String email = ((TextView) emailView).getText().toString();

        for (Customer customer : customerList) {
            if (Objects.equals(customer.email, email)) {
                customerToRemove = customer;
            }
        }
        customerList.remove(customerToRemove);

        Context context = emailView.getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                "com.example.gla2wagnerferreira.PREFERENCE_FILE_KEY", Context.MODE_PRIVATE);
        String json = new Gson().toJson(customerList);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("listusers", json);
        editor.apply();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }
}