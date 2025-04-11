package com.example.carspa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BillListActivity extends AppCompatActivity {
    private RecyclerView billRecyclerView;
    private BillAdapter billAdapter;
    private List<BillModal> billList;
    private DatabaseReference saleRef;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_list);

        billRecyclerView = findViewById(R.id.billRecyclerView);
        progressBar = findViewById(R.id.progressBar);
        billRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        billList = new ArrayList<>();
        billAdapter = new BillAdapter(billList, this::openSaleActivity);
        billRecyclerView.setAdapter(billAdapter);

        saleRef = FirebaseDatabase.getInstance().getReference("Sales");
        fetchBills();
    }

    private void fetchBills() {
        progressBar.setVisibility(View.VISIBLE);
        saleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                billList.clear();
                for (DataSnapshot billSnapshot : snapshot.getChildren()) {
                    BillModal bill = billSnapshot.getValue(BillModal.class);
                    if (bill != null) {
                        bill.setSaleId(billSnapshot.getKey()); // Set saleId
                        billList.add(bill);
                    }
                }
                Collections.reverse(billList);
                billAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BillListActivity.this, "Error fetching bills", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void openSaleActivity(BillModal bill) {
        Intent intent = new Intent(this, SaleActivity.class);
        intent.putExtra("saleId", bill.getSaleId());
        startActivity(intent);
    }
}
