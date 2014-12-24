package com.ucsantacruzconfession.confessions;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by simba on 11/10/14.
 */
public class Result {
    private String date;
    private String url;
    private String confession;
    public Result(JSONObject json){
        try {
            date = json.getString("date");
            date = date.substring(date.indexOf('-') + 1, date.lastIndexOf(':'));
            confession = json.getString("confession");
            url = json.getString("id");
        }catch(Exception e){
            Log.e("RESULTS", e.getMessage());
        }
    }

    public Result(String confession, String url, String date){
        this.confession = confession;
        this.url = url;
        this.date = date;
    }

    public String getConfession(){
        return confession;
    }

    public String getID(){
        return url;
    }

    public String getDate() {
        return date;
    }
}
