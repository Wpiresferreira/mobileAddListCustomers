package com.example.gla2wagnerferreira;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

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

    View selectedView;

    public class ViewHolder extends RecyclerView.ViewHolder {

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
            text_delete = itemView.findViewById(R.id.text_Detele);
        }
    }

    @NonNull
    @Override
    public CustomerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail, parent, false);
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                selectedView = v;
//                v.setBackgroundColor(Color.YELLOW);
//                TextView testing = v.findViewById(R.id.text_Email);
//                String newText = testing.getText().toString();
//                Toast.makeText(v.getContext(), newText, Toast.LENGTH_SHORT).show();
//            }
//        });

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.ViewHolder holder, int position) {
        String name = customerList.get(position).getFirstName() + " " + customerList.get(position).getLastName();
        holder.text_name.setText(name);
        holder.text_birthday.setText(customerList.get(position).getBirthday());
        holder.text_phone.setText(customerList.get(position).getPhoneNumber());
        holder.text_email.setText(customerList.get(position).getEmail());
        holder.text_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(v.getParent());
            }
        });
    }

    private void deleteItem(ViewParent v) {

        Customer customerToRemove = new Customer("","","","","");

        View emailView = ((View)v).findViewById(R.id.text_Email);
        String email = ((TextView)emailView).getText().toString();

        for(Customer customer : customerList){
            if(Objects.equals(customer.email, email)){
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


//
//    private List<Customer> userDataList;
//    public CustomerAdapter(List<Customer> userDataList) {
//
//        this.userDataList = userDataList;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail, parent, false);
//         return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Customer customer = userDataList.get(position);
//        holder.viewLastName.setText(customer.getLastName());
//        holder.viewFirstName.setText(String.valueOf(customer.getFirstName()));
//        holder.viewBirthday.setText(String.valueOf(customer.getBirthday()));
//        holder.viewPhone.setText(String.valueOf(customer.getPhoneNumber()));
//        holder.viewEmail.setText(String.valueOf(customer.getEmail()));
//    }
//
//    @Override
//    public int getItemCount() {
//        return userDataList.size();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        TextView viewLastName, viewFirstName, viewBirthday, viewPhone, viewEmail;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            viewLastName = itemView.findViewById(R.id.text_last_name);
//            viewFirstName = itemView.findViewById(R.id.text_first_name);
//            viewBirthday = itemView.findViewById(R.id.text_birthday);
//            viewPhone = itemView.findViewById(R.id.text_phone);
//            viewEmail = itemView.findViewById(R.id.text_email);
//
//        }
//
//    }
}
