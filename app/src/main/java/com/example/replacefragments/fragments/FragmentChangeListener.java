package com.example.replacefragments.fragments;


public interface FragmentChangeListener {
    void onFragmentChange(FragmentChangeEvent fragmentChangeEvent);
    boolean onFragmentPop(FragmentChangeEvent fragmentChangeEvent);

}
