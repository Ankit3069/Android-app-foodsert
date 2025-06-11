package com.example.personalizedecommerceapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.ShopOrderController;
import com.example.personalizedecommerceapp.model.Order;

public class DetailOrderActivity_User extends AppCompatActivity {
    String uname, pname, uid, contact, add, status, amount, date;
    TextView nametv, quantity, amounttv, statustv, type, datetv, username, usercontact, useradd, products;
    Button cancel;
    ImageView imageView;
    String[] statuslist = {"Approved", "Processing", "Dispatched", "Delivered", ""};
    String statustext;
    String oid;
    Order order;
    ShopOrderController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order_user);
        order = new Order();
        controller = new ShopOrderController(this);
        initUi();
        getdataIntent();
    }

    private void getdataIntent() {
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        uname = intent.getStringExtra("uname");
        pname = intent.getStringExtra("pname");
        contact = intent.getStringExtra("contact");
        add = intent.getStringExtra("add");
        amount = intent.getStringExtra("amount");
        status = intent.getStringExtra("status");
        date = intent.getStringExtra("date");
        oid = intent.getStringExtra("oid");
        Log.d("oid", oid);

        username.setText("Name: " + uname);
        amounttv.setText("Total: " + amount);
        datetv.setText("Date and Time: " + date);
        useradd.setText("Address: " + add);

        statustv.setText("Status: " + status);
        usercontact.setText("Contact: " + contact);

        if(!status.equals("Pending"))
        {
            cancel.setVisibility(View.GONE);
        }
        else {
            cancel.setVisibility(View.VISIBLE);
        }
        getProductdetails();

    }

    private void initUi() {
        username = (TextView) findViewById(R.id.busername);
        amounttv = (TextView) findViewById(R.id.bamount);
        statustv = (TextView) findViewById(R.id.bstatus);
        datetv = (TextView) findViewById(R.id.bdate);
        cancel = (Button) findViewById(R.id.bcancelorder);
        useradd = (TextView) findViewById(R.id.buserade);
        username = (TextView) findViewById(R.id.busername);
        usercontact = (TextView) findViewById(R.id.busercontact);
        cancel = (Button) findViewById(R.id.bcancelorder);
        products=findViewById(R.id.products);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                order.setOrderId(Integer.parseInt(oid));
                order.setStatus("Cancelled");
                controller.changeorderstatus(order);
                finish();
            }
        });


    }

    public void getProductdetails()
    {
        String pd="";
        ShopOrderController controller = new ShopOrderController(DetailOrderActivity_User.this);
        Cursor c1 = controller.getOrderDetails(String.valueOf(oid));
        if(c1.getCount()>0)
        {
            while (c1.moveToNext())
            {
                String pid=c1.getString(2);
                String Qty=c1.getString(3);
                String pname=getProductName(pid);
                pd+=pname+" (Qty : "+Qty+")\n";
            }
        }

        products.setText(pd);
    }

    public String getProductName(String pid)
    {
        ShopOrderController controller = new ShopOrderController(DetailOrderActivity_User.this);
        Cursor c1 = controller.getProductDetails(pid);
        if(c1.getCount()>0)
        {
            c1.moveToFirst();
            String pname = c1.getString(1);
            return pname;
        }
        return "Unknown";
    }

}