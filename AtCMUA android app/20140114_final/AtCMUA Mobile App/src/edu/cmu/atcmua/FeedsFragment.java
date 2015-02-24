package edu.cmu.atcmua;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.atcmua.toolkit.HttpTools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class FeedsFragment extends Fragment {
	
	static final String KEY_BLOGGUID = "blog_guid";
	static final String KEY_TITLE = "title";
	static final String KEY_AUTHOR = "name";
	
	private String token;
	private ListView list;
	private FeedsAdapter adapter;
	private View currentView;
	private String feedsJSON;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_feed, container, false);
		list = (ListView) currentView.findViewById(R.id.feeds_list);
		token = LoadActivity.loadToken();
		ArrayList<HashMap<String, String>> feedsList = new ArrayList<HashMap<String, String>>();
		
		//System.out.println("[Feeds Fragment]Token is: " + token);
		list.setFastScrollEnabled(true);
		
		Thread getFeedThread = new Thread(new Runnable(){
			public void run(){
				try{
					feedsJSON = HttpTools.getFeeds(token);
					//feedsJSON = JSONSample.feedsJSON;
					//LoadActivity.saveFeeds(feedsJSON);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		getFeedThread.start();
		
		try {
			getFeedThread.join();
			JSONObject rawJSON = new JSONObject(feedsJSON);
			JSONArray feedsResultArray = rawJSON.getJSONArray("result");
			
			for (int i = 0; i < feedsResultArray.length(); i++) {
				HashMap<String, String> feedsItem = new HashMap<String, String>();
				JSONObject feedsItemObject = feedsResultArray.getJSONObject(i);

				feedsItem.put(KEY_BLOGGUID, feedsItemObject.getString(KEY_BLOGGUID));
				feedsItem.put(KEY_TITLE, feedsItemObject.getString(KEY_TITLE));
				feedsItem.put(KEY_AUTHOR,feedsItemObject.getString(KEY_AUTHOR));
				
				feedsList.add(feedsItem);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		adapter = new FeedsAdapter(getActivity(), feedsList);
		list.setAdapter(adapter);
		
		//System.out.println("[FEEDSFRAGMENT]feeds JSON: " + feedsJSON);

		return currentView;
	}
}
