package com.example.personalizedecommerceapp.Shop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.ShopRegistrationController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.Helper;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class ShopRegister extends AppCompatActivity {
    TextInputEditText etemail, etpass;
    Button btn;
    RelativeLayout layout;
    Shop entity;
    ShopRegistrationController controller;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_register);
        initUi();
    }

    private void initUi() {
        entity = new Shop();
        controller = new ShopRegistrationController(this);
        etemail = (TextInputEditText) findViewById(R.id.etemail);
        etpass = (TextInputEditText) findViewById(R.id.etpassword);
        btn = (Button) findViewById(R.id.btnSubmit);
        layout = (RelativeLayout) findViewById(R.id.rl_reg);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etemail.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Email", Snackbar.LENGTH_SHORT).show();
                } else if (etpass.getText().toString().isEmpty()) {
                    Snackbar.make(layout, "Enter Password", Snackbar.LENGTH_SHORT).show();
                } else {
                    Shop entity = new Shop();
                    entity.setEmail(etemail.getText().toString());
                    entity.setPass(etpass.getText().toString());
                    long result = controller.save(entity);
                    if (result > 0) onSuccessResponse(result);
                    else Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
                }
            }
        });
    }

    private void onSuccessResponse(long result) {
        DialogUtils.dismissDialog(dialog);

        DialogUtils.openAlertDialog(ShopRegister.this, "Success",
                "Shop Added Successfully!!!",
                "OK", false, true
        ).show();
    }

}