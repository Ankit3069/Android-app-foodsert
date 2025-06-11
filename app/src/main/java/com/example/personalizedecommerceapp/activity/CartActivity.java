package com.example.personalizedecommerceapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.adapter.ViewProductListAdapter;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.interfaces.CartOperations;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Cart;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements View.OnClickListener, CartOperations {

    private ImageButton btnBack;
    private RelativeLayout rlCartLayout;
    private RecyclerView rvProduct;
    private TextView tvNoData;
    private ExtendedFloatingActionButton extendedFbOrder;

    private int shopId;
    private Context context;
    private List<Cart> cartList;
    private List<ProductMaster> productList;
    private IController<Cart> controller;
    private ViewProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        initToolbar();
        initUI();
        initListeners();
        initObj();
        loadIntentData();

    }

    private void initToolbar() {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            btnBack = toolbar.findViewById(R.id.btnBack);
            tvTitle.setText(Constants.CART_SCREEN);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUI() {
        rlCartLayout = findViewById(R.id.rlCartLayout);
        rvProduct = findViewById(R.id.rvProduct);
        tvNoData = findViewById(R.id.tvNoData);
        extendedFbOrder = findViewById(R.id.extendedFbOrder);
    }

    private void initListeners() {
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        extendedFbOrder.setOnClickListener(this);
    }

    private void initObj() {
        context = this;
        cartList = new ArrayList<>();
        productList = new ArrayList<>();
        controller = BaseController.getController(Constants.CART, context);
    }

    private void loadIntentData() {
        shopId = getIntent().getIntExtra(Constants.SHOP, -1);
        if (shopId > 0) loadData();
        else setDataVisibility(false);
    }

    private void loadData() {
        try {
            cartList.clear();
            productList.clear();
            String whereClause = Constants.COLUMN_SHOP_ID + " =? AND " + Constants.COLUMN_USER_ID
                    + " =? AND " + Constants.COLUMN_QUANTITY + " > 0 ";
            String[] condition = new String[]{String.valueOf(shopId), String.valueOf(UserPref.getUserId(context))};
            cartList = controller.getByCondition(whereClause, condition);
            if (cartList.size() > 0) setUpRecyclerView();
            else setDataVisibility(false);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlCartLayout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        try {
            List<ProductMaster> productList = extractAllProducts(cartList);
            productListAdapter = new ViewProductListAdapter(context, this, productList);
            rvProduct.setAdapter(productListAdapter);
            rvProduct.setLayoutManager(Helper.getVerticalManager(context));
            productListAdapter.notifyDataSetChanged();
            setDataVisibility(true);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlCartLayout, Constants.SOMETHING_WENT_WRONG);
            setDataVisibility(false);
        }
    }

    public List<ProductMaster> extractAllProducts(List<Cart> cartList) {
        for (Cart cart : cartList) {
            productList.add(cart.getProductMaster());
        }
        return productList;
    }

    private void setDataVisibility(boolean isDataAvailable) {
        if (isDataAvailable) {
            rvProduct.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            rvProduct.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void updateCart(int productId, int quantity, boolean isAdd) {
        int finalQuantity = quantity;
        try {
            String whereClause = Constants.COLUMN_PRODUCT_ID + " =? AND " + Constants.COLUMN_USER_ID
                    + " =? ";
            String[] condition = new String[]{String.valueOf(productId),
                    String.valueOf(UserPref.getUserId(context))};
            List<Cart> existingCartItemList = controller.getByCondition(whereClause, condition);
            if (existingCartItemList != null && !existingCartItemList.isEmpty()) {
                final Cart existingCartItem = existingCartItemList.get(0);
                if (quantity == 0) {
                    final boolean delete = controller.delete(existingCartItem);
                    if (delete) finalQuantity = quantity - 1;
                } else {
                    existingCartItem.setQuantity(quantity);
                    long result = controller.update(existingCartItem);
                    if (result < 0) finalQuantity = isAdd ? quantity - 1 : quantity + 1;
                }
            } else {
                Cart newCartItem = new Cart();
                newCartItem.setProductId(productId);
                newCartItem.setUserId(UserPref.getUserId(context));
                newCartItem.setShopId(shopId);
                newCartItem.setQuantity(quantity);
                long result = controller.save(newCartItem);
                if (result < 0) finalQuantity = isAdd ? quantity - 1 : quantity + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (finalQuantity > 0) {
                for (ProductMaster product : productList) {
                    if (product.getProductId() == productId) {
                        product.setQuantityInCart(finalQuantity);
                        break;
                    }
                }
                productListAdapter.setValues(productList);
            } else loadData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) {
            getOnBackPressedDispatcher().onBackPressed();
        } else if (id == R.id.extendedFbOrder) {
            onClickExtendedFbOrder();
        }
    }

    private void onClickExtendedFbOrder() {
        Helper.goTo(context, OrderDetailsActivity.class, Constants.CART, cartList);
    }

}