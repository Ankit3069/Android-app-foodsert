package com.example.personalizedecommerceapp.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.personalizedecommerceapp.activity.MainActivity;
import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.RegistrationController;
import com.example.personalizedecommerceapp.controller.ShopRegistrationController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class ShopChangePass extends AppCompatActivity {
    TextInputEditText oldedt, newedt;
    CardView btn;
    ProgressBar pb;
    RelativeLayout layout;
    Shop entity;
    ShopRegistrationController controller;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_change_pass);

        Log.d("shopid",String.valueOf(UserPref.getUserId(getApplicationContext())));

        oldedt = (TextInputEditText) findViewById(R.id.cp_oldpassedt);
        newedt = (TextInputEditText) findViewById(R.id.cp_newpassedt);
        btn = (CardView) findViewById(R.id.card_cp);
        layout = (RelativeLayout) findViewById(R.id.layout_cp);
        pb = (ProgressBar) findViewById(R.id.pb_cp);
        entity=new Shop();
        controller=new ShopRegistrationController(this);
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
            tvTitle.setText("Change Password");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isvalidate()) {
                    cp();
                }
            }
        });
    }

    private void cp() {
        Cursor reult = controller.getProfileUser(String.valueOf(UserPref.getUserId(getApplicationContext())));

        while (reult.moveToNext()) {
            Log.d("result",
                    reult.getString(0) + " " +
                            reult.getString(1) + " " +
                            reult.getString(2) + " " +
                            reult.getString(3) + " " +
                            reult.getString(4)+
                            reult.getString(13)


            );

            if (reult.getString(13).compareTo(oldedt.getText().toString()) == 0) {
                pb.setVisibility(View.VISIBLE);
                Shop shop=new Shop();
                shop.setShopId(Integer.parseInt(String.valueOf(UserPref.getUserId(getApplicationContext()))));
                shop.setPass(newedt.getText().toString());
                controller.cpshop(shop);
                Snackbar snackbar = Snackbar.make(layout, "Password Changed", Snackbar.LENGTH_SHORT);
                snackbar.show();
                UserPref.setUserId(getApplicationContext(), 0);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                pb.setVisibility(View.GONE);
            } else {
                Snackbar snackbar = Snackbar.make(layout, "Wrong Password!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }

    }

    private boolean isvalidate() {
        if (oldedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter Old Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        }
        if (newedt.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(layout, "Enter New Password", Snackbar.LENGTH_LONG);
            snackbar.show();
            return false;

        } else {

            return true;

        }
    }
}
