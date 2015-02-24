package edu.cmu.atcmua;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import edu.cmu.atcmua.toolkit.HttpTools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ContactsFragment extends Fragment {

	static final String KEY_NAME = "name";
	static final String KEY_USERNAME = "username";
	static final String KEY_AVATAR_URL = "avatar";

	private String token;
	private ListView list;
	private ContactsAdapter adapter;
	private View currentView;
	private String contactsJSON;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_contacts, container, false);
		list = (ListView) currentView.findViewById(R.id.contacts_list);
		token = LoadActivity.loadToken();
		ArrayList<HashMap<String, String>> contactsList = new ArrayList<HashMap<String, String>>();
		
		// System.out.println("[Contacts Fragment]Token is: " + token);
		list.setFastScrollEnabled(true);
		
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

		return currentView;
	}
}
