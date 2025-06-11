package com.example.personalizedecommerceapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.UserMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlRegistrationLayout;
    private TextInputEditText etFullName, etEmailId, etContactNumber, etPassword, etAddress;
    private Button btnSubmit;

    private Context context;
    private Dialog dialog;
    private IController<UserMaster> controller;

    String uid;
    UserMaster um;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initUI();
        initObj();
    }

    private void initUI() {
        rlRegistrationLayout = findViewById(R.id.rlRegistrationLayout);
        etFullName = findViewById(R.id.etFullName);
        etEmailId = findViewById(R.id.etEmailId);
        etContactNumber = findViewById(R.id.etContactNumber);
        etAddress = findViewById(R.id.etAddress);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
    }

    private void initObj() {
        context = this;
        uid= UserPref.getUserId(ProfileActivity.this)+"";
        controller = BaseController.getController(Constants.REGISTRATION_SCREEN, this);
        um=controller.getById(uid);
        if(um==null)
        {
            Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            etFullName.setText(um.getFullName());
            etEmailId.setText(um.getEmailId());
            etContactNumber.setText(um.getContactNumber());
            etAddress.setText(um.getAddress());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            onClickBtnSubmit();
        }
    }

    private void onClickBtnSubmit() {
        try {
            if (isValidate()) {
                dialog = DialogUtils.showLoadingDialog(context, Constants.PLEASE_WAIT, dialog);
                UserMaster entity = setInputDataToEntity();
                long result = controller.update(entity);
                if (result > 0) onSuccessResponse();
                else if (result == Constants.ALREADY) {
                    Helper.makeSnackBar(rlRegistrationLayout, "Email Id already exist");
                } else Helper.makeSnackBar(rlRegistrationLayout, Constants.SOMETHING_WENT_WRONG);
            }
        } catch (Exception exception) {
            Helper.makeSnackBar(rlRegistrationLayout, Constants.SOMETHING_WENT_WRONG);
        } finally {
            DialogUtils.dismissDialog(dialog);
        }
    }

    private UserMaster setInputDataToEntity() {
        UserMaster userMaster = new UserMaster();
        userMaster.setUserId(Integer.parseInt(uid));
        userMaster.setFullName(Objects.requireNonNull(etFullName.getText()).toString().trim());
        userMaster.setEmailId(Objects.requireNonNull(etEmailId.getText()).toString().trim());
        userMaster.setContactNumber(Objects.requireNonNull(etContactNumber.getText()).toString().trim());
        userMaster.setAddress(Objects.requireNonNull(etAddress.getText()).toString().trim());
        return userMaster;
    }

    private boolean isValidate() {

        TextInputEditText[] editTexts = new TextInputEditText[]{etFullName, etEmailId, etContactNumber};
        if (Helper.isEmptyFieldValidation(editTexts)) {
            if (!Patterns.EMAIL_ADDRESS.matcher(etEmailId.getText().toString()).matches()) {
                Helper.setTextInputError(etEmailId, "Invalid Email Id");
                etEmailId.requestFocus();
                return false;
            } else if (!Patterns.PHONE.matcher(etContactNumber.getText().toString()).matches()) {
                Helper.setTextInputError(etContactNumber, "Invalid contact number");
                etContactNumber.requestFocus();
                return false;
            } else if (etContactNumber.getText().toString().length() < 10) {
                Helper.setTextInputError(etContactNumber, "Invalid contact number");
                etContactNumber.requestFocus();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    private void onSuccessResponse() {
        DialogUtils.dismissDialog(dialog);
        DialogUtils.openAlertDialog(context, "Success",
                "Update Successfully!!!",
                "OK", false, true
        ).show();
    }

}