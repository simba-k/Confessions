package com.ucsantacruzconfession.confessions;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchFrag extends ListFragment {

	public SearchFrag() {
	}

    @Override
    public void onStart() {
        reset("");
        super.onStart();
    }

    public void reset(String query){
		ResultAdapter adapter = new ResultAdapter();
		setListAdapter(adapter);
		getListView().setOnScrollListener(new EndlessScrollListener(query) {
			@Override
			public void onLoadMore(int page, String query) {
				new SearchHandle().execute(query, "" + page + 1);
			}
		});
		new SearchHandle().execute(query);
	}

    private Intent createFBViewConfessionIntent(String url) {
        PackageManager pm = getActivity().getPackageManager();
        Uri uri;
        try {
            pm.getPackageInfo("com.facebook.katana", 0);
            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
        } catch (PackageManager.NameNotFoundException e) {
            uri = Uri.parse(url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
        return intent;
    }
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Result clicked = (Result) getListAdapter().getItem(position);
        Log.d("TAG", clicked.getConfession() + " BLUG LBUBLUGLGUSULGDSULGSD");
		Intent intent = createFBViewConfessionIntent
                ("https://www.facebook.com/UCSCConfessions/posts/"+clicked.getID());
	    startActivity(intent);
	}

	private class ResultAdapter extends ArrayAdapter<Result> {
		public ResultAdapter(){
			super(getActivity(), 0);
		}
		public ResultAdapter(ArrayList<Result> results){
			super(getActivity(), 0, results);
		}

		public View getView(int position, View convert, ViewGroup group){
			if(convert == null) {
				convert = getActivity().getLayoutInflater().inflate(R.layout.search_item, null);
			}
			Result result = getItem(position);
			TextView text = (TextView) convert.findViewById(R.id.search_text);
            TextView date = (TextView) convert.findViewById(R.id.search_date);
			text.setText(result.getConfession());
            date.setText(result.getDate());
			return convert;
		}
	}

	private class SearchHandle extends AsyncTask<String, Void, ArrayList<Result>> {

		@Override
		protected ArrayList<Result> doInBackground(String... params) {
			HttpURLConnection connection = null;
			try {
				URL url = new URL("http://ucsantacruzconfession.com/rawsearch");
				connection = (HttpURLConnection) url.openConnection();
				connection.setDoOutput(true);
				PrintWriter stream = new PrintWriter(connection.getOutputStream());
				stream.write("query="+params[0]);
				if(params.length > 1){
					stream.write("&page="+params[1]);
				}
				stream.close();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				InputStream in = connection.getInputStream();
				int bytesRead = 0;
				byte[] buffer = new byte[1024];
				while((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}
				out.close();
				JSONArray arr = new JSONArray(new String(out.toByteArray()));
				return parseRawSearch(arr);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				connection.disconnect();
			}
			return new ArrayList<Result>();
		}

		private ArrayList<Result> parseRawSearch(JSONArray results){
			int size = results.length();
			ArrayList<Result> rtrn = new ArrayList<Result>(size);
			for(int i = 0; i < size; i++){
				try {
					rtrn.add(new Result(results.getJSONObject(i)));
                    
				}catch(Exception e){
					Log.e("CONFESSIONS ERR", "ERROR PARSING CONFESSION #" + i);
				}
			}
			return rtrn;
		}

		@Override
		protected void onPostExecute(ArrayList<Result> results){
			ResultAdapter adapter = ((ResultAdapter) getListAdapter());
			for(int i = 0; i < results.size(); i++){
				adapter.add(results.get(i));
			}
		}
	}
}
