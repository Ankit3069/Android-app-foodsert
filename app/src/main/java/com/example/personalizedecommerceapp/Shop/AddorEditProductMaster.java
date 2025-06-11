package com.example.personalizedecommerceapp.Shop;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.ProductMasterController;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.FileUtils;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.PathUtils;
import com.example.personalizedecommerceapp.util.PermissionUtils;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.InputStream;

public class AddorEditProductMaster extends AppCompatActivity {
    TextInputEditText name, desc, price;
    RelativeLayout layout;
    ImageView img, delete;
    private final static int REQUEST_CODE = 1;
    private static final int PERMISSION_REQUEST_CODE = 11;
    CardView addcard;
    private static final int MY_RESULT_CODE_FILECHOOSER = 11111;
    String path;
    Dialog dialog;
    Context context;
    ProductMaster entity;
    ProductMasterController controller;
    private boolean permissionGranted = false;
    private ActivityResultLauncher<Intent> intentGalleryResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addor_edit_product_master);
        context = this;
        controller = new ProductMasterController(this);
        entity = new ProductMaster();
        initUi();
        loadIntentData();

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            ImageButton btnBack = toolbar.findViewById(R.id.btnBack);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            tvTitle.setText("Product");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    private void loadIntentData() {
        try {
            ProductMaster collection = (ProductMaster) getIntent().getSerializableExtra(Constants.COLLECTIONS);
            if (collection != null) {
                this.entity = collection;
                delete.setVisibility(View.VISIBLE);
                setEntityDataToEditText();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    private void setEntityDataToEditText() {
        name.setText(entity.getProductName());
        desc.setText(entity.getDescription());
        price.setText(entity.getProductPrice());
//        cat.setText(entity.getCat());
        path = entity.getProductImage();
        setImageToImageView(path);


    }

    private void setImageToImageView(String photo) {
        try {
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(context).load(photo).apply(requestOptions)
                    .error(R.drawable.logo).into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUi() {
        name = (TextInputEditText) findViewById(R.id.productname);
        layout = findViewById(R.id.layout_product);
        desc = (TextInputEditText) findViewById(R.id.productdesc);
        price = (TextInputEditText) findViewById(R.id.productprice);
        img = (ImageView) findViewById(R.id.productimg);
        addcard = (CardView) findViewById(R.id.productsubmit);
        delete = (ImageView) findViewById(R.id.ibDeleteproduct);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    checkPermissions();
                    if (permissionGranted) {
                        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        chooseFileIntent.setType("*/*");
                        // Only return URIs that can be opened with ContentResolver
                        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);

                        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Choose a file");
                        chooseFileIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivityForResult(chooseFileIntent, MY_RESULT_CODE_FILECHOOSER);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter Name", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else if (desc.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter Description", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                } else if (price.getText().toString().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(layout, "Enter Price", Snackbar.LENGTH_SHORT);
                    snackbar.show();

                } else if (path == null) {
                    Snackbar snackbar = Snackbar.make(layout, "Select Image", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } else {

                    entity.setProductName(name.getText().toString());
                    entity.setProductPrice(price.getText().toString());
                    entity.setDescription(desc.getText().toString());
                    entity.setProductImage(path);
                    entity.setShopId(UserPref.getUserId(getApplicationContext()));

                    long result = controller.saveOrUpdate(entity);
                    if (result > 0) onSuccessResponse(result);
                    else Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
                }
            }

        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickIbDelete();
            }
        });

    }

    private void onClickIbDelete() {
        try {
            dialog = confirmationDialog(context, "delete",
                    (dialogInterface, i) -> {
                        dialogInterface.cancel();
                        onClickDelete();
                    }
            );
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
        }

    }

    public static AlertDialog confirmationDialog(final Context context, String confirmationText
            , DialogInterface.OnClickListener onDeleteClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (confirmationText == null) confirmationText = "perform this operation";
        builder.setMessage("Are you sure you want to " + confirmationText + "?" +
                "\nWARNING: This action cannot be undone");
        builder.setPositiveButton("Yes", onDeleteClickListener);
        builder.setNegativeButton("No", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

    private void onClickDelete() {
        try {
            dialog = DialogUtils.showLoadingDialog(context, Constants.PLEASE_WAIT, dialog);
            boolean result = controller.delete(Long.parseLong(String.valueOf(entity.getProductId())));
            if (result) {
//                for (ProductPhotos productPhotos : entity.getProductPhotosList()) {
//                    MediaUtil.deleteMedia(Constants.COLLECTIONS, productPhotos.getPhoto());
//                }
                DialogUtils.openAlertDialog(context, "Success",
                        "Discount Deleted Successfully!!!",
                        "OK", false, true).show();
                onResume();
            } else {
                DialogUtils.dismissDialog(dialog);
                Helper.makeSnackBar(layout, "Failed to delete");
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.dismissDialog(dialog);
            Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
        }
    }


    private void onSuccessResponse(long result) {
        DialogUtils.dismissDialog(dialog);

        DialogUtils.openAlertDialog(context, "Success",
                "Asset Added Successfully!!!",
                "OK", false, true
        ).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == MY_RESULT_CODE_FILECHOOSER) {
                try {

                    Uri uri = data.getData();

                    Log.d("uri", uri.toString());


                    final InputStream inputStream = getContentResolver().openInputStream(uri);
                    final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                    Log.d("bitmap", imageMap.toString());
                    img.setImageBitmap(imageMap);
                    File file = FileUtils.copyFile(getApplicationContext(), uri);
                    if (file == null) {
                        Toast.makeText(getApplicationContext(), "Cant Copy Image", Toast.LENGTH_SHORT).show();
                    } else {
                        path = file.getAbsolutePath();
                    }


//                    Bitmap bitmap = StringToBitMap(b64);
//                    img.setImageBitmap(bitmap);

                } catch (Exception e) {

                    Log.d("exception", e.toString());
                }

            } else {
//            Snackbar snackbar = Snackbar.make(layout, "Failed Try Again!", Snackbar.LENGTH_SHORT);
//            snackbar.show();
            }


        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPermissions();
    }

    private void checkPermissions() {
        permissionGranted = PermissionUtils.requestPermission(this, PERMISSION_REQUEST_CODE);
    }

}