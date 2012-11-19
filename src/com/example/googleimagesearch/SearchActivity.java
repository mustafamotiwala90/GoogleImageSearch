package com.example.googleimagesearch;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Mustafa
 * The secondary activity called after the search button is clicked with the input string
 * */



@SuppressLint({ "UseValueOf", "UseValueOf" })
public class SearchActivity extends Activity {

	String responseBody = null;
	JSONArray samplearr = null;
	JSONObject json;
	JSONObject jobj;
	String searchUrl;
	String url1;
	public ArrayList<String> listImages = new ArrayList<String>();
	GridView gridview;

	@TargetApi(11)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		gridview = (GridView) findViewById(R.id.gridview);

	
		
		new LongOperation().execute(null, null, null);

	}

	/**
	 * The simple adapter to load the gridview dynamically
	 * */
	public class MyAdapter extends BaseAdapter {

		private Context cont;

		public MyAdapter(Context context) {
			cont = context;
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return listImages.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			View v;

			if (convertView == null) {
				v = new View(cont);
				LayoutInflater inflater = getLayoutInflater();
				v = inflater.inflate(R.layout.mygrid, parent, false);
			} else {
				v = (View) convertView;
			}
			ImageView imageView = (ImageView) v.findViewById(R.id.imagepart);
			byte[] imageData = null;

			try {
				final int THUMBNAIL_SIZE = 64;
				URL ulrn = new URL(listImages.get(position));

				HttpURLConnection con = (HttpURLConnection) ulrn
						.openConnection();
				InputStream is = con.getInputStream();
				Bitmap bmp = BitmapFactory.decodeStream(is);

				Float width = new Float(bmp.getWidth());
				Float height = new Float(bmp.getHeight());
				Float ratio = width / height;

				bmp = Bitmap.createScaledBitmap(bmp,
						(int) (THUMBNAIL_SIZE * ratio), THUMBNAIL_SIZE, false);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setLayoutParams(new GridView.LayoutParams(120, 120));

				imageView.setImageBitmap(bmp);

			} catch (Exception e) {
			}

			return imageView;
		}

	}
	
	/**
	 * Asynctask class for the loading of the images
	 * */
	private class LongOperation extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... arg0) {

			String strsearch = "";
			String delims[] = new String[10];
			StringTokenizer stToken = new StringTokenizer(MainActivity.searched, " ");
			strsearch=stToken.nextToken();
			Log.d("fuzzy",strsearch);
			delims = MainActivity.searched.split(" ");
			
		
			if(delims.length>=1)
			{
				strsearch="";
				for (int i = 0; i < delims.length - 1; i++) {
					strsearch += delims[i]+"%20";
				}
				strsearch += delims[delims.length - 1];
			}
			Log.d("Strsearch is : ", strsearch);

			String urls = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=Fuzzy%20monkey%20shut&start=0";
			int startnum = 0;
			while (startnum != 24) {

				url1 = "https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q="+strsearch+"&start="+startnum;

				Log.d("the url is ", url1);
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url1);

				ResponseHandler<String> responseHandler = new BasicResponseHandler();

				try {
					responseBody = client.execute(get, responseHandler);
				} catch (Exception ex) {

					ex.printStackTrace();
				}

				try {
					samplearr = new JSONArray(responseBody);

				} catch (Exception er) {
					er.printStackTrace();
				}

				try {
					json = new JSONObject(responseBody);

				} catch (Exception er) {
					er.printStackTrace();
				}

				try {
					startnum += 4;
					JSONObject responseObject = json
							.getJSONObject("responseData");
					JSONArray resultArray = responseObject
							.getJSONArray("results");

					ArrayList<String> listImage = new ArrayList<String>();
					listImage = getImageList(resultArray);

				} catch (Exception er) {
					er.printStackTrace();
				}

			}
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			gridview.setSmoothScrollbarEnabled(true);
			for (int j = 0; j < listImages.size(); j++)
				listImages.get(j);
			gridview.setAdapter(new MyAdapter(SearchActivity.this));
		}

	}

	
	/**
	 * helper function to parse the JSON into a arraylist
	 * */
	public ArrayList<String> getImageList(JSONArray resultArray) {
		ArrayList<String> listImages1 = new ArrayList<String>();
		try {
			for (int i = 0; i < resultArray.length(); i++) {
				JSONObject obj;
				obj = resultArray.getJSONObject(i);
				listImages1.add(obj.getString("url"));
				Log.d("image url ____--s-s-s", obj.getString("url"));
			}
		} catch (Exception er) {
			er.printStackTrace();
		}

		if (listImages1.size() > 0)
			listImages.addAll(listImages1);
		return listImages1;

	}
}
