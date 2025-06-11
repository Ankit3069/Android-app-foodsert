package com.example.personalizedecommerceapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.adapter.ViewProductListAdapter;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.interfaces.CartOperations;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Cart;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;

import java.util.ArrayList;
import java.util.List;

public class ViewShopProductActivity extends AppCompatActivity implements
        View.OnClickListener, CartOperations {

    private ImageButton btnBack, btnAction;
    private RelativeLayout rlProductLayout;
    private RecyclerView rvProduct;
    private TextView tvNoData;

    private Shop shop;
    private Context context;
    private List<ProductMaster> productList;
    private IController<ProductMaster> controller;
    private IController<Cart> cartController;
    private ViewProductListAdapter productListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_shop_product);
        initToolbar();
        initUI();
        initListeners();
        initObj();
        loadIntentData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDataVisibility(false);

        if (context != null && shop != null) loadData();
    }

    private void initToolbar() {
        try {
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.baseline_shopping_cart_24);
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            btnBack = toolbar.findViewById(R.id.btnBack);
            btnAction = toolbar.findViewById(R.id.btnAction);
            btnAction.setImageDrawable(drawable);
            tvTitle.setText(Constants.PRODUCT_SCREEN);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUI() {
        rlProductLayout = findViewById(R.id.rlProductLayout);
        rvProduct = findViewById(R.id.rvProduct);
        tvNoData = findViewById(R.id.tvNoData);
    }

    private void initListeners() {
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        btnAction.setVisibility(View.VISIBLE);
        btnAction.setOnClickListener(this);
    }

    private void initObj() {
        context = this;
        productList = new ArrayList<>();
        controller = BaseController.getController(Constants.PRODUCT, context);
        cartController = BaseController.getController(Constants.CART, context);
    }

    private void loadIntentData() {
        shop = (Shop) getIntent().getSerializableExtra(Constants.SHOP);
        if (shop == null) setDataVisibility(false);
    }

    private void loadData() {
        try {
            String whereClause = Constants.COLUMN_SHOP_ID + " =? ";
            String[] condition = new String[]{String.valueOf(shop.getShopId())};

            productList = controller.getByCondition(whereClause,condition);
            if (productList.size() > 0) {
                setUpRecyclerView();
            } else setDataVisibility(false);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlProductLayout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        try {
            productListAdapter = new ViewProductListAdapter(context, this, productList);
            rvProduct.setAdapter(productListAdapter);
            rvProduct.setLayoutManager(Helper.getVerticalManager(context));
            productListAdapter.notifyDataSetChanged();
            setDataVisibility(true);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlProductLayout, Constants.SOMETHING_WENT_WRONG);
            setDataVisibility(false);
        }
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnBack) finish();
        else if (id == R.id.btnAction) {
            onClickBtnAction();
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
            List<Cart> existingCartItemList = cartController.getByCondition(whereClause,condition);
            if (existingCartItemList != null && !existingCartItemList.isEmpty()) {
                final Cart existingCartItem = existingCartItemList.get(0);
                if (quantity <= 1) {
                    final boolean delete = cartController.delete(existingCartItem);
                    if (delete) finalQuantity = quantity - 1;
                } else {
                    existingCartItem.setQuantity(quantity);
                    long result = cartController.update(existingCartItem);
                    if (result < 0) finalQuantity = isAdd ? quantity - 1 : quantity + 1;
                }
            } else {
                Cart newCartItem = new Cart();
                newCartItem.setProductId(productId);
                newCartItem.setUserId(UserPref.getUserId(context));
                newCartItem.setShopId(shop.getShopId());
                newCartItem.setQuantity(quantity);
                long result = cartController.save(newCartItem);
                if (result < 0) finalQuantity = isAdd ? quantity - 1 : quantity + 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            for (ProductMaster product : productList) {
                if (product.getProductId() == productId) {
                    product.setQuantityInCart(finalQuantity);
                    break;
                }
            }
            productListAdapter.setValues(productList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onClickBtnAction() {
        Helper.goTo(context, CartActivity.class, Constants.SHOP, shop.getShopId());
    }
}
