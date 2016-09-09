package com.computing.millenium.springers.livedeadcounter;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mike on 6/25/2015.
 */
public class CountsListFragment extends ListFragment {
    private ArrayList<TotalCount> mTotalCounts;
    private ShareActionProvider mShareActionProvider;


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
                //Change item layout so it incorporates a color change on selection
                view.setBackgroundDrawable(
                        getResources().getDrawable(R.drawable.counts_list_item_activated));
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = super.onCreateView(inflater, container, savedInstanceState);

        ListView listView = (ListView)v.findViewById(android.R.id.list);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB){
            registerForContextMenu(listView);
        } else {
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuInflater inflater = mode.getMenuInflater();
                    inflater.inflate(R.menu.counts_list_context, menu);

                    // Locate MenuItem with ShareActionProvider
                    MenuItem item = menu.findItem(R.id.menu_item_share);

                    // Fetch and store ShareActionProvider
                    mShareActionProvider = (ShareActionProvider) item.getActionProvider();
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    ArrayAdapter<TotalCount> adapter =
                            (ArrayAdapter<TotalCount>) getListAdapter();

                    TotalCountSingleton totalCountSingleton = TotalCountSingleton.get(getActivity());
                    switch (item.getItemId()) {
                        case R.id.menu_item_delete_count:

                            //For each item checked, delete from Singleton
                            for (int i = adapter.getCount() - 1; i >=0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    totalCountSingleton.deleteCount(adapter.getItem(i));
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;

                        case R.id.menu_item_share:
                            //For each item checked, delete from Singleton
                            for (int i = adapter.getCount() - 1; i >=0; i--) {
                                if (getListView().isItemChecked(i)) {
                                    //TODO: Create CSV from each item
                                }
                            }
                            mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                        }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }


        return v;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.counts_list_context, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        ArrayAdapter<TotalCount> adapter = (ArrayAdapter<TotalCount>) getListAdapter();
        TotalCount totalCount = adapter.getItem(position);

        switch (item.getItemId()){
            //Delete item selected
            case R.id.menu_item_delete_count:
                TotalCountSingleton.get(getActivity()).deleteCount(totalCount);
                adapter.notifyDataSetChanged();
                return true;

            //Share item selected
            case R.id.menu_item_share:
                //TODO: Get all counts selected
        }
        return super.onContextItemSelected(item);
    }

    // Call to update the share intent
    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
