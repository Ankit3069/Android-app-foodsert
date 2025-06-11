package com.example.personalizedecommerceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.controller.RegistrationController;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.model.UserMaster;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class ChangePass extends AppCompatActivity {
    TextInputEditText oldedt, newedt;
    CardView btn;
    ProgressBar pb;
    RelativeLayout layout;
    Shop entity;
    private IController<UserMaster> controller;
    String uid;
    UserMaster um;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        Log.d("shopid",String.valueOf(UserPref.getUserId(getApplicationContext())));

        oldedt = (TextInputEditText) findViewById(R.id.cp_oldpassedt);
        newedt = (TextInputEditText) findViewById(R.id.cp_newpassedt);
        btn = (CardView) findViewById(R.id.card_cp);
        layout = (RelativeLayout) findViewById(R.id.layout_cp);
        pb = (ProgressBar) findViewById(R.id.pb_cp);
        entity=new Shop();
        controller=new RegistrationController(this);
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
        uid= UserPref.getUserId(ChangePass.this)+"";
        controller = BaseController.getController(Constants.REGISTRATION_SCREEN, this);
        um=controller.getById(uid);
        if(um==null)
        {
            Toast.makeText(ChangePass.this, "Data not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            if (um.getPassword().compareTo(oldedt.getText().toString()) == 0) {
                pb.setVisibility(View.VISIBLE);
                changepass(newedt.getText().toString(),uid);
                Snackbar snackbar = Snackbar.make(layout, "Password Changed", Snackbar.LENGTH_SHORT);
                snackbar.show();
                UserPref.deleteAll(ChangePass.this);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    public void changepass(String pass,String uid) {
        DBHelper dbHelper = new DBHelper(ChangePass.this);
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_PASSWORD, pass);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(Constants.TABLE_USER, values, Constants.COLUMN_USER_ID + " = ?", new String[]{String.valueOf(uid)});
    }
}
