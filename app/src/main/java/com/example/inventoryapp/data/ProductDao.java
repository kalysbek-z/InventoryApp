package com.example.inventoryapp.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addProduct(Product product);

    @Update
    void update(Product product);

    @Query("DELETE FROM product_data WHERE id = :id")
    void delete(int id);

    @Query("DELETE FROM product_data")
    void deleteAllData();

    @Query("SELECT * FROM product_data ORDER BY id ASC")
    LiveData<List<Product>> readAllData();
}
