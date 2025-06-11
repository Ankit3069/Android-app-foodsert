package com.example.personalizedecommerceapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.Shop.AddorEditProductMaster;
import com.example.personalizedecommerceapp.model.ProductMaster;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;

import java.util.List;

public class ManageProductMasterAdapter extends RecyclerView.Adapter<ManageProductMasterAdapter.ViewHolder> {

    private final Context context;


    private List<ProductMaster> discountList;
    private AlertDialog alertDialog;


    public ManageProductMasterAdapter(Context context, List<ProductMaster> discountList) {
        this.context = context;

        this.discountList = discountList;

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateProductMasterList(List<ProductMaster> discountList) {
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
                ProductMaster discount = discountList.get(position);
                holder.tvProductMasterName.setText("Name: "+discount.getProductName());
                holder.tvProductMasterEmail.setText("Price: "+discount.getProductPrice());
                holder.tvProductMasterContact.setText("Description: "+discount.getDescription());


                holder.itemView.setOnClickListener(v -> Helper.goTo(context,
                        AddorEditProductMaster.class, Constants.COLLECTIONS, discount));
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


        private final TextView tvProductMasterName;
        private final TextView tvProductMasterContact;
        private final TextView tvProductMasterEmail;
        private  ImageView empimg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvProductMasterName = itemView.findViewById(R.id.emp_name1);
            tvProductMasterEmail = itemView.findViewById(R.id.emp_email1);
            tvProductMasterContact = itemView.findViewById(R.id.emp_contact1);


        }
    }


}
