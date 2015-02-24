package au.edu.cmu.at;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactsAdapter extends BaseAdapter {
	private Activity activity;
	private ArrayList<HashMap<String, String>> data;
	private static LayoutInflater inflater = null;
	private ImageLoader imageLoader;

	public ContactsAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
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
			view = inflater.inflate(R.layout.listitem_contacts, null);
		}

		TextView title = (TextView) view.findViewById(R.id.contact_name); // name
		TextView username = (TextView) view.findViewById(R.id.contact_username);// username
		ImageView avatar = (ImageView) view.findViewById(R.id.contact_avatar); // avatar

		HashMap<String, String> contactItem = data.get(position);
        
        title.setText(contactItem.get(ContactsFragment.KEY_NAME));
        username.setText(contactItem.get(ContactsFragment.KEY_USERNAME));
        imageLoader.DisplayImage(contactItem.get(ContactsFragment.KEY_AVATAR_URL).replace("amp;", ""), avatar);
		
		return view;
	}
}
