package com.computing.millenium.springers.livedeadcounter;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Mike on 7/17/2015.
 */
public class AudioPlayer {

    private MediaPlayer mPlayer;

    public void stop(){
        if (mPlayer != null){
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void load(Context c, int id){
            mPlayer = MediaPlayer.create(c, id);
        try{
            mPlayer.prepare();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void play(){
        if(mPlayer != null)
            mPlayer.start();
    }
}
