package com.computing.millenium.springers.livedeadcounter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mike on 6/23/2015.
 */
public class TotalCountSingleton {
    private static final String TAG = "TotalCountSingleton";
    private static final String FILENAME = "Counts.json";
    private ArrayList<TotalCount> mTotalCounts;
    private CountsJSONSerializer mSerializer;

    private static TotalCountSingleton sTotalCountSingleton;
    private Context mAppContext;

    private TotalCountSingleton(Context appContext){
        mAppContext = appContext;
        mSerializer = new CountsJSONSerializer(mAppContext, FILENAME);
        try{
            mTotalCounts = mSerializer.loadCounts();
        } catch (Exception e){
            mTotalCounts = new ArrayList<TotalCount>();
            Log.e(TAG, "Error loading crimes: ", e);
        }
    }

    public static TotalCountSingleton get(Context c){
        if (sTotalCountSingleton == null){
            sTotalCountSingleton = new TotalCountSingleton(c.getApplicationContext());
        }
        return sTotalCountSingleton;
    }

    public ArrayList<TotalCount> getTotalCounts(){
        return mTotalCounts;
    }

    public TotalCount getTotalCount(UUID id){
        for (TotalCount c: mTotalCounts){
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }

    public void addCount(TotalCount c){
        mTotalCounts.add(c);
    }

    public boolean saveCounts() {
        try{
            mSerializer.saveCounts(mTotalCounts);
            return true;
        } catch(Exception e){
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }

}
