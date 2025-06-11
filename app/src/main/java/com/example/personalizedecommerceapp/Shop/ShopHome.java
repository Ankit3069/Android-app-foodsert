package com.example.personalizedecommerceapp.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.activity.MainActivity;
import com.example.personalizedecommerceapp.controller.ProductMasterController;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.UserPref;

public class ShopHome extends AppCompatActivity {
    CardView product, shop, cp, orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_home);
        initUi();
    }

    @SuppressLint("SetTextI18n")
    private void initUi() {
        product = (CardView) findViewById(R.id.productcard);
        shop = (CardView) findViewById(R.id.shopcard);
        cp = (CardView) findViewById(R.id.cpcard);
        orders = (CardView) findViewById(R.id.ordercard);

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ShopChangePass.class));
            }
        });
        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AddorEditShop.class));
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(getApplicationContext(), ManageProduct.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShopOrderActivity.class));
            }
        });

        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
           ImageButton btnBack = toolbar.findViewById(R.id.btnBack);
            tvTitle.setText("Home");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.slogout)
        {
            UserPref.deleteAll(ShopHome.this);
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
        }
        return true;
    }
}