package com.example.personalizedecommerceapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.interfaces.CartOperations;
import com.example.personalizedecommerceapp.model.ProductMaster;

import java.util.List;

public class ViewOrderProductListAdapter extends RecyclerView.Adapter<ViewOrderProductListAdapter.ViewHolder> {

    private final Context context;
    private List<ProductMaster> productList;

    public ViewOrderProductListAdapter(Context context,  List<ProductMaster> productList) {
        this.context = context;
        this.productList = productList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setValues(List<ProductMaster> productList) {
        try {
            if (productList != null) {
                this.productList = productList;
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
        View detailItem = inflater.inflate(R.layout.list_order_product, parent, false);
        return new ViewHolder(detailItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (productList.size() > 0) {
                ProductMaster product = productList.get(position);
                holder.tvProductName.setText(product.getProductName());
                holder.tvPrice.setText(product.getProductPrice());
                holder.tvQuantity.setText(String.valueOf(product.getQuantityInCart()));
                setImageToImageView(holder, product);
            }
        } catch (Exception e) {
            Log.d("RESPONSE","ErrorB-"+e.getMessage());
        }
    }

    private void setImageToImageView(ViewHolder holder, ProductMaster product) {
        try {
            Log.d("RESPONSE",product.getProductImage());
            RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE);
            Glide.with(context).load(product.getProductImage()).apply(requestOptions)
                    .error(R.drawable.logo).into(holder.ivProductImage);
        } catch (Exception e) {
            Log.d("RESPONSE","ErrorI-"+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivProductImage;
        private final TextView tvProductName, tvPrice, tvQuantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
        }
    }
}
