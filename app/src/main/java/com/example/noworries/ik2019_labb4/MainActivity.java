package com.example.noworries.ik2019_labb4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements AboutFragment.OnFragmentInteractionListener, ButtonFragment.OnFragmentInteractionListener {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);

        drawerToggle = setupDrawerToggle();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.setDrawerListener(drawerToggle);

        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        ViewTreeObserver viewTreeObserver = mDrawer.getViewTreeObserver();
        if (viewTreeObserver != null) {
            viewTreeObserver.addOnPreDrawListener(new PreDraw(mDrawer));
        }
    }

    @Override
    protected void onResume() {
        //View view = getLayoutInflater().inflate(R.layout.activity_main, nvDrawer);
        //view.invalidate();
        //getWindow().getDecorView().findViewById(R.id.drawer_layout).invalidate();
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                System.out.println("onDrawerOpened");
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
                String displayName = sharedPreferences.getString("example_text", "DEFAULT");

                textView = (TextView) findViewById(R.id.headerText);
                textView.setText(displayName);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

    }

    private class PreDraw implements ViewTreeObserver.OnPreDrawListener {
        public PreDraw(DrawerLayout mDrawer) {

        }

        @Override
        public boolean onPreDraw() {
            System.out.println("onPreDraw");
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
            String displayName = sharedPreferences.getString("example_text", "DEFAULT");

            System.out.println("onPredraw displayName: " + displayName);

            textView = (TextView) findViewById(R.id.headerText);
            textView.setText(displayName);

            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Make sure this is the method with just `Bundle` as the signature
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the planet to show based on
        // position
        Fragment fragment = null;

        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                fragmentClass = ButtonFragment.class;
                break;
            case R.id.nav_second_fragment:
                fragmentClass = AboutFragment.class;
                break;
            case R.id.nav_third_fragment:
                fragmentClass = AboutFragment.class;
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            default:
                fragmentClass = ButtonFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item, update the title, and close the drawer
        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        mDrawer.closeDrawers();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}