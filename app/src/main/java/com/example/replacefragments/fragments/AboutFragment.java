package com.example.replacefragments.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.replacefragments.R;
import com.example.replacefragments.model.EmployeeDataParcelable;

public class AboutFragment extends Fragment {

    private String TAG = "AboutFragment";
    private static String BUNDLE_KEY = "EmployeeDataParcelable";

    public AboutFragment () {
    }

    public static AboutFragment newInstance(Bundle bundle) {
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static AboutFragment newInstance(EmployeeDataParcelable employeeDataParcelable) {
        AboutFragment fragment = new AboutFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_KEY, employeeDataParcelable);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // set title for action bar
        ActionBar actionBar = ((AppCompatActivity)context).getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.title_section4));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: " + container);
        // version text field
        Bundle bundle = this.getArguments();
        String version = bundle.getString("version", "0.0");
        View view = inflater.inflate(R.layout.about, container, false);

        TextView textView = (TextView) view.findViewById(R.id.version_number);
        textView.setText(version);

        // dates

        textView = (TextView) view.findViewById(R.id.employees_download_date);
        textView.setText("01:01:01");

        textView = (TextView) view.findViewById(R.id.locations_download_date);
        textView.setText("02:02:02");

        textView = (TextView) view.findViewById(R.id.divisions_download_date);
        textView.setText("03:03:03");

        return view;
    }

}
