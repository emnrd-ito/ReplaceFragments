// Singleton
package com.example.replacefragments.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.EmptyStackException;
import java.util.Stack;

public class FragmentChange implements FragmentChangeListener {

    private final String TAG = "FragmentChange";

    // all the possible fragments to display
    // note that 0 - 3 correspond to MainActivity.onNavigationDrawerItemSelected(int position)
    static final public int FRAGMENT_POP = -1;
    static final public int FRAGMENT_EMPLOYEE_LIST = 0;
    static final public int FRAGMENT_LOCATIONS_LIST = 1;
    static final public int FRAGMENT_DIVISIONS_LIST = 2;
    static final public int FRAGMENT_ABOUT = 3;
    static final public int FRAGMENT_DIVISIONS_LIST_FAN = 4;
    static final public int FRAGMENT_LOCATIONS_EMPLOYEE_LIST = 5;
    static final public int FRAGMENT_DIVISIONS_EMPLOYEE_LIST = 6;
    static final public int FRAGMENT_INDIVIDUAL = 7;
    //static final public int FRAGMENT_ROUTE = 7;

    private Stack<FragmentChangeEvent> fragmentStack = new Stack<>();

    private static FragmentChange instance;
    private int mPosition = 0;
    private FragmentManager mFragmentManager;
    private String version = "0";

    public FragmentChange(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    @Override
    public void onFragmentChange(FragmentChangeEvent fragmentChangeEvent) {
        // update the main content by replacing fragments
        // note that the swap of individual employee fragment is done in adapter.onItemHolderClick()
        //FragmentManager fragmentManager = getSupportFragmentManager();

        mPosition = fragmentChangeEvent.getPosition();

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        switch (mPosition) {
            case FRAGMENT_EMPLOYEE_LIST: // App start or from onNavigationDrawerItemSelected
                Fragment fragment = EmployeesVerticalFragment.newInstance();
                fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                fragmentStack.clear();  // start over with the stack at the All Employee list
                fragmentStack.push(fragmentChangeEvent);
                break;
            case FRAGMENT_ABOUT: // About (from onNavigationDrawerItemSelected)
                version = fragmentChangeEvent.getVersion();
                Bundle bundle = new Bundle();
                bundle.putString("version", version);
                fragmentTransaction.replace(com.example.replacefragments.R.id.container, AboutFragment.newInstance(bundle));
                break;
            case FRAGMENT_LOCATIONS_EMPLOYEE_LIST: // Locations List +> Location Employees or back to All Employees
                String location = fragmentChangeEvent.getLocationName();

                // if (location.equalsIgnoreCase(getResources().getString(R.string.santa_fe)));
                // If location is Santa Fe, we need to restrict by Division as well
                if (location.equalsIgnoreCase("Santa Fe")) {
                    String division = fragmentChangeEvent.getDivisionName();
                    fragment = EmployeesVerticalFragment.newInstance(
                            EmployeesVerticalFragment.RESTRICT_BY_LOCATION_AND_DIVISION,
                            fragmentChangeEvent.getLocationName(),
                            fragmentChangeEvent.getDivisionName());
                }
                else {
                    fragment = EmployeesVerticalFragment.newInstance(EmployeesVerticalFragment.RESTRICT_BY_LOCATION, fragmentChangeEvent.getLocationName());
                }

                fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                fragmentStack.push(fragmentChangeEvent);
                break;
            case FRAGMENT_DIVISIONS_EMPLOYEE_LIST: // Divisions List => Division Employees or back to All Employees
                fragment = EmployeesVerticalFragment.newInstance(EmployeesVerticalFragment.RESTRICT_BY_DIVISION, fragmentChangeEvent.getDivisionName());
                fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                fragmentStack.push(fragmentChangeEvent);
                break;
            case FRAGMENT_INDIVIDUAL: // display an individual
                fragment = IndividualFragment.newInstance(fragmentChangeEvent.getEmployeeDataParcelable());
                fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                break;

            default:
                break; // do nothing
        }

        fragmentTransaction.commit(); // IllegalStateException: Activity has been destroyed
        //fragmentTransaction.commitAllowingStateLoss(); // IllegalStateException: Activity has been destroyed

//        if (mFragmentChangeListener != null)
//            mFragmentChangeListener.onFragmentChange(event); // event object :)
    }

    @Override
    public boolean onFragmentPop(FragmentChangeEvent fragmentChangeEvent) {
        //int gotoFragment = FRAGMENT_POP;
        boolean callSuper = true;

        try {
            FragmentChangeEvent gotofragmentChangeEvent = fragmentStack.pop();
            Log.d(TAG, "Pop: gotofragmentChangeEvent.getPosition(): " + gotofragmentChangeEvent.getPosition());
            if (gotofragmentChangeEvent.getPosition() >= 0) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                switch (gotofragmentChangeEvent.getPosition()) {
                    case FRAGMENT_EMPLOYEE_LIST: // All Employees => quit app?
                        Fragment fragment = EmployeesVerticalFragment.newInstance();
                        fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                        fragmentStack.clear();  // start over with the stack at the All Employee list
                        break;
                    case FRAGMENT_DIVISIONS_LIST_FAN: // Divisions => All Employees
                        fragment = EmployeesVerticalFragment.newInstance();
                        fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                        fragmentStack.clear();  // start over with the stack at the All Employee list
                        break;
                    case FRAGMENT_ABOUT: // currently on About
                        version = fragmentChangeEvent.getVersion();
                        Bundle bundle = new Bundle();
                        bundle.putString("version", version);

                        fragmentTransaction.replace(com.example.replacefragments.R.id.container, AboutFragment.newInstance(bundle));
                        break;
                    case FRAGMENT_INDIVIDUAL: // Individual => Employee list (all, locations, or divisions)
                        fragment = IndividualFragment.newInstance(fragmentChangeEvent.getEmployeeDataParcelable());
                        fragmentTransaction.replace(com.example.replacefragments.R.id.container, fragment);
                        fragmentStack.push(fragmentChangeEvent);
                        break;
                    default:
                        break; // do nothing
                }

                fragmentTransaction.commit();
                callSuper = false;

            }
            // else do nothing, exit the app

        }
        catch (EmptyStackException e) {
            // nothing to pop, so can exit the app
            Log.d(TAG, "exception: " + e.getLocalizedMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "exception: " + e.getLocalizedMessage());
        }

       return callSuper;

    }

}
