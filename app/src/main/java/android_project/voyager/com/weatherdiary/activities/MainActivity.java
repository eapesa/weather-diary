package android_project.voyager.com.weatherdiary.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import android_project.voyager.com.weatherdiary.R;
import android_project.voyager.com.weatherdiary.fragments.ForecastDiaryFragment;
import android_project.voyager.com.weatherdiary.fragments.HomeFragment;
import android_project.voyager.com.weatherdiary.fragments.MarkPlacesFragment;
import android_project.voyager.com.weatherdiary.fragments.NavigationDrawerFragment;


public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weatherdiary_main_activity);

        initializeViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(mTitle);

            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    /*
         * Initializers
         */
    private void initializeViews() {
        mTitle = getString(R.string.app_name);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /*
     * Navigation Drawer Methods
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, MarkPlacesFragment.newInstance(position))
                        .commit();
                break;
            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ForecastDiaryFragment.newInstance(position))
                        .commit();
                break;
            default:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, HomeFragment.newInstance(position))
                        .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
     * Custom methods
     */
    public void getSectionTitle(int position) {
        switch (position) {
            case 0:
                mTitle = getString(R.string.app_name);
                break;
            case 1:
                mTitle = getString(R.string.weatherdiary_navdrawer_section2_label);
                break;
            case 2:
                mTitle = getString(R.string.weatherdiary_navdrawer_section3_label);
                break;
            default:
                mTitle = getString(R.string.app_name);
        }
    }
}
