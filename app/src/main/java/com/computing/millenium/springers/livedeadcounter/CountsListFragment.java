package com.computing.millenium.springers.livedeadcounter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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
                android.R.layout.simple_list_item_2,android.R.id.text1, mTotalCounts)
        {
            @Override
           public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView headerText = (TextView) view.findViewById(android.R.id.text1);
                TextView dateText = (TextView) view.findViewById(android.R.id.text2);

                headerText.setText(mTotalCounts.get(position).getTitle());
                dateText.setText(mTotalCounts.get(position).getDateString(getActivity()));

                return view;
            }
        };

        setListAdapter(adapter);

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TotalCount totalCount = mTotalCounts.get(position);
        Intent intent = new Intent(getActivity(), TotalCountDetailsActivity.class);
        intent.putExtra(TotalCountDetailsFragment.EXTRA_ID, totalCount.getId());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ArrayAdapter<TotalCount>)getListAdapter()).notifyDataSetChanged();
    }
}
