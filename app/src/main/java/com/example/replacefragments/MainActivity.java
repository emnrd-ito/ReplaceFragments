package com.example.replacefragments;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.replacefragments.fragments.FragmentChange;
import com.example.replacefragments.fragments.FragmentChangeEvent;
import com.example.replacefragments.fragments.NavigationDrawerFragment;

import static com.example.replacefragments.fragments.FragmentChange.FRAGMENT_POP;

public class MainActivity extends AppCompatActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    private final String TAG = "MainActivity";

    // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private CharSequence mTitle;

    // only load fragment on fresh start
    // don't reload when re-displaying or orientation change
    private boolean reloadFragment = true;
    private FragmentChange mFragmentChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentChange = new FragmentChange(getSupportFragmentManager());
        // Data gets populated from SQLite here, usually

    } // end onCreate

    @Override
    protected void onRestart() { // not called on orientation change
        super.onRestart();
        // don't need to reload because instance state was saved by OS before onPause()
        reloadFragment = false;
    }

    @Override
    public void onResumeFragments() {
        super.onResumeFragments();

        Log.d(TAG, "onResumeFragments");

        // causes onNavigationDrawerItemSelected() to be called
        // which will instantiate the fragment EmployeesVerticalFragment
        // which instantiates the adapter
        Log.d(TAG, "setContentView");

        int changes = getChangingConfigurations();
        Log.d(TAG, "changes: " + changes);

        if (reloadFragment) {
            setContentView(com.example.replacefragments.R.layout.activity_main);
        }

        Log.d(TAG, "mNavigationDrawerFragment");
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(com.example.replacefragments.R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        Log.d(TAG, "mNavigationDrawerFragment.setUp");
        mNavigationDrawerFragment.setUp(
                com.example.replacefragments.R.id.navigation_drawer,
                (DrawerLayout) findViewById(com.example.replacefragments.R.id.drawer_layout));

    }

    // onStart() is called when device is rotated
    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {

        Log.d(TAG, "onNavigationDrawerItemSelected: position: " + position);

        // we may or may not be going to the About page, get version regardless
        String version = "0";
        try {
            version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; // in manifest
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        FragmentChangeEvent fragmentChangeEvent = new FragmentChangeEvent(null);
        fragmentChangeEvent.setPosition(position);
        fragmentChangeEvent.setVersion(version);
        mFragmentChange.onFragmentChange(fragmentChangeEvent);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(mTitle);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        FragmentChangeEvent fragmentChangeEvent = new FragmentChangeEvent(null);
        fragmentChangeEvent.setPosition(FRAGMENT_POP);
        boolean callSuper = mFragmentChange.onFragmentPop(fragmentChangeEvent);

        if (callSuper) {
            super.onBackPressed();
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("WORKAROUND_FOR_BUG_19917_KEY", "");
        super.onSaveInstanceState(outState);
    }

    public FragmentChange getFragmentChange() {
        return mFragmentChange;
    }
}
