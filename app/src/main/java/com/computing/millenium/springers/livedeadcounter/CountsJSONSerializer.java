package com.computing.millenium.springers.livedeadcounter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Mike on 6/28/2015.
 */
public class CountsJSONSerializer {
    private Context mContext;
    private String mFilename;

    public CountsJSONSerializer(Context c, String f){
        mContext = c;
        mFilename = f;
    }

    public void saveCounts(ArrayList<TotalCount> counts)throws JSONException, IOException {
        //Build an array in JSON
        JSONArray array = new JSONArray();
        for (TotalCount c: counts){
            array.put(c.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try{
            OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        }finally{
            if (writer != null){
                writer.close();
            }
        }

    }

    public ArrayList<TotalCount> loadCounts() throws IOException, JSONException {
        ArrayList<TotalCount> counts = new ArrayList<TotalCount>();
        BufferedReader reader = null;
        try{
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFilename);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < array.length(); i++){
                counts.add(new TotalCount(array.getJSONObject(i)));
            }
        } catch (FileNotFoundException e){
            //Ignore
        } finally {
            if (reader != null)
                    reader.close();
        }
        return counts;
    }

}
