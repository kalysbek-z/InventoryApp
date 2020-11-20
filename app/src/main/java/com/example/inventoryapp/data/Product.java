package com.example.inventoryapp.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "product_data")
public class Product {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    int id;
    @ColumnInfo(name = "name")
    String name;
    @ColumnInfo(name = "image")
    String imageUri;
    @ColumnInfo(name = "supplier")
    String supplier;
    @ColumnInfo(name = "quantity")
    int quantity;
    @ColumnInfo(name = "price")
    String price;

    public Product(String name, String imageUri, String supplier, int quantity, String price) {
        this.name = name;
        this.imageUri = imageUri;
        this.supplier = supplier;
        this.quantity = quantity;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getQuantity() {
        return Integer.toString(quantity);
    }

    public String getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }
}
