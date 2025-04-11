package com.example.carspa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class SaleItemAdapter extends RecyclerView.Adapter<SaleItemAdapter.ViewHolder> {
    private List<SaleItem> saleItemList;

    public SaleItemAdapter(List<SaleItem> saleItemList) {
        this.saleItemList = saleItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SaleItem item = saleItemList.get(position);
        holder.itemName.setText(item.getItemName());
        holder.itemQuantity.setText("Qty: " + item.getQuantity());
        holder.itemRate.setText("Rate: ₹" + item.getRate());
    }

    @Override
    public int getItemCount() {
        return saleItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, itemRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemRate = itemView.findViewById(R.id.itemRate);
        }
    }
}
