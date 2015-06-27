package com.computing.millenium.springers.livedeadcounter;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Mike on 6/23/2015.
 */
public class TotalCountSingleton {
    private ArrayList<TotalCount> mTotalCounts;

    private static TotalCountSingleton sTotalCountSingleton;
    private Context mAppContext;

    private TotalCountSingleton(Context appContext){
        mAppContext = appContext;
        mTotalCounts = new ArrayList<TotalCount>();
//        for (int i = 0; i< 100; i++){
//            TotalCount c  = new TotalCount();
//            c.setTitle("Count # " + i);
//            mTotalCounts.add(c);
//        }
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
}
