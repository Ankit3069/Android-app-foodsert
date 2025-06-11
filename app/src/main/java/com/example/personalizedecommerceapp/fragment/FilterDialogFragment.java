package com.example.personalizedecommerceapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.example.personalizedecommerceapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class FilterDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener {

    private static final String TAG = "FilterDialog";


    private ListView lvFilterDialogLayout;
    private Button btnClearFilter;

    private ArrayAdapter<String> arrayAdapter;

    private int filterSelectedPosition = -1;
    private final int resource;
    private final boolean shouldShowClearFilter;

    private final FilterDialogInterface filterDialogInterface;

    public FilterDialogFragment(FilterDialogInterface filterDialogInterface
            , int resourceId, boolean showClearFilter) {
        this.filterDialogInterface = filterDialogInterface;
        this.resource = resourceId;
        this.shouldShowClearFilter = showClearFilter;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initUI(view);
        loadFilterTypes();
    }


    @SuppressLint("SetTextI18n")
    private void initUI(View view) {
        lvFilterDialogLayout = view.findViewById(R.id.lvFilterDialogLayout);
        btnClearFilter = view.findViewById(R.id.btnClearFilter);
        btnClearFilter.setVisibility(shouldShowClearFilter ? View.VISIBLE : View.GONE);
        btnClearFilter.setOnClickListener(this);
    }

    private void loadFilterTypes() {
        arrayAdapter = new SimpleListAdapter(getActivity(), R.layout.filter_list_item
                , getResources().getStringArray(resource));
        lvFilterDialogLayout.setAdapter(arrayAdapter);
    }

    @Override
    public void show(@NonNull FragmentManager manager, @Nullable String tag) {
        super.show(manager, tag);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnClearFilter) {
            filterSelectedPosition = -1;
            filterDialogInterface.onClearClickFilter();
        }
    }

    private class SimpleListAdapter extends ArrayAdapter<String> {
        private final String[] list;
        private RadioButton mSelectedButtons;

        public SimpleListAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);
            list = objects;
        }

        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_list_item,
                        parent, false);
                viewHolder.view = convertView;
                viewHolder.mListItem = convertView.findViewById(R.id.rbFilter);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Log.d(TAG, "getView: Position : " + filterSelectedPosition);


            viewHolder.mListItem.setOnClickListener(view -> {
                if (position != filterSelectedPosition) {
                    viewHolder.mListItem.setChecked(false);
                }
                filterSelectedPosition = position;
                mSelectedButtons = (RadioButton) view;
                onItemClicked(view);

            });

            viewHolder.mListItem.setText(list[position]);

            if (filterSelectedPosition == position) {
                viewHolder.mListItem.setChecked(true);
            } else {
                viewHolder.mListItem.setChecked(false);

                if (mSelectedButtons != null && viewHolder.mListItem != mSelectedButtons) {
                    mSelectedButtons = viewHolder.mListItem;
                }
            }

            return convertView;
        }

        private class ViewHolder {
            private View view;
            private RadioButton mListItem;
        }
    }

    private void onItemClicked(View view) {
        synchronized (this) {
            arrayAdapter.notifyDataSetChanged();
            filterDialogInterface.onSubmitClick(view, filterSelectedPosition);
        }
    }

    public void setFilterSelectedPosition(int position) {
        this.filterSelectedPosition = position;
    }

    public interface FilterDialogInterface {
        void onSubmitClick(View view, int position);

        void onClearClickFilter();
    }
}
