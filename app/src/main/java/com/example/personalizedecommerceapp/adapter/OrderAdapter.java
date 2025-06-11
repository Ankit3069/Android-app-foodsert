package com.example.personalizedecommerceapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.Shop.DetailOrderActivity;
import com.example.personalizedecommerceapp.activity.DetailOrderActivity_User;
import com.example.personalizedecommerceapp.controller.ShopOrderController;
import com.example.personalizedecommerceapp.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private final Context context;

    String pname;
    private List<Order> discountList;
    private AlertDialog alertDialog;
    String pid, umname, amount, status, contact, add, uid;

    public OrderAdapter(Context context, List<Order> discountList) {
        this.context = context;

        this.discountList = discountList;

    }


    @SuppressLint("NotifyDataSetChanged")
    public void updateOrderList(List<Order> discountList) {
        try {
            if (discountList != null) {
                this.discountList = discountList;
                notifyDataSetChanged();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View detailItem = inflater.inflate(R.layout.item_product, parent, false);
        return new ViewHolder(detailItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            if (discountList.size() > 0) {
                Order discount = discountList.get(position);

                holder.tvOrderName.setText("Name: " + getName(discount));
                holder.tvOrderEmail.setText("Total: " + discount.getGrandTotal());
                holder.tvOrderContact.setText("Status: " + discount.getStatus());
                holder.tvOrderdt.setText("Ordered on: "+discount.getDateTime());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getDetails(discount);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return discountList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvOrderName;
        private final TextView tvOrderContact;
        private TextView tvOrderEmail,tvOrderdt;
        private ImageView empimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderName = itemView.findViewById(R.id.emp_name1);
            tvOrderEmail = itemView.findViewById(R.id.emp_email1);
            tvOrderContact = itemView.findViewById(R.id.emp_contact1);
            tvOrderdt = itemView.findViewById(R.id.dt);

        }
    }

    public String getName(Order discount)
    {
        ShopOrderController controller = new ShopOrderController(context);
        Cursor reult2 = controller.getShopDetails(String.valueOf(discount.getShopid()));
        if(reult2.getCount()>0)
        {
            reult2.moveToFirst();
            String umname = reult2.getString(1);
            return umname;
        }

        return "Unknown";
    }

    public void getDetails(Order discount)
    {

        ShopOrderController controller = new ShopOrderController(context);
        Cursor reult2 = controller.getShopDetails(String.valueOf(discount.getShopid()));
        if(reult2.getCount()>0)
        {
            reult2.moveToFirst();
            uid = reult2.getString(0);
            umname = reult2.getString(1);
            amount = discount.getGrandTotal();
            status = discount.getStatus();
            contact = reult2.getString(12);
            add = reult2.getString(4)+","+reult2.getString(3);

            Intent intent = new Intent(context, DetailOrderActivity_User.class);
            intent.putExtra("uid", uid);
            intent.putExtra("uname", umname);
            intent.putExtra("amount", discount.getGrandTotal());
            intent.putExtra("status", discount.getStatus());
            intent.putExtra("contact", contact);
            intent.putExtra("date", discount.getDateTime());
            intent.putExtra("add", add);
            intent.putExtra("oid", String.valueOf(discount.getOrderId()));
            context.startActivity(intent);
        }
    }

}
