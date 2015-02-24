package au.edu.cmu.at;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import au.edu.cmu.at.toolkit.*;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsFragment extends Fragment {

	static final String KEY_NAME = "name";
	static final String KEY_USERNAME = "username";
	static final String KEY_AVATAR_URL = "avatar";

	private String token;
	private ListView list;
	private ContactsAdapter adapter;
	private View currentView;
	private String contactsJSON;
	private String profileJSON;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_contacts, container, false);
		list = (ListView) currentView.findViewById(R.id.contacts_list);
		token = LoadActivity.loadToken();
		ArrayList<HashMap<String, String>> contactsList = new ArrayList<HashMap<String, String>>();
		
		// System.out.println("[Contacts Fragment]Token is: " + token);
		list.setFastScrollEnabled(true);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(list.getContext(), PreviewActivity.class);
				TextView username = (TextView) view.findViewById(R.id.contact_username);
				
				intent.putExtra("type", "profile");
				intent.putExtra("username", username.getText());
				startActivity(intent);
			}
		});
		
		
		Thread getContactThread = new Thread(new Runnable() {
			public void run() {
				try {
					contactsJSON = HttpTools.getContacts(token, "all");
					//contactsJSON = JSONSample.contactsJSON;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getContactThread.start();

		try {
			getContactThread.join();
			JSONObject rawJSON = new JSONObject(contactsJSON);
			JSONObject contactsResultObject = rawJSON.getJSONObject("result");
			
			int i = 0;
			String iString = "" + i;

			while (true) {
				JSONObject contactsResultObjectItem = contactsResultObject.getJSONObject(iString);
				HashMap<String, String> contactItem = new HashMap<String, String>();

				contactItem.put(KEY_NAME, contactsResultObjectItem.getString(KEY_NAME));
				contactItem.put(KEY_USERNAME, contactsResultObjectItem.getString(KEY_USERNAME));
				contactItem.put(KEY_AVATAR_URL, contactsResultObjectItem.getString(KEY_AVATAR_URL));

				contactsList.add(contactItem);
				i++;
				iString = "" + i;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		final Comparator<HashMap<String, String>> ContactsComparator = new Comparator<HashMap<String, String>>() {
			@Override
			public int compare(HashMap<String, String> h1, HashMap<String, String> h2) {
				return h1.get(KEY_NAME).compareToIgnoreCase(h2.get(KEY_NAME));
			}
		};

		Collections.sort(contactsList, ContactsComparator);
		adapter = new ContactsAdapter(getActivity(), contactsList);
		list.setAdapter(adapter);
		
		/*list.setOnItemClickListener(new OnItemClickListener() {
		 
		  @Override 
		  public void onItemClick(AdapterView<?> parent,
		  View view, int position, long id) {
			  final View v = view;
				
				
				Thread getProfileThread = new Thread(new Runnable() {

					@Override
					public void run() {
						TextView memberUsernameText = (TextView) v.findViewById(R.id.contact_username);
						String 	memberUsername = memberUsernameText.getText().toString();
						profileJSON = HttpTools.getMemberProfile(memberUsername, token);
					}
					
				});
				getProfileThread.start();
				
				try {
					getProfileThread.join();
					ProfileFragment firstFragment = new ProfileFragment();

		            // In case this activity was started with special instructions from an Intent,
		            // pass the Intent's extras to the fragment as arguments
		            //firstFragment.setArguments(getIntent().getExtras());

		            // Add the fragment to the 'fragment_container' FrameLayout
					//getActivity().getSupportFragmentManager().beginTransaction()
		            //        .replace(R.id.frame_container, firstFragment).commit();

				
					System.out.println("profileJSON = "+profileJSON);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		  
		  } });*/
		 
		return currentView;
	}

}
