package com.computing.millenium.springers.livedeadcounter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Mike on 6/25/2015.
 */
public class CountsListActivity extends FragmentActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counts_list);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.counts_list_fragment);
        if (fragment==null){
            fragment = new CountsListFragment();
            fm.beginTransaction()
                    .add(R.id.counts_list_fragment, fragment)
                    .commit();
        }
    }
}
