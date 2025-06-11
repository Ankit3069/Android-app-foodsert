package com.example.personalizedecommerceapp.fragment;

import static com.example.personalizedecommerceapp.util.Constants.COLUMN_CUISINE_CATEGORY;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.activity.PreferenceActivity;
import com.example.personalizedecommerceapp.adapter.ViewShopListAdapter;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.controller.RegistrationController;
import com.example.personalizedecommerceapp.controller.ShopController;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.example.personalizedecommerceapp.util.UserPref;

import java.util.ArrayList;
import java.util.List;

public class RecommendationFragment extends Fragment
{
    private RecyclerView rvShop;
    private TextView tvNoData;

    private List<Shop> shopList;
    private ViewShopListAdapter shopListAdapter;
    private IController<Shop> controller;

    Context context;

    View view;

    Cursor c;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recommendationfragment, container, false);
        initUI(view);
        initObj();
        return view;
    }

    private void initUI(View view) {
        rvShop = view.findViewById(R.id.rvShop);
        tvNoData = view.findViewById(R.id.tvNoData);
    }

    private void initObj() {
        shopList = new ArrayList<>();
        controller = BaseController.getController(Constants.SHOP, context);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (shopListAdapter != null) {
            setDataVisibility(false);
            shopList.clear();
            rvShop.setAdapter(null);
        }
        if (context != null) loadData();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public boolean checkdata()
    {
        String uid= UserPref.getUserId(getActivity())+"";
        RegistrationController controller=new RegistrationController(getActivity());
        c=controller.getUserPreference(uid);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            String txt=c.getString(0);
            if(txt!=null && txt.length()>0)
            {
                return true;
            }
        }

        return false;
    }

    private void loadData() {
        try {

            if(checkdata())
            {
                ShopController sp=new ShopController(getActivity());
                shopList = sp.getRecommendation(getActivity(),c);

                if (shopList.size() > 0) {
                    setUpRecyclerView();
                } else {
                    setDataVisibility(false);
                }
            }
            else
            {
                setDataVisibility(false);
                Helper.makeSnackBar(view,"Please set your Preference, from the Preference section");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(view, Constants.SOMETHING_WENT_WRONG);
        }
    }

    private void setUpRecyclerView() {
        try {
            shopListAdapter = new ViewShopListAdapter(context, shopList);
            rvShop.setAdapter(shopListAdapter);
            rvShop.setLayoutManager(Helper.getVerticalManager(context));
            shopListAdapter.notifyDataSetChanged();
            setDataVisibility(true);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(view, Constants.SOMETHING_WENT_WRONG);
            setDataVisibility(false);
        }
    }

    private void setDataVisibility(boolean isDataAvailable) {
        if (isDataAvailable) {
            rvShop.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        } else {
            rvShop.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
    }
}
