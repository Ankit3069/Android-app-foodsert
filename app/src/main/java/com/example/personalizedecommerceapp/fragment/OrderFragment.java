package com.example.personalizedecommerceapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.adapter.OrderAdapter;
import com.example.personalizedecommerceapp.adapter.ShopOrderAdapter;
import com.example.personalizedecommerceapp.controller.ShopOrderController;
import com.example.personalizedecommerceapp.model.Order;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;

import java.util.List;

public class OrderFragment extends Fragment {
    RecyclerView rv;
    TextView tv_nodata;
    private List<Order> discountlist;
    private ShopOrderController controller;
    RelativeLayout layout;
    private OrderAdapter manageProductsListAdapter;
    RelativeLayout rlViewProducts;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_shop_order, container, false);
        initUi();

        return view;
    }

    private void initUi() {
        controller = new ShopOrderController(getContext());
        layout = (RelativeLayout) view.findViewById(R.id.layout_ordersshop);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        tv_nodata = view.findViewById(R.id.tvNoData);
        rv = (RecyclerView) view.findViewById(R.id.rvorder);
        layout = (RelativeLayout) view.findViewById(R.id.rlmanageemp);
        toolbar.setVisibility(View.GONE);

    }

    private void loadData() {
        try {
            discountlist = controller.getAll_UserId(String.valueOf(UserPref.getUserId(getActivity())));
//            Toast.makeText(getActivity(), ""+discountlist.size(), Toast.LENGTH_SHORT).show();
            if (discountlist.size() == 0) {
//                Helper.makeSnackBar(layout, "No Data");
                Toast.makeText(getContext(),"No Data", Toast.LENGTH_SHORT).show();

            } else {
                setUpRecyclerView();
            }

        } catch (Exception e) {
            e.printStackTrace();
//            Helper.makeSnackBar(layout, Constants.SOMETHING_WENT_WRONG);
            Log.d("RESPONSE",e.getMessage());
//            Toast.makeText(getContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void setUpRecyclerView() {
        try {
            if (manageProductsListAdapter != null) {
                manageProductsListAdapter.updateOrderList(discountlist);
            } else {
                manageProductsListAdapter = new OrderAdapter(getActivity(),
                        discountlist);
                rv.setAdapter(manageProductsListAdapter);
                rv.setLayoutManager(Helper.getVerticalManager(getActivity()));
                manageProductsListAdapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlViewProducts, Constants.SOMETHING_WENT_WRONG);

        }
    }
}