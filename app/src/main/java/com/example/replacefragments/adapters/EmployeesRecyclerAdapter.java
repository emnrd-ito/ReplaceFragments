package com.example.replacefragments.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.replacefragments.R;
import com.example.replacefragments.fragments.FragmentChange;
import com.example.replacefragments.fragments.FragmentChangeEvent;
import com.example.replacefragments.model.EmployeeDataParcelable;
import com.example.replacefragments.model.EmployeeGeneratedData;

import java.util.ArrayList;
import java.util.List;

import static com.example.replacefragments.utilities.FakeDataUtilities.getFakeEmployeeData;
import static com.example.replacefragments.utilities.FakeDataUtilities.getRandomEmployee;

public class EmployeesRecyclerAdapter extends RecyclerView.Adapter<EmployeesRecyclerAdapter.VerticalItemHolder> {

    private final String TAG = "EmployeesRecyclerAdapte";

    private ArrayList<RowItem> mRowItems = new ArrayList<>();

    private AdapterView.OnItemClickListener mOnItemClickListener;

    private final Context mContext;

    // Can contain all employees, employees at a location, or employees at a division
    private ArrayList<EmployeeGeneratedData> employeeArrayList;
    private FragmentChange mFragmentChange;

    public EmployeesRecyclerAdapter(Context context, FragmentChange fragmentChange) {
        Log.d(TAG, "constructor: " + this + " context: " + context);
        mContext = context;
        mFragmentChange = fragmentChange;
    }

    /*
     * A common adapter modification or reset mechanism. As with ListAdapter,
     * calling notifyDataSetChanged() will trigger the RecyclerView to update
     * the view. However, this method will not trigger any of the RecyclerView
     * animation features.
     *
     * was setItemCount(int count) for dummy data
     */
    public void addEmployeesToRowItems() {
        mRowItems.clear();
        List<RowItem> rowItemList = getEmployeeList();
        Log.d(TAG, "RecycleAdapter: " + this);
        Log.d(TAG, "addAll(): rowItemList: " + rowItemList.size());
        mRowItems.addAll(rowItemList);

        notifyDataSetChanged();
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.row_view, container, false);

        return new VerticalItemHolder(root, this);
    }

    // keep code in here to a minimum otherwise scrolling can become slow
    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        RowItem item = mRowItems.get(position);

        // Get first part (string) of row1, up to comma. This is the Division
        //String[] parts = item.row1.split("\\,"); // String array, each element is text between comma
        //String division = parts[0];    // Text before the first dot
        String division = item.row1;

        itemHolder.setRow0(item.row0);
        itemHolder.setRow1(item.row1);
        itemHolder.setRow2(item.row2);
        itemHolder.setRow3(item.row3);
    }

    @Override
    public int getItemCount() {
        return mRowItems.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            Log.d(TAG, "onItemHolderClick: " + itemHolder.toString() );

            // display individual employee

            FragmentManager fragmentManager = ((FragmentActivity)mContext).getSupportFragmentManager();

            EmployeeGeneratedData employeeGeneratedData = getRandomEmployee();

            // make it parcelable for the bundle
            EmployeeDataParcelable employeeDataParcelable = new EmployeeDataParcelable(employeeGeneratedData);

            FragmentChangeEvent fragmentChangeEvent = new FragmentChangeEvent(null);
            fragmentChangeEvent.setPosition(FragmentChange.FRAGMENT_INDIVIDUAL);
            fragmentChangeEvent.setEmployeeDataParcelable(employeeDataParcelable);

            mFragmentChange.onFragmentChange(fragmentChangeEvent);

            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());

        }
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;

        private TextView row0; // name of person, location, or division
        private TextView row1; // address, division
        private TextView row2; // employee position, city/state/zip
        private TextView row3; // location only - phone

        private EmployeesRecyclerAdapter mAdapter;

        public VerticalItemHolder(View itemView, EmployeesRecyclerAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            imageView = (ImageView) itemView.findViewById(R.id.logo);

            row0 = (TextView) itemView.findViewById(R.id.row0);
            row1 = (TextView) itemView.findViewById(R.id.row1);
            row2 = (TextView) itemView.findViewById(R.id.row2);
            row3 = (TextView) itemView.findViewById(R.id.row3);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setImage(Drawable drawable) { this.imageView.setImageDrawable(drawable);}

        public void setRow0(CharSequence row0) {
            this.row0.setText(row0);
        }

        public void setRow1(CharSequence row1) {
            this.row1.setText(row1);
        }

        public void setRow2(CharSequence row2) {
            this.row2.setText(row2);
        }

        public void setRow3(CharSequence row3) {this.row3.setText(row3); }
    }

    private List<RowItem> getEmployeeList() {
        ArrayList<RowItem> items = new ArrayList<RowItem>();

        if (employeeArrayList == null) {
            employeeArrayList = getFakeEmployeeData();
        }

        if (employeeArrayList != null) {
            for (EmployeeGeneratedData employeeGeneratedData : employeeArrayList) {

                items.add(
                        new RowItem(
                                employeeGeneratedData.getName(),
                                employeeGeneratedData.getDivision(),
                                employeeGeneratedData.getTitle(),
                                ""));// nothing on 4th row
            }
        }
        return items;
    }

}
