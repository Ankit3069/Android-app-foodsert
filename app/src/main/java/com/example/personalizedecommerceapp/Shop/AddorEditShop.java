package com.example.personalizedecommerceapp.Shop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.ShopController;
import com.example.personalizedecommerceapp.controller.ShopRegistrationController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AddorEditShop extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    TextInputEditText locedt, nameedt, urledt, cityedt, reginedt, descedt, timingedt, ratingedt, priceedt;
    Spinner spinner;
    ImageView locimg;
    private final static int REQUEST_CODE = 1;
    CardView addcard;
    private static final int MY_RESULT_CODE_FILECHOOSER = 11111;
    TextInputEditText cusine_edt;
    Dialog dialog;
    Context context;
    ScrollView layout;
    List<String> designTypeList = Arrays.asList(new String[]{"Afghan","American","Andhra","Asian","Assamese","Awadhi","BBQ","Bakery","Bar","Bengali","Beverages","Bihari","Biryani","Brazilian","Bubble","Burger","Burmese","Cafe","Chinese","Coffee","Continental","Cream","Deli","Desserts","Eastern","European","Fast","Finger","Food","Fusion","German","Goan","Greek","Grill","Gujarati","Healthy","Hot dogs","Hyderabadi","Ice","Indian","Indonesian","Israeli","Italian","Japanese","Juices","Kashmiri","Kebab","Kerala","Konkan","Korean","Lebanese","Lucknowi","Maharashtrian","Malaysian","Malwani","Mangalorean","Mediterranean","Mexican","Middle","Mithai","Modern","Momos","Mughlai","North","Pakistani","Parsi","Pizza","Rajasthani","Rolls","Salad","Sandwich","Seafood","Sindhi","South","Spanish","Steak","Street","Sushi","Tea","Thai","Tibetan","Turkish","Vietnamese","Wraps"});
    String[] typelist = {"Bakery", "Bar", "Café", "Casual Dining", "none", "Quick Bites", "Sweet Shop", "Beverage Shop", "Dessert Parlor", "Fine Dining", "Food Truck", "Dhaba", "Food Court", "Kiosk", "Lounge", "Pub", "Paan Shop", "Mess", "Bhojanalya", "Confectionery", "Irani Cafe", "Microbrewery", "CUSINE TYPE"};
    String type, lat, lon;
    ShopController controller;
    Shop entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addor_edit_shop);
        context = this;
        initUi();
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
            tvTitle.setText("Shop");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUi() {
        locedt = (TextInputEditText) findViewById(R.id.locedt);
        locimg = (ImageView) findViewById(R.id.locationimg);
        cusine_edt = (TextInputEditText) findViewById(R.id.shopcuisine);
        spinner = (Spinner) findViewById(R.id.spinner_shoptype);
        nameedt = (TextInputEditText) findViewById(R.id.shopname);
        urledt = (TextInputEditText) findViewById(R.id.shopurl_edt);
        cityedt = (TextInputEditText) findViewById(R.id.shopcity);
        reginedt = (TextInputEditText) findViewById(R.id.shopregion);
        descedt = (TextInputEditText) findViewById(R.id.shopdesc);
        timingedt = (TextInputEditText) findViewById(R.id.shoptiming);
        ratingedt = (TextInputEditText) findViewById(R.id.shoprating);
        priceedt = (TextInputEditText) findViewById(R.id.shopprice);
        addcard = (CardView) findViewById(R.id.shopsubmit);
        layout = (ScrollView) findViewById(R.id.sv_addshp);
        entity = new Shop();
        controller = new ShopController(this);
        Cursor reult = getShopDetails(String.valueOf(UserPref.getUserId(getApplicationContext())));

        while (reult.moveToNext()) {
            Log.d("result",
                    reult.getString(0) + " " +
                            reult.getString(1) + " " +
                            reult.getString(2) + " " +
                            reult.getString(3) + " " +
                            reult.getString(4) +
                            reult.getString(11) +
                            reult.getString(12) +
                            reult.getString(14) +
                            reult.getString(13)+
                            reult.getString(11)
            );
            if (reult.getString(2)!=null){
              nameedt.setText(reult.getString(1));
              urledt.setText(reult.getString(2));
              cityedt.setText(reult.getString(3));
              reginedt.setText(reult.getString(4));
              cusine_edt.setText(reult.getString(6));
              locedt.setText(reult.getString(7)+","+reult.getString(8));
              descedt.setText(reult.getString(9));
              timingedt.setText(reult.getString(10));
              ratingedt.setText(reult.getString(14));
              priceedt.setText(reult.getString(11));
            }else{

            }
        }

        locimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ccc", "empty");
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        cusine_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEtDesign();
            }
        });

        spinner.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, typelist);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        addcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Name", Snackbar.LENGTH_SHORT).show();
                } else if (urledt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Url", Snackbar.LENGTH_SHORT).show();
                } else if (cityedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter City", Snackbar.LENGTH_SHORT).show();
                } else if (reginedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Region", Snackbar.LENGTH_SHORT).show();
                } else if (locedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Location", Snackbar.LENGTH_SHORT).show();
                } else if (cusine_edt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Cuisine", Snackbar.LENGTH_SHORT).show();
                } else if (descedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Description", Snackbar.LENGTH_SHORT).show();
                } else if (timingedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Timing", Snackbar.LENGTH_SHORT).show();
                } else if (ratingedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Rating", Snackbar.LENGTH_SHORT).show();
                } else if (priceedt.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Price", Snackbar.LENGTH_SHORT).show();
                } else {

                    entity.setShopId(UserPref.getUserId(getApplicationContext()));
                    entity.setShopName(nameedt.getText().toString());
                    entity.setShopUrl(urledt.getText().toString());
                    entity.setCity(cityedt.getText().toString());
                    entity.setRegion(reginedt.getText().toString());
                    entity.setShopType(type);
                    entity.setCuisineCategory(cusine_edt.getText().toString());
                    entity.setLongitude(lon);
                    entity.setLatitude(lat);
                    entity.setDescription(descedt.getText().toString());
                    entity.setTiming(timingedt.getText().toString());
                    entity.setRating(ratingedt.getText().toString());
                    entity.setPrice(priceedt.getText().toString());
                    ShopRegistrationController controller1 = new ShopRegistrationController(getApplicationContext());
                    Cursor reult = controller1.getProfileUser(String.valueOf(UserPref.getUserId(getApplicationContext())));

                    while (reult.moveToNext()) {
                        Log.d("result",
                                reult.getString(0) + " " +
                                        reult.getString(1) + " " +
                                        reult.getString(2) + " " +
                                        reult.getString(3) + " " +
                                        reult.getString(4) +
                                        reult.getString(13)
                        );
                        entity.setEmail(reult.getString(12));
                        entity.setPass(reult.getString(13));
                    }
                    long result = saveOrUpdate(entity);
                    if (result > 0) onSuccessResponse(result);
                    else Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);

                    }
                }
            });


    }

    public long saveOrUpdate(Shop entity) {
        boolean isSuccess = false;
        long result = Constants.ERROR_SQL_RESULT;
        SQLiteDatabase db = null;
        DBHelper dbHelper = new DBHelper(context);
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues contentValues = getContentValues(entity);
            Log.d("entity", String.valueOf(getContentValues(entity)));
            result = db.insertWithOnConflict(Constants.TABLE_SHOP, null, contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);

        } finally {
            dbHelper.endTransaction(db);
        }
        return result;
    }

    private static ContentValues getContentValues(Shop card) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.COLUMN_SHOP_ID,card.getShopId());
        contentValues.put(Constants.COLUMN_SHOP_NAME, card.getShopName());
        contentValues.put(Constants.COLUMN_SHOP_URL, card.getShopUrl());
        contentValues.put(Constants.COLUMN_CITY, card.getCity());
        contentValues.put(Constants.COLUMN_REGION, card.getRegion());
        contentValues.put(Constants.COLUMN_SHOP_TYPE, card.getShopType());
        contentValues.put(Constants.COLUMN_DESCRIPTION, card.getDescription());
        contentValues.put(Constants.COLUMN_LATITUDE, card.getLatitude());
        contentValues.put(Constants.COLUMN_LONGITUDE, card.getLongitude());
        contentValues.put(Constants.COLUMN_CUISINE_CATEGORY, card.getCuisineCategory());
        contentValues.put(Constants.COLUMN_TIMING, card.getTiming());
        contentValues.put(Constants.COLUMN_RATING, card.getRating());
        contentValues.put(Constants.COLUMN_PRICE, card.getPrice());
        contentValues.put(Constants.COLUMN_EMAIL, card.getEmail());
        contentValues.put(Constants.COLUMN_PASSWORD, card.getPass());

        return contentValues;
    }

    public Cursor getShopDetails(String id) {
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String qry = "select * from " + Constants.TABLE_SHOP + " where " + Constants.COLUMN_SHOP_ID + " = '" + id + "'";

        Cursor cursor = db.rawQuery(qry, null);
        return cursor;

    }



    private void onSuccessResponse(long result) {
        DialogUtils.dismissDialog(dialog);

        DialogUtils.openAlertDialog(context, "Success",
                " Added Successfully!!!",
                "OK", false, true
        ).show();
    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
            super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE) {
                    if (data != null) {
                        Log.d("value", data.getStringExtra("value"));
                        locedt.setText(data.getStringExtra("value"));
                        String currentString = locedt.getText().toString();
                        String[] separated = currentString.split(",");
                        lat = separated[0]; // this will contain "Fruit"
                        lon = separated[1];
                    }
                }
            }
        }

        private void onClickEtDesign () {
            DialogUtils.dismissDialog(dialog);
//        List<DesignType> designTypeList = designTypeController.getAll();
            final CharSequence[] items = new CharSequence[designTypeList.size()];
            for (int i = 0; i < designTypeList.size(); i++) {
                items[i] = designTypeList.get(i).toString();
            }
            final boolean[] checkedItems = new boolean[designTypeList.size()];
            dialog = openCheckboxDialog(context, designTypeList,
                    items, checkedItems, cusine_edt, dialog);
        }

        public static Dialog openCheckboxDialog (Context context, List objectList, CharSequence[]
        items
            ,boolean[] checkedItems, TextInputEditText textInputEditText, Dialog dialog){
            try {
                DialogUtils.dismissDialog(dialog);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select Items")
                        .setMultiChoiceItems(items, checkedItems, (dialog1, which, isChecked) -> {
                            checkedItems[which] = isChecked;
                        })
                        .setPositiveButton("OK", (dialog1, which) -> {
                            StringBuilder selectedItemsText = new StringBuilder();
                            for (int i = 0; i < objectList.size(); i++) {
                                if (checkedItems[i]) {
                                    Object item = objectList.get(i);
                                    selectedItemsText.append(item.toString());
                                    selectedItemsText.append(", ");
                                }
                            }
                            if (selectedItemsText.length() > 2) {
                                selectedItemsText.delete(selectedItemsText.length() - 2, selectedItemsText.length());
                            }
                            textInputEditText.setText(selectedItemsText.toString());
                            dialog1.cancel();

                            // Set the TextInputEditText as non-editable again after updating the text
                            textInputEditText.setInputType(InputType.TYPE_NULL);
                        }).setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());
                dialog = builder.create();
                if (!((Activity) context).isFinishing()) dialog.show();

                List<String> enteredValues = new ArrayList<>();
                String enteredItemsString = Objects.requireNonNull(textInputEditText.getText()).toString();
                String[] enteredItemValues = enteredItemsString.split(",");
                for (String enteredItemValue : enteredItemValues) {
                    enteredValues.add(enteredItemValue.trim());
                }

                for (int i = 0; i < objectList.size(); i++) {
                    if (enteredValues.contains(objectList.get(i).toString())) {
                        checkedItems[i] = true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dialog;
        }


        @Override
        public void onItemSelected (AdapterView < ? > adapterView, View view,int i, long l){
            type = typelist[i];
        }

        @Override
        public void onNothingSelected (AdapterView < ? > adapterView){

        }
    }