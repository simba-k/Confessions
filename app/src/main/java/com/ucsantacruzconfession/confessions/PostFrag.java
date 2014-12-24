package com.ucsantacruzconfession.confessions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class PostFrag extends Fragment {

    private EditText confessionET;
	public static PostFrag newInstance() {
		PostFrag fragment = new PostFrag();
		return fragment;
	}

	public PostFrag() {
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.post_frag, container, false);
		confessionET = (EditText) rootView.findViewById(R.id.confessionText);
		TextView button = (TextView) rootView.findViewById(R.id.post);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                new PostHandle().execute(confessionET.getText().toString());
                confessionET.setText("");
            }
		});
		return rootView;
	}

    private class PostHandle extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            HttpURLConnection connection = null;
            try {
                URL url = new URL("http://ucsantacruzconfession.com/post");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                PrintWriter stream = new PrintWriter(connection.getOutputStream());
                stream.write("confession="+params[0]);
                stream.close();
                InputStream in = connection.getInputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while((bytesRead = in.read(buffer)) > 0)
                    ;
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                connection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void nothing){
            Toast.makeText(getActivity(), "Confession Submitted", Toast.LENGTH_LONG).show();
        }
    }


}
