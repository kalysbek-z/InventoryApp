package com.example.inventoryapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inventoryapp.data.Product;
import com.example.inventoryapp.data.ProductDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> dataList = new ArrayList<>();
    private Activity context;
    private ProductDatabase database;

    private OnItemClickListener listener;

    public ProductAdapter(Activity context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = dataList.get(position);
        database = ProductDatabase.getInstance(context);

        if (dataList != null) {
            holder.Name.setText(product.getName());
            holder.Supplier.setText(product.getSupplier());
            holder.Quantity.setText(product.getQuantity());
            holder.Price.setText(product.getPrice());

            Glide.with(context).load(product.getImageUri()).into(holder.Photo);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setDataList(List<Product> products) {
        this.dataList = products;
        notifyDataSetChanged();
    }

    public Product getProductAt(int pos) {
        return dataList.get(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Photo;
        TextView Name;
        TextView Supplier;
        TextView Quantity;
        TextView Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Photo = itemView.findViewById(R.id.photo);
            Name = itemView.findViewById(R.id.name);
            Supplier = itemView.findViewById(R.id.supplier);
            Quantity = itemView.findViewById(R.id.quantity);
            Price = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (listener != null && pos != RecyclerView.NO_POSITION) {
                        listener.onItemClick(dataList.get(pos));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
