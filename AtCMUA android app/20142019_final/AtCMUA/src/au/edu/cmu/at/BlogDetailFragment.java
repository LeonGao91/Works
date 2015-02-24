package au.edu.cmu.at;

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
import android.widget.TextView;

public class BlogDetailFragment extends Fragment {
	public String token;
	private Thread getBlogThread;
	private String blog = "";
	private String content;
	private String title;
	private String author;
	private String avatar;
	private ImageLoader imageLoader;
	TextView contentView;
	TextView titleView;
	TextView authorView;
	ImageView avatarView;
	int guid;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_blog_detail, container, false);
		guid = Integer.parseInt(getArguments().getString("blog_guid"));

		getBlogThread = new Thread(new Runnable() {
			public void run() {
				try {
					token = LoadActivity.loadToken();
					String blog = HttpTools.getBlog(token, guid);
					Log.d("blog from thread",blog);
					JSONObject rawJSON = new JSONObject(blog);
					JSONObject resultJSON = new JSONObject(rawJSON.getString("result"));
					content = resultJSON.getString("content");
					title = resultJSON.getString("title");
					author = resultJSON.getString("name");
					avatar = resultJSON.getString("avatar");
					Log.d("content from thread",content);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		getBlogThread.start();
		
		 try { 
			 getBlogThread.join();
			 contentView = (TextView) view.findViewById(R.id.article_content);
			 titleView = (TextView) view.findViewById(R.id.article_title);
			 authorView = (TextView) view.findViewById(R.id.article_author);
			 contentView.setMovementMethod(LinkMovementMethod.getInstance());
			 contentView.setText(Html.fromHtml(content)); 
			 titleView.setText(title); 
			 authorView.setText(author); 
			 avatarView = (ImageView) view.findViewById(R.id.article_avatar); 
			 imageLoader = new ImageLoader(getActivity().getApplicationContext());
			 imageLoader.DisplayImage(avatar.replace("amp;", ""), avatarView);
		  } 
		 catch (InterruptedException e) 
		 { // TODO Auto-generated catch block e.printStackTrace(); }
	
		
	}
		 return view;
	}
	
}
