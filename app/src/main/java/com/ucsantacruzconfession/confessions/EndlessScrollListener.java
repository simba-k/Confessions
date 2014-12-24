package com.ucsantacruzconfession.confessions;

import android.widget.AbsListView;

public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
	private int visibleThreshold = 5;
	private int currentPage = 0;
	private int previousTotalItemCount = 0;
	private boolean loading = true;
	private int startingPageIndex = 0;
	private String query;

	public EndlessScrollListener(String query){
		this.query = query;
	}

	public EndlessScrollListener(int visibleThreshold){
		this.visibleThreshold = visibleThreshold;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount){
		if(loading && (totalItemCount > previousTotalItemCount)){
			loading = false;
			previousTotalItemCount = totalItemCount;
			currentPage++;
		}

		if(!loading && (totalItemCount - visibleItemCount)<= (firstVisibleItem + visibleThreshold)){
			onLoadMore(currentPage, query);
			loading = true;
		}
	}

	public abstract void onLoadMore(int page, String query);

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState){}
}
