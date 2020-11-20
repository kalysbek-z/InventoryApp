package com.example.inventoryapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;

import java.io.File;

public class EditorActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "package com.example.inventoryapp.EXTRA_NAME";
    public static final String EXTRA_PHOTO = "package com.example.inventoryapp.EXTRA_PHOTO";
    public static final String EXTRA_SUPPLIER = "package com.example.inventoryapp.EXTRA_SUPPLIER";
    public static final String EXTRA_QUANTITY = "package com.example.inventoryapp.EXTRA_QUANTITY";
    public static final String EXTRA_PRICE = "package com.example.inventoryapp.EXTRA_PRICE";
    public static final String EXTRA_ID = "package com.example.inventoryapp.EXTRA_ID";

    public static final int CAMERA_R_CODE = 20;
    public static final int GALLERY_R_CODE = 25;

    private final int UNDEFINED_ID_VALUE = -1;

    String photoUri;
    Bitmap bmpPhoto;
    Uri imageUri;
    String galleryImage;

    ImageView addPhoto;
    EditText Name;
    EditText Supplier;
    EditText Quantity;
    EditText Price;
    Button createButton;
    Button cameraButton;

    private ViewModel productViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        addPhoto = findViewById(R.id.add_photo);
        Name = findViewById(R.id.nameEdit);
        Supplier = findViewById(R.id.supplierEdit);
        Quantity = findViewById(R.id.quantityEdit);
        Price = findViewById(R.id.priceEdit);
        createButton = findViewById(R.id.createButton);
        cameraButton = findViewById(R.id.camera);

        bmpPhoto = null;

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_R_CODE);
            }
        });

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK);
                File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String path = directory.getPath();

                Uri imagePathUri = Uri.parse(path);

                gallery.setDataAndType(imagePathUri, "image/*");
                startActivityForResult(gallery, GALLERY_R_CODE);
            }
        });

        //

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit");
            Name.setText(intent.getStringExtra(EXTRA_NAME));
            galleryImage = intent.getStringExtra(EXTRA_PHOTO);

            Glide.with(getApplicationContext()).load(galleryImage).into(addPhoto);

            Supplier.setText(intent.getStringExtra(EXTRA_SUPPLIER));
            Quantity.setText(intent.getStringExtra(EXTRA_QUANTITY));
            Price.setText(intent.getStringExtra(EXTRA_PRICE));
        } else {
            setTitle("Add");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CAMERA_R_CODE:
                if (requestCode == Activity.RESULT_OK) {
                    bmpPhoto = (Bitmap) data.getExtras().get("data");
                    if (bmpPhoto != null) {
                        addPhoto.setImageBitmap(bmpPhoto);
                    }
                }
                break;
            case GALLERY_R_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == GALLERY_R_CODE) {
                        imageUri = data.getData();
                        photoUri = imageUri.toString();

                        Glide.with(getApplicationContext()).load(photoUri).into(addPhoto);
                    }
                }
        }
    }

    //for toolbar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_product:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you want to delete this product?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent delete = new Intent();
                        int id = getIntent().getIntExtra(EXTRA_ID, UNDEFINED_ID_VALUE);

                        if (id != UNDEFINED_ID_VALUE) {
                            delete.putExtra(EXTRA_ID, id);
                        }
                        setResult(RESULT_CANCELED, delete);
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", null);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveData() {
        String dataName = Name.getText().toString().trim();
        String dataSupplier = Supplier.getText().toString().trim();
        int dataQuantity = 0;
        if (Quantity.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Field is empty!", Toast.LENGTH_SHORT).show();
        } else {
            dataQuantity = Integer.parseInt(Quantity.getText().toString().trim());
        }
        String dataPrice = Price.getText().toString().trim();

        if (dataName.isEmpty() || dataSupplier.isEmpty() || dataPrice.isEmpty()) {
            Toast.makeText(this, "Field is empty!", Toast.LENGTH_SHORT).show();
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_NAME, dataName);
            data.putExtra(EXTRA_PHOTO, photoUri);
            data.putExtra(EXTRA_SUPPLIER, dataSupplier);
            data.putExtra(EXTRA_QUANTITY, dataQuantity);
            data.putExtra(EXTRA_PRICE, dataPrice);

            int id = getIntent().getIntExtra(EXTRA_ID, UNDEFINED_ID_VALUE);

            if (id != UNDEFINED_ID_VALUE) {
                data.putExtra(EXTRA_ID, id);
            }

            setResult(RESULT_OK, data);
            finish();
        }
    }
}
