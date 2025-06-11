package com.example.personalizedecommerceapp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.activity.MyRateAndReview;
import com.example.personalizedecommerceapp.activity.ViewShopDetailsActivity;
import com.example.personalizedecommerceapp.activity.ViewShopProductActivity;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.Helper;

import java.util.ArrayList;
import java.util.List;

public class ViewShopListAdapter extends RecyclerView.Adapter<ViewShopListAdapter.ViewHolder> {

    private final Context context;
    private List<Shop> shopList;
    private final String[] options = {"View Details", "Rate"};


    public ViewShopListAdapter(Context context, List<Shop> shopList) {
        this.context = context;
        this.shopList = shopList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setValues(List<Shop> shopList) {
        try {
            if (shopList != null) {
                this.shopList = shopList;
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
        View detailItem = inflater.inflate(R.layout.list_shop, parent, false);
        return new ViewHolder(detailItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            if (shopList.size() > 0) {
                Shop shop = shopList.get(position);
                holder.tvShopName.setText(shop.getShopName());
                holder.tvShopType.setText(shop.getShopType());
                holder.tvAverageRating.setText(shop.getRating());
                holder.itemView.setOnClickListener(view -> {
                    DialogUtils.showOptionAlertDialog(context, "", options,
                            onClickItemViewListener(position)).show();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private DialogInterface.OnClickListener onClickItemViewListener(int position) {
        return (dialogInterface, index) -> {
            dialogInterface.dismiss();
            if (index == 1) {
                Helper.goTo(context, MyRateAndReview.class, Constants.SHOP, shopList.get(position));
            } else {
                Helper.goTo(context, ViewShopDetailsActivity.class, Constants.SHOP, shopList.get(position));
            }
        };
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvShopName, tvShopType, tvAverageRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvShopType = itemView.findViewById(R.id.tvShopType);
            tvAverageRating = itemView.findViewById(R.id.tvAverageRating);
        }
    }
}
