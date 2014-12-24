package com.ucsantacruzconfession.confessions;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.TextView;


public class About extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new AboutFragment())
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class AboutFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_about, container, false);
            int[] ids = new int[]{R.id.mods_credit, R.id.spencer_credit, R.id.simba_credit, R.id.archie_credit,
                R.id.dylan_credit};
            String[] credits = new String[]{"<b>Mods:</b> Jenny Zhou, Adham Taman, Brian DeAngelis, Kong Vue",
                "<b>App Designer & Developer:</b><br/>Spencer Butterfield",
                "<b>Web & App Developer</b>:<br/>Wade 'Simba' Khadder",
                "<b>Web Designer & Developer<br/>Page Manager:</b><br/>Archavanich 'R-Chi' Kawmongkolsi",
                "<b>Marketing Strategy <br/> Page Manager:</b><br/>Dylan Quitiquit Hoffman"};
            for(int i = 0; i < ids.length; i++) {
                TextView tv = (TextView) rootView.findViewById(ids[i]);
                tv.setText(Html.fromHtml(credits[i]));
            }
            return rootView;
        }
    }
}
