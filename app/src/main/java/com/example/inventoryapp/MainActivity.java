package com.example.inventoryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.inventoryapp.data.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int ADD_DATA_REQUEST = 1;
    public static final int EDIT_DATA_REQUEST = 2;

    private final int UNDEFINED_ID_VALUE = -1;

    private ProductViewModel productViewModel;
    FloatingActionButton DataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataButton = findViewById(R.id.add_data);

        DataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivityForResult(intent, ADD_DATA_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ProductAdapter productAdapter = new ProductAdapter(this);
        recyclerView.setAdapter(productAdapter);

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.getAllProducts().observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                productAdapter.setDataList(products);
            }
        });

        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra(EditorActivity.EXTRA_ID, product.getId());
                intent.putExtra(EditorActivity.EXTRA_NAME, product.getName());
                intent.putExtra(EditorActivity.EXTRA_SUPPLIER, product.getSupplier());
                intent.putExtra(EditorActivity.EXTRA_QUANTITY, product.getQuantity());
                intent.putExtra(EditorActivity.EXTRA_PRICE, product.getPrice());
                startActivityForResult(intent, EDIT_DATA_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                productViewModel.deleteAllData();
                Toast.makeText(this, "All products were deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_DATA_REQUEST && resultCode == RESULT_OK) {
            String dataName = data.getStringExtra(EditorActivity.EXTRA_NAME);
            String dataSupplier = data.getStringExtra(EditorActivity.EXTRA_SUPPLIER);
            int dataQuantity = data.getIntExtra(EditorActivity.EXTRA_QUANTITY, 0);
            String dataPrice = data.getStringExtra(EditorActivity.EXTRA_PRICE);
//            String dataPhotoUri = data.getStringExtra(EditorActivity.EXTRA_PHOTO);

            Product product = new Product(dataName, dataSupplier, dataQuantity, dataPrice);
            productViewModel.insert(product);

            Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_DATA_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(EditorActivity.EXTRA_ID, UNDEFINED_ID_VALUE);

            if (id == UNDEFINED_ID_VALUE) {
                Toast.makeText(this, "Can't update data", Toast.LENGTH_SHORT).show();
                return;
            }

            String dataName = data.getStringExtra(EditorActivity.EXTRA_NAME);
            String dataSupplier = data.getStringExtra(EditorActivity.EXTRA_SUPPLIER);
            int dataQuantity = data.getIntExtra(EditorActivity.EXTRA_QUANTITY, 0);
            String dataPrice = data.getStringExtra(EditorActivity.EXTRA_PRICE);
//            String dataPhotoUri = data.getStringExtra(EditorActivity.EXTRA_PHOTO);

            Product product = new Product(dataName, dataSupplier, dataQuantity, dataPrice);
            product.setId(id);
            productViewModel.update(product);

            Toast.makeText(this, "Successfully updated", Toast.LENGTH_SHORT).show();
        } else {
//            int id = data.getIntExtra(EditorActivity.EXTRA_ID, UNDEFINED_ID_VALUE);
//            productViewModel.delete(id);
        }
    }
}