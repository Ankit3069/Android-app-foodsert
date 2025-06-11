package com.example.personalizedecommerceapp.Shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.personalizedecommerceapp.adapter.ManageProductMasterAdapter;
import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.ProductMasterController;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.List;

public class ManageProduct extends AppCompatActivity {

    ExtendedFloatingActionButton extendedFbEmp;
    RecyclerView rv;
    TextView tv_nodata;
    private List<ProductMaster> discountlist;
    private ProductMasterController controller;
    Context context;
    RelativeLayout layout;
    private ManageProductMasterAdapter manageProductsListAdapter;
    RelativeLayout rlViewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);
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
            tvTitle.setText("Product");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUi() {
        controller = new ProductMasterController(this);

        extendedFbEmp = (ExtendedFloatingActionButton) findViewById(R.id.extendedFbDepartment);
        tv_nodata = findViewById(R.id.tvNoData);
        rv = (RecyclerView) findViewById(R.id.rvDepartment);
        layout = (RelativeLayout) findViewById(R.id.rlmanageemp);
        extendedFbEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddorEditProductMaster.class));
            }
        });
    }

    private void loadData() {
        try {
            discountlist = controller.getByShopId(UserPref.getUserId(context));
            if (discountlist.size() == 0) {
                Helper.makeSnackBar(layout, "No Data");

            } else {
                setUpRecyclerView();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void setUpRecyclerView() {
        try {
            if (manageProductsListAdapter != null) {
                manageProductsListAdapter.updateProductMasterList(discountlist);
            } else {
                manageProductsListAdapter = new ManageProductMasterAdapter(context,
                        discountlist);
                rv.setAdapter(manageProductsListAdapter);
                rv.setLayoutManager(Helper.getVerticalManager(context));
                manageProductsListAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlViewProducts, Constants.SOMETHING_WENT_WRONG);

        }
    }
}