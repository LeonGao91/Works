package au.edu.cmu.at;

import java.util.TreeMap;

import org.json.JSONObject;

import au.edu.cmu.at.toolkit.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsDetailFragment extends Fragment{
	
	public String token;
	private String username;
	private TreeMap<String, String> profileMap= new TreeMap<String, String>();
	private LinearLayout profileLayout; 
	private View view;
	
	
	private Thread getContactDetailThread;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_contact_detail, container, false);
		profileLayout= (LinearLayout) view.findViewById(R.id.profile_layout);
		getContactDetailThread = new Thread(new Runnable() {
			public void run() {
				try {
					username = getArguments().getString("username");
					token = LoadActivity.loadToken();
					String memberProfile = HttpTools.getMemberProfile( username,token);
					JSONObject rawJSON = new JSONObject(memberProfile);
					JSONObject resultJSON = new JSONObject(rawJSON.getString("result"));
					Log.d("about me",resultJSON.getString("admin_defined_profile_1"));

					profileMap.put("About Me", resultJSON.getString("admin_defined_profile_1"));
					profileMap.put("Descripton",resultJSON.getString("admin_defined_profile_2"));
					profileMap.put("Email", resultJSON.getString("admin_defined_profile_3"));
					profileMap.put("Telephone", resultJSON.getString("admin_defined_profile_4"));
					profileMap.put("Mobile Phone", resultJSON.getString("admin_defined_profile_5"));
					profileMap.put("Attribute6", resultJSON.getString("admin_defined_profile_6"));
					profileMap.put("Location", resultJSON.getString("admin_defined_profile_7"));
					profileMap.put("Interest", resultJSON.getString("admin_defined_profile_8"));
					profileMap.put("Skills", resultJSON.getString("admin_defined_profile_9"));
					profileMap.put("Twitter", resultJSON.getString("admin_defined_profile_10"));
					profileMap.put("Program", resultJSON.getString("admin_defined_profile_11"));
					

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getContactDetailThread.start();
		 try { 
		 getContactDetailThread.join();
			for(TreeMap.Entry<String,String> entry : profileMap.entrySet()) {
				  String key = entry.getKey();
				  String value = entry.getValue();
				  if(value!="null"&&!value.isEmpty()){
					  Log.d("Empty",""+value.isEmpty());
					  Log.d("inloop","success");
					TextView attributeName =new TextView(profileLayout.getContext());  
					attributeName.setText(key);
					profileLayout.addView(attributeName);
					TextView attributeContent =new TextView(profileLayout.getContext());  
					attributeContent.setText(value);
					profileLayout.addView(attributeContent);
				  }

				  System.out.println(key + " => " + "-"+value+"-");
				}
//		 avatarView = (ImageView) view.findViewById(R.id.article_avatar); 
//		 imageLoader = new ImageLoader(getActivity().getApplicationContext());
//		 imageLoader.DisplayImage(avatar.replace("amp;", ""), avatarView);
	  } 
	 catch (InterruptedException e) 
	 { // TODO Auto-generated catch block e.printStackTrace(); }

	
}
	
		

		 return view;
	}
}
