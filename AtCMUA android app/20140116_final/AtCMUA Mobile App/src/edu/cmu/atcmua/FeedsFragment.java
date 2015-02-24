package edu.cmu.atcmua;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.atcmua.toolkit.HttpTools;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class FeedsFragment extends Fragment {

	static final String KEY_BLOGGUID = "blog_guid";
	static final String KEY_TITLE = "title";
	static final String KEY_AUTHOR = "name";

	private String token;
	private ListView list;
	private FeedsAdapter adapter;
	private View currentView;
	private String feedsJSON;
	private int index = 0;
	private boolean loadingMore = false;
	ArrayList<HashMap<String, String>> feedsList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> moreFeedsList = new ArrayList<HashMap<String, String>>();
	Thread loadingMoreFeeds;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_feed, container, false);
		list = (ListView) currentView.findViewById(R.id.feeds_list);
		token = LoadActivity.loadToken();
		// System.out.println("[Feeds Fragment]Token is: " + token);
		list.setFastScrollEnabled(true);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(list.getContext(), PreviewActivity.class);
				TextView blog_guid = (TextView) view.findViewById(R.id.feed_blog_guid);
				
				intent.putExtra("type", "article");
				intent.putExtra("blog_guid", blog_guid.getText());
				startActivity(intent);
			}
		});

		Thread getFeedThread = new Thread(new Runnable() {
			public void run() {
				try {
					feedsJSON = HttpTools.getFeeds(token, 20, index);
					System.out.println("feeds" + feedsJSON);
					// feedsJSON = JSONSample.feedsJSON;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getFeedThread.start();

		try {
			getFeedThread.join();
			//feedsList.clear();
			feedsList = addJSONtoList(feedsJSON);
			System.out.println("feedsList+++++" + feedsList.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		adapter = new FeedsAdapter(getActivity(), feedsList);
//		View footerView = ((LayoutInflater) getActivity().getSystemService(
//				Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_footer,
//				null, false);
//		list.addFooterView(footerView);
		list.setAdapter(adapter);
		//feedsList.clear();

		list.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				index = totalItemCount;
				int lastInScreen = firstVisibleItem + visibleItemCount;
				if ((lastInScreen == totalItemCount - 3) && !(loadingMore)) {
					loadingMoreFeeds = new Thread(new Runnable() {
						@Override
						public void run() {
							loadingMore = true;
							feedsJSON = HttpTools.getFeeds(token, 10, index - 1);
							System.out.println("second feeds  "+feedsJSON);
							// System.out.println("index: "+index);
						}
					});
					loadingMoreFeeds.start();

				}
				if (lastInScreen == totalItemCount&&loadingMore) {
					try {
						loadingMoreFeeds.join();
						//feedsList.clear();
						feedsList = new ArrayList<HashMap<String, String>>();;
						feedsList = addJSONtoList(feedsJSON);
						} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Runnable pushToAdapter = new Runnable() {

						@Override
						public void run() {
							if (feedsList != null && feedsList.size() > 0) {
								for (int i = 0; i < feedsList.size(); i++) {
									adapter.add(feedsList.get(i));
								}
								
								// Tell to the adapter that changes have been
								// made, this will cause the list to refresh
								adapter.notifyDataSetChanged();
								loadingMore = false;
							}
						}
					};
					getActivity().runOnUiThread(pushToAdapter);
				}
			}
		});
		// System.out.println("[FEEDSFRAGMENT]feeds JSON: " + feedsJSON);
		
		return currentView;
	}
	private ArrayList<HashMap<String, String>> addJSONtoList(String JSONResult){
		ArrayList<HashMap<String, String>> listResult = new ArrayList<HashMap<String, String>>();
		try {
			JSONObject rawJSON = new JSONObject(JSONResult);
			JSONArray feedsResultArray = rawJSON
					.getJSONArray("result");
			String feedsStatus;
			feedsStatus = rawJSON.getString("status");
			if(feedsStatus =="-1"){
				LoadActivity.userLogout(getActivity());
			}
			
			System.out.println("feedsStatus"+feedsStatus);

			for (int i = 0; i < feedsResultArray.length(); i++) {
				HashMap<String, String> feedsItem = new HashMap<String, String>();
				JSONObject feedsItemObject = feedsResultArray.getJSONObject(i);

				feedsItem.put(KEY_BLOGGUID,feedsItemObject.getString(KEY_BLOGGUID));
				feedsItem.put(KEY_TITLE,feedsItemObject.getString(KEY_TITLE));
				feedsItem.put(KEY_AUTHOR,feedsItemObject.getString(KEY_AUTHOR));

				listResult.add(feedsItem);

			}
		} catch (JSONException e) {
			e.printStackTrace();
		} 
		
		return listResult;
	}
}
