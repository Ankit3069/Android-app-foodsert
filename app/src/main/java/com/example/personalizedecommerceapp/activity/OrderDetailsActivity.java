package com.example.personalizedecommerceapp.activity;

import static com.example.personalizedecommerceapp.util.Constants.COLUMN_DATE_TIME;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_GRAND_TOTAL;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_ORDER_ID;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_ORDER_ID_ORDER;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_PRODUCT_ID_ORDER;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_QUANTITY;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_SHOP_ID_ORDER;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_STATUS;
import static com.example.personalizedecommerceapp.util.Constants.COLUMN_USER_ID_ORDER;
import static com.example.personalizedecommerceapp.util.Constants.TABLE_ORDER;
import static com.example.personalizedecommerceapp.util.Constants.TABLE_ORDER_PRODUCT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.adapter.ViewOrderProductListAdapter;
import com.example.personalizedecommerceapp.adapter.ViewProductListAdapter;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Cart;
import com.example.personalizedecommerceapp.model.Order;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.service.DBHelper;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    private ImageButton btnBack, btnAction;
    private TextView tvGrandTotal;
    private RelativeLayout rlOrderLayout;
    private RecyclerView rvProduct;
    private TextView tvNoData;

    private Context context;
    private List<Cart> cartList;
    private IController<Order> controller;
    private ViewOrderProductListAdapter productListAdapter;

    private ExtendedFloatingActionButton pay;
    SQLiteDatabase sqLiteDatabase;
    String uid;
    String shopid;
    String Gtotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
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
            tvTitle.setText(Constants.ORDER_SCREEN);
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void initUI() {
        rlOrderLayout = findViewById(R.id.rlOrderLayout);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        rvProduct = findViewById(R.id.rvOrderProduct);
        tvNoData = findViewById(R.id.tvNoData);
        pay=findViewById(R.id.pay);
    }

    private void initListeners() {
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(view -> finish());
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentDialog();
            }
        });
    }

    private void initObj() {
        try
        {
            context = this;
            controller = BaseController.getController(Constants.ORDER, context);
            uid= UserPref.getUserId(OrderDetailsActivity.this)+"";
        }
        catch (Exception e)
        {
            Log.d("RESPONSE",e.getMessage());
        }
    }

    private void loadIntentData() {
        cartList=new ArrayList<Cart>();
        double total=0;
        try
        {
            Intent intent = getIntent();
            if (intent != null) {
                ArrayList<? extends Serializable> serializableList = (ArrayList<? extends Serializable>) intent.getSerializableExtra(Constants.CART);
                if (serializableList != null) {
                    for (Serializable serializableObject : serializableList) {
                        if (serializableObject instanceof Cart) {
                            Cart cart = (Cart) serializableObject;
                            cartList.add(cart);
                            ProductMaster pm=cart.getProductMaster();
                            double price=Double.parseDouble(pm.getProductPrice());
                            double qty=Double.parseDouble(pm.getQuantityInCart()+"");
                            total+=(price*qty);
                        }
                    }
                }
            }
            Log.d("RESPONSE","Cart size-"+cartList.size());

            if (cartList != null && !cartList.isEmpty()) {
                shopid=cartList.get(0).getShopId()+"";
                Gtotal=total+"";
                tvGrandTotal.setText("Grand Total : "+total);
                setUpRecyclerView(cartList);
            } else setDataVisibility(false);
        }
        catch (Exception e)
        {
            Log.d("RESPONSE",e.getMessage());
        }

    }


    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView(List<Cart> cartList) {
        try {
            productListAdapter = new ViewOrderProductListAdapter(context, extractAllProducts(cartList));
            rvProduct.setAdapter(productListAdapter);
            rvProduct.setLayoutManager(Helper.getVerticalManager(context));
            productListAdapter.notifyDataSetChanged();
            setDataVisibility(true);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlOrderLayout, Constants.SOMETHING_WENT_WRONG);
            setDataVisibility(false);
        }
    }

    public List<ProductMaster> extractAllProducts(List<Cart> cartList) {
        List<ProductMaster> productList = new ArrayList<>();
        for (Cart cart : cartList) {
            productList.add(cart.getProductMaster());
        }
        return productList;
    }

    private void setDataVisibility(boolean isDataAvailable) {
        if (isDataAvailable) {
            rvProduct.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            pay.setVisibility(View.VISIBLE);
        } else {
            rvProduct.setVisibility(View.GONE);
            pay.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }

    public void PaymentDialog()
    {
        Dialog d=new Dialog(OrderDetailsActivity.this);
        d.setContentView(R.layout.paymentdialog);

        EditText cname= d.findViewById(R.id.pname);
        EditText cno= d.findViewById(R.id.pcno);
        EditText mon= d.findViewById(R.id.pmon);
        EditText year= d.findViewById(R.id.pyear);
        EditText cvv= d.findViewById(R.id.pcvv);
        Button submit=d.findViewById(R.id.pay);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check(cname,cno,mon,year,cvv))
                {
                    d.cancel();
                    db_actions();
                }
            }
        });

        d.show();
    }

    public boolean check(EditText cname,EditText cno,EditText mon,EditText year,EditText cvv)
    {
        int m=mon.getText().toString().length()>0?Integer.parseInt(mon.getText().toString()):0;
        int y=year.getText().toString().length()>0?Integer.parseInt(year.getText().toString()):0;
        SimpleDateFormat sdfy=new SimpleDateFormat("yyyy");
        SimpleDateFormat sdfm=new SimpleDateFormat("MM");
        Integer YEAR= Integer.parseInt(sdfy.format(new Date().getTime()));
        Integer MONTH=Integer.parseInt(sdfm.format(new Date().getTime()));

        if(cname.getText().toString().length()==0)
        {
            Snackbar.make(cname,"Enter Name",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(cno.getText().toString().length()!=16)
        {
            Snackbar.make(cname,"Enter Card no (16 digits)",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(year.getText().toString().length()!=4)
        {
            Snackbar.make(cname,"Enter Year (YYYY)",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(y<YEAR)
        {
            Snackbar.make(cname,"Your Card is Expired",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(mon.getText().toString().length()!=2)
        {
            Snackbar.make(cname,"Enter Month (MM)",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(m<=0 || m>12)
        {
            Snackbar.make(cname,"Invalid Month",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(y==YEAR && m<MONTH)
        {
            Snackbar.make(cname,"Your Card is Expired",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(cvv.getText().toString().length()!=3)
        {
            Snackbar.make(cname,"Enter CVV (3digits)",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void db_actions()
    {
        DBHelper dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        //add to order
        AddtoOrder();
        int orderid=getLastOrderId();
        //Add products
        AddOrderProducts(orderid);
        //delete from cart
        deletefromCart(shopid);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Order Placed");
        builder.setCancelable(false);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    public void AddtoOrder()
    {
        String dt=new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date().getTime());
        ContentValues cv=new ContentValues();
        cv.put(COLUMN_USER_ID_ORDER,Integer.parseInt(uid));
        cv.put(COLUMN_SHOP_ID_ORDER,Integer.parseInt(shopid));
        cv.put(COLUMN_GRAND_TOTAL,Gtotal);
        cv.put(COLUMN_DATE_TIME,dt);
        cv.put(COLUMN_STATUS,"Pending");
        sqLiteDatabase.insert(TABLE_ORDER,null,cv);
    }

    public int getLastOrderId()
    {
        Cursor cursor = sqLiteDatabase.rawQuery("select "+COLUMN_ORDER_ID_ORDER+" from "+TABLE_ORDER+" order by "+COLUMN_ORDER_ID_ORDER+" DESC",null);
        if(cursor.getCount()>0)
        {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }

        return 0;
    }

    public void AddOrderProducts(int orderid)
    {
        for(int i=0;i<cartList.size();i++)
        {
            ContentValues cv=new ContentValues();
            cv.put(COLUMN_ORDER_ID,orderid);
            cv.put(COLUMN_PRODUCT_ID_ORDER,cartList.get(i).getProductId());
            cv.put(COLUMN_QUANTITY,cartList.get(i).getQuantity());
            sqLiteDatabase.insert(TABLE_ORDER_PRODUCT,null,cv);
        }
    }

    public void deletefromCart(String shopid)
    {
        sqLiteDatabase.delete(Constants.TABLE_CART," "+Constants.COLUMN_SHOP_ID+" = '"+shopid+"' ",null);
    }
}