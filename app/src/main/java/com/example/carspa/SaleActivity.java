package com.example.carspa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class SaleActivity extends AppCompatActivity {

    private EditText billDate, billNo, customerPhone, customerName;
    private RecyclerView itemsRecyclerView;
    private Button addItemsButton, saveButton;

    private List<Item> itemList;
    private ItemAdapter itemAdapter;
    private DatabaseReference saleRef, saleItemRef;

    private String saleId = null;
    private double totalAmount = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        billDate = findViewById(R.id.billDate);
        billNo = findViewById(R.id.billNo);
        customerName = findViewById(R.id.customerName);
        customerPhone = findViewById(R.id.customerPhone);

        // Apply space restriction and single-line enforcement
        billNo.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }});
        billNo.setSingleLine(true);
        customerName.setSingleLine(true);
        customerPhone.setSingleLine(true);

        addItemsButton = findViewById(R.id.addItemsButton);
        saveButton = findViewById(R.id.saveButton);
        itemsRecyclerView = findViewById(R.id.itemsRecyclerView);

        saleRef = FirebaseDatabase.getInstance().getReference("Sales");
        saleItemRef = FirebaseDatabase.getInstance().getReference("SaleItems");

        itemList = new ArrayList<>();
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemAdapter = new ItemAdapter(itemList, this::showEditItemDialog);
        itemsRecyclerView.setAdapter(itemAdapter);

        saleId = getIntent().getStringExtra("saleId");
        if (saleId != null) {
            loadSaleDetails(saleId);
        }

        addItemsButton.setOnClickListener(view -> showAddItemDialog());
        saveButton.setOnClickListener(view -> saveSale());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        billDate.setText(dateFormat.format(calendar.getTime()));

        billDate.setOnClickListener(v -> {
            new DatePickerDialog(SaleActivity.this, (view, year, month, dayOfMonth) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                billDate.setText(dateFormat.format(selectedDate.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void loadSaleDetails(String saleId) {
        saleRef.child(saleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    billNo.setText(snapshot.child("billNo").getValue(String.class));
                    billDate.setText(snapshot.child("billDate").getValue(String.class));
                    customerName.setText(snapshot.child("customerName").getValue(String.class));
                    customerPhone.setText(snapshot.child("customerPhone").getValue(String.class));
                    totalAmount = snapshot.child("totalAmount").getValue(Double.class);
                    loadSaleItems(saleId);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SaleActivity.this, "Error loading sale details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadSaleItems(String saleId) {
        saleItemRef.orderByChild("saleId").equalTo(saleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    String name = itemSnapshot.child("itemName").getValue(String.class);
                    int quantity = itemSnapshot.child("quantity").getValue(Integer.class);
                    double rate = itemSnapshot.child("rate").getValue(Double.class);
                    itemList.add(new Item(name, quantity, rate));
                }
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SaleActivity.this, "Error loading items", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_modal, null);
        builder.setView(dialogView);

        EditText itemName = dialogView.findViewById(R.id.itemName);
        EditText itemQuantity = dialogView.findViewById(R.id.itemQuantity);
        EditText itemRate = dialogView.findViewById(R.id.itemRate);
        Button addItemButton = dialogView.findViewById(R.id.addItemButton);

        AlertDialog dialog = builder.create();
        dialog.show();

        addItemButton.setOnClickListener(view -> {
            String name = itemName.getText().toString().trim();
            String quantityStr = itemQuantity.getText().toString().trim();
            String rateStr = itemRate.getText().toString().trim();

            if (name.isEmpty() || quantityStr.isEmpty() || rateStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double rate = Double.parseDouble(rateStr);
            double total = quantity * rate;

            itemList.add(new Item(name, quantity, rate));
            totalAmount += total;

            itemAdapter.notifyDataSetChanged();
            dialog.dismiss();
        });
    }

    private void showEditItemDialog(int position) {
        Item item = itemList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.item_modal, null);
        builder.setView(dialogView);

        EditText itemName = dialogView.findViewById(R.id.itemName);
        EditText itemQuantity = dialogView.findViewById(R.id.itemQuantity);
        EditText itemRate = dialogView.findViewById(R.id.itemRate);
        Button addItemButton = dialogView.findViewById(R.id.addItemButton);
        addItemButton.setText("Update");

        itemName.setText(item.getName());
        itemQuantity.setText(String.valueOf(item.getQuantity()));
        itemRate.setText(String.valueOf(item.getRate()));

        AlertDialog dialog = builder.create();
        dialog.show();

        addItemButton.setOnClickListener(view -> {
            String name = itemName.getText().toString().trim();
            String quantityStr = itemQuantity.getText().toString().trim();
            String rateStr = itemRate.getText().toString().trim();

            if (name.isEmpty() || quantityStr.isEmpty() || rateStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            double rate = Double.parseDouble(rateStr);
            double total = quantity * rate;

            totalAmount -= item.getQuantity() * item.getRate();
            totalAmount += total;

            item.setName(name);
            item.setQuantity(quantity);
            item.setRate(rate);

            itemAdapter.notifyItemChanged(position);
            dialog.dismiss();
        });
    }

    private void saveSale() {
        String billNoStr = billNo.getText().toString().trim();
        String billDateStr = billDate.getText().toString().trim();
        String cusName = customerName.getText().toString().trim();
        String cusMob = customerPhone.getText().toString().trim();

        if (billNoStr.isEmpty() || billDateStr.isEmpty() || itemList.isEmpty()) {
            Toast.makeText(this, "Fill all details and add at least one item", Toast.LENGTH_SHORT).show();
            return;
        }

        if (saleId == null) {
            saleId = saleRef.push().getKey();
        }

        Map<String, Object> saleData = new HashMap<>();
        saleData.put("billNo", billNoStr);
        saleData.put("billDate", billDateStr);
        saleData.put("customerName", cusName);
        saleData.put("customerPhone", cusMob);
        saleData.put("totalAmount", totalAmount);

        saleRef.child(saleId).setValue(saleData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                saveSaleItems(saleId);
            } else {
                Toast.makeText(this, "Error saving sale", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSaleItems(String saleId) {
        saleItemRef.orderByChild("saleId").equalTo(saleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot : snapshot.getChildren()) {
                    itemSnapshot.getRef().removeValue();
                }

                for (Item item : itemList) {
                    String itemId = saleItemRef.push().getKey();
                    Map<String, Object> itemData = new HashMap<>();
                    itemData.put("saleId", saleId);
                    itemData.put("itemName", item.getName());
                    itemData.put("quantity", item.getQuantity());
                    itemData.put("rate", item.getRate());
                    itemData.put("total", item.getQuantity() * item.getRate());

                    saleItemRef.child(itemId).setValue(itemData);
                }

                Toast.makeText(SaleActivity.this, "Sale saved successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SaleActivity.this, BillListActivity.class));
                finish();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(SaleActivity.this, "Error saving sale items", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
