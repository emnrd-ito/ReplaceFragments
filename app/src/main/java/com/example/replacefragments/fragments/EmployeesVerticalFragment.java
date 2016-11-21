package com.example.replacefragments.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.replacefragments.R;
import com.example.replacefragments.adapters.EmployeesRecyclerAdapter;
import com.example.replacefragments.decoration.DividerDecoration;

public class EmployeesVerticalFragment extends RecyclerFragment {

    private final String TAG = "EmployeesVerticalFrame";

    public final static String RESTRICT_BY_NONE = "None"; // No restrictions, i.e. all employees
    public final static String RESTRICT_BY_LOCATION = "Location";
    public final static String RESTRICT_BY_DIVISION = "Division";
    public final static String RESTRICT_BY_LOCATION_AND_DIVISION = "LocationAndDivision";
    private static String listRestriction = RESTRICT_BY_NONE;

    private static String locationOrDivisonString = "NoString";
    private static String locationString = "NoLocation";
    private static String divisonString = "NoDivision";

    protected EmployeesRecyclerAdapter mAdapter;

    public EmployeesVerticalFragment () {
    }

    // can use this for All Employees list
    public static EmployeesVerticalFragment newInstance() {
        listRestriction = RESTRICT_BY_NONE;

        EmployeesVerticalFragment fragment = new EmployeesVerticalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static EmployeesVerticalFragment newInstance(String listRestriction, String restriction) {
        EmployeesVerticalFragment.listRestriction = listRestriction;
        EmployeesVerticalFragment.locationOrDivisonString = restriction;

        EmployeesVerticalFragment fragment = new EmployeesVerticalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static EmployeesVerticalFragment newInstance(String listRestriction,
                                                        String locationRestriction,
                                                        String divisionRestriction ) {
        EmployeesVerticalFragment.listRestriction = listRestriction;
        EmployeesVerticalFragment.locationString = locationRestriction;
        EmployeesVerticalFragment.divisonString = divisionRestriction;

        EmployeesVerticalFragment fragment = new EmployeesVerticalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // set title for action bar
        String title = getResources().getString(R.string.title_section1);
        if ( EmployeesVerticalFragment.listRestriction.equalsIgnoreCase(EmployeesVerticalFragment.RESTRICT_BY_LOCATION) ||
                EmployeesVerticalFragment.listRestriction.equalsIgnoreCase(EmployeesVerticalFragment.RESTRICT_BY_DIVISION )) {
            title += " - " + EmployeesVerticalFragment.locationOrDivisonString;
        }
        else if ( EmployeesVerticalFragment.listRestriction.equalsIgnoreCase(EmployeesVerticalFragment.RESTRICT_BY_LOCATION_AND_DIVISION)) {
            title += " - " + EmployeesVerticalFragment.locationString + " - " + EmployeesVerticalFragment.divisonString;
        }
        // else All Employees

        ActionBar actionBar = ((AppCompatActivity)context).getSupportActionBar();


        String device_size = getResources().getString(R.string.device_size);

        actionBar.setTitle(title + " : " + device_size);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.section_list);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.addItemDecoration(getItemDecoration());

        mRecyclerView.getItemAnimator().setAddDuration(1000);
        mRecyclerView.getItemAnimator().setChangeDuration(1000);
        mRecyclerView.getItemAnimator().setMoveDuration(1000);
        mRecyclerView.getItemAnimator().setRemoveDuration(1000);

        mAdapter = getAdapter(); // EmployeesRecyclerAdapter
        if (listRestriction.equalsIgnoreCase(RESTRICT_BY_NONE)) {
            mAdapter.addEmployeesToRowItems();
        }
//        else if (listRestriction.equalsIgnoreCase(RESTRICT_BY_LOCATION)) {
//            mAdapter.populateLocationEmployeeListFromSQLite(EmployeesVerticalFragment.locationOrDivisonString);
//        }
//        else if (listRestriction.equalsIgnoreCase(RESTRICT_BY_DIVISION)) {
//            mAdapter.populateDivisionEmployeeListFromSQLite(EmployeesVerticalFragment.locationOrDivisonString);
//        }
//        else if (listRestriction.equalsIgnoreCase(RESTRICT_BY_LOCATION_AND_DIVISION)) {
//            mAdapter.populateLocationAndDivisionEmployeeListFromSQLite(EmployeesVerticalFragment.locationString,
//                    EmployeesVerticalFragment.divisonString);
//        }
        else {
            mAdapter.addEmployeesToRowItems(); // shouldn't get here
        }

        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        Log.d(TAG, "mAdapter: " + mAdapter);

        return rootView;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        //We must draw dividers ourselves if we want them in a list
        return new DividerDecoration(getActivity());
    }

    @Override
    protected int getDefaultItemCount() {
        return 100;
    }

//    @Override
    protected EmployeesRecyclerAdapter getAdapter() {

        return new EmployeesRecyclerAdapter(getActivity());
    }
}
