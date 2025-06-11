package com.example.personalizedecommerceapp.activity;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.google.android.material.textfield.TextInputEditText;

public class ViewShopDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack;
    private TextInputEditText etShopName, etShopUrl, etShopType, etCuisine, etShopTimings,etprice;
    private TextInputEditText etShopDescription, etRegion, etCity;
    private Button btnViewProducts,btnLocation;


    private Context context;
    private Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_details);
        initToolbar();
        initUI();
        initListeners();
        loadIntentData();
    }

    private void initToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            btnBack = toolbar.findViewById(R.id.btnBack);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(view -> finish());
            tvTitle.setText("Shop Details");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initListeners() {
        context = this;
        btnBack.setOnClickListener(this);
        btnViewProducts.setOnClickListener(this);
    }

    private void initUI() {
        etShopName = findViewById(R.id.etShopName);
        etShopUrl = findViewById(R.id.etShopUrl);
        etShopType = findViewById(R.id.etShopType);
        etCuisine = findViewById(R.id.etCuisine);
        etShopTimings = findViewById(R.id.etShopTimings);
        etShopDescription = findViewById(R.id.etShopDescription);
        etRegion = findViewById(R.id.etRegion);
        etCity = findViewById(R.id.etCity);
        btnViewProducts = findViewById(R.id.btnViewProducts);
        etprice = findViewById(R.id.etPrice);
        btnLocation = findViewById(R.id.btnloc);

        etShopUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    String data = etShopUrl.getText().toString();
                    Intent defaultBrowser = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_APP_BROWSER);
                    defaultBrowser.setData(Uri.parse(data));
                    startActivity(defaultBrowser);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "No app found to Open the URL or URL is not correct", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try
                {
                    String loc=shop.getLatitude()+","+shop.getLongitude();
                    String geoUri = "http://maps.google.com/maps?daddr="+loc;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(geoUri));
                    startActivity(intent);
                }
                catch (Exception e)
                {
                    Toast.makeText(context, "No app found to Open the Location", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void loadIntentData() {
        shop = (Shop) getIntent().getSerializableExtra(Constants.SHOP);
        if (shop != null) setDataToText();
    }

    private void setDataToText() {
        etShopName.setText(shop.getShopName());
        etShopUrl.setText(shop.getShopUrl());
        etShopType.setText(shop.getShopType());
        etCuisine.setText(shop.getCuisineCategory());
        etShopTimings.setText(shop.getTiming());
        etShopDescription.setText(shop.getDescription());
        etRegion.setText(shop.getRegion());
        etCity.setText(shop.getCity());
        etprice.setText(shop.getPrice());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnViewProducts) {
            onClickViewProducts();
        }
    }

    private void onClickViewProducts() {
        Helper.goTo(context, ViewShopProductActivity.class, Constants.SHOP, shop);
    }
}