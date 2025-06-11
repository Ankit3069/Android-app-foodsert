package com.example.personalizedecommerceapp.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.Shop.MapActivity;
import com.example.personalizedecommerceapp.adapter.ViewShopListAdapter;
import com.example.personalizedecommerceapp.controller.BaseController;
import com.example.personalizedecommerceapp.controller.ShopController;
import com.example.personalizedecommerceapp.interfaces.IController;
import com.example.personalizedecommerceapp.model.Shop;
import com.example.personalizedecommerceapp.util.Constants;
import com.example.personalizedecommerceapp.util.Helper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class FragmentHomeScreen extends Fragment implements TextView.OnEditorActionListener,
        TextWatcher, View.OnClickListener, FilterDialogFragment.FilterDialogInterface{


    private RelativeLayout rlHomeLayout;
    private RecyclerView rvShop;
    private TextView tvNoData;

    private EditText etSearch;
    private ImageView ivCloseSearch, ivFilter;
    private FilterDialogFragment filterDialogFragment;
    private int filterSelectedPosition = -1;

    private Context context;
    private List<Shop> mainShopList;
    private List<Shop> shopList;
    private ViewShopListAdapter shopListAdapter;
    private IController<Shop> controller;

    public SwitchCompat locswitch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);
        initUI(view);
        initListeners();
        initObj();
        return view;
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

    private void initUI(View view) {
        rlHomeLayout = view.findViewById(R.id.rlHomeLayout);
        rvShop = view.findViewById(R.id.rvShop);
        tvNoData = view.findViewById(R.id.tvNoData);

        etSearch = view.findViewById(R.id.etSearch);
        ivCloseSearch = view.findViewById(R.id.ivCloseSearch);
        ivFilter = view.findViewById(R.id.ivFilter);
        locswitch=view.findViewById(R.id.btnNearby);
        locswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    loadData();
                }
                else {
                    loadData();
                }
            }
        });
    }

    private void initListeners() {
        etSearch.setOnEditorActionListener(this);
        etSearch.addTextChangedListener(this);
        ivCloseSearch.setOnClickListener(this);
        ivFilter.setOnClickListener(this);
    }

    private void initObj() {
        mainShopList = new ArrayList<>();
        shopList = new ArrayList<>();
        controller = BaseController.getController(Constants.SHOP, context);
        filterDialogFragment = new FilterDialogFragment(this,
                R.array.filters, true);
    }

    private void loadData() {
        try {
            if(locswitch.isChecked())
            {
                ShopController sp=new ShopController(getActivity());
                shopList = sp.getAll_byloc(getActivity());
            }
            else {
                shopList = controller.getAll();
            }

            mainShopList = shopList;
            if (shopList.size() > 0) {
                setUpRecyclerView();
            } else {
                setDataVisibility(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlHomeLayout, Constants.SOMETHING_WENT_WRONG);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setUpRecyclerView() {
        try {
            shopListAdapter = new ViewShopListAdapter(context, shopList);
            rvShop.setAdapter(shopListAdapter);
            rvShop.setLayoutManager(Helper.getVerticalManager(context));
            shopListAdapter.notifyDataSetChanged();
            setDataVisibility(true);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.makeSnackBar(rlHomeLayout, Constants.SOMETHING_WENT_WRONG);
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

    @Override
    public void setUserVisibleHint(boolean isVisible) {
        super.setUserVisibleHint(isVisible);
        if (isVisible) {
            assert getFragmentManager() != null;
            FragmentTransaction ftr = getFragmentManager().beginTransaction();
            ftr.detach(this).attach(this).commit();
            if (context != null) loadData();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        try {
            String userInput = charSequence.toString().toLowerCase().trim();
            if (userInput.isEmpty()) resetSearch();
            else filterList(userInput);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void resetSearch() {
        etSearch.clearFocus();
        if (shopListAdapter != null) {
            shopListAdapter.setValues(mainShopList);
        }
    }

    private void filterList(String userInput) {
        List<Shop> filteredList = new ArrayList<>();
        for (Shop shop : mainShopList) {
            if (isMatchingInput(userInput, shop)) {
                filteredList.add(shop);
            }
        }
        shopListAdapter.setValues(filteredList);
        setDataVisibility(!filteredList.isEmpty());
    }

    private boolean isMatchingInput(String userInput, Shop shop) {
        String filterBy = getFilterBy();

        if (shop == null || userInput == null) {
            return false;
        }
        switch (filterBy) {
            case "shopType":
                return shop.getShopType() != null && shop.getShopType().toLowerCase().contains(userInput);
            case "cuisineCategory":
                return shop.getCuisineCategory() != null && shop.getCuisineCategory().toLowerCase().contains(userInput);
            case "price":
                return shop.getPrice() != null && shop.getPrice().toLowerCase().contains(userInput);
            default:
                return shop.getShopName() != null && shop.getShopName().toLowerCase().contains(userInput);
        }
    }


    private String getFilterBy() {
        switch (filterSelectedPosition) {
            case 1:
                return "shopType";
            case 2:
                return "cuisineCategory";
            case 3:
                return "price";
            default:
                return "name";
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
        try {
            if (editable.toString().length() == 0) {
                Helper.closeKeyboard(context, etSearch);
                etSearch.clearFocus();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ivCloseSearch) {
            onClickIvClose();
        } else if (id == R.id.ivFilter) {
            onClickIvFilter();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Helper.closeKeyboard(context, etSearch);
            if (etSearch.getText().toString().length() == 0) {
                etSearch.clearFocus();
                etSearch.setText("");
                ivCloseSearch.setImageBitmap(null);
                if (shopListAdapter != null) {
                    shopListAdapter.setValues(mainShopList);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void onSubmitClick(View view, int position) {
        if (filterDialogFragment.isVisible()) {
            filterDialogFragment.dismiss();
        }
        filterSelectedPosition = position;
    }

    @Override
    public void onClearClickFilter() {
        if (filterDialogFragment.isVisible()) {
            filterDialogFragment.dismiss();
        }
        filterSelectedPosition = -1;
        filterDialogFragment.setFilterSelectedPosition(filterSelectedPosition);
        loadData();
    }

    private void onClickIvClose() {
        Helper.closeKeyboard(context, etSearch);
        etSearch.clearFocus();
        etSearch.setText("");
        ivCloseSearch.setImageBitmap(null);
    }

    private void onClickIvFilter() {
        if (!filterDialogFragment.isVisible())
            filterDialogFragment.show(getChildFragmentManager(), "SortDialog");
    }
}
