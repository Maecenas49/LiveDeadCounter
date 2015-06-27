package com.computing.millenium.springers.livedeadcounter;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Mike on 6/25/2015.
 */
public class CountsListFragment extends ListFragment {
    private ArrayList<TotalCount> mTotalCounts;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTotalCounts = TotalCountSingleton.get(getActivity()).getTotalCounts();

        ArrayAdapter<TotalCount> adapter = new ArrayAdapter<TotalCount>(getActivity(),
                android.R.layout.simple_list_item_1, mTotalCounts);

        setListAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((ArrayAdapter<TotalCount>)getListAdapter()).notifyDataSetChanged();
    }
}
