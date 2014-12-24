package com.ucsantacruzconfession.confessions;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class Pager extends ActionBarActivity {
    private static final int NUM_OF_FRAGS = 2;
	private SearchView searchView = null;
	private ViewPager pager = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pager);
        pager = (ViewPager) findViewById(R.id.main_pager);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                //open/close search view on swipe right/left
                if(i == 0 && searchView != null && !searchView.isIconified()){
                    searchView.setIconified(true);
                }else if(i == 1 && searchView != null && searchView.isIconified()){
                    searchView.setIconified(false);
                    searchView.requestFocus();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        pager.setAdapter(new SwipeAdapter(getSupportFragmentManager()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        //handle search
        if(intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {
                ((SearchFrag) getSupportFragmentManager().findFragmentById(R.id.main_pager)).reset(query);
            }catch (Exception e) {
                Log.e("SEARCH CONFESSIONS", "FATAL ERROR findFragmentById");
            }
        }
        super.onNewIntent(intent);
    }

    @Override
    @TargetApi(11)
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.search, menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            MenuItem searchItem = menu.findItem(R.id.action_search_view);
            searchView = (SearchView) searchItem.getActionView();
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(1);
                }
            });
            SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
            ComponentName name = getComponentName();
            SearchableInfo info = searchManager.getSearchableInfo(name);
            searchView.setSearchableInfo(info);
            searchView.setSubmitButtonEnabled(true);
        }
        return true;
    }

    @Override
    public boolean onSearchRequested() {
        pager.setCurrentItem(1);
        return super.onSearchRequested();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search_view) {
            onSearchRequested();
            pager.setCurrentItem(1);
            return true;
        } else if (id == R.id.action_post) {
            searchView.setIconified(true);
            pager.setCurrentItem(0);
            return true;
        } else if(id == R.id.action_rules) {
            Intent intent = new Intent(this, Rules.class);
            startActivity(intent);
        } else if(id == R.id.action_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    static class SwipeAdapter extends FragmentPagerAdapter{

        public SwipeAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return PostFrag.newInstance();
                case 1:
                    return new SearchFrag();
            }
            return PostFrag.newInstance();
        }

        @Override
        public int getCount() {
            return NUM_OF_FRAGS;
        }
    }
}
