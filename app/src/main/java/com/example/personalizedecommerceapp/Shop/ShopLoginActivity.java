package com.example.personalizedecommerceapp.Shop;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.controller.RegistrationController;
import com.example.personalizedecommerceapp.controller.ShopRegistrationController;
import com.example.personalizedecommerceapp.interfaces.ILoginController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.model.UserMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.color.MaterialColors;

public class ShopLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmailId, etPassword;
    private TextView tvSignUp, tvError;
    private Button btnLogin;

    private Context context;
    private String userType;
    private ShopRegistrationController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_login);
        controller=new ShopRegistrationController(this);
        setStatusBarColor();
        initUI();
        setListeners();
        initObj();
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

    private void initUI() {
        etEmailId = findViewById(R.id.etEmailIds);
        etPassword = findViewById(R.id.etPasswords);
        tvSignUp = findViewById(R.id.tvSignUps);
        tvError = findViewById(R.id.tvErrors);
        btnLogin = findViewById(R.id.btnLogins);
    }

    private void setListeners() {
        tvError.setVisibility(View.GONE);
        tvSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    private void initObj() {
        context = this;


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnLogins) {
            onClickBtnLogin();
        } else if (view.getId() == R.id.tvSignUps) {
            Helper.goTo(context, ShopRegister.class);
        }
    }

    private void onClickBtnLogin() {
        if (isValidate()) {
            String username = etEmailId.getText().toString();
            String password = etPassword.getText().toString();

                boolean result = controller.authenticateShop(username, password,"");
                if (result) {
                    tvError.setVisibility(View.GONE);
                    int flags = Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                            | Intent.FLAG_ACTIVITY_NEW_TASK;
//                    Helper.goToWithFlags(this, userType.equals(Constants.SHOP)
//                            ? AdminDashboardActivity.class : UserDashboardActivity.class, flags);
                    Log.d("shopid",String.valueOf(UserPref.getUserId(getApplicationContext())));
                    startActivity(new Intent(getApplicationContext(),ShopHome.class));
                    finish();
                } else {
                    String error = "Invalid UserName or Password";
                    tvError.setText(error);
                    tvError.setVisibility(View.VISIBLE);
                }
            } else {
                tvError.setText(Constants.SOMETHING_WENT_WRONG);
                tvError.setVisibility(View.VISIBLE);
            }
        }


    private boolean isValidate() {
        String error = "";
        if (etEmailId.getText().toString().trim().equals("")) {
            error = error + "Email Id required\n";
            etEmailId.setError("required");
        }else {
            if (!Patterns.EMAIL_ADDRESS.matcher(etEmailId.getText().toString()).matches()) {
                etEmailId.setError("Invalid");
                etEmailId.requestFocus();
                return false;
            }
        }
        if (etPassword.getText().toString().trim().equals("")) {
            error = error + "Password required\n";
            etPassword.setError("required");
        }

        if (error.equals("")) {
            tvError.setVisibility(View.GONE);
            return true;
        } else {
            tvError.setVisibility(View.VISIBLE);
            return false;
        }
    }
}