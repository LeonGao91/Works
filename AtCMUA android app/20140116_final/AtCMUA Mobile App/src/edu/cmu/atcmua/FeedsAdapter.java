package edu.cmu.atcmua;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FeedsAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;

	public FeedsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return data.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		
		if (convertView == null) {
			view = inflater.inflate(R.layout.listitem_feed, null);
		}

		TextView blog_guid = (TextView) view.findViewById(R.id.feed_blog_guid); //blog_guid
		TextView title = (TextView) view.findViewById(R.id.feed_title); // title
		TextView username = (TextView) view.findViewById(R.id.feed_author);// author

		HashMap<String, String> feedsItem = data.get(position);
        
		blog_guid.setText(feedsItem.get(FeedsFragment.KEY_BLOGGUID));
        title.setText(feedsItem.get(FeedsFragment.KEY_TITLE));
        username.setText(feedsItem.get(FeedsFragment.KEY_AUTHOR));
		
		return view;
	}

	public void add(HashMap<String, String> hashMap) {
		data.add(hashMap);
	}
}
