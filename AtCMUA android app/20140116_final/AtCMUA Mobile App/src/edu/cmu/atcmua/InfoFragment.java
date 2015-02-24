package edu.cmu.atcmua;

import org.json.JSONObject;

import edu.cmu.atcmua.toolkit.HttpTools;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class InfoFragment extends Fragment {
	public String token;
	private Thread getBlogThread;
	private String blog = "";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_info, container, false);
		token = LoadActivity.loadToken();
		//System.out.println("token in info: " + token);

		Button bblog = (Button) view.findViewById(R.id.bblog);
		getBlogThread = new Thread(new Runnable() {
			public void run() {
				try {
					// String s = URLEncoder.encode("printer", "UTF-8");
					int guid1 = 7471;
					String blog1 = HttpTools.getBlog(token, guid1);
					JSONObject rawJSON = new JSONObject(blog1);
					JSONObject resultJSON = new JSONObject(
							rawJSON.getString("result"));
					blog = resultJSON.getString("content");
					//System.out.println("CONTENT:" + blog);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getBlogThread.start();
		bblog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				try {
					getBlogThread.join();
					
					TextView textView = (TextView)getActivity().findViewById(
							R.id.textView_blog);
					textView.setMovementMethod(LinkMovementMethod.getInstance());
					textView.setText(Html.fromHtml(blog));
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		});

		return view;
	}
}

