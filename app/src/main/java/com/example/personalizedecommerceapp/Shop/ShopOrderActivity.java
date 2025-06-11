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
import android.widget.Toast;

import com.example.personalizedecommerceapp.adapter.ManageProductMasterAdapter;
import com.example.personalizedecommerceapp.adapter.ShopOrderAdapter;
import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.ProductMasterController;
import com.example.personalizedecommerceapp.controller.ShopOrderController;
import com.example.personalizedecommerceapp.model.Order;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ShopOrderActivity extends AppCompatActivity {
    RecyclerView rv;
    TextView tv_nodata;
    private List<Order> discountlist;
    private ShopOrderController controller;
    Context context;
    RelativeLayout layout;
    private ShopOrderAdapter manageProductsListAdapter;
    RelativeLayout rlViewProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);

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
            tvTitle.setText("Orders");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUi() {
        controller = new ShopOrderController(this);
        layout = (RelativeLayout) findViewById(R.id.layout_ordersshop);

        tv_nodata = findViewById(R.id.tvNoData);
        rv = (RecyclerView) findViewById(R.id.rvorder);
        layout = (RelativeLayout) findViewById(R.id.rlmanageemp);

    }

    private void loadData() {
        try {
            discountlist = controller.getAll(String.valueOf(UserPref.getUserId(getApplicationContext())));
            if (discountlist.size() == 0) {
//                Helper.makeSnackBar(layout, "No Data");
                Toast.makeText(getApplicationContext(),"No Data", Toast.LENGTH_SHORT).show();

            } else {
                setUpRecyclerView();
            }

        } catch (Exception e) {
            e.printStackTrace();
//            Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
            Toast.makeText(getApplicationContext(),"SomeThing Went Wrong", Toast.LENGTH_SHORT).show();
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
                manageProductsListAdapter.updateOrderList(discountlist);
            } else {
                manageProductsListAdapter = new ShopOrderAdapter(context,
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