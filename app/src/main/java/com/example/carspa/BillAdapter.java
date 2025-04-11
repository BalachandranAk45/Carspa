package com.example.carspa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillViewHolder> {
    private final List<BillModal> billList;
    private final OnBillClickListener listener;

    public interface OnBillClickListener {
        void onBillClick(BillModal bill);
    }

    public BillAdapter(List<BillModal> billList, OnBillClickListener listener) {
        this.billList = billList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bill, parent, false);
        return new BillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillViewHolder holder, int position) {
        BillModal bill = billList.get(position);
        holder.customerName.setText("Name: " + bill.getCustomerName());
        holder.customerPhone.setText("Mobile: " + bill.getCustomerPhone());
        holder.billNo.setText("Bill No: " + bill.getBillNo());
        holder.billDate.setText("Date: " + bill.getBillDate());
        holder.totalAmount.setText("Total: ₹" + bill.getTotalAmount());

        holder.itemView.setOnClickListener(v -> listener.onBillClick(bill));
    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    static class BillViewHolder extends RecyclerView.ViewHolder {
        TextView billNo, billDate, totalAmount,customerName,customerPhone;

        public BillViewHolder(@NonNull View itemView) {
            super(itemView);
            billNo = itemView.findViewById(R.id.billNo);
            billDate = itemView.findViewById(R.id.billDate);
            totalAmount = itemView.findViewById(R.id.totalsAmount);
            customerName = itemView.findViewById(R.id.customerName);
            customerPhone = itemView.findViewById(R.id.customerPhone);
        }
    }
}
