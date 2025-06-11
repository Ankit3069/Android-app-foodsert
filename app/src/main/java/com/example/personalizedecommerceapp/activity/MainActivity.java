package com.example.personalizedecommerceapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.Shop.ShopHome;
import com.example.personalizedecommerceapp.Shop.ShopLoginActivity;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.color.MaterialColors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llShopLoginLayout, llUserLoginLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setStatusBarColor();
        checkForLoginStatusAndNavigate();
    }

    private void setStatusBarColor() {
        Window window = this.getWindow();
        int colorOnPrimary, color;
        switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) {
            case Configuration.UI_MODE_NIGHT_YES:
            case Configuration.UI_MODE_NIGHT_NO:
                colorOnPrimary = com.google.android.material.R.attr.colorPrimaryContainer;
                color = MaterialColors.getColor(this, colorOnPrimary, Color.BLACK);
                window.setStatusBarColor(color);
                break;
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void checkForLoginStatusAndNavigate() {
        String loginStatus = UserPref.getLoginUserType(this);
        int flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_NEW_TASK;
        if (loginStatus.equals(Constants.SHOP)) {
            Helper.goToWithFlags(this, ShopHome.class, flags);
            finish();
        } else if (loginStatus.equals(Constants.USER)) {
            Helper.goToWithFlags(this, BottomNavigationActivity.class, flags);
            finish();
        }
        setContentView(R.layout.activity_main);
        initUI();
        setListeners();
    }

    private void initUI() {
        llShopLoginLayout = findViewById(R.id.llShopLoginLayout);
        llUserLoginLayout = findViewById(R.id.llUserLoginLayout);
    }

    private void setListeners() {
        llShopLoginLayout.setOnClickListener(this);
        llUserLoginLayout.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.llShopLoginLayout) {
            onClickShopLayout();
        } else if (id == R.id.llUserLoginLayout) {
            onClickLoginLayout();
        }
    }

    private void onClickShopLayout() {
        Helper.goTo(this, ShopLoginActivity.class, Constants.USER_TYPE, Constants.SHOP);
    }

    private void onClickLoginLayout() {
        Helper.goTo(this, LoginActivity.class, Constants.USER_TYPE, Constants.USER);
    }
}