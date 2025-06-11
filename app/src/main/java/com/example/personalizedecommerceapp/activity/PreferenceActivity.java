package com.example.personalizedecommerceapp.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.personalizedecommerceapp.R;
import com.example.personalizedecommerceapp.controller.RegistrationController;
import com.example.personalizedecommerceapp.util.DialogUtils;
import com.example.personalizedecommerceapp.util.UserPref;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PreferenceActivity extends AppCompatActivity {

    Dialog dialog;
    List<String> CuisineList = Arrays.asList(new String[]{"Afghan","American","Andhra","Asian","Assamese","Awadhi","BBQ","Bakery","Bar","Bengali","Beverages","Bihari","Biryani","Brazilian","Bubble","Burger","Burmese","Cafe","Chinese","Coffee","Continental","Cream","Deli","Desserts","Eastern","European","Fast","Finger","Food","Fusion","German","Goan","Greek","Grill","Gujarati","Healthy","Hot dogs","Hyderabadi","Ice","Indian","Indonesian","Israeli","Italian","Japanese","Juices","Kashmiri","Kebab","Kerala","Konkan","Korean","Lebanese","Lucknowi","Maharashtrian","Malaysian","Malwani","Mangalorean","Mediterranean","Mexican","Middle","Mithai","Modern","Momos","Mughlai","North","Pakistani","Parsi","Pizza","Rajasthani","Rolls","Salad","Sandwich","Seafood","Sindhi","South","Spanish","Steak","Street","Sushi","Tea","Thai","Tibetan","Turkish","Vietnamese","Wraps"});    List<String> ShoptypeList=Arrays.asList(new String[]{"Bakery", "Bar", "Café", "Casual Dining", "none", "Quick Bites", "Sweet Shop", "Beverage Shop", "Dessert Parlor", "Fine Dining", "Food Truck", "Dhaba", "Food Court", "Kiosk", "Lounge", "Pub", "Paan Shop", "Mess", "Bhojanalya", "Confectionery", "Irani Cafe", "Microbrewery", "CUSINE TYPE"});
    TextInputEditText shoptype,cuisinetype,minprice,maxprice;

    Button btnSubmit;
    String uid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferencedialog);
        initToolbar();
        initUI();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void initToolbar()
    {
        try {
            Toolbar toolbar = findViewById(R.id.toolbar);
            TextView tvTitle = toolbar.findViewById(R.id.tvTitle);
            ImageButton btnBack = toolbar.findViewById(R.id.btnBack);
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            tvTitle.setText("Your Preference");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) getSupportActionBar().setTitle("");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void initUI()
    {
        shoptype = findViewById(R.id.shoptype);
        cuisinetype = findViewById(R.id.cuisinetype);
        minprice = findViewById(R.id.minprice);
        maxprice = findViewById(R.id.maxprice);
        btnSubmit=findViewById(R.id.btnSubmit);
    }

    public void initListeners()
    {
        cuisinetype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_CusineDialog();
            }
        });

        shoptype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_ShopDialog();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid(v))
                {
                    Updatedata();
                }
            }
        });
    }

    public void loadData()
    {
        uid= UserPref.getUserId(PreferenceActivity.this)+"";
        RegistrationController controller=new RegistrationController(PreferenceActivity.this);
        Cursor c=controller.getUserPreference(uid);
        if(c.getCount()>0)
        {
            c.moveToFirst();
            shoptype.setText(c.getString(0));
            cuisinetype.setText(c.getString(1));
            minprice.setText(c.getString(2));
            maxprice.setText(c.getString(3));
        }
    }

    public void Updatedata()
    {
        RegistrationController controller=new RegistrationController(PreferenceActivity.this);
        controller.updatePreference(uid,shoptype.getText().toString(),cuisinetype.getText().toString(),minprice.getText().toString(),maxprice.getText().toString());
        Toast.makeText(this, "Preference Updated", Toast.LENGTH_SHORT).show();
    }

    public boolean valid(View v)
    {
        if(shoptype.getText().toString().length()==0)
        {
            Snackbar.make(v,"Choose Shop Type",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(cuisinetype.getText().toString().length()==0)
        {
            Snackbar.make(v,"Choose Cuisine Type",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(minprice.getText().toString().length()==0)
        {
            Snackbar.make(v,"Enter Min Price",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(maxprice.getText().toString().length()==0)
        {
            Snackbar.make(v,"Enter Max Price",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        else if(Integer.parseInt(minprice.getText().toString())>=Integer.parseInt(maxprice.getText().toString()))
        {
            Snackbar.make(v,"Min Price cannot be equal or greater then Max Price",Snackbar.LENGTH_SHORT).show();
            return false;
        }

        return  true;
    }

    private void open_CusineDialog () {
        DialogUtils.dismissDialog(dialog);
        final CharSequence[] items = new CharSequence[CuisineList.size()];
        for (int i = 0; i < CuisineList.size(); i++) {
            items[i] = CuisineList.get(i).toString();
        }
        final boolean[] checkedItems = new boolean[CuisineList.size()];
        dialog = openCheckboxDialog(PreferenceActivity.this, CuisineList,
                items, checkedItems, cuisinetype, dialog);
    }

    private void open_ShopDialog () {
        DialogUtils.dismissDialog(dialog);
        final CharSequence[] items = new CharSequence[ShoptypeList.size()];
        for (int i = 0; i < ShoptypeList.size(); i++) {
            items[i] = ShoptypeList.get(i).toString();
        }
        final boolean[] checkedItems = new boolean[ShoptypeList.size()];
        dialog = openCheckboxDialog(PreferenceActivity.this, ShoptypeList,
                items, checkedItems, shoptype, dialog);
    }

    public static Dialog openCheckboxDialog (Context context, List objectList, CharSequence[]
            items
            , boolean[] checkedItems, TextInputEditText textInputEditText, Dialog dialog){
        try {
            DialogUtils.dismissDialog(dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Select Items")
                    .setMultiChoiceItems(items, checkedItems, (dialog1, which, isChecked) -> {
                        checkedItems[which] = isChecked;
                    })
                    .setPositiveButton("OK", (dialog1, which) -> {
                        StringBuilder selectedItemsText = new StringBuilder();
                        for (int i = 0; i < objectList.size(); i++) {
                            if (checkedItems[i]) {
                                Object item = objectList.get(i);
                                selectedItemsText.append(item.toString());
                                selectedItemsText.append(", ");
                            }
                        }
                        if (selectedItemsText.length() > 2) {
                            selectedItemsText.delete(selectedItemsText.length() - 2, selectedItemsText.length());
                        }
                        textInputEditText.setText(selectedItemsText.toString());
                        dialog1.cancel();

                        // Set the TextInputEditText as non-editable again after updating the text
                        textInputEditText.setInputType(InputType.TYPE_NULL);
                    }).setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());
            dialog = builder.create();
            if (!((Activity) context).isFinishing()) dialog.show();

            List<String> enteredValues = new ArrayList<>();
            String enteredItemsString = Objects.requireNonNull(textInputEditText.getText()).toString();
            String[] enteredItemValues = enteredItemsString.split(",");
            for (String enteredItemValue : enteredItemValues) {
                enteredValues.add(enteredItemValue.trim());
            }

            for (int i = 0; i < objectList.size(); i++) {
                if (enteredValues.contains(objectList.get(i).toString())) {
                    checkedItems[i] = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dialog;
    }
}
