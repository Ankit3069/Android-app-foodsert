package com.example.personalizedecommerceapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Ratings;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class MyRateAndReview extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlMyRateAndReviewLayout;
    private TextView tvAverageRating;
    private RatingBar rbMyRating;
    private TextInputEditText etFeedBack;
    private Button btnSubmit;

    private Context context;
    private IController<Ratings> controller;

    private int shopId = -1;
    private int userId;
    private Ratings entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_rate_and_review);

        initToolbar();
        initUI();
        initObj();
        loadIntentData();
    }

    private void initToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            tvTitle.setText(Constants.RATING_SCREEN);
            ImageButton btnBack = toolbar.findViewById(R.id.btnBack);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(this);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUI() {
        rlMyRateAndReviewLayout = findViewById(R.id.rlMyRateAndReviewLayout);
        tvAverageRating = findViewById(R.id.tvAverageRating);
        rbMyRating = findViewById(R.id.rbMyRating);
        etFeedBack = findViewById(R.id.etFeedBack);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
    }

    private void initObj() {
        context = this;
        entity = new Ratings();
        controller = BaseController.getController(Constants.RATING_SCREEN, this);
        userId = UserPref.getUserId(context);
    }

    private void loadIntentData() {
        try {
            Shop shop = (Shop) getIntent().getSerializableExtra(Constants.SHOP);
            if (shop != null) {
                shopId = shop.getShopId();
                tvAverageRating.setText(shop.getRating());
                String whereClause = Constants.COLUMN_SHOP_ID_RATINGS + " =? AND "
                        + Constants.COLUMN_USER_ID_RATINGS + " =? ";
                String[] condition = new String[]{String.valueOf(shopId), String.valueOf(userId)};
                final List<Ratings> ratings = controller.getByCondition(whereClause, condition);
                if (ratings != null && ratings.size() > 0) entity = ratings.get(0);
                if (entity != null) {
                    rbMyRating.setRating(entity.getRatingCount());
                    etFeedBack.setText(entity.getReview());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlMyRateAndReviewLayout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            onBackPressed();
        } else if (id == R.id.btnSubmit) {
            onClickBtnSubmit();
        }
    }

    private void onClickBtnSubmit() {
        try {
            if (Helper.isEmptyFieldValidation(etFeedBack)) {
                if (rbMyRating.getRating() > 0) {
                    entity.setRatingCount((int) rbMyRating.getRating());
                    entity.setShopId(shopId);
                    entity.setUserId(userId);
                    entity.setReview(Objects.requireNonNull(etFeedBack.getText()).toString().trim());
                    long result = controller.save(entity);
                    if (result > 0) onSuccessResponse();
                    else
                        Helper.makeSnackBar(rlMyRateAndReviewLayout, Constants.SOMETHING_WENT_WRONG);
                } else {
                    Helper.makeSnackBar(rlMyRateAndReviewLayout, "Invalid Rating");
                }

            }
        } catch (Exception exception) {
            Helper.makeSnackBar(rlMyRateAndReviewLayout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    private void onSuccessResponse() {
        DialogUtils.openAlertDialog(context, "Success",
                "Submitted Successfully!!!",
                "OK", false, true
        ).show();
    }

}